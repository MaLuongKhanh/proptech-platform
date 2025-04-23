package vn.proptech.listing.application.mapper.Input;

import vn.proptech.listing.application.dto.Input.AddListingRequest;
import vn.proptech.listing.domain.model.Listing;
import vn.proptech.listing.domain.model.ListingType;

import java.util.List;

public class AddListingRequestMapper {
    public static Listing AddListingMapDTOToEntity(AddListingRequest addListingRequest, String featureImage, List<String> image, String id) {
        return Listing.builder()
            .id(id)
            .propertyId(addListingRequest.getPropertyId())
            .name(addListingRequest.getName())
            .description(addListingRequest.getDescription())
            .price(addListingRequest.getPrice())
            .area(addListingRequest.getArea())
            .bedrooms(addListingRequest.getBedrooms())
            .bathrooms(addListingRequest.getBathrooms())
            .listingType(mapListingType(addListingRequest.getListingType()))
            .featuredImageUrl(featureImage)
            .imageUrls(image)
            .agentId(addListingRequest.getAgentId())
            .build();
    }

    private static ListingType mapListingType(String listingType) {
        if (listingType == null || listingType.isEmpty()) {
            return ListingType.UNKNOWN_LISTING;
        }
        try {
            return ListingType.valueOf(listingType.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ListingType.UNKNOWN_LISTING;
        }
    }
}
