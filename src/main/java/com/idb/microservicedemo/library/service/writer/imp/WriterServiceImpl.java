package com.idb.microservicedemo.library.service.writer.imp;

import com.idb.microservicedemo.library.domain.entities.writer.Writer;
import com.idb.microservicedemo.library.dto.writer.WriterDto;
import com.idb.microservicedemo.library.mapper.WriterMapper;
import com.idb.microservicedemo.library.repository.WriterRepository;
import com.idb.microservicedemo.library.service.writer.WriterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WriterServiceImpl implements WriterService {

    private final WriterRepository writerRepository;
    private final WriterMapper writerMapper;

    public WriterServiceImpl(WriterRepository writerRepository, WriterMapper writerMapper) {
        this.writerRepository = writerRepository;
        this.writerMapper = writerMapper;
    }

    @Override
    public WriterDto createWriter(WriterDto dto) {
        // DTO -> Entity
        Writer entity = writerMapper.toEntity(dto);

        // Kaydet
        Writer saved = writerRepository.save(entity);

        // Entity -> DTO
        return writerMapper.toDto(saved);
    }
}
