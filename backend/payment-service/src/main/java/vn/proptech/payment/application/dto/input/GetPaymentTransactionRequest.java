package vn.proptech.payment.application.dto.input;

import java.time.Instant;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.proptech.payment.domain.model.PaymentTransactionStatus;
import vn.proptech.payment.domain.model.PaymentTransactionType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Bỏ qua các trường null khi serialize
public class GetPaymentTransactionRequest {
    public Integer page;
    public Integer size;
    public String sort;
    public String direction;
    public String walletId;
    public PaymentTransactionType type;
    public PaymentTransactionStatus status;
    public Instant startDate;
    public Instant endDate;
}