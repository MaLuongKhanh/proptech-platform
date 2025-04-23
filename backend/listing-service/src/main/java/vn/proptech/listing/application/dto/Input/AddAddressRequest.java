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
@JsonInclude(JsonInclude.Include.NON_NULL) // Bỏ qua các trường null khi serialize
public class AddAddressRequest {
    public String street;
    public String city;
    public String state;
    public String country;
    public String zipCode;
    public double latitude;
    public double longitude;
}
