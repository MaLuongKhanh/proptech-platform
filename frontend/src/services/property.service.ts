import api from './api';
import { API_ENDPOINTS } from '../constants/api.endpoints';
import {
  Property,
  PropertyResponse,
  PropertySearchParams,
  AddPropertyRequest,
  UpdatePropertyRequest,
} from '../types/property.types';

class PropertyService {
  async getAll(params?: PropertySearchParams): Promise<Property[]> {
    const response = await api.get<PropertyResponse>(API_ENDPOINTS.PROPERTY.BASE, { params });
    return response.data.data;
  }

  async getById(id: string): Promise<Property | null> {
    try {
      const response = await api.get<PropertyResponse>(API_ENDPOINTS.PROPERTY.BY_ID(id));
      return response.data.data[0];
    } catch (error) {
      console.error('Error fetching property:', error);
      return null;
    }
  }

  async create(data: AddPropertyRequest): Promise<Property | null> {
    try {
      const response = await api.post<PropertyResponse>(API_ENDPOINTS.PROPERTY.BASE, data);
      return response.data.data[0];
    } catch (error) {
      console.error('Error creating property:', error);
      return null;
    }
  }

  async update(id: string, data: UpdatePropertyRequest): Promise<Property | null> {
    try {
      const response = await api.put<PropertyResponse>(API_ENDPOINTS.PROPERTY.BY_ID(id), data);
      return response.data.data[0];
    } catch (error) {
      console.error('Error updating property:', error);
      return null;
    }
  }

  async delete(id: string): Promise<boolean> {
    try {
      await api.delete(API_ENDPOINTS.PROPERTY.BY_ID(id));
      return true;
    } catch (error) {
      console.error('Error deleting property:', error);
      return false;
    }
  }
}

export const propertyService = new PropertyService(); 