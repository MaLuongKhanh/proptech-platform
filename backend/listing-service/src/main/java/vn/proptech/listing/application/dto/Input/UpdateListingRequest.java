package vn.proptech.listing.application.dto.Input;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Bỏ qua các trường null khi serialize
public class UpdateListingRequest {
    public String propertyId;
    public String name;
    public String description;
    public double price;
    public String listingType;
    public String agentId;
    public int bedrooms;
    public int bathrooms;
    public double area;
    public MultipartFile[] images;
    public MultipartFile featuredImage;
    public boolean isSold;
}
