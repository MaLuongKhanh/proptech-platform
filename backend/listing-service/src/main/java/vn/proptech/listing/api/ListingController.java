package vn.proptech.listing.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import vn.proptech.listing.application.dto.Input.AddListingRequest;
import vn.proptech.listing.application.dto.Input.GetListingRequest;
import vn.proptech.listing.application.dto.Input.UpdateListingRequest;
import vn.proptech.listing.application.dto.Output.GetListingResponse;
import vn.proptech.listing.api.common.ApiResponse;
import vn.proptech.listing.domain.service.ListingService;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/listings")
@RequiredArgsConstructor
@Slf4j
public class ListingController {
    
    private final ListingService listingService;
    
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<GetListingResponse>> createListing(@ModelAttribute AddListingRequest request) {
        log.info("REST request to create listing: {}", request.getName());

        GetListingResponse response = listingService.createListing(request);

        return ApiResponse.created(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GetListingResponse>> getListing(@PathVariable String id) {
        log.info("REST request to get listing: {}", id);
        
        Optional<GetListingResponse> response = listingService.getListingById(id);
        
        if (response.isPresent()) {
            return ApiResponse.ok(response.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GetListingResponse>> updateListing(
            @PathVariable String id,
            @RequestBody UpdateListingRequest request) {
        
        log.info("REST request to update listing: {}", id);
        
        try {
            GetListingResponse response = listingService.updateListing(id, request);
            return ApiResponse.updated(response);
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteListing(@PathVariable String id) {
        log.info("REST request to delete listing: {}", id);
        
        boolean deleted = listingService.deleteListing(id);
        
        return deleted ? ApiResponse.noContent() : ApiResponse.error("Không tìm thấy listing với ID: " + id, null, HttpStatus.NOT_FOUND);
    }
    
    @GetMapping()
    public ResponseEntity<ApiResponse<List<GetListingResponse>>> searchListings(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String direction,
            @RequestParam(required = false) String listingType,
            @RequestParam(required = false) String propertyType,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String district,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Double minArea,
            @RequestParam(required = false) Double maxArea,
            @RequestParam(required = false) Integer minBedrooms,
            @RequestParam(required = false) Integer maxBedrooms,
            @RequestParam(required = false) Double minBathrooms,
            @RequestParam(required = false) Double maxBathrooms) {
        
        log.info("REST request to search listings with criteria");
        
        // Tạo đối tượng GetListingRequest từ các tham số
        GetListingRequest request = GetListingRequest.builder()
            .page(page)
            .size(size)
            .sort(sort)
            .direction(direction)
            .listingType(listingType != null ? vn.proptech.listing.domain.model.ListingType.valueOf(listingType) : null)
            .propertyType(propertyType != null ? vn.proptech.listing.domain.model.PropertyType.valueOf(propertyType) : null)
            .city(city)
            .district(district)
            .minPrice(minPrice)
            .maxPrice(maxPrice)
            .minArea(minArea)
            .maxArea(maxArea)
            .minBedrooms(minBedrooms)
            .maxBedrooms(maxBedrooms)
            .minBathrooms(minBathrooms)
            .maxBathrooms(maxBathrooms)
            .build();
        
        List<GetListingResponse> responses = listingService.getAllListings(request);
        
        return ApiResponse.ok(responses);
    }
    
    @GetMapping("/agent/{agentId}")
    public ResponseEntity<ApiResponse<List<GetListingResponse>>> getListingsByAgentId(
            @PathVariable String agentId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String direction,
            @RequestParam(required = false) String listingType,
            @RequestParam(required = false) String propertyType,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String district,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Double minArea,
            @RequestParam(required = false) Double maxArea,
            @RequestParam(required = false) Integer minBedrooms,
            @RequestParam(required = false) Integer maxBedrooms,
            @RequestParam(required = false) Double minBathrooms,
            @RequestParam(required = false) Double maxBathrooms) {
        
        log.info("REST request to get listings by agent ID: {}", agentId);
        
        // Tạo đối tượng GetListingRequest từ các tham số
        GetListingRequest request = GetListingRequest.builder()
            .page(page)
            .size(size)
            .sort(sort)
            .direction(direction)
            .listingType(listingType != null ? vn.proptech.listing.domain.model.ListingType.valueOf(listingType) : null)
            .propertyType(propertyType != null ? vn.proptech.listing.domain.model.PropertyType.valueOf(propertyType) : null)
            .city(city)
            .district(district)
            .minPrice(minPrice)
            .maxPrice(maxPrice)
            .minArea(minArea)
            .maxArea(maxArea)
            .minBedrooms(minBedrooms)
            .maxBedrooms(maxBedrooms)
            .minBathrooms(minBathrooms)
            .maxBathrooms(maxBathrooms)
            .build();
            
        List<GetListingResponse> responses = listingService.findByAgentId(agentId, request);
        return ApiResponse.ok(responses);
    }
    
    @GetMapping("/location")
    public ResponseEntity<ApiResponse<List<GetListingResponse>>> getListingsByLocation(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "5.0") double maxDistanceKm) {
        
        log.info("REST request to get listings by location: lat={}, lng={}, distance={}km", 
                latitude, longitude, maxDistanceKm);
        
        List<GetListingResponse> responses = listingService.findByLocation(latitude, longitude, maxDistanceKm);
        
        return ApiResponse.ok(responses);
    }
    
    @GetMapping("/address")
    public ResponseEntity<ApiResponse<List<GetListingResponse>>> getListingsByAddress(
            @RequestParam String keyword) {
        
        log.info("REST request to get listings by address keyword: {}", keyword);
        
        List<GetListingResponse> responses = listingService.findByAddressKeyword(keyword);
        
        return ApiResponse.ok(responses);
    }
}