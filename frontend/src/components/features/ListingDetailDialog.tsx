import React, { useRef, useEffect, useState } from 'react';
import { Dialog, DialogContent, Box, Typography, IconButton, Grid, Button, Divider, Chip, Stack } from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import { Swiper, SwiperSlide } from 'swiper/react';
import { Autoplay } from 'swiper/modules';
import 'swiper/css';
import 'swiper/css/autoplay';
import { formatCurrency } from '../../utils/helpers';
import AgentContactDialog from './AgentContactDialog';

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

  useEffect(() => {
    setActiveSlide(currentIndex); // Đồng bộ activeSlide với currentIndex
  }, [currentIndex]);

  // Di chuyển kiểm tra listing xuống sau khi tất cả hooks được gọi
  if (!listing) {
    return null;
  }

  const images = listing.imageUrls && listing.imageUrls.length > 0 ? listing.imageUrls : [listing.featuredImageUrl];
  const address = listing.property?.address || {};
  const property = listing.property || {};
  const agentInfo = [listing.agentName, listing.agentPhone, listing.agentEmail].filter(Boolean).join(' | ');

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
          {agentInfo && (
            <Box sx={{ mb: 2 }}>
              <Typography variant="subtitle2">Agent Information</Typography>
              <Typography variant="body2">{agentInfo}</Typography>
            </Box>
          )}
          <Button variant="contained" color="primary" fullWidth onClick={() => setContactOpen(true)}>
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