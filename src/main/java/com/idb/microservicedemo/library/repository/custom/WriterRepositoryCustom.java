package com.idb.microservicedemo.library.repository.custom;

import com.idb.microservicedemo.library.dto.writer.GetPageWriterRequest;
import com.idb.microservicedemo.library.dto.writer.GetPageWriterResponse;

public interface WriterRepositoryCustom {
    GetPageWriterResponse getPage(GetPageWriterRequest request);
}
