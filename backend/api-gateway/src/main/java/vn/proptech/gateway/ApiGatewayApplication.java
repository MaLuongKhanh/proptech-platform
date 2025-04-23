package vn.proptech.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
    
    @Bean
    public OpenAPI propTechOpenAPI() {
        Server localServer = new Server()
            .url("http://localhost:8080")
            .description("Development server");
            
        Server productionServer = new Server()
            .url("https://api.proptech.vn")
            .description("Production server");
            
        Contact contact = new Contact()
            .name("PropTech Development Team")
            .email("dev@proptech.vn")
            .url("https://proptech.vn/contact");
            
        License mitLicense = new License()
            .name("MIT License")
            .url("https://opensource.org/licenses/MIT");
            
        Info info = new Info()
            .title("PropTech Platform API")
            .version("1.0.0")
            .description("API documentation for the PropTech Platform")
            .contact(contact)
            .license(mitLicense);
            
        return new OpenAPI()
            .info(info)
            .servers(List.of(localServer, productionServer));
    }
}