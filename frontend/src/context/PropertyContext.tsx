import React, { createContext, useContext, useState, useEffect } from 'react';
import { propertyService } from '../services/property.service';
import { Property } from '../types/property.types';
import { ListingSearchParams } from '../types/listing.types';

interface PropertyContextType {
  properties: Property[];
  loading: boolean;
  error: string | null;
  fetchProperties: (params?: ListingSearchParams) => Promise<void>;
  getPropertyById: (id: string) => Promise<Property | null>;
}

const PropertyContext = createContext<PropertyContextType | undefined>(undefined);

export const PropertyProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [properties, setProperties] = useState<Property[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchProperties = async (params?: ListingSearchParams) => {
    try {
      setLoading(true);
      setError(null);
      const data = await propertyService.getAll(params);
      setProperties(data);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Không thể tải danh sách bất động sản');
    } finally {
      setLoading(false);
    }
  };

  const getPropertyById = async (id: string) => {
    try {
      setLoading(true);
      setError(null);
      const property = await propertyService.getById(id);
      return property;
    } catch (err: any) {
      setError(err.response?.data?.message || 'Không thể tải thông tin bất động sản');
      return null;
    } finally {
      setLoading(false);
    }
  };

  return (
    <PropertyContext.Provider
      value={{
        properties,
        loading,
        error,
        fetchProperties,
        getPropertyById,
      }}
    >
      {children}
    </PropertyContext.Provider>
  );
};

export const useProperty = () => {
  const context = useContext(PropertyContext);
  if (context === undefined) {
    throw new Error('useProperty must be used within a PropertyProvider');
  }
  return context;
}; 