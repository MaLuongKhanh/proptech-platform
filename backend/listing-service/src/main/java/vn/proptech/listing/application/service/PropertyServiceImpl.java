package vn.proptech.listing.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.proptech.listing.application.dto.Input.AddPropertyRequest;
import vn.proptech.listing.application.dto.Input.UpdatePropertyRequest;
import vn.proptech.listing.application.dto.Output.GetPropertyResponse;
import vn.proptech.listing.application.dto.Input.GetPropertyRequest;
import vn.proptech.listing.application.mapper.Input.AddAddressRequestMapper;
import vn.proptech.listing.application.mapper.Input.AddPropertyRequestMapper;
import vn.proptech.listing.application.mapper.Output.GetPropertyResponseMapper;
import vn.proptech.listing.domain.model.Property;
import vn.proptech.listing.domain.repository.PropertyRepository;
import vn.proptech.listing.infrastructure.messaging.PropertyEventPublisher;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class PropertyServiceImpl implements PropertyService {
    private final PropertyRepository propertyRepository;
    private final PropertyEventPublisher eventPublisher;

    @Override
    public GetPropertyResponse createProperty(AddPropertyRequest addPropertyRequest) {
        log.info("Creating property: {}", addPropertyRequest);
        try {
            String newId = UUID.randomUUID().toString();

            // Map property DTO to entity
            Property property = AddPropertyRequestMapper.AddPropertyMapDTOToEntity(addPropertyRequest, newId);
            property.setActive(true);
            
            // Save property to the database
            Property saveProperty = propertyRepository.save(property);

            // Publish event to the message broker
            eventPublisher.publishPropertyCreatedEvent(property);

            log.info("Property created successfully: {}", property);
            return GetPropertyResponseMapper.GetPropertyMapEntityToDTO(saveProperty);
        } catch (Exception e) {
            log.error("Error creating property: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create property: " + e.getMessage(), e);
        }
    }

    @Override
    public List<GetPropertyResponse> getAllProperties(GetPropertyRequest request) {
        log.info("Fetching all properties with request: {}", request);
        
        // Set default values if null or invalid
        int page = (request.getPage() != null && request.getPage() >= 0) ? request.getPage() : 0;
        int size = (request.getSize() != null && request.getSize() > 0) ? request.getSize() : 10;
        String sort = (request.getSort() != null && !request.getSort().isEmpty()) ? request.getSort() : "createdAt";
        String direction = (request.getDirection() != null && !request.getDirection().isEmpty()) ? 
                request.getDirection().toUpperCase() : "DESC";
        log.debug("Pagination: page={}, size={}, sort={}, direction={}", page, size, sort, direction);

        // Tìm kiếm danh sách
        int offset = page > 0 ? (page - 1) * size : 0;

        List<Property> properties = propertyRepository.findAll(size, offset);
        log.debug("Fetched properties: {}", properties);

        return properties.stream()
                .map(GetPropertyResponseMapper::GetPropertyMapEntityToDTO)
                .toList();
    }

    @Override
    public Optional<GetPropertyResponse> getPropertyById(String propertyId) {
        log.info("Fetching property with ID: {}", propertyId);
        try {
            return propertyRepository.findById(propertyId)
                    .map(GetPropertyResponseMapper::GetPropertyMapEntityToDTO);
        } catch (Exception e) {
            log.error("Error fetching property with ID {}: {}", propertyId, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch property with ID " + propertyId + ": " + e.getMessage(), e);
        }
    }

    @Override
    public GetPropertyResponse updateProperty(String propertyId, UpdatePropertyRequest updatePropertyRequest) {
        log.info("Updating property with ID: {}", propertyId);
        try {
            // Fetch the existing property
            Property existingProperty = propertyRepository.findById(propertyId)
                    .orElseThrow(() -> new IllegalArgumentException("Property not found with ID: " + propertyId));

            // Update the property details with null checks
            if (updatePropertyRequest.getAddress() != null && !updatePropertyRequest.getAddress().equals(existingProperty.getAddress())) {
                existingProperty.setAddress(AddAddressRequestMapper.mapAddress(updatePropertyRequest.getAddress()));
            }
            if (updatePropertyRequest.getPropertyType() != null) {
                existingProperty.setPropertyType(updatePropertyRequest.getPropertyType());
            }
            if (updatePropertyRequest.getYearBuilt() > 0) {
                existingProperty.setYearBuilt(updatePropertyRequest.getYearBuilt());
            }
            if (updatePropertyRequest.getLotSize() > 0) {
                existingProperty.setLotSize(updatePropertyRequest.getLotSize());
            }
            if (updatePropertyRequest.getParkingSpaces() >= 0) {
                existingProperty.setParkingSpaces(updatePropertyRequest.getParkingSpaces());
            }
            if (updatePropertyRequest.getGarageSize() >= 0) {
                existingProperty.setGarageSize(updatePropertyRequest.getGarageSize());
            }
            if (updatePropertyRequest.getAmenities() != null) {
                existingProperty.setAmenities(List.of(updatePropertyRequest.getAmenities()));
            }
            if (updatePropertyRequest.getHoaFee() >= 0) {
                existingProperty.setHoaFee(updatePropertyRequest.getHoaFee());
            }

            // Save the updated property to the database
            Property updatedProperty = propertyRepository.save(existingProperty);

            // Publish event to the message broker
            eventPublisher.publishPropertyUpdatedEvent(updatedProperty);

            log.info("Property updated successfully: {}", updatedProperty);
            return GetPropertyResponseMapper.GetPropertyMapEntityToDTO(updatedProperty);
        } catch (IllegalArgumentException e) {
            log.error("Property not found: {}", e.getMessage());
            throw e; // Re-throw exception for proper error response
        } catch (Exception e) {
            log.error("Error updating property: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update property: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteProperty(String propertyId) {
        log.info("Deleting property with ID: {}", propertyId);
        Optional<Property> propertyOptional = propertyRepository.findById(propertyId);
        if (propertyOptional.isPresent()) {
            Property property = propertyOptional.get();
            property.setActive(false); // Mark as inactive instead of deleting
            propertyRepository.save(property);
            eventPublisher.publishPropertyDeletedEvent(property);
            log.info("Property deleted successfully: {}", propertyId);
            return true;
        } else {
            log.warn("Property not found with ID: {}", propertyId);
            return false; // Property not found
        }
    }

    @Override
    public List<String> findPropertyIdsByLocation(double minLat, double maxLat, double minLon, double maxLon) {
        log.info("Finding property IDs by location: minLat={}, maxLat={}, minLon={}, maxLon={}", 
                minLat, maxLat, minLon, maxLon);
        try {
            return propertyRepository.findPropertyIdsByLocation(minLat, maxLat, minLon, maxLon);
        } catch (Exception e) {
            log.error("Error finding property IDs by location: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to find property IDs by location: " + e.getMessage(), e);
        }
    }

    @Override
    public List<GetPropertyResponse> findPropertiesByAddressKeyword(String keyword) {
        log.info("Finding properties by address keyword: {}", keyword);
        try {
            List<Property> properties = propertyRepository.findByAddressKeyword(keyword);
            return properties.stream()
                    .map(GetPropertyResponseMapper::GetPropertyMapEntityToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error finding properties by address keyword: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to find properties by address keyword: " + e.getMessage(), e);
        }
    }
}
