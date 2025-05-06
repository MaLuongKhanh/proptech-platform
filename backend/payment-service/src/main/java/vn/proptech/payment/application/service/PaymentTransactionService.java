package vn.proptech.payment.application.service;

import vn.proptech.payment.application.dto.input.AddPaymentTransactionRequest;
import vn.proptech.payment.application.dto.input.GetPaymentTransactionRequest;
import vn.proptech.payment.application.dto.input.UpdatePaymentTransactionRequest;
import vn.proptech.payment.application.dto.output.GetPaymentTransactionResponse;

import java.util.List;

public interface PaymentTransactionService {
    
    GetPaymentTransactionResponse createPaymentTransaction(AddPaymentTransactionRequest request);
    
    GetPaymentTransactionResponse getPaymentTransactionById(String id);

    List<GetPaymentTransactionResponse> getAllPaymentTransactions(GetPaymentTransactionRequest request);

    GetPaymentTransactionResponse updatePaymentTransaction(String id, UpdatePaymentTransactionRequest request);

    boolean deletePaymentTransaction(String id);
}