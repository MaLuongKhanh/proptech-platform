package vn.proptech.payment.application.mapper.output;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import vn.proptech.payment.application.dto.output.GetPaymentTransactionResponse;
import vn.proptech.payment.domain.model.PaymentTransaction;

@Component
public class GetPaymentTransactionResponseMapper {
    public static GetPaymentTransactionResponse GetPaymentTransactionMapEntityToDTO(PaymentTransaction transaction) {
        return GetPaymentTransactionResponse.builder()
                .id(transaction.getId())
                .walletId(transaction.getWalletId())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .type(transaction.getType())
                .status(transaction.getStatus())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .build();
    }

    public static List<GetPaymentTransactionResponse> GetPaymentTransactionMapEntityToDTO(List<PaymentTransaction> transactions) {
        return transactions.stream()
            .map(GetPaymentTransactionResponseMapper::GetPaymentTransactionMapEntityToDTO)
            .collect(Collectors.toList());
    }
}