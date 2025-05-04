package vn.proptech.sale.application.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import vn.proptech.sale.application.dto.input.AddContractRequest;
import vn.proptech.sale.application.dto.input.GetContractRequest;
import vn.proptech.sale.application.dto.input.UpdateContractRequest;
import vn.proptech.sale.application.dto.output.GetContractResponse;
import vn.proptech.sale.application.extension.CommonCloudinaryAttribute;
import vn.proptech.sale.application.extension.CommonFileType;
import vn.proptech.sale.application.mapper.input.AddContractRequestMapper;
import vn.proptech.sale.application.mapper.output.GetContractResponseMapper;
import vn.proptech.sale.domain.model.Contract;
import vn.proptech.sale.domain.repository.ContractRepository;
import vn.proptech.sale.infrastructure.messaging.ContractEventPublisher;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final ContractEventPublisher eventPublisher;
    private final Cloudinary cloudinary;

    @Override
    public GetContractResponse createContract(AddContractRequest request) {
        log.info("Creating contract with request: {}", request);
        try {
            String newId = UUID.randomUUID().toString();
            String assetFolderImage = CommonCloudinaryAttribute.assetFolderContract;
            String publicId = Contract.class.getSimpleName() + "_" + newId;

            String fileUrl = uploadFileToCloudinary(request.getFile(), assetFolderImage, publicId, cloudinary);

            // Map DTO to Entity
            Contract contract = AddContractRequestMapper.AddContractMapDTOToEntity(
                request, 
                fileUrl, 
                newId
            );
            contract.setActive(true);

            contract.setCreatedAt(Instant.now());

            Contract savedContract = contractRepository.save(contract);

            // Publish event
            eventPublisher.publishContractCreatedEvent(savedContract);

            log.info("Contract created successfully with ID: {}", savedContract.getId());
            return GetContractResponseMapper.GetContractMapEntityToDTO(savedContract);
        } catch (Exception e) {
            log.error("Error creating contract: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create contract: " + e.getMessage(), e);
        }
    }

    @Override
    public GetContractResponse getContractById(String id) {
        log.info("Fetching contract with ID: {}", id);
        try {
            Contract contract = contractRepository.findById(id);
            if (contract == null) {
                throw new RuntimeException("Contract not found with ID: " + id);
            }
            return GetContractResponseMapper.GetContractMapEntityToDTO(contract);
        } catch (Exception e) {
            log.error("Error fetching contract: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch contract with ID" + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public List<GetContractResponse> getAllContracts(GetContractRequest request) {
        log.info("Fetching all contracts with request: {}", request);
        try {
            int page = (request.getPage() != null && request.getPage() >= 0) ? request.getPage() : 0;
            int size = (request.getSize() != null && request.getSize() > 0) ? request.getSize() : 10;
            String sort = (request.getSort() != null && !request.getSort().isEmpty()) ? request.getSort() : "createdAt";
            String direction = (request.getDirection() != null && !request.getDirection().isEmpty()) ? 
                    request.getDirection().toUpperCase() : "DESC";

            String transactionId = request.getTransactionId();
            String uploadBy = request.getUploadBy();
            Instant startDate = request.getStartDate();
            Instant endDate = request.getEndDate();
            // Tìm kiếm danh sách
            int offset = page > 0 ? (page - 1) * size : 0;

            List<Contract> contracts = contractRepository.findAll(size, offset, sort, direction, transactionId, uploadBy, startDate, endDate);
            return contracts.stream()
                    .map(GetContractResponseMapper::GetContractMapEntityToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching all contracts: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch all contracts: " + e.getMessage(), e);
        }
    }

    @Override
    public GetContractResponse updateContract(String id, UpdateContractRequest request) {
        log.info("Updating contract with ID: {}", id);
        try {
            Contract existingContract = contractRepository.findById(id);
            if (existingContract == null) {
                throw new RuntimeException("Contract not found with ID: " + id);
            }

            // Update fields with null checks
            if (request.getTransactionId() != null) {
                existingContract.setTransactionId(request.getTransactionId());
            }
            if (request.getUploadBy() != null) {
                existingContract.setUploadBy(request.getUploadBy());
            }
            if (request.getFileName() != null) {
                existingContract.setFileName(request.getFileName());
            }
            if (request.getFile() != null) {
                // Upload new file to Cloudinary and get the URL
                String assetFolderImage = CommonCloudinaryAttribute.assetFolderContract;
                String publicId = Contract.class.getSimpleName() + "_" + id;

                String fileUrl = uploadFileToCloudinary(request.getFile(), assetFolderImage, publicId, cloudinary);
                existingContract.setFileUrl(fileUrl);
            }
            existingContract.setActive(true);

            Contract updatedContract = contractRepository.save(existingContract);
            // Publish event
            eventPublisher.publishContractUpdatedEvent(updatedContract);

            log.info("Contract updated successfully with ID: {}", updatedContract.getId());
            return GetContractResponseMapper.GetContractMapEntityToDTO(updatedContract);
        } catch (Exception e) {
            log.error("Error updating contract: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update contract: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteContract(String id) {
        log.info("Deleting contract with ID: {}", id);
        try {
            Contract existingContract = contractRepository.findById(id);
            if (existingContract == null) {
                throw new RuntimeException("Contract not found with ID: " + id);
            }
            existingContract.setActive(false);
            contractRepository.save(existingContract);

            // Publish event
            eventPublisher.publishContractDeletedEvent(existingContract);

            log.info("Contract deleted successfully with ID: {}", id);
            return true;
        } catch (Exception e) {
            log.error("Error deleting contract: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete contract: " + e.getMessage(), e);
        }
    }

    private String uploadFileToCloudinary(MultipartFile file, String assetFolder, String publicId, Cloudinary cloudinary) throws Exception {
        // Danh sách các loại file được phép
        List<String> allowedContentTypes = Arrays.asList(
                CommonFileType.JPEG,
                CommonFileType.PNG,
                CommonFileType.WEBP,
                CommonFileType.GIF,
                CommonFileType.SVG,
                CommonFileType.PDF,
                CommonFileType.DOCX,
                CommonFileType.DOC
        );
        String contentType = file.getContentType();

        // Kiểm tra loại file
        if (!allowedContentTypes.contains(contentType)) {
            throw new IllegalArgumentException("Invalid file type: " + contentType + ". Allowed types: " + String.join(", ", allowedContentTypes));
        }

        // Lưu file tạm thời vào local
        String folderPath = Paths.get(System.getProperty("user.dir"), "upload").toString();
        File uploadDir = new File(folderPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        File tempFile = new File(folderPath, file.getOriginalFilename());
        file.transferTo(tempFile);

        // Upload lên Cloudinary
        Map uploadResult = cloudinary.uploader().upload(tempFile, ObjectUtils.asMap(
                "folder", assetFolder,
                "public_id", publicId
        ));

        // Xóa file tạm thời
        tempFile.delete();

        // Trả về URL của ảnh
        return (String) uploadResult.get("secure_url");
    }
}