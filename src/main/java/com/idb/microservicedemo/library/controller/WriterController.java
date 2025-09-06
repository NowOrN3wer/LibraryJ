package com.idb.microservicedemo.library.controller;

import com.idb.microservicedemo.library.common.Result;
import com.idb.microservicedemo.library.dto.writer.WriterDto;
import com.idb.microservicedemo.library.service.writer.WriterService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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
        WriterDto saved = writerService.createWriter(dto);
        return Result.succeed(saved); // 201 döner
    }
}
