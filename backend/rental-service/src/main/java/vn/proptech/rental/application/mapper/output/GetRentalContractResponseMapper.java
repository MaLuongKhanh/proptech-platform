package vn.proptech.rental.application.mapper.output;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import vn.proptech.rental.application.dto.output.GetRentalContractResponse;
import vn.proptech.rental.domain.model.RentalContract;

@Component
public class GetRentalContractResponseMapper {
    public static GetRentalContractResponse GetRentalContractMapEntityToDTO(RentalContract contract) {
        return GetRentalContractResponse.builder()
            .id(contract.getId())
            .rentalTransactionId(contract.getRentalTransactionId())
            .fileUrl(contract.getFileUrl())
            .uploadBy(contract.getUploadBy())
            .fileName(contract.getFileName())
            .isActive(contract.isActive())
            .createdAt(contract.getCreatedAt())
            .build();
    }

    public static List<GetRentalContractResponse> GetRentalContractMapEntityToDTO(List<RentalContract> contracts) {
        return contracts.stream()
            .map(GetRentalContractResponseMapper::GetRentalContractMapEntityToDTO)
            .collect(Collectors.toList());
    }
}