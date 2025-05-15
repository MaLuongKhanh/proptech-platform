// Sale Transaction Types
export type TransactionStatus = 'PENDING' | 'DEPOSIT_PAID' | 'COMPLETED' | 'CANCELLED';

export interface SaleTransaction {
  id: string;
  propertyId: string;
  listingId: string;
  buyerName: string;
  buyerIdentity: string;
  price: number;
  transactionDate?: string;
  depositDate?: string;
  agentId: string;
  status: TransactionStatus;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface SaleTransactionResponse {
  message: string;
  data: SaleTransaction;
}

export interface SaleTransactionListResponse {
  message: string;
  data: SaleTransaction[];
}

export interface SaleTransactionSearchParams {
  page?: number;
  size?: number;
  sort?: string;
  direction?: 'ASC' | 'DESC';
  listingId?: string;
  propertyId?: string;
  agentId?: string;
  startDate?: string;
  endDate?: string;
  status?: TransactionStatus;
}

export interface AddSaleTransactionRequest {
  listingId: string;
  buyerName: string;
  buyerIdentity: string;
  price: number;
  agentId: string;
  status?: TransactionStatus;
}

export interface UpdateSaleTransactionRequest {
  listingId?: string;
  buyerName?: string;
  buyerIdentity?: string;
  price?: number;
  transactionDate?: string;
  depositDate?: string;
  agentId?: string;
  status?: TransactionStatus;
} 