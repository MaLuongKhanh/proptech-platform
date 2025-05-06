package vn.proptech.payment.application.mapper.input;

import vn.proptech.payment.application.dto.input.AddPaymentTransactionRequest;
import vn.proptech.payment.domain.model.PaymentTransaction;

public class AddPaymentTransactionRequestMapper {
    public static PaymentTransaction AddPaymentTransactionMapDTOToEntity(AddPaymentTransactionRequest request, String id) {
        return PaymentTransaction.builder()
                .id(id)
                .walletId(request.getWalletId())
                .amount(request.getAmount())
                .description(request.getDescription())
                .type(request.getType())
                .build();
    }
}