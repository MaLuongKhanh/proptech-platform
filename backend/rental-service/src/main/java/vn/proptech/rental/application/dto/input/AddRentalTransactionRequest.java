package vn.proptech.rental.application.dto.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.proptech.rental.domain.model.RentalTransactionStatus;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Bỏ qua các trường null khi serialize
public class AddRentalTransactionRequest {
    private String listingId;
    
    private String tenantName;
    
    private String tenantIdentity;
    
    private double price;
    
    private String agentId;

    private Instant startDate;

    private Instant endDate;
    
    private RentalTransactionStatus status;
}