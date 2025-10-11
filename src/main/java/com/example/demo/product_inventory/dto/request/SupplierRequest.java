// File: dto/request/SupplierRequest.java
package com.example.demo.product_inventory.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierRequest {
    private String name;
    private String contactPerson;
    private String email; // Đã nhất quán
}