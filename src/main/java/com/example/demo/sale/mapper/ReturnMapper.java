package com.example.demo.sale.mapper;

import com.example.demo.sale.dto.request.CreateReturnRequest;
import com.example.demo.sale.dto.response.ReturnResponse;
import com.example.demo.sale.entity.Return;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ReturnMapper {
    ReturnMapper INSTANCE = Mappers.getMapper(ReturnMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "status", ignore = true)
    Return toEntity(CreateReturnRequest request);

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "createdBy.id", target = "createdByEmployeeId")
    ReturnResponse toDto(Return returnEntity);
}