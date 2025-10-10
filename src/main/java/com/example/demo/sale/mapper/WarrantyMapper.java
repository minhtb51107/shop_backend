package com.example.demo.sale.mapper;

import com.example.demo.sale.dto.request.CreateWarrantyCaseRequest;
import com.example.demo.sale.dto.response.WarrantyCaseResponse;
import com.example.demo.sale.entity.WarrantyCase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WarrantyMapper {
    WarrantyMapper INSTANCE = Mappers.getMapper(WarrantyMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderItem", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "status", ignore = true)
    WarrantyCase toEntity(CreateWarrantyCaseRequest request);

    @Mapping(source = "orderItem.id", target = "orderItemId")
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "createdBy.id", target = "createdByEmployeeId")
    WarrantyCaseResponse toDto(WarrantyCase warrantyCase);
}