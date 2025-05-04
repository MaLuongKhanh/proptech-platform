package vn.proptech.sale.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import vn.proptech.sale.api.common.ApiResponse;
import vn.proptech.sale.application.dto.input.AddContractRequest;
import vn.proptech.sale.application.dto.input.GetContractRequest;
import vn.proptech.sale.application.dto.input.UpdateContractRequest;
import vn.proptech.sale.application.dto.output.GetContractResponse;
import vn.proptech.sale.application.service.ContractService;

import java.util.List;

@RestController
@RequestMapping("/api/sales/contracts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Sale Contract", description = "Sale Contract management APIs")
public class ContractController {

    private final ContractService contractService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<GetContractResponse>> createContract(@ModelAttribute AddContractRequest request) {
            
        log.info("REST request to create contract with file: {}, transactionId: {}", request.getFile(), request.getTransactionId());
        try {
            if (request.getFile() == null) {
                return ApiResponse.error("File is required", null, HttpStatus.BAD_REQUEST);
            }
            GetContractResponse response = contractService.createContract(request);
            return ApiResponse.created(response);

        } catch (IllegalArgumentException e) {
            return ApiResponse.error(e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GetContractResponse>> getContractById(
            @PathVariable String id) {
        log.info("REST request to get contract by ID: {}", id);
        GetContractResponse response = contractService.getContractById(id);
        if (response != null) {
            return ApiResponse.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<GetContractResponse>>> getAllContracts(
            @ModelAttribute GetContractRequest request) {
        log.info("REST request to get all contracts with filter: {}", request);
        List<GetContractResponse> responses = contractService.getAllContracts(request);
        return ApiResponse.ok(responses);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<GetContractResponse>> updateContract(
        @PathVariable String id,
        @ModelAttribute UpdateContractRequest request) {
        log.info("REST request to update contract ID: {}", id);
        try {
            GetContractResponse response = contractService.updateContract(id, request);
            return ApiResponse.updated(response);
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }    
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteContract(@PathVariable String id) {
        log.info("REST request to delete contract ID: {}", id);
        boolean result = contractService.deleteContract(id);
        return result ? ApiResponse.noContent() : ApiResponse.error("Failed to delete contract", null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}