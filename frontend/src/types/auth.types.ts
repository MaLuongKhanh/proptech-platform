import { User } from './user.types';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  password: string;
  confirmPassword: string;
  email: string;
  fullName: string;
  phoneNumber?: string;
}

export interface ResetPasswordRequest {
  token: string;
  newPassword: string;
  confirmPassword: string;
}

export interface JwtResponse {
  accessToken: string;
  refreshToken: string;
  id: string;
  fullName: string;
  avatarUrl: string | null;
  roles: string[];
}

export interface AuthResponse {
  status: string;
  message: string;
  data: JwtResponse;
  timestamp: string;
}

export interface AuthState {
  user: JwtResponse | null;
  error: string | null;
} 