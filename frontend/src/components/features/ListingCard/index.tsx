import React from 'react';
import {
  Card,
  CardContent,
  CardMedia,
  Typography,
  Box,
  Chip,
  IconButton,
} from '@mui/material';
import FavoriteIcon from '@mui/icons-material/Favorite';
import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder';
import { formatCurrency } from '../../../utils/helpers';
import styles from '../../../styles/ListingCard/ListingCard.module.css';
import { Swiper, SwiperSlide } from 'swiper/react';
import 'swiper/css';

interface ListingCardProps {
  listing: any;
  onFavorite: (id: string) => void;
  isFavorite?: boolean;
}

export const ListingCard: React.FC<ListingCardProps> = ({
  listing,
  onFavorite,
  isFavorite = false,
}) => {
  const {
    id,
    name,
    price,
    listingType,
    bedrooms,
    bathrooms,
    featuredImageUrl,
    images,
    property,
    isActive,
    agentName,
    propertyId,
  } = listing;

  const address = property?.address || {};
  const imageList = images && images.length > 0 ? images : [featuredImageUrl];

  return (
    <Card className={styles.cardRoot}>
      <Box className={styles.cardMediaBox} sx={{ position: 'relative' }}>
        <Swiper spaceBetween={10} slidesPerView={1} style={{ borderRadius: 8 }}>
          {imageList.map((img: string, idx: number) => (
            <SwiperSlide key={idx}>
              <CardMedia
                component="img"
                image={img}
                alt={name}
                className={styles.cardMedia}
              />
            </SwiperSlide>
          ))}
        </Swiper>
        <Chip
          label={listingType === 'SALE' ? 'For Sale' : 'For Rent'}
          color={listingType === 'SALE' ? 'primary' : 'secondary'}
          size="small"
          className={styles.chip}
          sx={{ position: 'absolute', top: 12, left: 12, zIndex: 2 }}
        />
        <IconButton
          className={styles.favoriteBtn}
          onClick={() => onFavorite(id)}
          sx={{ position: 'absolute', top: 12, right: 12, zIndex: 2 }}
        >
          {isFavorite ? (
            <FavoriteIcon color="error" />
          ) : (
            <FavoriteBorderIcon />
          )}
        </IconButton>
      </Box>
      <CardContent className={styles.cardContent}>
        <Typography variant="h5" color="primary" sx={{ fontWeight: 700, mb: 1 }}>
          {formatCurrency(price)}
        </Typography>
        <Typography variant="body2" sx={{ fontWeight: 500, mb: 1 }}>
          {bedrooms} bd&nbsp;|&nbsp;{bathrooms} ba&nbsp;|&nbsp;{isActive ? 'Active' : 'Inactive'}
        </Typography>
        <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
          {address.street}{address.street ? ', ' : ''}{address.city}{address.city ? ', ' : ''}{address.province}{address.province ? ', ' : ''}{address.country}{address.postalCode ? ', ' + address.postalCode : ''}
        </Typography>
        <Typography variant="caption" color="text.secondary" sx={{ display: 'block', mb: 0.5 }}>
          MLS® ID #{propertyId}{agentName ? `, ${agentName}` : ''}
        </Typography>
      </CardContent>
    </Card>
  );
}; 