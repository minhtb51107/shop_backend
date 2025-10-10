package com.example.demo.finance.mapper;

import com.example.demo.finance.dto.response.AccountResponse;
import com.example.demo.finance.entity.ChartOfAccounts;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring") // Báo cho MapStruct tạo ra một Spring Bean
public interface ChartOfAccountsMapper {

    // Tự động chuyển 1 Entity sang 1 DTO
    AccountResponse toResponse(ChartOfAccounts entity);

    // Tự động chuyển 1 List Entity sang 1 List DTO
    List<AccountResponse> toResponseList(List<ChartOfAccounts> entities);
}