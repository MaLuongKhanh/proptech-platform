package vn.proptech.listing.application.dto.Input;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.proptech.listing.domain.model.ListingType;
import vn.proptech.listing.domain.model.PropertyType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Bỏ qua các trường null khi serialize
public class GetListingRequest {
    public Integer page;
    public Integer size;
    public String sort;
    public String direction;
    public ListingType listingType;
    public PropertyType propertyType;
    public String city;
    public String district;
    public Double minPrice;
    public Double maxPrice;
    public Double minArea;
    public Double maxArea;
    public Integer minBedrooms;
    public Integer maxBedrooms;
    public Double minBathrooms;
    public Double maxBathrooms;
}
