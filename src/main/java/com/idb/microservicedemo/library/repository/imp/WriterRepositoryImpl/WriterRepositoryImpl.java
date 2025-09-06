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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
public class WriterRepositoryImpl implements WriterRepositoryCustom {

    private final EntityManager em;
    private final WriterMapper writerMapper;

    @Override
    public GetPageWriterResponse getPage(GetPageWriterRequest request) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QWriter w = QWriter.writer;

        // --- filtreler ---
        BooleanBuilder where = new BooleanBuilder();
        if (request.getFirstName() != null && !request.getFirstName().isBlank()) {
            where.and(w.firstName.containsIgnoreCase(request.getFirstName()));
        }
        if (request.getNationality() != null && !request.getNationality().isBlank()) {
            where.and(w.nationality.eq(request.getNationality()));
        }

        // --- toplam kayıt sayısı ---
        Long total = queryFactory.select(w.count()).from(w).where(where).fetchOne();
        int totalCount = Objects.requireNonNullElse(total, 0L).intValue();

        // --- asıl sorgu ---
        JPAQuery<Writer> query = queryFactory.selectFrom(w).where(where);

        // --- sıralama (whitelist) ---
        if (request.getOrderByField() != null && !request.getOrderByField().isBlank()) {
            Map<String, ComparableExpressionBase<?>> sortable = new HashMap<>();
            sortable.put("firstName", w.firstName);
            sortable.put("lastName", w.lastName);
            sortable.put("email", w.email);
            sortable.put("nationality", w.nationality);

            ComparableExpressionBase<?> sortField = sortable.get(request.getOrderByField());
            if (sortField != null) {
                Order order = request.isOrderByAsc() ? Order.ASC : Order.DESC;
                query.orderBy(new OrderSpecifier<>(order, sortField));
            } else {
                query.orderBy(w.firstName.asc()); // default
            }
        } else {
            query.orderBy(w.firstName.asc()); // default
        }

        // --- sayfalama ---
        if (!request.isGetAllData()) {
            int pageNumber = Math.max(1, request.getPageNumber());
            int pageSize = Math.max(1, request.getPageSize());
            query.offset((long) (pageNumber - 1) * pageSize).limit(pageSize);
        }

        // --- fetch & map ---
        List<WriterDto> list = query.fetch().stream()
                .map(writerMapper::toDto)
                .toList();

        // --- response ---
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
}
