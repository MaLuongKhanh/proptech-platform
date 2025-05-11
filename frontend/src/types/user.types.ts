export interface User {
  id: string;
  username: string;
  email: string;
  fullName: string;
  phoneNumber: string | null;
  avatarUrl: string | null;
  roles: string[];
  enabled: boolean;
  createdAt: string | null;
  lastLoginAt: string | null;
}

export interface UserResponse {
  status: string;
  message: string;
  data: User;
  timestamp: string;
}

export interface UpdateUserRequest {
  email?: string;
  fullName?: string;
  phoneNumber?: string;
  avatarUrl?: string;
} 