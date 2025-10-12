package com.example.demo.sale.mapper;

import com.example.demo.sale.dto.request.CreateReturnRequest;
import com.example.demo.sale.dto.response.ReturnResponse;
import com.example.demo.sale.entity.Return;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReturnMapper {
    public Return toEntity(CreateReturnRequest request) {
        if (request == null) {
            return null;
        }
        Return returnEntity = new Return();
        returnEntity.setReason(request.getReason());
        return returnEntity;
    }

    public ReturnResponse toDto(Return returnEntity) {
        if (returnEntity == null) {
            return null;
        }
        ReturnResponse dto = new ReturnResponse();
        dto.setId(returnEntity.getId());
        dto.setOrderId(returnEntity.getOrder().getId());
        dto.setReason(returnEntity.getReason());
        dto.setStatus(returnEntity.getStatus());
        if (returnEntity.getCreatedBy() != null) {
            dto.setCreatedByEmployeeId(returnEntity.getCreatedBy().getId());
        }
        // Giả định bạn có ReturnItemMapper để ánh xạ các item
        // dto.setItems(returnEntity.getItems().stream().map(itemMapper::toDto).collect(Collectors.toList()));
        return dto;
    }

    public List<ReturnResponse> toDtoList(List<Return> returns) {
        return returns.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}