package vn.proptech.sale.application.mapper.output;

import org.springframework.stereotype.Component;
import vn.proptech.sale.application.dto.output.GetTransactionResponse;
import vn.proptech.sale.domain.model.Transaction;

@Component
public class GetTransactionResponseMapper {
    public static GetTransactionResponse GetTransactionMapEntityToDTO(Transaction transaction) {
        return GetTransactionResponse.builder()
                .id(transaction.getId())
                .propertyId(transaction.getPropertyId())
                .listingId(transaction.getListingId())
                .buyerName(transaction.getBuyerName())
                .buyerIdentity(transaction.getBuyerIdentity())
                .price(transaction.getPrice())
                .transactionDate(transaction.getTransactionDate())
                .depositDate(transaction.getDepositDate())
                .agentId(transaction.getAgentId())
                .status(transaction.getStatus())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .isActive(transaction.isActive())
                .build();
    }
}