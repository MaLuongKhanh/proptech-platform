import api from './api';
import { API_ENDPOINTS } from '../constants/api.endpoints';
import {
  Listing,
  ListingResponse,
  ListingListResponse,
  ListingSearchParams,
  AddListingRequest,
  UpdateListingRequest,
} from '../types/listing.types';

class ListingService {
  async getAll(params?: ListingSearchParams): Promise<Listing[]> {
    const response = await api.get<ListingListResponse>(API_ENDPOINTS.LISTING.BASE, { params });
    return response.data.data;
  }

  async getById(id: string): Promise<Listing | null> {
    try {
      const response = await api.get<ListingResponse>(API_ENDPOINTS.LISTING.BY_ID(id));
      return response.data.data;
    } catch (error) {
      console.error('Error fetching listing:', error);
      return null;
    }
  }

  async create(data: AddListingRequest): Promise<Listing | null> {
    try {
      const formData = new FormData();
      Object.entries(data).forEach(([key, value]) => {
        if (key === 'images') {
          (value as File[]).forEach((file) => {
            formData.append('images', file);
          });
        } else if (key === 'featuredImage') {
          formData.append('featuredImage', value as File);
        } else {
          formData.append(key, value.toString());
        }
      });

      const response = await api.post<ListingResponse>(API_ENDPOINTS.LISTING.BASE, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      return response.data.data;
    } catch (error) {
      console.error('Error creating listing:', error);
      return null;
    }
  }

  async update(id: string, data: UpdateListingRequest): Promise<Listing | null> {
    try {
      const response = await api.put<ListingResponse>(API_ENDPOINTS.LISTING.BY_ID(id), data);
      return response.data.data;
    } catch (error) {
      console.error('Error updating listing:', error);
      return null;
    }
  }

  async delete(id: string): Promise<boolean> {
    try {
      await api.delete(API_ENDPOINTS.LISTING.BY_ID(id));
      return true;
    } catch (error) {
      console.error('Error deleting listing:', error);
      return false;
    }
  }

  async getByAgentId(agentId: string, params?: ListingSearchParams): Promise<Listing[]> {
    const response = await api.get<ListingListResponse>(API_ENDPOINTS.LISTING.BY_AGENT(agentId), { params });
    return response.data.data;
  }

  async getByLocation(latitude: number, longitude: number, maxDistanceKm: number = 5): Promise<Listing[]> {
    const response = await api.get<ListingListResponse>(API_ENDPOINTS.LISTING.BY_LOCATION, {
      params: { latitude, longitude, maxDistanceKm },
    });
    return response.data.data;
  }

  async getByAddress(keyword: string): Promise<Listing[]> {
    const response = await api.get<ListingListResponse>(API_ENDPOINTS.LISTING.BY_ADDRESS, {
      params: { keyword },
    });
    return response.data.data;
  }

  async getAgentStats(agentId: string): Promise<{ total: number; newThisMonth: number }> {
    const response = await api.get(API_ENDPOINTS.LISTING.BY_AGENT(agentId));
    const listings = response.data.data;

    const startOfMonth = new Date();
    startOfMonth.setDate(1);
    startOfMonth.setHours(0, 0, 0, 0);

    const newThisMonth = listings.filter((listing: Listing) => 
      new Date(listing.updatedAt) >= startOfMonth
    ).length;

    return {
      total: listings.length,
      newThisMonth
    };
  }
}

export const listingService = new ListingService(); 