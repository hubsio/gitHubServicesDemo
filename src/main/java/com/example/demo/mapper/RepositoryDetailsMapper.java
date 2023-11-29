package com.example.demo.mapper;

import com.example.demo.model.dto.RepositoryDetailsDTO;
import com.example.demo.model.entity.RepositoryDetails;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RepositoryDetailsMapper {
    RepositoryDetailsDTO entityToDto(RepositoryDetails entity);
    RepositoryDetails dtoToEntity(RepositoryDetailsDTO dto);
}
