package vn.proptech.listing.application.mapper.Input;

import vn.proptech.listing.application.dto.Input.AddAddressRequest;
import vn.proptech.listing.domain.model.Address;

public class AddAddressRequestMapper {
    public static Address mapAddress(AddAddressRequest addAddressRequest) {
        if (addAddressRequest == null) {
            return null;
        }
        return Address.builder()
                .street(addAddressRequest.getStreet())
                .city(addAddressRequest.getCity())
                .province(addAddressRequest.getState())
                .country(addAddressRequest.getCountry())
                .postalCode(addAddressRequest.getZipCode())
                .latitude(addAddressRequest.getLatitude())
                .longitude(addAddressRequest.getLongitude())
                .build();
    }
}
