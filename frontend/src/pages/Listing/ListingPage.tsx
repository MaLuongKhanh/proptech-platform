import React, { useState, useEffect, useCallback } from 'react';
import { Box, Grid, Paper, Typography, FormControl, InputLabel, Select, MenuItem, Slider, TextField, CircularProgress } from '@mui/material';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import { ListingCard } from '../../components/features/ListingCard';
import ListingDetailDialog from '../../components/features/ListingDetailDialog';
import { listingService } from '../../services/listing.service';
import { Listing, ListingSearchParams } from '../../types/listing.types';
import styles from '../../styles/Listing/ListingPage.module.css';
import L from 'leaflet';
import { useLocation } from 'react-router-dom';

// Fix Leaflet default icon paths
delete (L.Icon.Default.prototype as any)._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png',
  iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
  shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
});

// Custom marker icon
const customMarkerIcon = new L.Icon({
  iconUrl: 'https://cdn-icons-png.flaticon.com/512/684/684908.png',
  iconSize: [32, 32],
  iconAnchor: [16, 32],
  popupAnchor: [0, -32],
  shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
  shadowSize: [41, 41],
});

export const ListingPage: React.FC = () => {
  const [type, setType] = useState<string>('all');
  const [price, setPrice] = useState<number[]>([0, 10000000000]);
  const [bedrooms, setBedrooms] = useState<string>('all');
  const [search, setSearch] = useState('');
  const [listings, setListings] = useState<Listing[]>([]);
  const [loading, setLoading] = useState(false);
  const [selectedListing, setSelectedListing] = useState<Listing | null>(null);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [propertyType, setPropertyType] = useState<string>('all');
  const location = useLocation();
  const [initialized, setInitialized] = useState(false);
  const [sold, setSold] = useState<string>('all');

  // Lấy filter params
  const getParams = useCallback((): ListingSearchParams => {
    const params: ListingSearchParams = {};
    if (type !== 'all') params.listingType = type as 'SALE' | 'RENT';
    if (propertyType !== 'all') params.propertyType = propertyType as any;
    if (bedrooms !== 'all') params.minBedrooms = Number(bedrooms);
    if (price[0] > 0) params.minPrice = price[0];
    if (price[1] < 10000000000) params.maxPrice = price[1];
    if (search) params.address = search;
    return params;
  }, [type, propertyType, bedrooms, price, search]);

  // Đọc query param và set filter, chỉ fetch sau khi đã set xong
  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const typeParam = params.get('type');
    const provinceParam = params.get('province');
    let changed = false;
    if (typeParam && (typeParam === 'SALE' || typeParam === 'RENT')) {
      setType(typeParam);
      changed = true;
    }
    if (provinceParam) {
      setSearch(provinceParam);
      changed = true;
    }
    if (changed) {
      setInitialized(false); // Đợi state cập nhật xong mới fetch
    } else {
      setInitialized(true);
    }
  }, [location.search]);

  // Khi filter đổi, chỉ fetch nếu đã initialized
  useEffect(() => {
    if (!initialized) {
      setInitialized(true);
      return;
    }
    setLoading(true);
    listingService.getAll({
      ...(type !== 'all' && { listingType: type as 'SALE' | 'RENT' }),
      ...(propertyType !== 'all' && { propertyType: propertyType as any }),
      ...(bedrooms !== 'all' && { minBedrooms: Number(bedrooms) }),
      ...(price[0] > 0 && { minPrice: price[0] }),
      ...(price[1] < 10000000000 && { maxPrice: price[1] }),
      ...(search && { address: search }),
    })
      .then((data) => {
        let filtered = data;
        if (sold === 'sold') filtered = filtered.filter(item => item.isSold === true);
        if (sold === 'not_sold') filtered = filtered.filter(item => item.isSold === false);
        setListings(filtered);
      })
      .finally(() => setLoading(false));
  }, [type, propertyType, bedrooms, price, search, initialized, sold]);

  // Xử lý popup detail và history
  useEffect(() => {
    const onPopState = () => {
      setDialogOpen(false);
      setSelectedListing(null);
    };
    window.addEventListener('popstate', onPopState);
    return () => window.removeEventListener('popstate', onPopState);
  }, []);

  const handleOpenDetail = (listing: Listing) => {
    setSelectedListing(listing);
    setDialogOpen(true);
    window.history.pushState({}, '', `/listings/${listing.id}`);
  };
  const handleCloseDetail = () => {
    setDialogOpen(false);
    setSelectedListing(null);
    window.history.back();
  };

  return (
    <Box className={styles.container}>
      {/* Filter */}
      <Paper className={styles.filterBar}>
        <TextField
          label="Search by area, address..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          className={styles.searchInput}
          placeholder="Address, neighborhood, city, ZIP"
        />
        <FormControl className={styles.filterDropdown}>
          <InputLabel>For Sale / Rent</InputLabel>
          <Select value={type} label="For Sale / Rent" onChange={(e) => setType(e.target.value)}>
            <MenuItem value="all">All</MenuItem>
            <MenuItem value="SALE">For Sale</MenuItem>
            <MenuItem value="RENT">For Rent</MenuItem>
          </Select>
        </FormControl>
        <FormControl className={styles.filterDropdown}>
          <InputLabel>Price</InputLabel>
          <Select
            value={price.join('-')}
            label="Price"
            onChange={(e) => {
              const [min, max] = e.target.value.split('-').map(Number);
              setPrice([min, max]);
            }}
          >
            <MenuItem value="0-10000000000">All</MenuItem>
            <MenuItem value="0-1000000000">Under 1B</MenuItem>
            <MenuItem value="1000000000-3000000000">1B - 3B</MenuItem>
            <MenuItem value="3000000000-5000000000">3B - 5B</MenuItem>
            <MenuItem value="5000000000-10000000000">Above 5B</MenuItem>
          </Select>
        </FormControl>
        <FormControl className={styles.filterDropdown}>
          <InputLabel>Beds & Baths</InputLabel>
          <Select value={bedrooms} label="Beds & Baths" onChange={(e) => setBedrooms(e.target.value)}>
            <MenuItem value="all">All</MenuItem>
            <MenuItem value="1">1+</MenuItem>
            <MenuItem value="2">2+</MenuItem>
            <MenuItem value="3">3+</MenuItem>
            <MenuItem value="4">4+</MenuItem>
          </Select>
        </FormControl>
        <FormControl className={styles.filterDropdown}>
          <InputLabel>Sold</InputLabel>
          <Select value={sold} label="Sold" onChange={(e) => setSold(e.target.value)}>
            <MenuItem value="all">All</MenuItem>
            <MenuItem value="sold">Sold</MenuItem>
            <MenuItem value="not_sold">Not Sold</MenuItem>
          </Select>
        </FormControl>
      </Paper>
      {/* Main content */}
      <Grid container className={styles.mainContent}>
        {/* Map */}
        <Grid item xs={12} md={6} className={styles.mapArea}>
          <MapContainer
            center={[10.7769, 106.7009]}
            zoom={12}
            style={{ height: '100%', width: '100%' }}
          >
            <TileLayer
              attribution='&copy; <a href="https://osm.org/copyright">OpenStreetMap</a> contributors'
              url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            />
            {listings.map((item) => (
              <Marker
                key={item.id}
                position={item.property && item.property.address.latitude && item.property.address.longitude ? [item.property.address.latitude, item.property.address.longitude] : [10.7769, 106.7009]}
                icon={customMarkerIcon}
                eventHandlers={{
                  click: () => handleOpenDetail(item),
                }}
              >
                <Popup>
                  <Typography variant="subtitle2">{item.name}</Typography>
                  <Typography variant="body2" color="primary">
                    {item.price?.toLocaleString('vi-VN')} VNĐ
                  </Typography>
                </Popup>
              </Marker>
            ))}
          </MapContainer>
        </Grid>
        {/* Listing */}
        <Grid item xs={12} md={6} className={styles.listingArea}>
          <Typography variant="h6" className={styles.resultTitle}>
            {loading ? <CircularProgress size={24} /> : `${listings.length} results found`}
          </Typography>
          <Grid container spacing={2}>
            {listings.map((item) => (
              <Grid item xs={12} sm={6} key={item.id}>
                <Box>
                  <ListingCard listing={item} onFavorite={() => {}} isFavorite={false} onClick={() => handleOpenDetail(item)} />
                </Box>
              </Grid>
            ))}
          </Grid>
        </Grid>
      </Grid>
      <ListingDetailDialog open={dialogOpen} onClose={handleCloseDetail} listing={selectedListing} />
    </Box>
  );
};
 