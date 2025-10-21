package com.example.demo.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor; // <-- THÊM IMPORT NÀY
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor // <-- THÊM ANNOTATION NÀY
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;
}