package vn.proptech.rental.application.dto.input;

import java.time.Instant;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.proptech.rental.domain.model.RentalTransactionStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Bỏ qua các trường null khi serialize
public class GetRentalTransactionRequest {
    public Integer page;
    public Integer size;
    public String sort;
    public String direction;
    public String listingId;
    public String propertyId;
    public String agentId;
    public Instant startDate;
    public Instant endDate;
    public RentalTransactionStatus status;
}