package vn.proptech.rental.application.dto.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Bỏ qua các trường null khi serialize
public class UpdateRentalContractRequest {
    private String rentalTransactionId;
    
    private MultipartFile file;
    
    private String uploadBy;
    
    private String fileName;
}