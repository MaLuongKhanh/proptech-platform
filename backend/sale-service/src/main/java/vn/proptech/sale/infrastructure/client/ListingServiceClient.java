package vn.proptech.sale.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vn.proptech.sale.infrastructure.client.dto.ApiResponse;
import vn.proptech.sale.infrastructure.client.dto.GetListingResponse;

@FeignClient(name = "listing-service")
public interface ListingServiceClient {
    
    @GetMapping("/api/listings/listings/{id}")
    ApiResponse<GetListingResponse> getListingById(@PathVariable("id") String id);
}