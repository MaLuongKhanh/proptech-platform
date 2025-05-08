package vn.proptech.sale.api.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private String message;
    private T data;
    
    // Thêm các trường có thể xuất hiện trong response API
    private String status;
    private int code;
}