package vn.proptech.payment.application.dto.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.proptech.payment.domain.model.PaymentTransactionStatus;
import vn.proptech.payment.domain.model.PaymentTransactionType;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Bỏ qua các trường null khi serialize
public class UpdatePaymentTransactionRequest {
    private String walletId;

    private PaymentTransactionType type;

    private BigDecimal amount;

    private String description;

    private PaymentTransactionStatus status;
}