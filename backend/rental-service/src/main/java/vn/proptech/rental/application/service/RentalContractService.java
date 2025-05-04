package vn.proptech.rental.application.service;

import vn.proptech.rental.application.dto.input.GetRentalContractRequest;
import vn.proptech.rental.application.dto.input.AddRentalContractRequest;
import vn.proptech.rental.application.dto.input.UpdateRentalContractRequest;
import vn.proptech.rental.application.dto.output.GetRentalContractResponse;

import java.util.List;

public interface RentalContractService {
    GetRentalContractResponse createRentalContract(AddRentalContractRequest request);
    GetRentalContractResponse getRentalContractById(String id);
    List<GetRentalContractResponse> getAllRentalContracts(GetRentalContractRequest request);
    GetRentalContractResponse updateRentalContract(String id, UpdateRentalContractRequest request);
    boolean deleteRentalContract(String id);
}