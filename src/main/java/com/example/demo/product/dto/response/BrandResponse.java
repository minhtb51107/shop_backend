package com.example.demo.product.dto.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import com.example.demo.finance.mapper.AccountingMapper;
import com.example.demo.finance.repository.ChartOfAccountsRepository;
import com.example.demo.finance.repository.FinancialPeriodRepository;
import com.example.demo.finance.repository.JournalEntryRepository;
import com.example.demo.sale.entity.OrderItem;
import com.example.demo.sale.entity.OrderStatusHistory;
import com.example.demo.sale.entity.PromotionAppliedOrder;
import com.example.demo.sale.entity.Shipment;
import com.example.demo.supplychain.entity.Warehouse;
import com.example.demo.user.entity.Customer;
import com.example.demo.user.entity.Employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

// DTO này định nghĩa dữ liệu Brand sẽ được trả về cho client.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrandResponse {
    private Integer id;
    private String name;
}
