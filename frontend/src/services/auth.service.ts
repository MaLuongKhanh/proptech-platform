import api from './api';
import { API_ENDPOINTS } from '../constants/api.endpoints';
import {
  LoginRequest,
  RegisterRequest,
  ResetPasswordRequest,
  JwtResponse,
  AuthResponse,
} from '../types/auth.types';
import { User } from '../types/user.types';

class AuthService {
  getAccessToken(): string | null {
    return localStorage.getItem('accessToken');
  }

  getCurrentUser(): User | null {
    const userStr = localStorage.getItem('user');
    if (userStr) {
      try {
        return JSON.parse(userStr);
      } catch (error) {
        console.error('Error parsing user from localStorage:', error);
        return null;
      }
    }
    return null;
  }

  logout() {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('tokenType');
    localStorage.removeItem('user');
  }

  async login(data: LoginRequest): Promise<JwtResponse | null> {
    try {
      const response = await api.post<AuthResponse>(API_ENDPOINTS.AUTH.LOGIN, data);
      const { accessToken, refreshToken, ...user } = response.data.data;
      localStorage.setItem('accessToken', accessToken);
      localStorage.setItem('refreshToken', refreshToken);
      localStorage.setItem('user', JSON.stringify(user));
      return response.data.data;
    } catch (error) {
      console.error('Error during login:', error);
      return null;
    }
  }

  async register(data: RegisterRequest): Promise<JwtResponse | null> {
    try {
      const response = await api.post<AuthResponse>(API_ENDPOINTS.AUTH.REGISTER, data);
      return response.data.data;
    } catch (error) {
      console.error('Error during registration:', error);
      return null;
    }
  }

  async refreshToken(): Promise<JwtResponse | null> {
    try {
      const getRefreshToken = localStorage.getItem('refreshToken');
      if (!getRefreshToken) {
        this.logout();
        return null;
      }
      const response = await api.post<AuthResponse>(API_ENDPOINTS.AUTH.REFRESH, null, {
        params: { refreshToken: getRefreshToken },
      });
      const { accessToken, refreshToken} = response.data.data;
      localStorage.setItem('accessToken', accessToken);
      localStorage.setItem('refreshToken', refreshToken);
      return response.data.data;
    } catch (error) {
      console.error('Error refreshing token:', error);
      this.logout();
      return null;
    }
  }

  async forgotPassword(email: string): Promise<boolean> {
    try {
      await api.post(API_ENDPOINTS.AUTH.FORGOT_PASSWORD, null, {
        params: { email },
      });
      return true;
    } catch (error) {
      console.error('Error requesting password reset:', error);
      return false;
    }
  }

  async resetPassword(data: ResetPasswordRequest): Promise<boolean> {
    try {
      await api.post(API_ENDPOINTS.AUTH.RESET_PASSWORD, data);
      return true;
    } catch (error) {
      console.error('Error resetting password:', error);
      return false;
    }
  }

  async validateResetToken(token: string): Promise<boolean> {
    try {
      const response = await api.post<{ data: boolean }>(API_ENDPOINTS.AUTH.VALIDATE_TOKEN, null, {
        params: { token },
      });
      return response.data.data;
    } catch (error) {
      console.error('Error validating reset token:', error);
      return false;
    }
  }
}

export const authService = new AuthService(); 