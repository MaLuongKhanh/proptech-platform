package vn.proptech.rental.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "rental_transactions")
public class RentalTransaction {

    @Id
    private String id;
    
    private String propertyId;

    private String listingId;

    private String tenantName;

    private String tenantIdentity;

    private double price;

    private Instant startDate;

    private Instant endDate;

    private Instant depositDate;

    private String agentId;

    private RentalTransactionStatus status;

    private boolean isActive;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}