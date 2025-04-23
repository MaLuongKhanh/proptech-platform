package vn.proptech.listing.application.mapper.Output;

import vn.proptech.listing.application.dto.Output.GetListingResponse;
import vn.proptech.listing.application.dto.Output.GetPropertyResponse;
import vn.proptech.listing.domain.model.Listing;

import java.util.List;
import java.util.stream.Collectors;

public class GetListingResponseMapper {
    public static GetListingResponse GetListingMapEntityToDTO(Listing listing) {
        return GetListingMapEntityToDTO(listing, null);
    }
    
    public static GetListingResponse GetListingMapEntityToDTO(Listing listing, GetPropertyResponse property) {
        if (listing == null) return null;

        return GetListingResponse.builder()
                .id(listing.getId())
                .propertyId(listing.getPropertyId())
                .property(property)
                .name(listing.getName())
                .description(listing.getDescription())
                .price(listing.getPrice())
                .listingType(listing.getListingType())
                .agentId(listing.getAgentId())
                .agentName(listing.getAgentName())
                .agentPhone(listing.getAgentPhone())
                .agentEmail(listing.getAgentEmail())
                .bedrooms(listing.getBedrooms())
                .bathrooms(listing.getBathrooms())
                .area(listing.getArea())
                .imageUrls(listing.getImageUrls())
                .featuredImageUrl(listing.getFeaturedImageUrl())
                .isActive(listing.isActive())
                .createdAt(listing.getCreatedAt())
                .updatedAt(listing.getUpdatedAt())
                .isSold(listing.isSold())
                .build();
    }

    public static List<GetListingResponse> GetListingListMapEntityToDTO(List<Listing> listings) {
        if (listings == null) return null;

        return listings.stream()
                .map(GetListingResponseMapper::GetListingMapEntityToDTO)
                .collect(Collectors.toList());
    }
}
