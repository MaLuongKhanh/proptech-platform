package vn.proptech.sale.application.mapper.input;

import vn.proptech.sale.application.dto.input.AddTransactionRequest;
import vn.proptech.sale.domain.model.Transaction;

public class AddTransactionRequestMapper {
    public static Transaction AddTransactionMapDTOToEntity(AddTransactionRequest request, String id) {
        return Transaction.builder()
                .id(id)
                .listingId(request.getListingId())
                .agentId(request.getAgentId())
                .buyerName(request.getBuyerName())
                .buyerIdentity(request.getBuyerIdentity())
                .price(request.getPrice())
                .agentId(request.getAgentId())
                .status(request.getStatus())
                .build();
    }
}