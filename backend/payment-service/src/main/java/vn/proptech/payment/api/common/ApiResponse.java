package vn.proptech.payment.api.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private String msg;
    private T data;
    
    public static <T> ApiResponse<T> of(String message, T data) {
        return ApiResponse.<T>builder()
                .msg(message)
                .data(data)
                .build();
    }
    
    public static <T> ResponseEntity<ApiResponse<T>> success(String message, T data, HttpStatus status) {
        return new ResponseEntity<>(of(message, data), status);
    }

    public static <T> ResponseEntity<ApiResponse<T>> updated(T data) {
        return success("Cập nhật thành công", data, HttpStatus.OK);
    }
    
    public static <T> ResponseEntity<ApiResponse<T>> success(T data, HttpStatus status) {
        return success("Thành công", data, status);
    }
    
    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return success("Tạo thành công", data, HttpStatus.CREATED);
    }
    
    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return success("Thành công", data, HttpStatus.OK);
    }
    
    public static <T> ResponseEntity<ApiResponse<Boolean>> noContent() {
        return success("Xóa thành công", null, HttpStatus.NO_CONTENT);
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(String message, T data, HttpStatus status) {
        return new ResponseEntity<>(of(message, data), status);
    }
}