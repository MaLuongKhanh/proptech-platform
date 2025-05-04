package vn.proptech.rental.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import vn.proptech.rental.api.common.ApiResponse;
import vn.proptech.rental.application.dto.input.AddRentalContractRequest;
import vn.proptech.rental.application.dto.input.GetRentalContractRequest;
import vn.proptech.rental.application.dto.input.UpdateRentalContractRequest;
import vn.proptech.rental.application.dto.output.GetRentalContractResponse;
import vn.proptech.rental.application.service.RentalContractService;

import java.util.List;

@RestController
@RequestMapping("/api/rentals/contracts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Rental Contract", description = "Rental Contract management APIs")
public class RentalContractController {

    private final RentalContractService contractService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<GetRentalContractResponse>> createRentalContract(@ModelAttribute AddRentalContractRequest request) {
            
        log.info("REST request to create contract with file: {}, transactionId: {}", request.getFile(), request.getRentalTransactionId());
        try {
            if (request.getFile() == null) {
                return ApiResponse.error("File is required", null, HttpStatus.BAD_REQUEST);
            }
            GetRentalContractResponse response = contractService.createRentalContract(request);
            return ApiResponse.created(response);

        } catch (IllegalArgumentException e) {
            return ApiResponse.error(e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GetRentalContractResponse>> getRentalContractById(
            @PathVariable String id) {
        log.info("REST request to get contract by ID: {}", id);
        GetRentalContractResponse response = contractService.getRentalContractById(id);
        if (response != null) {
            return ApiResponse.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<GetRentalContractResponse>>> getAllRentalContracts(
            @ModelAttribute GetRentalContractRequest request) {
        log.info("REST request to get all contracts with filter: {}", request);
        List<GetRentalContractResponse> responses = contractService.getAllRentalContracts(request);
        return ApiResponse.ok(responses);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<GetRentalContractResponse>> updateRentalContract(
        @PathVariable String id,
        @ModelAttribute UpdateRentalContractRequest request) {
        log.info("REST request to update contract ID: {}", id);
        try {
            GetRentalContractResponse response = contractService.updateRentalContract(id, request);
            return ApiResponse.updated(response);
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }    
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteRentalContract(@PathVariable String id) {
        log.info("REST request to delete contract ID: {}", id);
        boolean result = contractService.deleteRentalContract(id);
        return result ? ApiResponse.noContent() : ApiResponse.error("Failed to delete contract", null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}