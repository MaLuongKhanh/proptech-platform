package vn.proptech.rental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableDiscoveryClient
@EnableMongoAuditing
@EnableFeignClients
public class RentalServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RentalServiceApplication.class, args);
    }
}