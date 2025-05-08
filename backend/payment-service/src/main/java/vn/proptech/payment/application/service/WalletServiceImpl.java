package vn.proptech.payment.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.proptech.payment.application.dto.input.AddWalletRequest;
import vn.proptech.payment.application.dto.input.UpdateWalletRequest;
import vn.proptech.payment.application.dto.output.GetWalletResponse;
import vn.proptech.payment.application.mapper.input.AddWalletRequestMapper;
import vn.proptech.payment.application.mapper.output.GetWalletResponseMapper;
import vn.proptech.payment.domain.model.Wallet;
import vn.proptech.payment.domain.repository.WalletRepository;
import vn.proptech.payment.infrastructure.messaging.WalletEventPublisher;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletEventPublisher eventPublisher;

    @Override
    public GetWalletResponse createWallet(AddWalletRequest request) {
        log.info("Creating wallet with request: {}", request);
        try {
            String newId = UUID.randomUUID().toString();
            // Check if wallet already exists for user
            GetWalletResponse existingWallet = null;
            try {
                existingWallet = this.getWalletByUserId(request.getUserId());
            } catch (RuntimeException e) {
                // Ignore error, wallet doesn't exist which is what we want
            }
            if (existingWallet != null) {
                throw new Exception("Wallet for this user already exists");
            }

            // Map DTO to Entity
            Wallet wallet = AddWalletRequestMapper.AddWalletMapDTOToEntity(
                request, 
                newId
            );

            wallet.setActive(true);

            wallet.setCreatedAt(Instant.now());

            wallet.setUpdatedAt(Instant.now());

            Wallet savedWallet = walletRepository.save(wallet);

            eventPublisher.publishWalletCreatedEvent(savedWallet);

            log.info("Created new wallet for user: {}", request.getUserId());
            
            return GetWalletResponseMapper.GetWalletMapEntityToDTO(savedWallet);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create wallet: " + e.getMessage(), e);
        }
    }

    @Override
    public GetWalletResponse getWalletByUserId(String userId) {
        try {
            Wallet wallet = walletRepository.findByUserId(userId);
            return GetWalletResponseMapper.GetWalletMapEntityToDTO(wallet);
        } catch (Exception e) {
            log.error("Error fetching wallet: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch wallet with user ID" + userId + ": " + e.getMessage(), e);
        }
    }

    @Override
    public GetWalletResponse updateWallet(String id, UpdateWalletRequest request) {
        
        try {
            Wallet existingWallet = walletRepository.findById(id);
            if (existingWallet == null) {
                throw new RuntimeException("Wallet not found with ID: " + id);
            }
            if (request.getBalance() != null) {
                if (request.getBalance().signum() < 0) {
                    throw new Exception("Insufficient balance in wallet");
                }
                existingWallet.setBalance(request.getBalance());
            }
            if (request.getCurrency() != null) {
                existingWallet.setCurrency(request.getCurrency());
            }
            existingWallet.setActive(true);

            existingWallet.setUpdatedAt(Instant.now());

            Wallet updatedWallet = walletRepository.save(existingWallet);

            eventPublisher.publishWalletUpdatedEvent(updatedWallet);

            return GetWalletResponseMapper.GetWalletMapEntityToDTO(updatedWallet);
        } catch (Exception e) {
            log.error("Error updating wallet: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update wallet: " + e.getMessage(), e);
        }
    }

    public GetWalletResponse topUpWallet(String id, String amount) {
        try {
            Wallet existingWallet = walletRepository.findById(id);
            if (existingWallet == null) {
                throw new RuntimeException("Wallet not found with ID: " + id);
            }
            existingWallet.setBalance(existingWallet.getBalance().add(new BigDecimal(amount)));

            existingWallet.setUpdatedAt(Instant.now());

            Wallet updatedWallet = walletRepository.save(existingWallet);

            eventPublisher.publishWalletUpdatedEvent(updatedWallet);

            return GetWalletResponseMapper.GetWalletMapEntityToDTO(updatedWallet);
        } catch (Exception e) {
            log.error("Error topping up wallet: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to top up wallet: " + e.getMessage(), e);
        }
    }

    public GetWalletResponse paymentWallet(String id, String amount) {
        try {
            Wallet existingWallet = walletRepository.findById(id);
            if (existingWallet == null) {
                throw new RuntimeException("Wallet not found with ID: " + id);
            }
            if (existingWallet.getBalance().compareTo(new BigDecimal(amount)) < 0) {
                throw new Exception("Insufficient balance in wallet");
            }
            existingWallet.setBalance(existingWallet.getBalance().subtract(new BigDecimal(amount)));

            existingWallet.setUpdatedAt(Instant.now());

            Wallet updatedWallet = walletRepository.save(existingWallet);

            eventPublisher.publishWalletUpdatedEvent(updatedWallet);

            return GetWalletResponseMapper.GetWalletMapEntityToDTO(updatedWallet);
        } catch (Exception e) {
            log.error("Error processing payment from wallet: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process payment from wallet: " + e.getMessage(), e);
        }
    }

    public boolean deleteWallet(String id) {
        try {
            Wallet existingWallet = walletRepository.findById(id);
            if (existingWallet == null) {
                throw new RuntimeException("Wallet not found with ID: " + id);
            }
            existingWallet.setActive(false);

            existingWallet.setUpdatedAt(Instant.now());

            Wallet updatedWallet = walletRepository.save(existingWallet);

            eventPublisher.publishWalletDeletedEvent(updatedWallet);

            return true;
        } catch (Exception e) {
            log.error("Error deleting wallet: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete wallet: " + e.getMessage(), e);
        }
    }
}