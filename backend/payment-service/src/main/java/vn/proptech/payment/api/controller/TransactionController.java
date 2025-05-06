package vn.proptech.payment.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import vn.proptech.payment.api.common.ApiResponse;
import vn.proptech.payment.application.dto.input.AddPaymentTransactionRequest;
import vn.proptech.payment.application.dto.input.GetPaymentTransactionRequest;
import vn.proptech.payment.application.dto.input.UpdatePaymentTransactionRequest;
import vn.proptech.payment.application.dto.output.GetPaymentTransactionResponse;
import vn.proptech.payment.application.service.PaymentTransactionService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments/transactions")
@Tag(name = "Payment Transaction", description = "Payment Transactions management APIs")
public class TransactionController {

    private final PaymentTransactionService transactionService;

    @PostMapping
    public ResponseEntity<ApiResponse<GetPaymentTransactionResponse>> createPaymentTransaction(
            @RequestBody AddPaymentTransactionRequest request) {
        log.info("REST request to create transaction: {}", request);
        try {
            GetPaymentTransactionResponse response = transactionService.createPaymentTransaction(request);
            return ApiResponse.created(response);
        } catch (Exception e) {
            log.error("Error creating transaction: {}", e.getMessage(), e);
            return ApiResponse.error("Failed to create transaction: " + e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GetPaymentTransactionResponse>> getPaymentTransactionById(
            @PathVariable String id) {
        log.info("REST request to get transaction by ID: {}", id);
        GetPaymentTransactionResponse response = transactionService.getPaymentTransactionById(id);
        if (response != null) {
            return ApiResponse.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<GetPaymentTransactionResponse>>> getAllPaymentTransactions(
            @ModelAttribute GetPaymentTransactionRequest request) {
        log.info("REST request to get all transactions with filter: {}", request);
        List<GetPaymentTransactionResponse> responses = transactionService.getAllPaymentTransactions(request);
        return ApiResponse.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GetPaymentTransactionResponse>> updatePaymentTransaction(
            @PathVariable String id,
            @RequestBody UpdatePaymentTransactionRequest request) {
        log.info("REST request to update transaction ID: {} with data: {}", id, request);
        try {
            GetPaymentTransactionResponse response = transactionService.updatePaymentTransaction(id, request);
            return ApiResponse.updated(response);
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deletePaymentTransaction(@PathVariable String id) {
        log.info("REST request to delete transaction ID: {}", id);
        boolean result = transactionService.deletePaymentTransaction(id);
        return result ? ApiResponse.noContent() : ApiResponse.error("Transaction not found with ID: " + id, null, HttpStatus.NOT_FOUND);
    }
}