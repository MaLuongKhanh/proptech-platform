package vn.proptech.listing.application.service;

import vn.proptech.listing.application.dto.Input.AddPropertyRequest;
import vn.proptech.listing.application.dto.Input.GetPropertyRequest;
import vn.proptech.listing.application.dto.Input.UpdatePropertyRequest;
import vn.proptech.listing.application.dto.Output.GetPropertyResponse;

import java.util.Optional;
import java.util.List;

public interface PropertyService {
    Optional<GetPropertyResponse> getPropertyById(String propertyId);

    List<GetPropertyResponse> getAllProperties(GetPropertyRequest request);

    GetPropertyResponse createProperty(AddPropertyRequest request);

    GetPropertyResponse updateProperty(String propertyId, UpdatePropertyRequest request);

    boolean deleteProperty(String propertyId);
    
    // Phương thức mới để tìm propertyIds dựa trên tọa độ vị trí
    List<String> findPropertyIdsByLocation(double minLat, double maxLat, double minLon, double maxLon);
    
    // Phương thức mới để tìm kiếm theo từ khóa địa chỉ
    List<GetPropertyResponse> findPropertiesByAddressKeyword(String keyword);
}
