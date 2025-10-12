package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
    "com.example.demo.config",
    "com.example.demo.supplychain",
    "com.example.demo.user",
    "com.example.demo.shared" // Thêm các gói này
})
@EntityScan("com.example.demo.*") // Mở rộng để quét tất cả các entity
@EnableJpaRepositories("com.example.demo.*") // Mở rộng để quét tất cả các repository
public class ShopBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopBackendApplication.class, args);
	}

}