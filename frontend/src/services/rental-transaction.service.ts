import api from './api';
import { API_ENDPOINTS } from '../constants/api.endpoints';
import {
  RentalTransaction,
  RentalTransactionResponse,
  RentalTransactionListResponse,
  RentalTransactionSearchParams,
  AddRentalTransactionRequest,
  UpdateRentalTransactionRequest,
} from '../types/rental-transaction.types';

class RentalTransactionService {
  async getAll(params?: RentalTransactionSearchParams): Promise<RentalTransaction[]> {
    const response = await api.get<RentalTransactionListResponse>(API_ENDPOINTS.RENTAL_TRANSACTION.BASE, { params });
    return response.data.data;
  }

  async getById(id: string): Promise<RentalTransaction | null> {
    try {
      const response = await api.get<RentalTransactionResponse>(API_ENDPOINTS.RENTAL_TRANSACTION.BY_ID(id));
      return response.data.data;
    } catch (error) {
      console.error('Error fetching rental transaction:', error);
      return null;
    }
  }

  async create(data: AddRentalTransactionRequest): Promise<RentalTransaction | null> {
    try {
      const response = await api.post<RentalTransactionResponse>(API_ENDPOINTS.RENTAL_TRANSACTION.BASE, data);
      return response.data.data;
    } catch (error) {
      console.error('Error creating rental transaction:', error);
      return null;
    }
  }

  async update(id: string, data: UpdateRentalTransactionRequest): Promise<RentalTransaction | null> {
    try {
      const response = await api.put<RentalTransactionResponse>(API_ENDPOINTS.RENTAL_TRANSACTION.BY_ID(id), data);
      return response.data.data;
    } catch (error) {
      console.error('Error updating rental transaction:', error);
      return null;
    }
  }

  async delete(id: string): Promise<boolean> {
    try {
      await api.delete(API_ENDPOINTS.RENTAL_TRANSACTION.BY_ID(id));
      return true;
    } catch (error) {
      console.error('Error deleting rental transaction:', error);
      return false;
    }
  }

  async getByPropertyId(propertyId: string): Promise<RentalTransaction[]> {
    const response = await api.get<RentalTransactionListResponse>(API_ENDPOINTS.RENTAL_TRANSACTION.BASE, { params: { propertyId } });
    return response.data.data;
  }
}

export const rentalTransactionService = new RentalTransactionService(); 