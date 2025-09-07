package com.idb.microservicedemo.library.repository.imp.WriterRepositoryImpl;

import com.idb.microservicedemo.library.domain.entities.writer.QWriter;
import com.idb.microservicedemo.library.domain.entities.writer.Writer;
import com.idb.microservicedemo.library.dto.writer.GetPageWriterRequest;
import com.idb.microservicedemo.library.dto.writer.GetPageWriterResponse;
import com.idb.microservicedemo.library.dto.writer.WriterDto;
import com.idb.microservicedemo.library.mapper.WriterMapper;
import com.idb.microservicedemo.library.repository.custom.WriterRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class WriterRepositoryImpl implements WriterRepositoryCustom {

    // --- Tekil whitelist (her yerde aynı) ---
    private static final Map<String, ComparableExpressionBase<?>> SORTABLE_FIELDS;

    static {
        QWriter w = QWriter.writer;
        SORTABLE_FIELDS = Map.of(
                "firstName", w.firstName,
                "lastName", w.lastName,
                "email", w.email,
                "nationality", w.nationality
        );
    }

    private final EntityManager em;
    private final WriterMapper writerMapper;

    @Override
    public GetPageWriterResponse getPage(GetPageWriterRequest request) {
        JPAQueryFactory qf = new JPAQueryFactory(em);
        QWriter w = QWriter.writer;

        // 1) Ortak filtreyi tek yerde kur
        BooleanBuilder where = buildFilters(request, w);

        // 2) Toplam kayıt
        int totalCount = countAll(qf, w, where);

        // 3) Veri sorgusu
        JPAQuery<Writer> query = qf.selectFrom(w).where(where);
        applySorting(request, query, w);
        applyPaging(request, query);

        // 4) Map & response
        List<WriterDto> list = query.fetch().stream()
                .map(writerMapper::toDto)
                .toList();

        GetPageWriterResponse resp = new GetPageWriterResponse();
        resp.setList(list);
        resp.setTotalCount(totalCount);
        resp.setPageNumber(request.getPageNumber());
        resp.setPageSize(request.getPageSize());
        resp.setOrderByField(request.getOrderByField());
        resp.setOrderByAsc(request.isOrderByAsc());
        resp.setGetAllData(request.isGetAllData());

        return resp;
    }

    // --- Helpers ---

    private BooleanBuilder buildFilters(GetPageWriterRequest req, QWriter w) {
        BooleanBuilder where = new BooleanBuilder();
        if (req.getFirstName() != null && !req.getFirstName().isBlank()) {
            where.and(w.firstName.containsIgnoreCase(req.getFirstName()));
        }
        if (req.getNationality() != null && !req.getNationality().isBlank()) {
            where.and(w.nationality.eq(req.getNationality()));
        }
        return where;
    }

    private int countAll(JPAQueryFactory qf, QWriter w, BooleanBuilder where) {
        Long total = qf.select(w.id.count())
                .from(w)
                .where(where)
                .fetchOne();
        return total != null ? total.intValue() : 0;
    }

    private void applySorting(GetPageWriterRequest req, JPAQuery<Writer> query, QWriter w) {
        String field = req.getOrderByField();
        ComparableExpressionBase<?> expr = (field == null || field.isBlank())
                ? null
                : SORTABLE_FIELDS.get(field);

        if (expr != null) {
            Order order = req.isOrderByAsc() ? Order.ASC : Order.DESC;
            query.orderBy(new OrderSpecifier<>(order, expr));
        } else {
            // Default
            query.orderBy(w.firstName.asc());
        }
    }

    private void applyPaging(GetPageWriterRequest req, JPAQuery<Writer> query) {
        if (req.isGetAllData()) return;
        int pageNumber = Math.max(1, req.getPageNumber());
        int pageSize = Math.max(1, req.getPageSize());
        long offset = (long) (pageNumber - 1) * pageSize;
        query.offset(offset).limit(pageSize);
    }
}
