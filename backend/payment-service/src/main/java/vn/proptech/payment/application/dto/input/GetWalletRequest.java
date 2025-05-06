package vn.proptech.payment.application.dto.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetWalletRequest {
    public Integer page;
    public Integer size;
    public String sort;
    public String direction;
    private String userId;
}