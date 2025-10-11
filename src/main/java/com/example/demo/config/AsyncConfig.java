package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;

@Configuration
@EnableAsync // Bật tính năng @Async của Spring
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // Số lượng thread cốt lõi, luôn sẵn sàng
        executor.setCorePoolSize(5);
        // Số lượng thread tối đa có thể tạo
        executor.setMaxPoolSize(10);
        // Số lượng tác vụ có thể chờ trong hàng đợi
        executor.setQueueCapacity(25);
        // Tên tiền tố cho các thread trong pool để dễ dàng debug
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
        return executor;
    }
}