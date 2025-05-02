package vn.proptech.sale.application.service;

import vn.proptech.sale.application.dto.input.AddContractRequest;
import vn.proptech.sale.application.dto.input.GetContractRequest;
import vn.proptech.sale.application.dto.input.UpdateContractRequest;
import vn.proptech.sale.application.dto.output.GetContractResponse;

import java.util.List;

public interface ContractService {
    GetContractResponse createContract(AddContractRequest request);
    GetContractResponse getContractById(String id);
    List<GetContractResponse> getAllContracts(GetContractRequest request);
    GetContractResponse updateContract(String id, UpdateContractRequest request);
    boolean deleteContract(String id);
}