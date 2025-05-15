// Rental Transaction Types
export type RentalTransactionStatus = 'PENDING' | 'DEPOSIT_PAID' | 'COMPLETED' | 'CANCELLED' | 'EXPIRED';

export interface RentalTransaction {
  id: string;
  propertyId: string;
  listingId: string;
  tenantName: string;
  tenantIdentity: string;
  price: number;
  startDate?: string;
  endDate?: string;
  depositDate?: string;
  agentId: string;
  status: RentalTransactionStatus;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface RentalTransactionResponse {
  message: string;
  data: RentalTransaction;
}

export interface RentalTransactionListResponse {
  message: string;
  data: RentalTransaction[];
}

export interface RentalTransactionSearchParams {
  page?: number;
  size?: number;
  sort?: string;
  direction?: 'ASC' | 'DESC';
  listingId?: string;
  propertyId?: string;
  agentId?: string;
  startDate?: string;
  endDate?: string;
  status?: RentalTransactionStatus;
}

export interface AddRentalTransactionRequest {
  listingId: string;
  tenantName: string;
  tenantIdentity: string;
  price: number;
  agentId: string;
  startDate?: string;
  endDate?: string;
  status?: RentalTransactionStatus;
}

export interface UpdateRentalTransactionRequest {
  listingId?: string;
  tenantName?: string;
  tenantIdentity?: string;
  price?: number;
  startDate?: string;
  endDate?: string;
  depositDate?: string;
  agentId?: string;
  status?: RentalTransactionStatus;
} 