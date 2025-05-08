package vn.proptech.rental.api.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetListingResponse {
    private String id;
    private String propertyId;
    private GetPropertyResponse property;
    private String name;
    private String description;
    private double price;
    private String listingType;
    private String agentId;
    private String agentName;
    private String agentPhone;
    private String agentEmail;
    private int bedrooms;
    private int bathrooms;
    private double area;
    private List<String> imageUrls;
    private String featuredImageUrl;
    
    @JsonProperty("isActive")
    private boolean active;
    
    @JsonProperty("isSold")
    private boolean sold;
    
    private Instant createdAt;
    private Instant updatedAt;
}