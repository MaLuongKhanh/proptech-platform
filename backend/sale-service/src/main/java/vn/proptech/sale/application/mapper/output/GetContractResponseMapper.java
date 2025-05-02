package vn.proptech.sale.application.mapper.output;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import vn.proptech.sale.application.dto.output.GetContractResponse;
import vn.proptech.sale.domain.model.Contract;

@Component
public class GetContractResponseMapper {
    public static GetContractResponse GetContractMapEntityToDTO(Contract contract) {
        return GetContractResponse.builder()
            .id(contract.getId())
            .transactionId(contract.getTransactionId())
            .fileUrl(contract.getFileUrl())
            .uploadBy(contract.getUploadBy())
            .fileName(contract.getFileName())
            .build();
    }

    public static List<GetContractResponse> GetContractMapEntityToDTO(List<Contract> contracts) {
        return contracts.stream()
            .map(GetContractResponseMapper::GetContractMapEntityToDTO)
            .collect(Collectors.toList());
    }
}