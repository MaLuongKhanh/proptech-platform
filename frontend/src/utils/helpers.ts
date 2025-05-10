export const formatCurrency = (amount: number): string => {
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND',
  }).format(amount);
};

export const formatDate = (date: string | Date): string => {
  return new Intl.DateTimeFormat('vi-VN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  }).format(new Date(date));
};

export const truncateText = (text: string, maxLength: number): string => {
  if (text.length <= maxLength) return text;
  return text.slice(0, maxLength) + '...';
};

export const getPropertyTypeLabel = (type: string): string => {
  const types: { [key: string]: string } = {
    HOUSE: 'Nhà',
    APARTMENT: 'Căn hộ',
    CONDO: 'Chung cư',
    LAND: 'Đất',
  };
  return types[type] || type;
};

export const getListingTypeLabel = (type: string): string => {
  const types: { [key: string]: string } = {
    SALE: 'Bán',
    RENT: 'Cho thuê',
  };
  return types[type] || type;
}; 