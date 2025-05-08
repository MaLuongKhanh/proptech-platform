package vn.proptech.rental.application.mapper.output;

import org.springframework.stereotype.Component;
import vn.proptech.rental.application.dto.output.GetRentalTransactionResponse;
import vn.proptech.rental.domain.model.RentalTransaction;

@Component
public class GetRentalTransactionResponseMapper {
    public static GetRentalTransactionResponse GetRentalTransactionMapEntityToDTO(RentalTransaction transaction) {
        return GetRentalTransactionResponse.builder()
                .id(transaction.getId())
                .propertyId(transaction.getPropertyId())
                .listingId(transaction.getListingId())
                .tenantName(transaction.getTenantName())
                .tenantIdentity(transaction.getTenantIdentity())
                .price(transaction.getPrice())
                .startDate(transaction.getStartDate())
                .endDate(transaction.getEndDate())
                .depositDate(transaction.getDepositDate())
                .agentId(transaction.getAgentId())
                .status(transaction.getStatus())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .isActive(transaction.isActive())
                .build();
    }
}