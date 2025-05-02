package vn.proptech.sale.application.mapper.input;

import vn.proptech.sale.application.dto.input.AddContractRequest;
import vn.proptech.sale.domain.model.Contract;

public class AddContractRequestMapper {
    public static Contract AddContractMapDTOToEntity(AddContractRequest request, String fileUrl, String id) {
        return Contract.builder()
                .id(id)
                .transactionId(request.getTransactionId())
                .fileName(request.getFileName())
                .fileUrl(fileUrl)
                .uploadBy(request.getUploadBy())
                .build();
    }
}