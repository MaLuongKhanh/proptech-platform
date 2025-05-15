import React, { useRef, useEffect, useState } from 'react';
import { Dialog, DialogContent, Box, Typography, IconButton, Grid, Button, Divider, Chip, Stack } from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import { Swiper, SwiperSlide } from 'swiper/react';
import { Autoplay } from 'swiper/modules';
import 'swiper/css';
import 'swiper/css/autoplay';
import { formatCurrency } from '../../utils/helpers';
import AgentContactDialog from './AgentContactDialog';
import { saleTransactionService } from '../../services/sale-transaction.service';
import { rentalTransactionService } from '../../services/rental-transaction.service';
import { useTheme } from '@mui/material';

interface ListingDetailDialogProps {
  open: boolean;
  onClose: () => void;
  listing: any | null;
}

const ParallaxImage: React.FC<{ src: string; alt: string; onEnd: () => void; active: boolean }> = ({ src, alt, onEnd, active }) => {
  const imgRef = useRef<HTMLImageElement>(null);

  useEffect(() => {
    let frame: number;
    let x = 0;
    let animationRunning = false;

    const animate = () => {
      if (!active || !imgRef.current || animationRunning) return;

      animationRunning = true;
      const maxX = 48;
      const speed = 0.12;

      const updateFrame = () => {
        x += speed;
        if (x >= maxX) {
          onEnd();
          x = 0; // Reset x khi kết thúc
          animationRunning = false;
          return;
        }
        if (imgRef.current) {
          imgRef.current.style.transform = `scale(1.12) translateX(${x}px)`;
        }
        frame = requestAnimationFrame(updateFrame);
      };

      if (imgRef.current) {
        imgRef.current.style.transform = `scale(1) translateX(0px)`; // Reset ban đầu
      }
      frame = requestAnimationFrame(updateFrame);

      return () => {
        cancelAnimationFrame(frame);
        animationRunning = false;
      };
    };

    if (active) {
      animate();
    }

    return () => cancelAnimationFrame(frame);
  }, [active, onEnd]);

  return (
    <img
      ref={imgRef}
      src={src}
      alt={alt}
      style={{
        width: '100%',
        maxHeight: 420,
        objectFit: 'cover',
        display: 'block',
        transition: 'transform 0.2s',
      }}
    />
  );
};

const ListingDetailDialog: React.FC<ListingDetailDialogProps> = ({ open, onClose, listing }) => {
  const [contactOpen, setContactOpen] = useState(false);
  const swiperRef = useRef<any>(null);
  const [currentIndex, setCurrentIndex] = useState(0);
  const [activeSlide, setActiveSlide] = useState(0);
  const [priceHistory, setPriceHistory] = useState<any[]>([]);
  const [loadingHistory, setLoadingHistory] = useState(false);
  const theme = useTheme();

  useEffect(() => {
    setActiveSlide(currentIndex); // Đồng bộ activeSlide với currentIndex
  }, [currentIndex]);

  useEffect(() => {
    if (!listing) return;
    setLoadingHistory(true);
    const fetchHistory = async () => {
      try {
        let data: any[] = [];
        if (listing.listingType === 'SALE') {
          data = await saleTransactionService.getByPropertyId(listing.propertyId);
        } else if (listing.listingType === 'RENT') {
          data = await rentalTransactionService.getByPropertyId(listing.propertyId);
        }
        setPriceHistory(
          (data || []).sort((a, b) => new Date(b.updatedAt).getTime() - new Date(a.updatedAt).getTime())
        );
      } catch (e) {
        setPriceHistory([]);
      } finally {
        setLoadingHistory(false);
      }
    };
    fetchHistory();
  }, [listing]);

  // Di chuyển kiểm tra listing xuống sau khi tất cả hooks được gọi
  if (!listing) {
    return null;
  }

  const images = listing.imageUrls && listing.imageUrls.length > 0 ? listing.imageUrls : [listing.featuredImageUrl];
  const address = listing.property?.address || {};
  const property = listing.property || {};

  return (
    <Dialog open={open} onClose={onClose} maxWidth="md" fullWidth scroll="body" PaperProps={{ sx: { borderRadius: 3, p: 0 } }}>
      <DialogContent sx={{ p: 0 }}>
        <Box sx={{ position: 'relative', overflow: 'hidden'}}>
          <Swiper
            spaceBetween={10}
            slidesPerView={1}
            style={{ width: '100%', maxHeight: 420 }}
            allowTouchMove={false}
            loop
            modules={[Autoplay]}
            onSwiper={(swiper) => { swiperRef.current = swiper; }}
            onSlideChange={(swiper) => setCurrentIndex(swiper.realIndex)}
          >
            {images.map((img: string, idx: number) => (
              <SwiperSlide key={idx}>
                <ParallaxImage
                  src={img}
                  alt={listing.name}
                  onEnd={() => {
                    if (swiperRef.current) {
                      swiperRef.current.slideNext();
                    }
                  }}
                  active={activeSlide === idx}
                />
              </SwiperSlide>
            ))}
          </Swiper>
          <IconButton
            onClick={onClose}
            sx={{ position: 'absolute', top: 8, right: 8, zIndex: 2, background: 'rgba(255,255,255,0.7)' }}
          >
            <CloseIcon />
          </IconButton>
        </Box>
        <Box sx={{ p: 3 }}>
          <Stack direction="row" spacing={2} alignItems="center" sx={{ mb: 1 }}>
            <Typography variant="h4" color="primary" sx={{ fontWeight: 700 }}>
              {formatCurrency(listing.price)}
            </Typography>
            {listing.isSold && <Chip label="Sold" color="default" />}
            {!listing.isActive && <Chip label="Inactive" color="warning" />}
          </Stack>
          <Typography variant="subtitle1" sx={{ mb: 1 }}>
            {address.street}
            {address.street ? ', ' : ''}{address.city}
            {address.city ? ', ' : ''}{address.province}
            {address.province ? ', ' : ''}{address.country}
            {address.postalCode ? ', ' + address.postalCode : ''}
          </Typography>
          <Stack direction="row" spacing={2} sx={{ mb: 2 }}>
            <Chip label={property.propertyType?.replace('UNKNOWN_PROPERTY', 'Other') || 'Other'} color="info" />
            <Chip label={listing.listingType?.replace('UNKNOWN_LISTING', 'Other')} color="secondary" />
          </Stack>
          <Typography variant="body1" sx={{ mb: 2 }}>{listing.name}</Typography>
          <Box sx={{ display: 'flex', gap: 4, mb: 2 }}>
            <Box>
              <Typography variant="h6" align="center">{listing.bedrooms}</Typography>
              <Typography variant="body2" align="center">Beds</Typography>
            </Box>
            <Box>
              <Typography variant="h6" align="center">{listing.bathrooms}</Typography>
              <Typography variant="body2" align="center">Baths</Typography>
            </Box>
            <Box>
              <Typography variant="h6" align="center">{listing.area}</Typography>
              <Typography variant="body2" align="center">m²</Typography>
            </Box>
            {property.lotSize > 0 && (
              <Box>
                <Typography variant="h6" align="center">{property.lotSize}</Typography>
                <Typography variant="body2" align="center">Lot Size</Typography>
              </Box>
            )}
            {property.garageSize > 0 && (
              <Box>
                <Typography variant="h6" align="center">{property.garageSize}</Typography>
                <Typography variant="body2" align="center">Garage</Typography>
              </Box>
            )}
            {property.parkingSpaces > 0 && (
              <Box>
                <Typography variant="h6" align="center">{property.parkingSpaces}</Typography>
                <Typography variant="body2" align="center">Parking</Typography>
              </Box>
            )}
          </Box>
          <Divider sx={{ my: 2 }} />
          <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>{listing.description}</Typography>
          {property.amenities && property.amenities.length > 0 && (
            <Box sx={{ mb: 2 }}>
              <Typography variant="subtitle2" sx={{ mb: 1 }}>Amenities</Typography>
              <Stack direction="row" spacing={1} flexWrap="wrap">
                {property.amenities.map((am: string, idx: number) => (
                  <Chip key={idx} label={am} variant="outlined" />
                ))}
              </Stack>
            </Box>
          )}
          <Grid container spacing={2} sx={{ mb: 2 }}>
            <Grid item xs={6}>
              <Typography variant="caption" color="text.secondary">MLS® ID</Typography>
              <Typography variant="body2">{listing.propertyId}</Typography>
            </Grid>
            <Grid item xs={6}>
              <Typography variant="caption" color="text.secondary">Last updated</Typography>
              <Typography variant="body2">{listing.updatedAt ? new Date(listing.updatedAt).toLocaleDateString() : ''}</Typography>
            </Grid>
            {property.hoaFee > 0 && (
              <Grid item xs={6}>
                <Typography variant="caption" color="text.secondary">HOA Fee</Typography>
                <Typography variant="body2">{formatCurrency(property.hoaFee)}</Typography>
              </Grid>
            )}
            {property.yearBuilt > 0 && (
              <Grid item xs={6}>
                <Typography variant="caption" color="text.secondary">Year Built</Typography>
                <Typography variant="body2">{property.yearBuilt}</Typography>
              </Grid>
            )}
          </Grid>
          {/* Price History Section */}
          {priceHistory.length > 0 && (
            <Box sx={{ mb: 3 }}>
              <Typography variant="h6" sx={{ fontWeight: 700, mb: 2 }}>Price history</Typography>
              <Box sx={{ border: `1px solid ${theme.palette.divider}`, borderRadius: 2, overflow: 'hidden' }}>
                <Box sx={{ display: 'flex', fontWeight: 600, bgcolor: theme.palette.action.hover, p: 1 }}>
                  <Box sx={{ flex: 1 }}>Date</Box>
                  <Box sx={{ flex: 2 }}>Event</Box>
                  <Box sx={{ flex: 2 }}>Price</Box>
                </Box>
                {priceHistory.map((item, idx) => {
                  // Xác định event và phần trăm thay đổi
                  let event = '';
                  let priceChange = null;
                  let pricePerSqm = '';
                  let priceStr = formatCurrency(item.price);
                  let percent = null;
                  let color = undefined;
                  let prev = priceHistory[idx + 1];
                  if (item.status === 'COMPLETED') event = 'Price change';
                  if (item.status === 'PENDING') event = 'Listed for sale';
                  if (item.status === 'CANCELLED') event = 'Listing removed';
                  if (listing.area && item.price) pricePerSqm = `$${Math.round(item.price / listing.area)}/sqft`;
                  if (prev && prev.price && item.price) {
                    percent = Math.round(((item.price - prev.price) / prev.price) * 1000) / 10;
                    color = percent > 0 ? 'success.main' : 'error.main';
                  }
                  return (
                    <Box key={item.id} sx={{ display: 'flex', alignItems: 'center', borderTop: idx === 0 ? 'none' : `1px solid ${theme.palette.divider}`, p: 1 }}>
                      <Box sx={{ flex: 1 }}>{item.updatedAt ? new Date(item.updatedAt).toLocaleDateString() : ''}</Box>
                      <Box sx={{ flex: 2, fontWeight: 500 }}>{event}</Box>
                      <Box sx={{ flex: 2 }}>
                        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                          <Typography component="span">{priceStr}</Typography>
                          {percent !== null && (
                            <Typography component="span" sx={{ color: color, fontWeight: 600 }}>
                              {percent > 0 ? `+${percent}%` : `${percent}%`}
                            </Typography>
                          )}
                        </Box>
                        <Typography variant="caption" color="text.secondary">{pricePerSqm}</Typography>
                      </Box>
                    </Box>
                  );
                })}
              </Box>
            </Box>
          )}
          <Button sx={{ mt: 2 }} variant="contained" color="primary" fullWidth onClick={() => setContactOpen(true)}>
            Contact Agent
          </Button>
        </Box>
      </DialogContent>
      <AgentContactDialog
        open={contactOpen}
        onClose={() => setContactOpen(false)}
        agentId={listing.agentId}
        agentName={listing.agentName}
        agentPhone={listing.agentPhone}
        agentEmail={listing.agentEmail}
      />
    </Dialog>
  );
};

export default ListingDetailDialog;