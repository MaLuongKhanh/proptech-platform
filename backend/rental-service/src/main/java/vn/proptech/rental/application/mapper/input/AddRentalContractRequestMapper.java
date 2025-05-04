package vn.proptech.rental.application.mapper.input;

import vn.proptech.rental.application.dto.input.AddRentalContractRequest;
import vn.proptech.rental.domain.model.RentalContract;

public class AddRentalContractRequestMapper {
    public static RentalContract AddContractMapDTOToEntity(AddRentalContractRequest request, String fileUrl, String id) {
        return RentalContract.builder()
                .id(id)
                .rentalTransactionId(request.getRentalTransactionId())
                .fileName(request.getFileName())
                .fileUrl(fileUrl)
                .uploadBy(request.getUploadBy())
                .build();
    }
}