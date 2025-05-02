package vn.proptech.sale.application.dto.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.proptech.sale.domain.model.TransactionStatus;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Bỏ qua các trường null khi serialize
public class UpdateTransactionRequest {
    private String listingId;
    private String buyerName;
    private String buyerIdentity;
    private double price;
    private Instant transactionDate;
    private Instant depositDate;
    private String agentId;
    private TransactionStatus status;
}