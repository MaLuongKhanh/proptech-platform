export const API_ENDPOINTS = {
  LISTING: {
    BASE: '/listings/listings',
    BY_ID: (id: string) => `/listings/listings/${id}`,
    BY_AGENT: (agentId: string) => `/listings/listings/agent/${agentId}`,
    BY_LOCATION: '/listings/listings/location',
    BY_ADDRESS: '/listings/listings/address',
  },
  PROPERTY: {
    BASE: '/listings/properties',
    BY_ID: (id: string) => `/listings/properties/${id}`,
  },
  AUTH: {
    BASE: '/securities/auth',
    LOGIN: '/securities/auth/login',
    REGISTER: '/securities/auth/register',
    REFRESH: '/securities/auth/refresh',
    FORGOT_PASSWORD: '/securities/auth/forgot-password',
    RESET_PASSWORD: '/securities/auth/reset-password',
    VALIDATE_TOKEN: '/securities/auth/validate-token',
  },
  USER: {
    BASE: '/securities/users',
    BY_ID: (id: string) => `/securities/users/${id}`,
    BY_USERNAME: (username: string) => `/securities/users/username/${username}`,
    DISABLE: (id: string) => `/securities/users/${id}/disable`,
    ENABLE: (id: string) => `/securities/users/${id}/enable`,
    ADD_ROLE: (userId: string, roleName: string) => `/securities/users/${userId}/roles/${roleName}`,
    REMOVE_ROLE: (userId: string, roleName: string) => `/securities/users/${userId}/roles/${roleName}`,
  },
  WALLET: {
    BASE: '/payments/wallets',
    BY_ID: (id: string) => `/payments/wallets/${id}`,
    BY_USER_ID: (userId: string) => `/payments/wallets/${userId}`,
    TOP_UP: (id: string) => `/payments/wallets/${id}/topup`,
    PAYMENT: (id: string) => `/payments/wallets/${id}/payment`,
  },
  TRANSACTION: {
    BASE: '/payments/transactions',
    BY_ID: (id: string) => `/payments/transactions/${id}`,
  },
  SALE_TRANSACTION: {
    BASE: '/sales/transactions',
    BY_ID: (id: string) => `/sales/transactions/${id}`,
  },
  RENTAL_TRANSACTION: {
    BASE: '/rentals/transactions',
    BY_ID: (id: string) => `/rentals/transactions/${id}`,
  },
  // Thêm các endpoints khác ở đây
} as const; 