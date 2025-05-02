package vn.proptech.gateway.config;

import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public CommandLineRunner openApiGroups(
            SwaggerUiConfigParameters swaggerUiConfigParameters,
            SwaggerUiConfigProperties swaggerUiConfigProperties) {
        return args -> {
            swaggerUiConfigProperties.setEnabled(true);
            
            // Định nghĩa các nhóm API service
            String[] services = {
                    "listing-service",
                    "payment-service",
                    "rental-service",
                    "sale-service",  // Corrected from "sales-service" to "sale-service"
                    "security-service"
            };
            
            // Đăng ký từng nhóm API cho Swagger UI
            for (String service : services) {
                swaggerUiConfigParameters.addGroup(service);
            }
        };
    }
}