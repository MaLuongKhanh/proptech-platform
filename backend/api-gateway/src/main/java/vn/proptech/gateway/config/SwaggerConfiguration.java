package vn.proptech.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public RouteLocator swaggerRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Route for listing service swagger docs
                .route("listing-service-swagger", r -> r
                        .path("/v3/api-docs/listing-service/**")
                        .filters(f -> f.rewritePath("/v3/api-docs/listing-service/(?<path>.*)", "/v3/api-docs/$\\{path}"))
                        .uri("lb://listing-service"))
                        
                // Route for payment service swagger docs
                .route("payment-service-swagger", r -> r
                        .path("/v3/api-docs/payment-service/**")
                        .filters(f -> f.rewritePath("/v3/api-docs/payment-service/(?<path>.*)", "/v3/api-docs/$\\{path}"))
                        .uri("lb://payment-service"))
                        
                // Route for rental service swagger docs
                .route("rental-service-swagger", r -> r
                        .path("/v3/api-docs/rental-service/**")
                        .filters(f -> f.rewritePath("/v3/api-docs/rental-service/(?<path>.*)", "/v3/api-docs/$\\{path}"))
                        .uri("lb://rental-service"))
                        
                // Route for sale service swagger docs (corrected from sales-service)
                .route("sale-service-swagger", r -> r
                        .path("/v3/api-docs/sale-service/**")
                        .filters(f -> f.rewritePath("/v3/api-docs/sale-service/(?<path>.*)", "/v3/api-docs/$\\{path}"))
                        .uri("lb://sale-service"))

                // Route for security service swagger docs
                .route("security-service-swagger", r -> r
                        .path("/v3/api-docs/security-service/**")
                        .filters(f -> f.rewritePath("/v3/api-docs/security-service/(?<path>.*)", "/v3/api-docs/$\\{path}"))
                        .uri("lb://security-service"))
                .build();
    }
}