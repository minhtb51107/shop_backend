// File: dto/response/SupplierResponse.java
package com.example.demo.product_inventory.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierResponse {
    private Integer id;
    private String name;
    private String contactPerson;
    private String email; // Đã nhất quán
}