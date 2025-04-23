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
@Document(collection = "properties")
public class Property {
    @Id
    private String id;
    private Address address;
    private PropertyType propertyType;
    private int yearBuilt;
    private double lotSize;
    private int parkingSpaces;
    private double garageSize;
    private List<String> amenities;
    private double hoaFee;
    private boolean isActive;
    
    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}