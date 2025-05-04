package vn.proptech.sale.infrastructure.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private String msg;
    private T data;
    
    // Thêm các trường có thể xuất hiện trong response API
    private String status;
    private int code;
}