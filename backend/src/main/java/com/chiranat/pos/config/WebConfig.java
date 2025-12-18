package com.chiranat.pos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry; // เพิ่ม import
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    // --- ส่วนเดิมของคุณ ---
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath.toString() + "/");
    }

    // CORS configuration has been moved to SecurityConfig.java
    // @Override
    // public void addCorsMappings(CorsRegistry registry) {
    //     registry.addMapping("/**") // อนุญาตทุก Path ใน API
    //                             .allowedOrigins("http://localhost:5173", "http://127.0.0.1:5173", "http://172.16.52.38:5173")
    //             .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
    //             .allowedHeaders("*")
    //             .allowCredentials(true); // ถ้ามีการใช้ Cookie หรือ Header พิเศษ
    // }
}