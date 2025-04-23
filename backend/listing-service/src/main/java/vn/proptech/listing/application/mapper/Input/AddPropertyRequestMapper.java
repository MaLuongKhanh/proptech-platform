package vn.proptech.listing.application.mapper.Input;


import vn.proptech.listing.domain.model.Property;
import vn.proptech.listing.application.dto.Input.AddPropertyRequest;
import vn.proptech.listing.domain.model.PropertyType;

import java.util.List;


public class AddPropertyRequestMapper {
    public static Property AddPropertyMapDTOToEntity(AddPropertyRequest addPropertyRequest, String id) {
        if (addPropertyRequest == null) {
            return null;
        }
        return Property.builder()
            .id(id)
            .address(AddAddressRequestMapper.mapAddress(addPropertyRequest.getAddress()))
            .propertyType(mapPropertyType(addPropertyRequest.getPropertyType()))
            .yearBuilt(addPropertyRequest.getYearBuilt())
            .lotSize(addPropertyRequest.getLotSize())
            .parkingSpaces(addPropertyRequest.getParkingSpaces())
            .garageSize(addPropertyRequest.getGarageSize())
            .amenities(List.of(addPropertyRequest.getAmenities()))
            .hoaFee(addPropertyRequest.getHoaFee())
            .build();
    }

    private static PropertyType mapPropertyType(String propertyType) {
        if (propertyType == null || propertyType.isEmpty()) {
            return PropertyType.UNKNOWN_PROPERTY; // Giá trị mặc định nếu null hoặc rỗng
        }
        try {
            return PropertyType.valueOf(propertyType.toUpperCase());
        } catch (IllegalArgumentException e) {
            return PropertyType.UNKNOWN_PROPERTY; // Giá trị mặc định nếu không tìm thấy
        }
    }

}
