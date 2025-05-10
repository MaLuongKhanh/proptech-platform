import React from 'react';
import { Box, Skeleton } from '@mui/material';

export const ListingCardSkeleton: React.FC = () => {
  return (
    <Box sx={{ p: 1 }}>
      <Skeleton variant="rectangular" width="100%" height={260} sx={{ borderRadius: 3 }} />
      <Skeleton width="60%" height={32} sx={{ mt: 2 }} />
      <Skeleton width="40%" height={24} sx={{ mt: 1 }} />
      <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 2 }}>
        <Skeleton width="30%" height={24} />
        <Skeleton width="30%" height={24} />
      </Box>
    </Box>
  );
}; 