export type PropertyType = 
  | 'UNKNOWN_PROPERTY'
  | 'APARTMENT'
  | 'HOUSE'
  | 'VILLA'
  | 'OFFICE'
  | 'RETAIL'
  | 'INDUSTRIAL'
  | 'LAND';

export interface Address {
  street: string;
  city: string;
  province: string;
  country: string;
  postalCode: string;
  latitude: number;
  longitude: number;
}

export interface Property {
  propertyId: string;
  address: Address;
  propertyType: PropertyType;
  yearBuilt: number;
  lotSize: number;
  parkingSpaces: number;
  garageSize: number;
  amenities: string[];
  hoaFee: number;
  isActive: boolean;
}

export interface PropertyResponse {
  message: string;
  data: Property;
}

export interface PropertyListResponse {
  message: string;
  data: Property[];
}

export interface PropertySearchParams {
  page?: number;
  size?: number;
  sort?: string;
  direction?: 'ASC' | 'DESC';
}

export interface AddPropertyRequest {
  address: Address;
  propertyType: PropertyType;
  yearBuilt: number;
  lotSize: number;
  parkingSpaces: number;
  garageSize: number;
  amenities: string[];
  hoaFee: number;
}

export interface UpdatePropertyRequest {
  address?: {
    street?: string;
    city?: string;
    province?: string;
    country?: string;
    postalCode?: string;
    latitude?: number;
    longitude?: number;
  };
  propertyType?: PropertyType;
  yearBuilt?: number;
  lotSize?: number;
  parkingSpaces?: number;
  garageSize?: number;
  amenities?: string[];
  hoaFee?: number;
  isActive?: boolean;
} 