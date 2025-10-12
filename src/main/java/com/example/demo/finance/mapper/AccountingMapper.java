package com.example.demo.finance.mapper;

import com.example.demo.finance.dto.response.JournalEntryResponse;
import com.example.demo.finance.entity.JournalEntry;
import com.example.demo.finance.entity.JournalEntryItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountingMapper {

    // Định nghĩa cách chuyển từ JournalEntry -> JournalEntryResponse
    JournalEntryResponse toJournalEntryResponse(JournalEntry entity);

    // Định nghĩa cách chuyển từ JournalEntryItem -> JournalEntryItemResponse
    // MapStruct sẽ tự biết lấy accountCode và accountName từ đối tượng account bên trong
    @Mapping(source = "account.accountCode", target = "accountCode")
    @Mapping(source = "account.accountName", target = "accountName")
    JournalEntryResponse.JournalEntryItemResponse toJournalEntryItemResponse(JournalEntryItem item);
}