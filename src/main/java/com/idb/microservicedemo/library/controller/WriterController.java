package com.idb.microservicedemo.library.controller;

import com.idb.microservicedemo.library.common.Result;
import com.idb.microservicedemo.library.dto.writer.GetPageWriterRequest;
import com.idb.microservicedemo.library.dto.writer.GetPageWriterResponse;
import com.idb.microservicedemo.library.dto.writer.WriterDto;
import com.idb.microservicedemo.library.service.writer.WriterService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/writers")
@SecurityRequirement(name = "bearerAuth")
public class WriterController {

    private final WriterService writerService;

    public WriterController(WriterService writerService) {
        this.writerService = writerService;
    }

    @PostMapping
    public Result<WriterDto> create(@Valid @RequestBody WriterDto dto) {
        WriterDto saved = writerService.create(dto);
        return Result.succeed(saved); // 201 döner
    }

    @PostMapping("/page")
    public Result<GetPageWriterResponse> getPage(@RequestBody GetPageWriterRequest request) {
        GetPageWriterResponse page = writerService.getPage(request);
        return Result.succeed(page); // status 200 ve body standardize edilmiş şekilde döner
    }
}
