import React, { useState } from 'react';
import { useParams } from 'react-router-dom';
import {
  Box,
  Container,
  Grid,
  Typography,
  Button,
  Paper,
  Divider,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  IconButton,
  Tab,
  Tabs,
} from '@mui/material';
import {
  Favorite,
  FavoriteBorder,
  LocationOn,
  Bed,
  Bathtub,
  SquareFoot,
  DirectionsCar,
  Security,
  Pool,
  FitnessCenter,
} from '@mui/icons-material';

interface TabPanelProps {
  children?: React.ReactNode;
  index: number;
  value: number;
}

function TabPanel(props: TabPanelProps) {
  const { children, value, index, ...other } = props;

  return (
    <div
      role="tabpanel"
      hidden={value !== index}
      id={`property-tabpanel-${index}`}
      aria-labelledby={`property-tab-${index}`}
      {...other}
    >
      {value === index && <Box sx={{ p: 3 }}>{children}</Box>}
    </div>
  );
}

const PropertyDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [favorite, setFavorite] = useState(false);
  const [tabValue, setTabValue] = useState(0);

  // Mock data
  const property = {
    id,
    title: 'Căn hộ cao cấp tại Quận 1',
    price: 2500000000,
    location: 'Quận 1, TP.HCM',
    images: [
      'https://source.unsplash.com/random/1200x800/?apartment',
      'https://source.unsplash.com/random/1200x800/?living-room',
      'https://source.unsplash.com/random/1200x800/?bedroom',
    ],
    description: `Căn hộ cao cấp với thiết kế hiện đại, nằm tại vị trí đắc địa của Quận 1. 
    Căn hộ được trang bị đầy đủ tiện nghi, nội thất cao cấp và view đẹp ra thành phố.`,
    features: {
      bedrooms: 3,
      bathrooms: 2,
      area: 120,
      parking: 1,
    },
    amenities: [
      'Hồ bơi',
      'Phòng gym',
      'Bảo vệ 24/7',
      'Khu vui chơi trẻ em',
      'Vườn cây xanh',
    ],
  };

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND',
    }).format(price);
  };

  const handleTabChange = (event: React.SyntheticEvent, newValue: number) => {
    setTabValue(newValue);
  };

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Grid container spacing={4}>
        {/* Main Content */}
        <Grid item xs={12} md={8}>
          {/* Image Gallery */}
          <Paper sx={{ mb: 4 }}>
            <Box
              component="img"
              src={property.images[0]}
              alt={property.title}
              sx={{
                width: '100%',
                height: 400,
                objectFit: 'cover',
              }}
            />
          </Paper>

          {/* Property Info */}
          <Paper sx={{ p: 3, mb: 4 }}>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
              <Typography variant="h4" component="h1">
                {property.title}
              </Typography>
              <IconButton onClick={() => setFavorite(!favorite)}>
                {favorite ? <Favorite color="error" /> : <FavoriteBorder />}
              </IconButton>
            </Box>

            <Typography variant="h5" color="primary" gutterBottom>
              {formatPrice(property.price)}
            </Typography>

            <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
              <LocationOn color="action" sx={{ mr: 1 }} />
              <Typography variant="body1">{property.location}</Typography>
            </Box>

            <Divider sx={{ my: 2 }} />

            <Grid container spacing={2}>
              <Grid item xs={6} sm={3}>
                <Box sx={{ display: 'flex', alignItems: 'center' }}>
                  <Bed sx={{ mr: 1 }} />
                  <Typography>{property.features.bedrooms} phòng ngủ</Typography>
                </Box>
              </Grid>
              <Grid item xs={6} sm={3}>
                <Box sx={{ display: 'flex', alignItems: 'center' }}>
                  <Bathtub sx={{ mr: 1 }} />
                  <Typography>{property.features.bathrooms} phòng tắm</Typography>
                </Box>
              </Grid>
              <Grid item xs={6} sm={3}>
                <Box sx={{ display: 'flex', alignItems: 'center' }}>
                  <SquareFoot sx={{ mr: 1 }} />
                  <Typography>{property.features.area}m²</Typography>
                </Box>
              </Grid>
              <Grid item xs={6} sm={3}>
                <Box sx={{ display: 'flex', alignItems: 'center' }}>
                  <DirectionsCar sx={{ mr: 1 }} />
                  <Typography>{property.features.parking} chỗ đậu xe</Typography>
                </Box>
              </Grid>
            </Grid>
          </Paper>

          {/* Tabs */}
          <Paper>
            <Tabs value={tabValue} onChange={handleTabChange} centered>
              <Tab label="Mô tả" />
              <Tab label="Tiện ích" />
              <Tab label="Vị trí" />
            </Tabs>

            <TabPanel value={tabValue} index={0}>
              <Typography variant="body1" paragraph>
                {property.description}
              </Typography>
            </TabPanel>

            <TabPanel value={tabValue} index={1}>
              <List>
                {property.amenities.map((amenity, index) => (
                  <ListItem key={index}>
                    <ListItemIcon>
                      {index === 0 ? (
                        <Pool />
                      ) : index === 1 ? (
                        <FitnessCenter />
                      ) : index === 2 ? (
                        <Security />
                      ) : (
                        <SquareFoot />
                      )}
                    </ListItemIcon>
                    <ListItemText primary={amenity} />
                  </ListItem>
                ))}
              </List>
            </TabPanel>

            <TabPanel value={tabValue} index={2}>
              <Typography variant="body1">
                Vị trí đắc địa tại trung tâm Quận 1, gần các tiện ích:
              </Typography>
              <List>
                <ListItem>
                  <ListItemIcon>
                    <LocationOn />
                  </ListItemIcon>
                  <ListItemText
                    primary="Trung tâm thương mại"
                    secondary="5 phút đi bộ"
                  />
                </ListItem>
                <ListItem>
                  <ListItemIcon>
                    <LocationOn />
                  </ListItemIcon>
                  <ListItemText
                    primary="Trường học"
                    secondary="10 phút đi bộ"
                  />
                </ListItem>
                <ListItem>
                  <ListItemIcon>
                    <LocationOn />
                  </ListItemIcon>
                  <ListItemText
                    primary="Bệnh viện"
                    secondary="15 phút đi xe"
                  />
                </ListItem>
              </List>
            </TabPanel>
          </Paper>
        </Grid>

        {/* Sidebar */}
        <Grid item xs={12} md={4}>
          <Paper sx={{ p: 3, position: 'sticky', top: 20 }}>
            <Typography variant="h6" gutterBottom>
              Liên hệ
            </Typography>
            <Button
              variant="contained"
              color="primary"
              fullWidth
              size="large"
              sx={{ mb: 2 }}
            >
              Gọi ngay
            </Button>
            <Button
              variant="outlined"
              color="primary"
              fullWidth
              size="large"
            >
              Nhắn tin
            </Button>
          </Paper>
        </Grid>
      </Grid>
    </Container>
  );
};

export default PropertyDetailPage; 