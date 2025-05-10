import React, { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import {
  Box,
  Container,
  Grid,
  Typography,
  Card,
  CardContent,
  CardMedia,
  Button,
  TextField,
  InputAdornment,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Slider,
  Paper,
  Chip,
} from '@mui/material';
import {
  Search as SearchIcon,
  LocationOn,
  Bed,
  Bathtub,
  SquareFoot,
  Favorite,
  FavoriteBorder,
} from '@mui/icons-material';

interface Property {
  id: string;
  title: string;
  price: number;
  location: string;
  image: string;
  bedrooms: number;
  bathrooms: number;
  area: number;
}

const SearchPage: React.FC = () => {
  const [searchParams] = useSearchParams();
  const query = searchParams.get('q') || '';
  const [favorites, setFavorites] = useState<string[]>([]);
  const [priceRange, setPriceRange] = useState<number[]>([0, 1000000000]);
  const [propertyType, setPropertyType] = useState<string>('all');
  const [bedrooms, setBedrooms] = useState<string>('all');
  const [searchResults, setSearchResults] = useState<Property[]>([]);

  // Mock data
  const mockProperties: Property[] = [
    {
      id: '1',
      title: 'Căn hộ cao cấp tại Quận 1',
      price: 2500000000,
      location: 'Quận 1, TP.HCM',
      image: 'https://source.unsplash.com/random/800x600/?apartment',
      bedrooms: 3,
      bathrooms: 2,
      area: 120,
    },
    // Thêm các property khác ở đây
  ];

  useEffect(() => {
    // Simulate search
    const results = mockProperties.filter((property) =>
      property.title.toLowerCase().includes(query.toLowerCase()) ||
      property.location.toLowerCase().includes(query.toLowerCase())
    );
    setSearchResults(results);
  }, [query]);

  const handleFavorite = (id: string) => {
    setFavorites((prev) =>
      prev.includes(id) ? prev.filter((fid) => fid !== id) : [...prev, id]
    );
  };

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND',
    }).format(price);
  };

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Grid container spacing={4}>
        {/* Filters */}
        <Grid item xs={12} md={3}>
          <Paper sx={{ p: 2 }}>
            <Typography variant="h6" gutterBottom>
              Bộ lọc
            </Typography>

            <TextField
              fullWidth
              variant="outlined"
              placeholder="Tìm kiếm..."
              value={query}
              sx={{ mb: 2 }}
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                    <SearchIcon />
                  </InputAdornment>
                ),
              }}
            />

            <Box sx={{ mb: 3 }}>
              <Typography gutterBottom>Khoảng giá</Typography>
              <Slider
                value={priceRange}
                onChange={(_, newValue) => setPriceRange(newValue as number[])}
                valueLabelDisplay="auto"
                min={0}
                max={1000000000}
                step={100000000}
                valueLabelFormat={(value) => formatPrice(value)}
              />
            </Box>

            <FormControl fullWidth sx={{ mb: 2 }}>
              <InputLabel>Loại bất động sản</InputLabel>
              <Select
                value={propertyType}
                label="Loại bất động sản"
                onChange={(e) => setPropertyType(e.target.value)}
              >
                <MenuItem value="all">Tất cả</MenuItem>
                <MenuItem value="apartment">Căn hộ</MenuItem>
                <MenuItem value="house">Nhà riêng</MenuItem>
                <MenuItem value="villa">Biệt thự</MenuItem>
              </Select>
            </FormControl>

            <FormControl fullWidth>
              <InputLabel>Số phòng ngủ</InputLabel>
              <Select
                value={bedrooms}
                label="Số phòng ngủ"
                onChange={(e) => setBedrooms(e.target.value)}
              >
                <MenuItem value="all">Tất cả</MenuItem>
                <MenuItem value="1">1 phòng</MenuItem>
                <MenuItem value="2">2 phòng</MenuItem>
                <MenuItem value="3">3 phòng</MenuItem>
                <MenuItem value="4+">4+ phòng</MenuItem>
              </Select>
            </FormControl>
          </Paper>
        </Grid>

        {/* Search Results */}
        <Grid item xs={12} md={9}>
          <Box sx={{ mb: 3 }}>
            <Typography variant="h5" gutterBottom>
              Kết quả tìm kiếm cho "{query}"
            </Typography>
            <Typography variant="body1" color="text.secondary">
              Tìm thấy {searchResults.length} kết quả
            </Typography>
          </Box>

          <Grid container spacing={3}>
            {searchResults.map((property) => (
              <Grid item xs={12} sm={6} md={4} key={property.id}>
                <Card
                  sx={{
                    height: '100%',
                    display: 'flex',
                    flexDirection: 'column',
                  }}
                >
                  <CardMedia
                    component="img"
                    height="200"
                    image={property.image}
                    alt={property.title}
                  />
                  <CardContent sx={{ flexGrow: 1 }}>
                    <Typography gutterBottom variant="h6" component="h2">
                      {property.title}
                    </Typography>
                    <Typography variant="h6" color="primary" gutterBottom>
                      {formatPrice(property.price)}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      {property.location}
                    </Typography>
                    <Box sx={{ mt: 1, display: 'flex', gap: 2 }}>
                      <Typography variant="body2">
                        {property.bedrooms} phòng ngủ
                      </Typography>
                      <Typography variant="body2">
                        {property.bathrooms} phòng tắm
                      </Typography>
                      <Typography variant="body2">
                        {property.area}m²
                      </Typography>
                    </Box>
                  </CardContent>
                  <Box sx={{ p: 1, display: 'flex', justifyContent: 'flex-end' }}>
                    <Button
                      onClick={() => handleFavorite(property.id)}
                    >
                      {favorites.includes(property.id) ? (
                        <Favorite color="error" />
                      ) : (
                        <FavoriteBorder />
                      )}
                    </Button>
                  </Box>
                </Card>
              </Grid>
            ))}
          </Grid>
        </Grid>
      </Grid>
    </Container>
  );
};

export default SearchPage; 