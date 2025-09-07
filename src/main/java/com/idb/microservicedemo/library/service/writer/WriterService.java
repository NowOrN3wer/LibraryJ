package com.idb.microservicedemo.library.service.writer;

import com.idb.microservicedemo.library.dto.writer.GetPageWriterRequest;
import com.idb.microservicedemo.library.dto.writer.GetPageWriterResponse;
import com.idb.microservicedemo.library.dto.writer.WriterDto;

public interface WriterService {
    WriterDto create(WriterDto dto);

    GetPageWriterResponse getPage(GetPageWriterRequest request);
}
