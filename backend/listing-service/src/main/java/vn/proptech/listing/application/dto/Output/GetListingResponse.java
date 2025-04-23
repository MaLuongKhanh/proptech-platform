package vn.proptech.listing.application.dto.Output;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.proptech.listing.domain.model.ListingType;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Bỏ qua các trường null khi serialize
public class GetListingResponse {
    public String id;
    public String propertyId;
    public GetPropertyResponse property; // Thêm property object thay vì chỉ lưu propertyId
    public String name;
    public String description;
    public double price;
    public ListingType listingType;
    public String agentId;
    public String agentName;
    public String agentPhone;
    public String agentEmail;
    public int bedrooms;
    public int bathrooms;
    public double area;
    public List<String> imageUrls;
    public String featuredImageUrl;
    
    @JsonProperty("isActive")
    public boolean isActive;
    
    @JsonProperty("isSold")
    public boolean isSold;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    public Instant createdAt;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    public Instant updatedAt;
}
