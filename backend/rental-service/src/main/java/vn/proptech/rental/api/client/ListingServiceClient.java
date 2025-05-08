package vn.proptech.rental.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import vn.proptech.rental.api.client.dto.ApiResponse;
import vn.proptech.rental.api.client.dto.GetListingResponse;

@FeignClient(name = "listing-service")
public interface ListingServiceClient {
    
    @GetMapping("/api/listings/listings/{id}")
    ApiResponse<GetListingResponse> getListingById(@PathVariable("id") String id);
}