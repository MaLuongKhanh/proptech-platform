import api from './api';
import { API_ENDPOINTS } from '../constants/api.endpoints';
import {
  User,
  UserResponse,
  UpdateUserRequest,
} from '../types/user.types';

class UserService {
  async getAll(): Promise<User[]> {
    try {
      const response = await api.get<UserResponse>(API_ENDPOINTS.USER.BASE);
      return response.data.data;
    } catch (error) {
      console.error('Error getting all users:', error);
      return [];
    }
  }

  async getById(id: string): Promise<User | null> {
    try {
      const response = await api.get<UserResponse>(API_ENDPOINTS.USER.BY_ID(id));
      return response.data.data[0];
    } catch (error) {
      console.error('Error getting user by id:', error);
      return null;
    }
  }

  async getByUsername(username: string): Promise<User | null> {
    try {
      const response = await api.get<UserResponse>(API_ENDPOINTS.USER.BY_USERNAME(username));
      return response.data.data[0];
    } catch (error) {
      console.error('Error getting user by username:', error);
      return null;
    }
  }

  async update(id: string, data: UpdateUserRequest): Promise<User | null> {
    try {
      const response = await api.put<UserResponse>(API_ENDPOINTS.USER.BY_ID(id), data);
      return response.data.data[0];
    } catch (error) {
      console.error('Error updating user:', error);
      return null;
    }
  }

  async delete(id: string): Promise<boolean> {
    try {
      await api.delete(API_ENDPOINTS.USER.BY_ID(id));
      return true;
    } catch (error) {
      console.error('Error deleting user:', error);
      return false;
    }
  }

  async disable(id: string): Promise<boolean> {
    try {
      await api.patch(API_ENDPOINTS.USER.DISABLE(id));
      return true;
    } catch (error) {
      console.error('Error disabling user:', error);
      return false;
    }
  }

  async enable(id: string): Promise<boolean> {
    try {
      await api.patch(API_ENDPOINTS.USER.ENABLE(id));
      return true;
    } catch (error) {
      console.error('Error enabling user:', error);
      return false;
    }
  }

  async addRole(userId: string, roleName: string): Promise<boolean> {
    try {
      await api.patch(API_ENDPOINTS.USER.ADD_ROLE(userId, roleName));
      return true;
    } catch (error) {
      console.error('Error adding role to user:', error);
      return false;
    }
  }

  async removeRole(userId: string, roleName: string): Promise<boolean> {
    try {
      await api.delete(API_ENDPOINTS.USER.REMOVE_ROLE(userId, roleName));
      return true;
    } catch (error) {
      console.error('Error removing role from user:', error);
      return false;
    }
  }
}

export const userService = new UserService(); 