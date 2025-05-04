package vn.proptech.rental.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import vn.proptech.rental.api.common.ApiResponse;
import vn.proptech.rental.application.dto.input.AddRentalTransactionRequest;
import vn.proptech.rental.application.dto.input.GetRentalTransactionRequest;
import vn.proptech.rental.application.dto.input.UpdateRentalTransactionRequest;
import vn.proptech.rental.application.dto.output.GetRentalTransactionResponse;
import vn.proptech.rental.application.service.RentalTransactionService;

import java.util.List;

@RestController
@RequestMapping("/api/rentals/transactions")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Rental Transaction", description = "Rental Transaction management APIs")
public class RentalTransactionController {

    private final RentalTransactionService transactionService;

    @PostMapping
    public ResponseEntity<ApiResponse<GetRentalTransactionResponse>> createRentalTransaction(
            @RequestBody AddRentalTransactionRequest request) {
        log.info("REST request to create transaction: {}", request);
        try {
            GetRentalTransactionResponse response = transactionService.createRentalTransaction(request);
            return ApiResponse.created(response);
        } catch (Exception e) {
            log.error("Error creating transaction: {}", e.getMessage(), e);
            return ApiResponse.error("Failed to create transaction: " + e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GetRentalTransactionResponse>> getRentalTransactionById(
            @PathVariable String id) {
        log.info("REST request to get transaction by ID: {}", id);
        GetRentalTransactionResponse response = transactionService.getRentalTransactionById(id);
        if (response != null) {
            return ApiResponse.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<GetRentalTransactionResponse>>> getAllRentalTransactions(
            @ModelAttribute GetRentalTransactionRequest request) {
        log.info("REST request to get all transactions with filter: {}", request);
        List<GetRentalTransactionResponse> responses = transactionService.getAllRentalTransactions(request);
        return ApiResponse.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GetRentalTransactionResponse>> updateRentalTransaction(
            @PathVariable String id,
            @RequestBody UpdateRentalTransactionRequest request) {
        log.info("REST request to update transaction ID: {} with data: {}", id, request);
        try {
            GetRentalTransactionResponse response = transactionService.updateRentalTransaction(id, request);
            return ApiResponse.updated(response);
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteRentalTransaction(@PathVariable String id) {
        log.info("REST request to delete transaction ID: {}", id);
        boolean result = transactionService.deleteRentalTransaction(id);
        return result ? ApiResponse.noContent() : ApiResponse.error("Transaction not found with ID: " + id, null, HttpStatus.NOT_FOUND);
    }
}