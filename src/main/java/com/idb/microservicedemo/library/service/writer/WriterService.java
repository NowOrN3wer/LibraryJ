package com.idb.microservicedemo.library.service.writer;

import com.idb.microservicedemo.library.dto.writer.WriterDto;

public interface WriterService {
    WriterDto createWriter(WriterDto dto);
}
