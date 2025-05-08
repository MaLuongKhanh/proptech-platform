package vn.proptech.listing.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import vn.proptech.listing.api.client.dto.ApiResponse;
import vn.proptech.listing.api.client.dto.GetUserResponse;

@FeignClient(name = "security-service")
public interface SecurityServiceClient {
    
    @GetMapping("/api/securities/users/{id}")
    ApiResponse<GetUserResponse> getUserById(@PathVariable("id") String id);
}