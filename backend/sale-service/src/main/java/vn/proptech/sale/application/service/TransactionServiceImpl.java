package vn.proptech.sale.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.proptech.sale.application.dto.input.AddTransactionRequest;
import vn.proptech.sale.application.dto.input.GetTransactionRequest;
import vn.proptech.sale.application.dto.input.UpdateTransactionRequest;
import vn.proptech.sale.application.dto.output.GetTransactionResponse;
import vn.proptech.sale.application.mapper.input.AddTransactionRequestMapper;
import vn.proptech.sale.application.mapper.output.GetTransactionResponseMapper;
import vn.proptech.sale.domain.model.Transaction;
import vn.proptech.sale.domain.repository.TransactionRepository;
import vn.proptech.sale.infrastructure.messaging.TransactionEventPublisher;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionEventPublisher eventPublisher;

    @Override
    public GetTransactionResponse createTransaction(AddTransactionRequest request) {
        log.info("Creating transaction with request: {}", request);
        try {
            String newId = UUID.randomUUID().toString();
            //find propertyId by listingId

            // Map DTO to Entity
            Transaction transaction = AddTransactionRequestMapper.AddTransactionMapDTOToEntity(
                request, 
                newId
            );
            transaction.setActive(true);

            Transaction savedTransaction = transactionRepository.save(transaction);

            // Publish event
            eventPublisher.publishTransactionCreatedEvent(savedTransaction);

            log.info("Transaction created successfully with ID: {}", savedTransaction.getId());
            return GetTransactionResponseMapper.GetTransactionMapEntityToDTO(savedTransaction);
        } catch (Exception e) {
            log.error("Error creating transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create transaction: " + e.getMessage(), e);
        }
    }

    @Override
    public GetTransactionResponse getTransactionById(String id) {
        log.info("Fetching transaction with ID: {}", id);
        try {
            Transaction transaction = transactionRepository.findById(id);
            if (transaction == null) {
                throw new RuntimeException("Transaction not found with ID: " + id);
            }
            return GetTransactionResponseMapper.GetTransactionMapEntityToDTO(transaction);
        } catch (Exception e) {
            log.error("Error fetching transaction: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch transaction with ID" + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public List<GetTransactionResponse> getAllTransactions(GetTransactionRequest request) {
        log.info("Fetching all transactions");
        try {
            int page = (request.getPage() != null && request.getPage() >= 0) ? request.getPage() : 0;
            int size = (request.getSize() != null && request.getSize() > 0) ? request.getSize() : 10;
            String sort = (request.getSort() != null && !request.getSort().isEmpty()) ? 
                request.getSort() : "createdAt";
            String direction = (request.getDirection() != null && !request.getDirection().isEmpty()) ? 
                request.getDirection().toUpperCase() : "DESC";
            log.debug("Pagination: page={}, size={}, sort={}, direction={}", page, size, sort, direction);

            int offset = page > 0 ? (page - 1) * size : 0;

            List<Transaction> transactions = transactionRepository.findAll(
                size, 
                offset, 
                sort, 
                direction, 
                request.getPropertyId(),
                request.getListingId(),
                request.getAgentId(),
                request.getStartDate(),
                request.getEndDate(),
                request.getStatus()
            );
            log.debug("Fetched transactions: {}", transactions);

            return transactions.stream()
                .map(GetTransactionResponseMapper::GetTransactionMapEntityToDTO)
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching all transactions: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch all transactions: " + e.getMessage(), e);
        }
    }

    @Override
    public GetTransactionResponse updateTransaction(String id, UpdateTransactionRequest request) {
        log.info("Updating transaction with ID: {}", id);
        try {
            Transaction existingTransaction = transactionRepository.findById(id);
            if (existingTransaction == null) {
                throw new RuntimeException("Transaction not found with ID: " + id);
            }

            // Update fields with null checks
            if (request.getListingId() != null) {
                existingTransaction.setListingId(request.getListingId());
            }
            if (request.getBuyerName() != null) {
                existingTransaction.setBuyerName(request.getBuyerName());
            }
            if (request.getBuyerIdentity() != null) {
                existingTransaction.setBuyerIdentity(request.getBuyerIdentity());
            }
            if (request.getPrice() > 0) {
                existingTransaction.setPrice(request.getPrice());
            }
            if (request.getTransactionDate() != null) {
                existingTransaction.setTransactionDate(request.getTransactionDate());
            }
            if (request.getDepositDate() != null) {
                existingTransaction.setDepositDate(request.getDepositDate());
            }
            if (request.getAgentId() != null) {
                existingTransaction.setAgentId(request.getAgentId());
            }
            if (request.getStatus() != null) {
                existingTransaction.setStatus(request.getStatus());
            }
            existingTransaction.setActive(true);

            Transaction updatedTransaction = transactionRepository.save(existingTransaction);
            
            // Publish event
            eventPublisher.publishTransactionUpdatedEvent(updatedTransaction);

            log.info("Transaction updated successfully with ID: {}", updatedTransaction.getId());
            return GetTransactionResponseMapper.GetTransactionMapEntityToDTO(updatedTransaction);
        } catch (Exception e) {
            log.error("Error updating transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update transaction: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteTransaction(String id) {
        log.info("Deleting transaction with ID: {}", id);
        try {
            Transaction existingTransaction = transactionRepository.findById(id);
            if (existingTransaction == null) {
                throw new RuntimeException("Transaction not found with ID: " + id);
            }
            existingTransaction.setActive(false);
            transactionRepository.save(existingTransaction);

            // Publish event
            eventPublisher.publishTransactionDeletedEvent(existingTransaction);

            log.info("Transaction deleted successfully with ID: {}", id);
            return true;
        } catch (Exception e) {
            log.error("Error deleting transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete transaction: " + e.getMessage(), e);
        }
    }
}