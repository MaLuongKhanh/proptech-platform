import api from './api';
import { API_ENDPOINTS } from '../constants/api.endpoints';
import {
  SaleTransaction,
  SaleTransactionResponse,
  SaleTransactionListResponse,
  SaleTransactionSearchParams,
  AddSaleTransactionRequest,
  UpdateSaleTransactionRequest,
} from '../types/sale-transaction.types';

class SaleTransactionService {
  async getAll(params?: SaleTransactionSearchParams): Promise<SaleTransaction[]> {
    const response = await api.get<SaleTransactionListResponse>(API_ENDPOINTS.SALE_TRANSACTION.BASE, { params });
    return response.data.data;
  }

  async getById(id: string): Promise<SaleTransaction | null> {
    try {
      const response = await api.get<SaleTransactionResponse>(API_ENDPOINTS.SALE_TRANSACTION.BY_ID(id));
      return response.data.data;
    } catch (error) {
      console.error('Error fetching sale transaction:', error);
      return null;
    }
  }

  async create(data: AddSaleTransactionRequest): Promise<SaleTransaction | null> {
    try {
      const response = await api.post<SaleTransactionResponse>(API_ENDPOINTS.SALE_TRANSACTION.BASE, data);
      return response.data.data;
    } catch (error) {
      console.error('Error creating sale transaction:', error);
      return null;
    }
  }

  async update(id: string, data: UpdateSaleTransactionRequest): Promise<SaleTransaction | null> {
    try {
      const response = await api.put<SaleTransactionResponse>(API_ENDPOINTS.SALE_TRANSACTION.BY_ID(id), data);
      return response.data.data;
    } catch (error) {
      console.error('Error updating sale transaction:', error);
      return null;
    }
  }

  async delete(id: string): Promise<boolean> {
    try {
      await api.delete(API_ENDPOINTS.SALE_TRANSACTION.BY_ID(id));
      return true;
    } catch (error) {
      console.error('Error deleting sale transaction:', error);
      return false;
    }
  }

  async getByPropertyId(propertyId: string): Promise<SaleTransaction[]> {
    const response = await api.get<SaleTransactionListResponse>(API_ENDPOINTS.SALE_TRANSACTION.BASE, { params: { propertyId } });
    return response.data.data;
  }
}

export const saleTransactionService = new SaleTransactionService(); 