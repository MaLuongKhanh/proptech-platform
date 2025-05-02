package vn.proptech.sale.application.dto.input;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Bỏ qua các trường null khi serialize
public class GetContractRequest {
    public Integer page;
    public Integer size;
    public String sort;
    public String direction;
    public String transactionId;
    public String uploadBy;
    public Instant startDate;
    public Instant endDate;
}
