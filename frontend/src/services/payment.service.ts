import { API_ENDPOINTS } from '../constants/api.endpoints';
import api from './api';
import {
  Wallet,
  Transaction,
  CreateWalletRequest,
  UpdateWalletRequest,
  CreateTransactionRequest,
  UpdateTransactionRequest,
  GetTransactionsRequest,
} from '../types/payment.types';

class PaymentService {
  // Wallet APIs
  async createWallet(request: CreateWalletRequest): Promise<Wallet> {
    const response = await api.post(API_ENDPOINTS.WALLET.BASE, request);
    return response.data.data;
  }

  async getWalletByUserId(userId: string): Promise<Wallet> {
    const response = await api.get(API_ENDPOINTS.WALLET.BY_USER_ID(userId));
    return response.data.data;
  }

  async updateWallet(id: string, request: UpdateWalletRequest): Promise<Wallet> {
    const response = await api.put(API_ENDPOINTS.WALLET.BY_ID(id), request);
    return response.data.data;
  }

  async topUpWallet(id: string, amount: string): Promise<Wallet> {
    const response = await api.post(API_ENDPOINTS.WALLET.TOP_UP(id), null, {
      params: { amount },
    });
    return response.data.data;
  }

  async paymentWallet(id: string, amount: string): Promise<Wallet> {
    const response = await api.post(API_ENDPOINTS.WALLET.PAYMENT(id), null, {
      params: { amount },
    });
    return response.data.data;
  }

  async deleteWallet(id: string): Promise<boolean> {
    const response = await api.delete(API_ENDPOINTS.WALLET.BY_ID(id));
    return response.data.data;
  }

  // Transaction APIs
  async createTransaction(request: CreateTransactionRequest): Promise<Transaction> {
    const response = await api.post(API_ENDPOINTS.TRANSACTION.BASE, request);
    return response.data.data;
  }

  async getTransactionById(id: string): Promise<Transaction> {
    const response = await api.get(API_ENDPOINTS.TRANSACTION.BY_ID(id));
    return response.data.data;
  }

  async getTransactions(request: GetTransactionsRequest): Promise<Transaction[]> {
    const response = await api.get(API_ENDPOINTS.TRANSACTION.BASE, {
      params: request,
    });
    return response.data.data;
  }

  async updateTransaction(
    id: string,
    request: UpdateTransactionRequest
  ): Promise<Transaction> {
    const response = await api.put(API_ENDPOINTS.TRANSACTION.BY_ID(id), request);
    return response.data.data;
  }

  async deleteTransaction(id: string): Promise<boolean> {
    const response = await api.delete(API_ENDPOINTS.TRANSACTION.BY_ID(id));
    return response.data.data;
  }

  async getMonthlyDeposits(userId: string): Promise<number> {
    const startOfMonth = new Date();
    startOfMonth.setDate(1);
    startOfMonth.setHours(0, 0, 0, 0);

    // Lấy wallet của user trước
    const wallet = await this.getWalletByUserId(userId);
    if (!wallet) return 0;

    const response = await api.get(API_ENDPOINTS.TRANSACTION.BASE, {
      params: {
        walletId: wallet.id,
        type: 'TOPUP',
        startDate: startOfMonth.toISOString(),
        endDate: new Date().toISOString(),
      },
    });
    
    const transactions = response.data.data;
    console.log(transactions);
    return transactions.reduce((total: number, transaction: Transaction) => {
      return total + (transaction.status === 'SUCCESS' ? transaction.amount : 0);
    }, 0);
  }
}

export const paymentService = new PaymentService(); 