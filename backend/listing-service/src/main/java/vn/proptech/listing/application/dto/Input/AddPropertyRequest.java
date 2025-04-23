package vn.proptech.listing.application.dto.Input;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddPropertyRequest {
    public AddAddressRequest address;
    public String propertyType;
    public int yearBuilt;
    public double lotSize;
    public int parkingSpaces;
    public double garageSize;
    public String[] amenities;
    public double hoaFee;
}
