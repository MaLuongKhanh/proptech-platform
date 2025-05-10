import axios from 'axios';
import { authService } from './auth.service';


const api = axios.create({
  baseURL: process.env.REACT_APP_API_URL || 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add request interceptor để thêm token vào header
api.interceptors.request.use(
  (config) => {
    const token = authService.getAccessToken();
    if (token && token !== '') {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    console.log(error);
    return Promise.reject(error);
  }
);

// Add response interceptor để xử lý refresh token
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    // Nếu lỗi là 401 và chưa thử refresh token
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        // Thử refresh token
        const response  = await authService.refreshToken();
        if (response) {
          // Cập nhật token mới vào header
          originalRequest.headers.Authorization = `Bearer ${response.accessToken}`;
        }
        // Thử lại request ban đầu với token mới
        console.log('originalRequest', originalRequest);
        return api(originalRequest);
      } catch (refreshError) {
        // Nếu refresh thất bại, xóa thông tin đăng nhập và chuyển về trang login
        window.location.href = '/login';
        console.log('refreshError', refreshError);
        return Promise.reject(refreshError);
      }
    }

    return Promise.reject(error);
  }
);

export default api; 