package com.itelectric.backend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @Info(title = "API Documentation", version = "1.0"),
        tags = {
                @Tag(name = "Admin", description = "Admin operations"),
                @Tag(name = "Authentication", description = "Authentication operations"),
                @Tag(name = "Company", description = "Company operations"),
                @Tag(name = "Customer", description = "Customer operations"),
                @Tag(name = "Product", description = "Product operations"),
                @Tag(name = "Quotation", description = "Quotation operations"),
                @Tag(name = "Service", description = "Product operations")
        }
)
@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}
