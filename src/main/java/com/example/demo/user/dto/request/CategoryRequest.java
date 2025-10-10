package com.example.demo.user.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {
    private Integer id;
    private String name;
    private String description;
}
