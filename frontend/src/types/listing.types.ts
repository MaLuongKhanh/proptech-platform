import { Property } from './property.types';

export interface Listing {
  id: string;
  propertyId: string;
  property: Property;
  name: string;
  description: string;
  price: number;
  listingType: 'SALE' | 'RENT';
  agentId: string;
  bedrooms: number;
  bathrooms: number;
  area: number;
  imageUrls: string[];
  featuredImageUrl: string;
  updatedAt: string;
  isActive: boolean;
  isSold: boolean;
}

export interface ListingResponse {
  message: string;
  data: Listing[];
}

export interface ListingSearchParams {
  page?: number;
  size?: number;
  sort?: string;
  direction?: 'ASC' | 'DESC';
  listingType?: 'SALE' | 'RENT';
  propertyType?: 'HOUSE' | 'APARTMENT' | 'CONDO' | 'TOWNHOUSE' | 'LAND' | 'COMMERCIAL';
  city?: string;
  district?: string;
  minPrice?: number;
  maxPrice?: number;
  minArea?: number;
  maxArea?: number;
  minBedrooms?: number;
  maxBedrooms?: number;
  minBathrooms?: number;
  maxBathrooms?: number;
}

export interface AddListingRequest {
  propertyId: string;
  name: string;
  description: string;
  price: number;
  listingType: 'SALE' | 'RENT';
  agentId: string;
  bedrooms: number;
  bathrooms: number;
  area: number;
  images: File[];
  featuredImage: File;
}

export interface UpdateListingRequest {
  name?: string;
  description?: string;
  price?: number;
  listingType?: 'SALE' | 'RENT';
  bedrooms?: number;
  bathrooms?: number;
  area?: number;
  isActive?: boolean;
  isSold?: boolean;
} 