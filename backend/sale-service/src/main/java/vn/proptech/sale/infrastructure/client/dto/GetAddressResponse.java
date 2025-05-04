package vn.proptech.sale.infrastructure.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetAddressResponse {
    private String streetAddress;
    private String district;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private double latitude;
    private double longitude;
}