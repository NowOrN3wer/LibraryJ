package com.idb.microservicedemo.library.dto.abstractions;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasePageResponseDto<T> {
    @Builder.Default
    private List<T> list = new ArrayList<>();
    private int totalCount;
    private int pageNumber;
    private int pageSize;
    private String orderByField;
    private boolean orderByAsc;
    private boolean getAllData;

    public int getTotalPages() {
        return (int) Math.ceil((double) totalCount / pageSize);
    }

    public boolean getHasNextPage() {
        return pageNumber < getTotalPages();
    }

    public boolean getHasPreviousPage() {
        return pageNumber > 1;
    }
}
