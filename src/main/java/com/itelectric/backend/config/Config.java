package com.itelectric.backend.config;

import com.itelectric.backend.service.impl.AuditingService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class Config {
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditingService();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
