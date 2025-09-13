package com.example.backend.Config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {
    // Cấu hình cache sẽ được tự động tạo bởi Spring Boot
    // xóa lỗi ráng chịu :)
}