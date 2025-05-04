package vn.proptech.rental.application.service;

import vn.proptech.rental.application.dto.input.AddRentalTransactionRequest;
import vn.proptech.rental.application.dto.input.GetRentalTransactionRequest;
import vn.proptech.rental.application.dto.input.UpdateRentalTransactionRequest;
import vn.proptech.rental.application.dto.output.GetRentalTransactionResponse;

import java.util.List;

public interface RentalTransactionService {
    GetRentalTransactionResponse createRentalTransaction(AddRentalTransactionRequest request);
    GetRentalTransactionResponse getRentalTransactionById(String id);
    List<GetRentalTransactionResponse> getAllRentalTransactions(GetRentalTransactionRequest request);
    GetRentalTransactionResponse updateRentalTransaction(String id, UpdateRentalTransactionRequest request);
    boolean deleteRentalTransaction(String id);
}