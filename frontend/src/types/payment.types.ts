export interface Wallet {
  id: string;
  userId: string;
  balance: number;
  status: 'ACTIVE' | 'INACTIVE';
  createdAt: string;
  updatedAt: string;
}

export interface Transaction {
  id: string;
  walletId: string;
  amount: number;
  type: 'TOPUP' | 'SERVICE_PAYMENT';
  status: 'PENDING' | 'SUCCESS' | 'FAILED';
  description: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreateWalletRequest {
  userId: string;
}

export interface UpdateWalletRequest {
  balance?: number;
  status?: 'ACTIVE' | 'INACTIVE';
}

export interface CreateTransactionRequest {
  walletId: string;
  amount: number;
  type: 'TOPUP' | 'SERVICE_PAYMENT';
  description: string;
}

export interface UpdateTransactionRequest {
  status?: 'PENDING' | 'COMPLETED' | 'FAILED';
  description?: string;
}

export interface GetTransactionsRequest {
  walletId?: string;
  type?: 'TOPUP' | 'SERVICE_PAYMENT';
  status?: 'PENDING' | 'SUCCESS' | 'FAILED';
  startDate?: string;
  endDate?: string;
  page?: number;
  size?: number;
} 