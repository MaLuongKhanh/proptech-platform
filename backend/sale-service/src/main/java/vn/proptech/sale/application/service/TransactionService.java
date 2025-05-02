package vn.proptech.sale.application.service;

import vn.proptech.sale.application.dto.input.AddTransactionRequest;
import vn.proptech.sale.application.dto.input.GetTransactionRequest;
import vn.proptech.sale.application.dto.input.UpdateTransactionRequest;
import vn.proptech.sale.application.dto.output.GetTransactionResponse;

import java.util.List;

public interface TransactionService {
    GetTransactionResponse createTransaction(AddTransactionRequest request);
    GetTransactionResponse getTransactionById(String id);
    List<GetTransactionResponse> getAllTransactions(GetTransactionRequest request);
    GetTransactionResponse updateTransaction(String id, UpdateTransactionRequest request);
    boolean deleteTransaction(String id);
}