package com.idb.microservicedemo.library.service.writer.imp;

import com.idb.microservicedemo.library.domain.entities.writer.QWriter;
import com.idb.microservicedemo.library.domain.entities.writer.Writer;
import com.idb.microservicedemo.library.dto.writer.GetPageWriterRequest;
import com.idb.microservicedemo.library.dto.writer.GetPageWriterResponse;
import com.idb.microservicedemo.library.dto.writer.WriterDto;
import com.idb.microservicedemo.library.mapper.WriterMapper;
import com.idb.microservicedemo.library.repository.WriterRepository;
import com.idb.microservicedemo.library.service.writer.WriterService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class WriterServiceImpl implements WriterService {

    private static final Map<String, ComparableExpressionBase<?>> SORTABLE = Map.of(
            "firstName", QWriter.writer.firstName,
            "lastName", QWriter.writer.lastName,
            "email", QWriter.writer.email,
            "nationality", QWriter.writer.nationality
    );
    private final WriterRepository writerRepository;
    private final WriterMapper writerMapper;
    private final EntityManager em;

    public WriterServiceImpl(WriterRepository writerRepository, WriterMapper writerMapper, EntityManager em) {
        this.writerRepository = writerRepository;
        this.writerMapper = writerMapper;
        this.em = em;
    }

    @Override
    public WriterDto create(WriterDto dto) {
        Writer entity = writerMapper.toEntity(dto);
        Writer saved = writerRepository.save(entity);
        return writerMapper.toDto(saved);
    }

    @Override
    public GetPageWriterResponse getPage(GetPageWriterRequest request) {
        JPAQueryFactory qf = new JPAQueryFactory(em);
        QWriter w = QWriter.writer;

        // 1) Filtre
        BooleanBuilder where = buildFilters(request, w);

        // 2) Toplam kayıt
        int totalCount = countAll(qf, w, where);

        // 3) Veri sorgusu
        JPAQuery<Writer> query = qf.selectFrom(w).where(where);
        applySorting(request, query, w);
        Paging p = normalizePaging(request);
        applyPaging(p, request.isGetAllData(), query);

        // 4) Map & response
        List<WriterDto> list = query.fetch().stream()
                .map(writerMapper::toDto)
                .toList();

        GetPageWriterResponse resp = new GetPageWriterResponse();
        resp.setList(list);
        resp.setTotalCount(totalCount);
        resp.setPageNumber(p.pageNumber);
        resp.setPageSize(p.pageSize);
        resp.setOrderByField(request.getOrderByField());
        resp.setOrderByAsc(request.isOrderByAsc());
        resp.setGetAllData(request.isGetAllData());
        return resp;
    }

    private BooleanBuilder buildFilters(GetPageWriterRequest req, QWriter w) {
        BooleanBuilder where = new BooleanBuilder();
        if (req.getFirstName() != null && !req.getFirstName().isBlank()) {
            where.and(w.firstName.containsIgnoreCase(req.getFirstName().trim()));
        }
        if (req.getNationality() != null && !req.getNationality().isBlank()) {
            where.and(w.nationality.eq(req.getNationality().trim()));
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
        ComparableExpressionBase<?> expr =
                (field == null || field.isBlank()) ? null : SORTABLE.get(field);

        if (expr != null) {
            query.orderBy(new OrderSpecifier<>(req.isOrderByAsc() ? Order.ASC : Order.DESC, expr));
        } else {
            // default
            query.orderBy(w.firstName.asc());
        }
    }

    private Paging normalizePaging(GetPageWriterRequest req) {
        int pageNumber = Math.max(1, req.getPageNumber());
        int pageSize = Math.max(1, req.getPageSize());
        return new Paging(pageNumber, pageSize);
    }

    private void applyPaging(Paging p, boolean getAllData, JPAQuery<Writer> query) {
        if (getAllData) return;
        long offset = (long) (p.pageNumber - 1) * p.pageSize;
        query.offset(offset).limit(p.pageSize);
    }

    private record Paging(int pageNumber, int pageSize) {
    }
}
