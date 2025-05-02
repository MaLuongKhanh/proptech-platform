package vn.proptech.listing.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.proptech.listing.application.dto.Input.AddPropertyRequest;
import vn.proptech.listing.application.dto.Input.GetPropertyRequest;
import vn.proptech.listing.application.dto.Input.UpdatePropertyRequest;
import vn.proptech.listing.application.dto.Output.GetPropertyResponse;
import vn.proptech.listing.application.service.PropertyService;
import vn.proptech.listing.api.common.ApiResponse;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/listings/properties")
@RequiredArgsConstructor
@Slf4j
public class PropertyController {
    
    private final PropertyService propertyService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<GetPropertyResponse>> createProperty(@RequestBody AddPropertyRequest request) {
        log.info("Creating new property with data: {}", request);
        try {
            GetPropertyResponse response = propertyService.createProperty(request);
            return ApiResponse.created(response);
        } catch (IllegalArgumentException e) {
            log.error("Error creating property: {}", e.getMessage());
            return ApiResponse.error(e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GetPropertyResponse>> getProperty(@PathVariable String id) {
        log.info("Retrieving property with id: {}", id);
        Optional<GetPropertyResponse> propertyResponse = propertyService.getPropertyById(id);
        if (propertyResponse.isPresent()) {
            return ApiResponse.ok(propertyResponse.get());
        } else {
            log.warn("Property with id {} not found", id);
            return ApiResponse.error("Không tìm thấy bất động sản", null, HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GetPropertyResponse>> updateProperty(
            @PathVariable String id,
            @RequestBody UpdatePropertyRequest request) {
        log.info("Updating property with id: {}", id);
        try {
            GetPropertyResponse response = propertyService.updateProperty(id, request);
            return ApiResponse.updated(response);
        } catch (IllegalArgumentException e) {
            log.error("Error updating property: {}", e.getMessage());
            return new ResponseEntity<>(ApiResponse.of(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProperty(@PathVariable String id) {
        log.info("Deleting property with id: {}", id);
        boolean deleted = propertyService.deleteProperty(id);
        return deleted
                ? ApiResponse.noContent()
                : new ResponseEntity<>(ApiResponse.of("Không tìm thấy bất động sản", null), HttpStatus.NOT_FOUND);
    }
    
    @GetMapping()
    public ResponseEntity<ApiResponse<List<GetPropertyResponse>>> getAllProperties(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String direction
            ) {
        
        log.info("Searching properties with filters");
        GetPropertyRequest request = GetPropertyRequest.builder()
                .page(page != null ? page : 0)
                .size(size != null ? size : 10)
                .sort(sort)
                .direction(direction)
                .build();
        
        List<GetPropertyResponse> properties = propertyService.getAllProperties(request);
        return ApiResponse.ok(properties);
    }
}