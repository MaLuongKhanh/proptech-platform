package vn.proptech.listing.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    
    private String street;
    
    private String district;
    
    private String city;
    
    private String province;
    
    private String country;
    
    private String postalCode;
    
    private double latitude;
    
    private double longitude;
} 