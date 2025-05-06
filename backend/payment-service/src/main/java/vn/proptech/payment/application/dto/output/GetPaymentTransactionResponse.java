package vn.proptech.payment.application.dto.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.proptech.payment.domain.model.PaymentTransactionStatus;
import vn.proptech.payment.domain.model.PaymentTransactionType;

import java.math.BigDecimal;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetPaymentTransactionResponse {
    private String id;
    private String walletId;
    private PaymentTransactionType type;
    private BigDecimal amount;
    private String description;
    private PaymentTransactionStatus status;
    
    @JsonProperty("isActive")
    public boolean isActive;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    public Instant createdAt;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    public Instant updatedAt;
}