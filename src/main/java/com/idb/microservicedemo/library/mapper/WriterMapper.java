package com.idb.microservicedemo.library.mapper;


import com.idb.microservicedemo.library.domain.entities.writer.Writer;
import com.idb.microservicedemo.library.dto.writer.WriterDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WriterMapper {

    // Entity -> DTO
    WriterDto toDto(Writer writer);

    // DTO -> Entity
    //@Mapping(target = "books", ignore = true) // gerekirse ilişkileri ignore et
    @Mapping(target = "isDeleted", ignore = true) // gerekirse ilişkileri ignore et
    Writer toEntity(WriterDto dto);
}
