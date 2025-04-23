package vn.proptech.listing.application.mapper.Output;

import vn.proptech.listing.application.dto.Output.GetPropertyResponse;
import vn.proptech.listing.domain.model.Property;
import java.util.List;
import java.util.stream.Collectors;

public class GetPropertyResponseMapper {
    public static GetPropertyResponse GetPropertyMapEntityToDTO(Property property) {
        return GetPropertyResponse.builder()
            .propertyId(property.getId())
            .propertyType(property.getPropertyType())
            .address(GetAddressResponseMapper.mapAddressToDTO(property.getAddress()))
            .yearBuilt(property.getYearBuilt())
            .lotSize(property.getLotSize())
            .parkingSpaces(property.getParkingSpaces())
            .garageSize(property.getGarageSize())
            .amenities(property.getAmenities())
            .hoaFee(property.getHoaFee())
            .isActive(property.isActive())
            .build();
    }

    public static List<GetPropertyResponse> GetPropertyMapEntityToDTO(List<Property> properties) {
        return properties.stream()
            .map(GetPropertyResponseMapper::GetPropertyMapEntityToDTO)
            .collect(Collectors.toList());
    }
}
