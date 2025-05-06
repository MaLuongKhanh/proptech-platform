package vn.proptech.payment.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import vn.proptech.payment.api.common.ApiResponse;
import vn.proptech.payment.application.dto.input.AddWalletRequest;
import vn.proptech.payment.application.dto.input.UpdateWalletRequest;
import vn.proptech.payment.application.dto.output.GetWalletResponse;
import vn.proptech.payment.application.service.WalletService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments/wallets")
@Tag(name = "Wallet API", description = "Endpoints for managing user wallets")
public class WalletController {

    private final WalletService walletService;

    @PostMapping
    public ResponseEntity<ApiResponse<GetWalletResponse>> createWallet(
        @RequestBody AddWalletRequest request) {
        log.info("REST request to create wallet: {}", request);
        try {
            GetWalletResponse response = walletService.createWallet(request);
            return ApiResponse.created(response);
        } catch (Exception e) {
            log.error("Error creating wallet: {}", e.getMessage(), e);
            return ApiResponse.error("Failed to create wallet: " + e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<GetWalletResponse>> getWalletByUserId(
        @PathVariable String userId) {
        log.info("Fetching wallet for user: {}", userId);
        try {
            GetWalletResponse response = walletService.getWalletByUserId(userId);
            if (response != null) {
                return ApiResponse.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error fetching wallet: {}", e.getMessage(), e);
            return ApiResponse.error("Failed to fetch wallet: " + e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GetWalletResponse>> updateWallet(
            @PathVariable String id,
            @RequestBody UpdateWalletRequest request) {
        log.info("Manual balance update for id: {}, data: {}", id, request);
        try {
            GetWalletResponse response = walletService.updateWallet(id, request);
            return ApiResponse.updated(response);
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/topup")
    public ResponseEntity<ApiResponse<GetWalletResponse>> topUpWallet(
        @PathVariable String id,
        @RequestParam String amount) {
        log.info("Top up wallet with id: {}, amount: {}", id, amount);
        try {
            GetWalletResponse response = walletService.topUpWallet(id, amount);
            return ApiResponse.ok(response);
        } catch (Exception e) {
            log.error("Error topping up wallet: {}", e.getMessage(), e);
            return ApiResponse.error("Failed to top up wallet: " + e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/payment")
    public ResponseEntity<ApiResponse<GetWalletResponse>> paymentWallet(
        @PathVariable String id,
        @RequestParam String amount) {
        log.info("Payment from wallet with id: {}, amount: {}", id, amount);
        try {
            GetWalletResponse response = walletService.paymentWallet(id, amount);
            return ApiResponse.ok(response);
        } catch (Exception e) {
            log.error("Error processing payment from wallet: {}", e.getMessage(), e);
            return ApiResponse.error("Failed to process payment from wallet: " + e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteWallet(
        @PathVariable String id) {
        log.info("Delete wallet with id: {}", id);
        try {
            boolean response = walletService.deleteWallet(id);
            return response ? ApiResponse.noContent() : ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error deleting wallet: {}", e.getMessage(), e);
            return ApiResponse.error("Failed to delete wallet: " + e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }
    }
}