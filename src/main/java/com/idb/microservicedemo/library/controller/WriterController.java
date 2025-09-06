package com.idb.microservicedemo.library.controller;

import com.idb.microservicedemo.library.dto.writer.WriterDto;
import com.idb.microservicedemo.library.service.writer.WriterService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<WriterDto> create(@Valid @RequestBody WriterDto dto) {
        WriterDto saved = writerService.createWriter(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
