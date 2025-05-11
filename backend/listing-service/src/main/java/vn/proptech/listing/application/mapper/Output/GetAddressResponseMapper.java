package vn.proptech.listing.application.mapper.Output;

import vn.proptech.listing.application.dto.Output.GetAddressResponse;
import vn.proptech.listing.domain.model.Address;

public class GetAddressResponseMapper {
    public static GetAddressResponse mapAddressToDTO(Address address) {
        if (address == null) return null;
        return GetAddressResponse.builder()
                .street(address.getStreet())
                .city(address.getCity())
                .province(address.getProvince())
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .latitude(address.getLatitude())
                .longitude(address.getLongitude())
                .build();
    }
}
