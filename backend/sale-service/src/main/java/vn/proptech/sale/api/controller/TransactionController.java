package vn.proptech.sale.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import vn.proptech.sale.api.common.ApiResponse;
import vn.proptech.sale.application.dto.input.AddTransactionRequest;
import vn.proptech.sale.application.dto.input.GetTransactionRequest;
import vn.proptech.sale.application.dto.input.UpdateTransactionRequest;
import vn.proptech.sale.application.dto.output.GetTransactionResponse;
import vn.proptech.sale.application.service.TransactionService;

import java.util.List;

@RestController
@RequestMapping("/api/sales/transactions")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Sale Transaction", description = "Sale Transaction management APIs")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<ApiResponse<GetTransactionResponse>> createTransaction(
            @RequestBody AddTransactionRequest request) {
        log.info("REST request to create transaction: {}", request);
        try {
            GetTransactionResponse response = transactionService.createTransaction(request);
            return ApiResponse.created(response);
        } catch (Exception e) {
            log.error("Error creating transaction: {}", e.getMessage(), e);
            return ApiResponse.error("Failed to create transaction: " + e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GetTransactionResponse>> getTransactionById(
            @PathVariable String id) {
        log.info("REST request to get transaction by ID: {}", id);
        GetTransactionResponse response = transactionService.getTransactionById(id);
        if (response != null) {
            return ApiResponse.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<GetTransactionResponse>>> getAllTransactions(
            @ModelAttribute GetTransactionRequest request) {
        log.info("REST request to get all transactions with filter: {}", request);
        List<GetTransactionResponse> responses = transactionService.getAllTransactions(request);
        return ApiResponse.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GetTransactionResponse>> updateTransaction(
            @PathVariable String id,
            @RequestBody UpdateTransactionRequest request) {
        log.info("REST request to update transaction ID: {} with data: {}", id, request);
        try {
            GetTransactionResponse response = transactionService.updateTransaction(id, request);
            return ApiResponse.updated(response);
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteTransaction(@PathVariable String id) {
        log.info("REST request to delete transaction ID: {}", id);
        boolean result = transactionService.deleteTransaction(id);
        return result ? ApiResponse.noContent() : ApiResponse.error("Transaction not found with ID: " + id, null, HttpStatus.NOT_FOUND);
    }
}