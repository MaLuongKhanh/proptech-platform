package vn.proptech.payment.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import vn.proptech.payment.application.dto.input.AddPaymentTransactionRequest;
import vn.proptech.payment.application.dto.input.GetPaymentTransactionRequest;
import vn.proptech.payment.application.dto.input.UpdatePaymentTransactionRequest;
import vn.proptech.payment.application.dto.output.GetPaymentTransactionResponse;
import vn.proptech.payment.application.mapper.input.AddPaymentTransactionRequestMapper;
import vn.proptech.payment.application.mapper.output.GetPaymentTransactionResponseMapper;
import vn.proptech.payment.domain.model.PaymentTransaction;
import vn.proptech.payment.domain.repository.PaymentTransactionRepository;
import vn.proptech.payment.infrastructure.messaging.PaymentTransactionEventPublisher;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentTransactionServiceImpl implements PaymentTransactionService {

    private final PaymentTransactionRepository transactionRepository;
    private final PaymentTransactionEventPublisher eventPublisher;

    @Override
    public GetPaymentTransactionResponse createPaymentTransaction(AddPaymentTransactionRequest request) {
        log.info("Creating payment transaction with request: {}", request);
        try {
            String newId = UUID.randomUUID().toString();

            // Map DTO to Entity
            PaymentTransaction transaction = AddPaymentTransactionRequestMapper.AddPaymentTransactionMapDTOToEntity(
                request, 
                newId
            );

            transaction.setActive(true);

            transaction.setCreatedAt(Instant.now());

            PaymentTransaction savedTransaction = transactionRepository.save(transaction);

            eventPublisher.publishPaymentTransactionCreatedEvent(savedTransaction);

            log.info("Transaction created successflly with ID: {}", savedTransaction);
            return GetPaymentTransactionResponseMapper.GetPaymentTransactionMapEntityToDTO(savedTransaction);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create transaction:" + e.getMessage(), e);
        }
    }
    
    @Override
    public GetPaymentTransactionResponse getPaymentTransactionById(String id) {
        log.info("Fetching transaction with ID: {}", id);
        try {
            PaymentTransaction transaction = transactionRepository.findById(id);
            if (transaction == null) {
                throw new RuntimeException("Transaction not found with ID: " + id);
            }
            return GetPaymentTransactionResponseMapper.GetPaymentTransactionMapEntityToDTO(transaction);
        } catch (Exception e) {
            log.error("Error fetching transaction: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch transaction with ID" + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public List<GetPaymentTransactionResponse> getAllPaymentTransactions(GetPaymentTransactionRequest request) {
        log.info("Fetching all transactions with request: {}", request);
        try {
            int page = (request.getPage() != null && request.getPage() >= 0) ? request.getPage() : 0;
            int size = (request.getSize() != null && request.getSize() > 0) ? request.getSize() : 10;
            String sort = (request.getSort() != null && !request.getSort().isEmpty()) ? request.getSort() : "createdAt";
            String direction = (request.getDirection() != null && !request.getDirection().isEmpty()) ? 
                    request.getDirection().toUpperCase() : "DESC";

            int offset = page > 0 ? (page - 1) * size : 0;

            List<PaymentTransaction> transactions = transactionRepository.findAll(
                size, 
                offset, 
                sort, 
                direction, 
                request.getWalletId(), 
                request.getStartDate(), 
                request.getEndDate(), 
                request.getStatus(),
                request.getType()
            );
            return transactions.stream()
                    .map(GetPaymentTransactionResponseMapper::GetPaymentTransactionMapEntityToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching all transactions: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch all transactions: " + e.getMessage(), e);
        }
    }

    @Override
    public GetPaymentTransactionResponse updatePaymentTransaction(String id, UpdatePaymentTransactionRequest request) {
        log.info("Updating transaction with ID: {}", id);
        try {
            PaymentTransaction existingPaymentTransaction = transactionRepository.findById(id);
            if (existingPaymentTransaction == null) {
                throw new RuntimeException("PaymentTransaction not found with ID: " + id);
            }

            // Update fields with null checks
            if (request.getWalletId() != null) {
                existingPaymentTransaction.setWalletId(request.getWalletId());
            }
            if (request.getAmount() != null) {
                existingPaymentTransaction.setAmount(request.getAmount());
            }
            if (request.getDescription() != null) {
                existingPaymentTransaction.setDescription(request.getDescription());
            }
            if (request.getType() != null) {
                existingPaymentTransaction.setType(request.getType());
            }
            if (request.getStatus() != null) {
                existingPaymentTransaction.setStatus(request.getStatus());
            }
            existingPaymentTransaction.setActive(true);

            PaymentTransaction updatedPaymentTransaction = transactionRepository.save(existingPaymentTransaction);
            
            // Publish event
            eventPublisher.publishPaymentTransactionUpdatedEvent(updatedPaymentTransaction);

            log.info("PaymentTransaction updated successfully with ID: {}", updatedPaymentTransaction.getId());
            return GetPaymentTransactionResponseMapper.GetPaymentTransactionMapEntityToDTO(updatedPaymentTransaction);
        } catch (Exception e) {
            log.error("Error updating transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update transaction: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deletePaymentTransaction(String id) {
        log.info("Deleting transaction with ID: {}", id);
        try {
            PaymentTransaction existingPaymentTransaction = transactionRepository.findById(id);
            if (existingPaymentTransaction == null) {
                throw new RuntimeException("PaymentTransaction not found with ID: " + id);
            }
            existingPaymentTransaction.setActive(false);
            transactionRepository.save(existingPaymentTransaction);

            // Publish event
            eventPublisher.publishPaymentTransactionDeletedEvent(existingPaymentTransaction);

            log.info("PaymentTransaction deleted successfully with ID: {}", id);
            return true;
        } catch (Exception e) {
            log.error("Error deleting transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete transaction: " + e.getMessage(), e);
        }
    }
}