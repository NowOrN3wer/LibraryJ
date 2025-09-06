package com.idb.microservicedemo.library.dto.abstractions;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasePageRequestDto {
    private int pageNumber = 1;
    private int pageSize = 10;
    private String orderByField;
    private boolean orderByAsc = true;
    private boolean getAllData = false;
}
