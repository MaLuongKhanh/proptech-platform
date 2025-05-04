package vn.proptech.rental.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import vn.proptech.rental.application.dto.input.AddRentalTransactionRequest;
import vn.proptech.rental.application.dto.input.GetRentalTransactionRequest;
import vn.proptech.rental.application.dto.input.UpdateRentalTransactionRequest;
import vn.proptech.rental.application.dto.output.GetRentalTransactionResponse;
import vn.proptech.rental.application.mapper.input.AddRentalTransactionRequestMapper;
import vn.proptech.rental.application.mapper.output.GetRentalTransactionResponseMapper;
import vn.proptech.rental.domain.model.RentalTransaction;
import vn.proptech.rental.domain.repository.RentalTransactionRepository;
import vn.proptech.rental.infrastructure.messaging.RentalTransactionEventPublisher;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentalTransactionServiceImpl implements RentalTransactionService {
    private final RentalTransactionRepository transactionRepository;
    private final RentalTransactionEventPublisher eventPublisher;

    @Override
    public GetRentalTransactionResponse createRentalTransaction(AddRentalTransactionRequest request) {
        log.info("Creating transaction with request: {}", request);
        try {
            String newId = UUID.randomUUID().toString();

            // Map DTO to Entity
            RentalTransaction transaction = AddRentalTransactionRequestMapper.AddRentalTransactionMapDTOToEntity(
                request, 
                newId
            );
            transaction.setActive(true);

            RentalTransaction savedRentalTransaction = transactionRepository.save(transaction);

            // Publish event
            eventPublisher.publishRentalTransactionCreatedEvent(savedRentalTransaction);

            log.info("RentalTransaction created successfully with ID: {}", savedRentalTransaction.getId());
            return GetRentalTransactionResponseMapper.GetRentalTransactionMapEntityToDTO(savedRentalTransaction);
        } catch (Exception e) {
            log.error("Error creating transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create transaction: " + e.getMessage(), e);
        }
    }

    @Override
    public GetRentalTransactionResponse getRentalTransactionById(String id) {
        log.info("Fetching transaction with ID: {}", id);
        try {
            RentalTransaction transaction = transactionRepository.findById(id);
            if (transaction == null) {
                throw new RuntimeException("RentalTransaction not found with ID: " + id);
            }
            return GetRentalTransactionResponseMapper.GetRentalTransactionMapEntityToDTO(transaction);
        } catch (Exception e) {
            log.error("Error fetching transaction: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch transaction with ID" + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public List<GetRentalTransactionResponse> getAllRentalTransactions(GetRentalTransactionRequest request) {
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

            List<RentalTransaction> transactions = transactionRepository.findAll(
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
                .map(GetRentalTransactionResponseMapper::GetRentalTransactionMapEntityToDTO)
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching all transactions: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch all transactions: " + e.getMessage(), e);
        }
    }

    @Override
    public GetRentalTransactionResponse updateRentalTransaction(String id, UpdateRentalTransactionRequest request) {
        log.info("Updating transaction with ID: {}", id);
        try {
            RentalTransaction existingRentalTransaction = transactionRepository.findById(id);
            if (existingRentalTransaction == null) {
                throw new RuntimeException("RentalTransaction not found with ID: " + id);
            }

            // Update fields with null checks
            if (request.getListingId() != null) {
                existingRentalTransaction.setListingId(request.getListingId());
            }
            if (request.getTenantName() != null) {
                existingRentalTransaction.setTenantName(request.getTenantName());
            }
            if (request.getTenantIdentity() != null) {
                existingRentalTransaction.setTenantIdentity(request.getTenantIdentity());
            }
            if (request.getPrice() > 0) {
                existingRentalTransaction.setPrice(request.getPrice());
            }
            if (request.getStartDate() != null) {
                existingRentalTransaction.setStartDate(request.getStartDate());
            }
            if (request.getEndDate() != null) {
                existingRentalTransaction.setEndDate(request.getEndDate());
            }
            if (request.getDepositDate() != null) {
                existingRentalTransaction.setDepositDate(request.getDepositDate());
            }
            if (request.getAgentId() != null) {
                existingRentalTransaction.setAgentId(request.getAgentId());
            }
            if (request.getStatus() != null) {
                existingRentalTransaction.setStatus(request.getStatus());
            }
            existingRentalTransaction.setActive(true);

            RentalTransaction updatedRentalTransaction = transactionRepository.save(existingRentalTransaction);
            
            // Publish event
            eventPublisher.publishRentalTransactionUpdatedEvent(updatedRentalTransaction);

            log.info("RentalTransaction updated successfully with ID: {}", updatedRentalTransaction.getId());
            return GetRentalTransactionResponseMapper.GetRentalTransactionMapEntityToDTO(updatedRentalTransaction);
        } catch (Exception e) {
            log.error("Error updating transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update transaction: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteRentalTransaction(String id) {
        log.info("Deleting transaction with ID: {}", id);
        try {
            RentalTransaction existingRentalTransaction = transactionRepository.findById(id);
            if (existingRentalTransaction == null) {
                throw new RuntimeException("RentalTransaction not found with ID: " + id);
            }
            existingRentalTransaction.setActive(false);
            transactionRepository.save(existingRentalTransaction);

            // Publish event
            eventPublisher.publishRentalTransactionDeletedEvent(existingRentalTransaction);

            log.info("RentalTransaction deleted successfully with ID: {}", id);
            return true;
        } catch (Exception e) {
            log.error("Error deleting transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete transaction: " + e.getMessage(), e);
        }
    }
}