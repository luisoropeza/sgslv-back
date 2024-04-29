package com.example.demo.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class CloudinaryConfig {
    
    @Value("${CLOUD_NAME}")
    private String CLOUD_NAME;
    @Value("${API_KEY}")
    private String API_KEY;
    @Value("${API_SECRET}")
    private String API_SECRET;

    @Bean
    Cloudinary cloudinary() {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CLOUD_NAME,
                "api_key", API_KEY,
                "api_secret", API_SECRET,
                "secure", true));
        return cloudinary;
    }
}
