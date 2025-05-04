package vn.proptech.sale.infrastructure.client.dto;

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
public class GetPropertyResponse {
    private String propertyId;
    private GetAddressResponse address;
    private String propertyType;
    private int yearBuilt;
    private double lotSize;
    private int parkingSpaces;
    private double garageSize;
    private List<String> amenities;
    private double hoaFee;
    
    @JsonProperty("isActive")
    private boolean active;
    
    private Instant createdAt;
    private Instant updatedAt;
}