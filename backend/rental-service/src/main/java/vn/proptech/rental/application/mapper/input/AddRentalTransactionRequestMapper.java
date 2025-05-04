package vn.proptech.rental.application.mapper.input;

import vn.proptech.rental.application.dto.input.AddRentalTransactionRequest;
import vn.proptech.rental.domain.model.RentalTransaction;
import vn.proptech.rental.domain.model.RentalTransactionStatus;

public class AddRentalTransactionRequestMapper {
    public static RentalTransaction AddRentalTransactionMapDTOToEntity(AddRentalTransactionRequest request, String id) {
        return RentalTransaction.builder()
                .id(id)
                .listingId(request.getListingId())
                .agentId(request.getAgentId())
                .tenantName(request.getTenantName())
                .tenantIdentity(request.getTenantIdentity())
                .price(request.getPrice())
                .agentId(request.getAgentId())
                .status(RentalTransactionStatus.PENDING)
                .build();
    }
}