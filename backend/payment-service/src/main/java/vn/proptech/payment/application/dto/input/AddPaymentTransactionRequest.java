package vn.proptech.payment.application.dto.input;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.proptech.payment.domain.model.PaymentTransactionType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddPaymentTransactionRequest {
    private String walletId;
    private BigDecimal amount;
    private String description;
    private PaymentTransactionType type;
}