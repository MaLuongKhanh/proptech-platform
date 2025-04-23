package vn.proptech.listing.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "listings")
public class Listing {

    @Id
    private String id;  

    private String propertyId;

    private String name;

    private String description;

    private double price;

    private ListingType listingType;

    private String agentId;      // Lưu 1 phần info của agent
    private String agentName;    // Tên agent
    private String agentPhone;   // Số điện thoại agent
    private String agentEmail;   // Email agent

    private int bedrooms;

    private int bathrooms;

    private double area;

    private List<String> imageUrls;

    private String featuredImageUrl;

    private boolean isActive;

    private boolean isSold;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}

