import React, { useState, useEffect } from 'react';
import {
  Box,
  Grid,
  Paper,
  Typography,
  Button,
  Chip,
  IconButton,
  Menu,
  MenuItem,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Autocomplete,
  CircularProgress,
  Alert,
  ImageList,
  ImageListItem,
  LinearProgress,
  Snackbar,
  Pagination,
} from '@mui/material';
import {
  MoreVert as MoreVertIcon,
  Edit as EditIcon,
  Delete as DeleteIcon,
  Visibility as VisibilityIcon,
  Add as AddIcon,
  AddPhotoAlternate as AddPhotoIcon,
} from '@mui/icons-material';
import { motion } from 'framer-motion';
import { listingService } from '../../../services/listing.service';
import { propertyService } from '../../../services/property.service';
import { Listing, AddListingRequest, UpdateListingRequest } from '../../../types/listing.types';
import { Property, PropertyType } from '../../../types/property.types';
import ListingDetailDialog from '../../../components/features/ListingDetailDialog';
import { AddPropertyRequest } from '../../../types/property.types';
import { saleTransactionService } from '../../../services/sale-transaction.service';
import { rentalTransactionService } from '../../../services/rental-transaction.service';
import { paymentService } from '../../../services/payment.service';
import { Transaction } from '../../../types/payment.types';

const Listings: React.FC = () => {
  const [listings, setListings] = useState<Listing[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
  const [selectedListing, setSelectedListing] = useState<Listing | null>(null);
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
  const [viewDialogOpen, setViewDialogOpen] = useState(false);
  const [editDialogOpen, setEditDialogOpen] = useState(false);
  const [createDialogOpen, setCreateDialogOpen] = useState(false);
  const [properties, setProperties] = useState<Property[]>([]);
  const [selectedProperty, setSelectedProperty] = useState<Property | null>(null);
  const [newListing, setNewListing] = useState<Partial<AddListingRequest>>({
    name: '',
    description: '',
    price: 0,
    listingType: 'SALE',
    bedrooms: 0,
    bathrooms: 0,
    area: 0,
  });
  const [images, setImages] = useState<File[]>([]);
  const [featuredImage, setFeaturedImage] = useState<File | null>(null);
  const [previewUrls, setPreviewUrls] = useState<string[]>([]);
  const [featuredPreviewUrl, setFeaturedPreviewUrl] = useState<string | null>(null);
  const [menuAnchorEl, setMenuAnchorEl] = useState<null | HTMLElement>(null);
  const [uploadProgress, setUploadProgress] = useState(0);
  const [isUploading, setIsUploading] = useState(false);
  const [snackbar, setSnackbar] = useState({
    open: false,
    message: '',
    severity: 'success' as 'success' | 'error',
  });
  const [createPropertyDialogOpen, setCreatePropertyDialogOpen] = useState(false);
  const [newProperty, setNewProperty] = useState<Partial<AddPropertyRequest>>({
    address: {
      street: '',
      city: '',
      province: '',
      country: '',
      postalCode: '',
      latitude: 0,
      longitude: 0
    },
    propertyType: 'UNKNOWN_PROPERTY',
    yearBuilt: new Date().getFullYear(),
    lotSize: 0,
    parkingSpaces: 0,
    garageSize: 0,
    amenities: [],
    hoaFee: 0
  });
  const [selectedAmenities, setSelectedAmenities] = useState<string[]>([]);
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(5);
  const [soldDialogOpen, setSoldDialogOpen] = useState(false);
  const [soldDialogListing, setSoldDialogListing] = useState<Listing | null>(null);
  const [buyerName, setBuyerName] = useState('');
  const [buyerIdentity, setBuyerIdentity] = useState('');
  const [isSubmittingSold, setIsSubmittingSold] = useState(false);
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [remainingQuota, setRemainingQuota] = useState(0);
  const [quotaError, setQuotaError] = useState<string | null>(null);

  const amenityOptions = [
    'Swimming Pool',
    'Gym',
    'Parking',
    'Security',
    'Elevator',
    'Air Conditioning',
    'Balcony',
    'Garden',
    'Playground',
    'BBQ Area',
    'Tennis Court',
    'Basketball Court',
    'Clubhouse',
    '24/7 Security',
    'CCTV',
    'Smart Home',
    'Pet Friendly',
    'Furnished',
    'Storage',
    'Laundry'
  ];

  useEffect(() => {
    fetchListings();
    fetchProperties();
    checkRemainingQuota();
  }, []);

  const fetchListings = async () => {
    try {
      setLoading(true);
      const userString = localStorage.getItem('user');
      if (userString) {
        const parsedUser = JSON.parse(userString);
        const data = await listingService.getByAgentId(parsedUser.id);
        console.log('Fetched listings:', data);
        setListings(data);
      } else {
        throw new Error('Can not find user ID');
      }
    } catch (err) {
      setError('Failed to load listings');
      console.error('Error fetching listings:', err);
    } finally {
      setLoading(false);
    }
  };

  const fetchProperties = async () => {
    try {
      const data = await propertyService.getAll();
      console.log('Fetched properties:', data);
      setProperties(data);
    } catch (err) {
      console.error('Error fetching properties:', err);
    }
  };

  const checkRemainingQuota = () => {
    const userString = localStorage.getItem('user');
    if (userString) {
      const user = JSON.parse(userString);
      const data = JSON.parse(localStorage.getItem(`user_package_${user.id}`) || '{}');
      setRemainingQuota(data.remaining || 0);
    }
  };

  const handleMenuClick = (event: React.MouseEvent<HTMLElement>, listing: Listing) => {
    setMenuAnchorEl(event.currentTarget);
    setSelectedListing(listing);
  };

  const handleMenuClose = () => {
    setMenuAnchorEl(null);
  };

  const handleDeleteClick = () => {
    setDeleteDialogOpen(true);
    handleMenuClose();
  };

  const handleDeleteConfirm = async () => {
    if (!selectedListing) return;
    try {
      const success = await listingService.delete(selectedListing.id);
      console.log('Delete success:', success);
      if (success) {
        await fetchListings();
        setSnackbar({
          open: true,
          message: 'Successfully deleted listing',
          severity: 'success',
        });
      }
    } catch (err) {
      setError('Failed to delete listing');
      console.error('Error deleting listing:', err);
      setSnackbar({
        open: true,
        message: 'Failed to delete listing',
        severity: 'error',
      });
    }
    setDeleteDialogOpen(false);
  };

  const handleViewClick = () => {
    setViewDialogOpen(true);
    handleMenuClose();
  };

  const handleEditClick = () => {
    if (selectedListing) {
      setNewListing({
        name: selectedListing.name,
        description: selectedListing.description,
        price: selectedListing.price,
        listingType: selectedListing.listingType,
        bedrooms: selectedListing.bedrooms,
        bathrooms: selectedListing.bathrooms,
        area: selectedListing.area,
      });
      setEditDialogOpen(true);
    }
    handleMenuClose();
  };

  const handleCreateClick = () => {
    setCreateDialogOpen(true);
  };

  const handleImageChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.files) {
      const files = Array.from(event.target.files);
      setImages(files);
      const urls = files.map(file => URL.createObjectURL(file));
      setPreviewUrls(urls);
    }
  };

  const handleFeaturedImageChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.files && event.target.files[0]) {
      const file = event.target.files[0];
      setFeaturedImage(file);
      setFeaturedPreviewUrl(URL.createObjectURL(file));
    }
  };

  const simulateProgress = () => {
    let progress = 0;
    const interval = setInterval(() => {
      progress += Math.random() * 10;
      if (progress > 99) {
        clearInterval(interval);
        return;
      }
      setUploadProgress(progress);
    }, 500);
    return interval;
  };

  const handleCreateSubmit = async () => {
    if (!selectedProperty || !featuredImage) {
      setSnackbar({
        open: true,
        message: 'Please select a property and featured image',
        severity: 'error',
      });
      return;
    }

    if (remainingQuota <= 0) {
      setQuotaError('Bạn đã hết lượt đăng tin. Vui lòng mua thêm gói dịch vụ!');
      return;
    }

    try {
      setIsUploading(true);
      const progressInterval = simulateProgress();

      const listingData: AddListingRequest = {
        ...newListing as AddListingRequest,
        propertyId: selectedProperty.propertyId,
        images,
        featuredImage,
        agentId: JSON.parse(localStorage.getItem('user') || '{}').id
      };

      const createdListing = await listingService.create(listingData);
      console.log('Create result:', createdListing);

      // Trừ số lần đăng
      const userString = localStorage.getItem('user');
      if (userString) {
        const user = JSON.parse(userString);
        const data = JSON.parse(localStorage.getItem(`user_package_${user.id}`) || '{}');
        const newRemaining = data.remaining - 1;
        localStorage.setItem(`user_package_${user.id}`, JSON.stringify({
          ...data,
          remaining: newRemaining
        }));
        setRemainingQuota(newRemaining);

        // Thêm vào lịch sử
        const history = JSON.parse(localStorage.getItem(`user_package_history_${user.id}`) || '[]');
        history.unshift({
          action: 'Đăng tin',
          time: new Date().toLocaleString('vi-VN'),
          value: -1
        });
        localStorage.setItem(`user_package_history_${user.id}`, JSON.stringify(history));
      }

      clearInterval(progressInterval);
      setUploadProgress(100);

      if (createdListing) {
        setSnackbar({
          open: true,
          message: 'Successfully created listing',
          severity: 'success',
        });
        await fetchListings();
        setCreateDialogOpen(false);
        resetForm();
      } else {
        throw new Error('Created listing is null');
      }
    } catch (err) {
      clearInterval(simulateProgress());
      setUploadProgress(0);
      console.error('Error creating listing:', err);
      setSnackbar({
        open: true,
        message: 'Failed to create new listing',
        severity: 'error',
      });
    } finally {
      setIsUploading(false);
      setUploadProgress(0);
    }
  };

  const handleEditSubmit = async () => {
    if (!selectedListing) {
      setSnackbar({
        open: true,
        message: 'No listing selected for editing',
        severity: 'error',
      });
      return;
    }

    try {
      setIsUploading(true);
      const progressInterval = simulateProgress();

      const updatedListing = await listingService.update(selectedListing.id, {
        ...newListing,
        images: images.length > 0 ? images : undefined,
        featuredImage: featuredImage || undefined,
      } as UpdateListingRequest);

      console.log('Update result:', updatedListing);

      clearInterval(progressInterval);
      setUploadProgress(100);

      if (updatedListing) {
        setSnackbar({
          open: true,
          message: 'Successfully updated listing',
          severity: 'success',
        });
        await fetchListings();
        setEditDialogOpen(false);
        resetForm();
      } else {
        throw new Error('Updated listing is null');
      }
    } catch (err) {
      clearInterval(simulateProgress());
      setUploadProgress(0);
      console.error('Error updating listing:', err);
      setSnackbar({
        open: true,
        message: 'Failed to update listing',
        severity: 'error',
      });
    } finally {
      setIsUploading(false);
      setUploadProgress(0);
    }
  };

  const resetForm = () => {
    setNewListing({
      name: '',
      description: '',
      price: 0,
      listingType: 'SALE',
      bedrooms: 0,
      bathrooms: 0,
      area: 0,
    });
    setImages([]);
    setFeaturedImage(null);
    setPreviewUrls([]);
    setFeaturedPreviewUrl(null);
    setSelectedProperty(null);
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'active':
        return 'success';
      case 'pending':
        return 'warning';
      case 'inactive':
        return 'error';
      default:
        return 'default';
    }
  };

  const handleCreateProperty = async () => {
    try {
      const propertyData = {
        ...newProperty,
        amenities: selectedAmenities
      } as AddPropertyRequest;
      
      const createdProperty = await propertyService.create(propertyData);
      if (createdProperty) {
        setProperties([...properties, createdProperty]);
        setSelectedProperty(createdProperty);
        setCreatePropertyDialogOpen(false);
        setSelectedAmenities([]);
        setNewProperty({
          address: {
            street: '',
            city: '',
            province: '',
            country: '',
            postalCode: '',
            latitude: 0,
            longitude: 0
          },
          propertyType: 'UNKNOWN_PROPERTY',
          yearBuilt: new Date().getFullYear(),
          lotSize: 0,
          parkingSpaces: 0,
          garageSize: 0,
          amenities: [],
          hoaFee: 0
        });
        setSnackbar({
          open: true,
          message: 'Successfully created property',
          severity: 'success'
        });
      }
    } catch (err) {
      console.error('Error creating property:', err);
      setSnackbar({
        open: true,
        message: 'Failed to create new property',
        severity: 'error'
      });
    }
  };

  // Sắp xếp listing: chưa bán lên trên, đã bán xuống dưới
  const sortedListings = [...listings].sort((a, b) => Number(a.isSold) - Number(b.isSold));
  const pagedListings = sortedListings.slice((page - 1) * pageSize, page * pageSize);

  // Convert yyyy-MM-dd to ISO string for Instant
  function toISOString(dateStr: string) {
    if (!dateStr) return undefined;
    return dateStr.length === 10 ? `${dateStr}T00:00:00Z` : dateStr;
  }

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', p: 3 }}>
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Box sx={{ p: 3 }}>
        <Alert severity="error">{error}</Alert>
      </Box>
    );
  }

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 3 }}>
        <Typography variant="h6">My Listings</Typography>
        <Box sx={{ mb: 3, display: 'flex', gap: 2 }}>
          <Button
            variant="contained"
            color="primary"
            onClick={() => setCreateDialogOpen(true)}
            startIcon={<AddIcon />}
          >
            Create New Listing
          </Button>
          <Button
            variant="outlined"
            color="primary"
            onClick={() => setCreatePropertyDialogOpen(true)}
            startIcon={<AddIcon />}
          >
            Create New Property
          </Button>
        </Box>
      </Box>

      {quotaError && (
        <Alert severity="error" sx={{ mb: 2 }} onClose={() => setQuotaError(null)}>
          {quotaError}
        </Alert>
      )}

      <Grid container spacing={3}>
        {pagedListings.map((listing) => (
          <Grid item xs={12} key={listing.id}>
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.3 }}
            >
              <Paper sx={{ p: 2 }}>
                <Grid container spacing={2}>
                  <Grid item xs={12} sm={3}>
                    <Box
                      component="img"
                      src={listing.featuredImageUrl}
                      alt={listing.name}
                      sx={{
                        width: '100%',
                        height: 150,
                        objectFit: 'cover',
                        borderRadius: 1,
                      }}
                    />
                  </Grid>
                  <Grid item xs={12} sm={7}>
                    <Typography variant="h6" gutterBottom>
                      {listing.name}
                    </Typography>
                    <Typography variant="h6" color="primary" gutterBottom>
                      {new Intl.NumberFormat('vi-VN', {
                        style: 'currency',
                        currency: 'VND',
                      }).format(listing.price)}
                    </Typography>
                    <Box sx={{ display: 'flex', gap: 1, mb: 1 }}>
                      <Chip
                        label={listing.listingType === 'SALE' ? 'Sale' : 'Rent'}
                        size="small"
                        color="primary"
                      />
                      <Chip
                        label={listing.isActive ? 'Active' : 'Pending'}
                        size="small"
                        color={getStatusColor(listing.isActive ? 'active' : 'pending')}
                      />
                      {listing.isSold && (
                        <Chip
                          label="Sold"
                          color="default"
                          size="small"
                          sx={{ backgroundColor: '#888', color: '#fff' }}
                        />
                      )}
                    </Box>
                    <Typography variant="body2" color="text.secondary">
                      {listing.bedrooms} beds • {listing.bathrooms} baths • {listing.area}m²
                    </Typography>
                  </Grid>
                  <Grid item xs={12} sm={2} sx={{ textAlign: 'right' }}>
                    {!listing.isSold && (
                      <Button
                        variant="outlined"
                        color="secondary"
                        size="small"
                        sx={{ mb: 1 }}
                        onClick={() => {
                          setSoldDialogListing(listing);
                          setBuyerName('');
                          setBuyerIdentity('');
                          setStartDate('');
                          setEndDate('');
                          setSoldDialogOpen(true);
                        }}
                      >
                        Set Sold
                      </Button>
                    )}
                    <IconButton
                      onClick={(e) => handleMenuClick(e, listing)}
                      size="small"
                    >
                      <MoreVertIcon />
                    </IconButton>
                  </Grid>
                </Grid>
              </Paper>
            </motion.div>
          </Grid>
        ))}
      </Grid>

      <Box sx={{ display: 'flex', justifyContent: 'center', mt: 3 }}>
        <Pagination
          count={Math.ceil(listings.length / pageSize)}
          page={page}
          onChange={(_, value) => setPage(value)}
          color="primary"
        />
      </Box>

      <Menu
        anchorEl={menuAnchorEl}
        open={Boolean(menuAnchorEl)}
        onClose={handleMenuClose}
      >
        <MenuItem onClick={handleViewClick}>
          <VisibilityIcon fontSize="small" sx={{ mr: 1 }} />
          View Details
        </MenuItem>
        <MenuItem onClick={handleEditClick}>
          <EditIcon fontSize="small" sx={{ mr: 1 }} />
          Edit
        </MenuItem>
        <MenuItem onClick={handleDeleteClick}>
          <DeleteIcon fontSize="small" sx={{ mr: 1 }} />
          Delete
        </MenuItem>
      </Menu>

      <ListingDetailDialog
        open={viewDialogOpen}
        onClose={() => {
          setViewDialogOpen(false);
          setSelectedListing(null);
        }}
        listing={selectedListing}
      />

      <Dialog
        open={editDialogOpen}
        onClose={() => {
          setEditDialogOpen(false);
          resetForm();
        }}
        maxWidth="md"
        fullWidth
      >
        <DialogTitle>Edit Listing</DialogTitle>
        <DialogContent>
          {isUploading && (
            <Box sx={{ width: '100%', mb: 2 }}>
              <LinearProgress variant="determinate" value={uploadProgress} />
              <Typography variant="body2" color="text.secondary" align="center" sx={{ mt: 1 }}>
                Đang cập nhật... {Math.round(uploadProgress)}%
              </Typography>
            </Box>
          )}
          <Box sx={{ mt: 2 }}>
            <TextField
              fullWidth
              label="Listing Name"
              value={newListing.name}
              onChange={(e) => setNewListing({ ...newListing, name: e.target.value })}
              sx={{ mb: 2 }}
            />
            <TextField
              fullWidth
              label="Description"
              multiline
              rows={4}
              value={newListing.description}
              onChange={(e) => setNewListing({ ...newListing, description: e.target.value })}
              sx={{ mb: 2 }}
            />
            <TextField
              fullWidth
              label="Price"
              type="number"
              value={newListing.price}
              onChange={(e) => setNewListing({ ...newListing, price: Number(e.target.value) })}
              sx={{ mb: 2 }}
            />
            <TextField
              fullWidth
              label="Bedrooms"
              type="number"
              value={newListing.bedrooms}
              onChange={(e) => setNewListing({ ...newListing, bedrooms: Number(e.target.value) })}
              sx={{ mb: 2 }}
            />
            <TextField
              fullWidth
              label="Bathrooms"
              type="number"
              value={newListing.bathrooms}
              onChange={(e) => setNewListing({ ...newListing, bathrooms: Number(e.target.value) })}
              sx={{ mb: 2 }}
            />
            <TextField
              fullWidth
              label="Area (m²)"
              type="number"
              value={newListing.area}
              onChange={(e) => setNewListing({ ...newListing, area: Number(e.target.value) })}
              sx={{ mb: 2 }}
            />
            <Box sx={{ mb: 2 }}>
              <Typography variant="subtitle2" gutterBottom>
                Featured Image
              </Typography>
              <Button
                variant="outlined"
                component="label"
                startIcon={<AddPhotoIcon />}
                sx={{ mb: 1 }}
              >
                Choose Featured Image
                <input
                  type="file"
                  hidden
                  accept="image/*"
                  onChange={handleFeaturedImageChange}
                />
              </Button>
              {featuredPreviewUrl && (
                <Box
                  component="img"
                  src={featuredPreviewUrl}
                  alt="Featured preview"
                  sx={{
                    width: '100%',
                    maxHeight: 200,
                    objectFit: 'cover',
                    borderRadius: 1,
                  }}
                />
              )}
            </Box>
            <Box>
              <Typography variant="subtitle2" gutterBottom>
                Other Images
              </Typography>
              <Button
                variant="outlined"
                component="label"
                startIcon={<AddPhotoIcon />}
                sx={{ mb: 1 }}
              >
                Choose Multiple Images
                <input
                  type="file"
                  hidden
                  accept="image/*"
                  multiple
                  onChange={handleImageChange}
                />
              </Button>
              <ImageList cols={3} gap={8}>
                {previewUrls.map((url, index) => (
                  <ImageListItem key={index}>
                    <img
                      src={url}
                      alt={`Preview ${index + 1}`}
                      loading="lazy"
                      style={{ height: 200, objectFit: 'cover' }}
                    />
                  </ImageListItem>
                ))}
              </ImageList>
            </Box>
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => {
            setEditDialogOpen(false);
            resetForm();
          }}>Cancel</Button>
          <Button
            onClick={handleEditSubmit}
            variant="contained"
            disabled={isUploading}
          >
            {isUploading ? 'Updating...' : 'Save Changes'}
          </Button>
        </DialogActions>
      </Dialog>

      <Dialog
        open={createDialogOpen}
        onClose={() => {
          setCreateDialogOpen(false);
          resetForm();
        }}
        maxWidth="md"
        fullWidth
      >
        <DialogTitle>Create New Listing</DialogTitle>
        <DialogContent>
          {isUploading && (
            <Box sx={{ width: '100%', mb: 2 }}>
              <LinearProgress variant="determinate" value={uploadProgress} />
              <Typography variant="body2" color="text.secondary" align="center" sx={{ mt: 1 }}>
                Đang tải lên... {Math.round(uploadProgress)}%
              </Typography>
            </Box>
          )}
          <Box sx={{ mt: 2 }}>
            <Autocomplete
              options={properties}
              getOptionLabel={(option) => `${option.address.street}, ${option.address.city}`}
              value={selectedProperty}
              onChange={(_, newValue) => setSelectedProperty(newValue)}
              renderInput={(params) => (
                <TextField
                  {...params}
                  label="Chọn bất động sản"
                  fullWidth
                  sx={{ mb: 2 }}
                />
              )}
            />
            <TextField
              fullWidth
              label="Listing Name"
              value={newListing.name}
              onChange={(e) => setNewListing({ ...newListing, name: e.target.value })}
              sx={{ mb: 2 }}
            />
            <TextField
              fullWidth
              label="Description"
              multiline
              rows={4}
              value={newListing.description}
              onChange={(e) => setNewListing({ ...newListing, description: e.target.value })}
              sx={{ mb: 2 }}
            />
            <TextField
              fullWidth
              label="Price"
              type="number"
              value={newListing.price}
              onChange={(e) => setNewListing({ ...newListing, price: Number(e.target.value) })}
              sx={{ mb: 2 }}
            />
            <TextField
              fullWidth
              label="Bedrooms"
              type="number"
              value={newListing.bedrooms}
              onChange={(e) => setNewListing({ ...newListing, bedrooms: Number(e.target.value) })}
              sx={{ mb: 2 }}
            />
            <TextField
              fullWidth
              label="Bathrooms"
              type="number"
              value={newListing.bathrooms}
              onChange={(e) => setNewListing({ ...newListing, bathrooms: Number(e.target.value) })}
              sx={{ mb: 2 }}
            />
            <TextField
              fullWidth
              label="Area (m²)"
              type="number"
              value={newListing.area}
              onChange={(e) => setNewListing({ ...newListing, area: Number(e.target.value) })}
              sx={{ mb: 2 }}
            />
            <Box sx={{ mb: 2 }}>
              <Typography variant="subtitle2" gutterBottom>
                Featured Image
              </Typography>
              <Button
                variant="outlined"
                component="label"
                startIcon={<AddPhotoIcon />}
                sx={{ mb: 1 }}
              >
                Choose Featured Image
                <input
                  type="file"
                  hidden
                  accept="image/*"
                  onChange={handleFeaturedImageChange}
                />
              </Button>
              {featuredPreviewUrl && (
                <Box
                  component="img"
                  src={featuredPreviewUrl}
                  alt="Featured preview"
                  sx={{
                    width: '100%',
                    maxHeight: 200,
                    objectFit: 'cover',
                    borderRadius: 1,
                  }}
                />
              )}
            </Box>
            <Box>
              <Typography variant="subtitle2" gutterBottom>
                Other Images
              </Typography>
              <Button
                variant="outlined"
                component="label"
                startIcon={<AddPhotoIcon />}
                sx={{ mb: 1 }}
              >
                Choose Multiple Images
                <input
                  type="file"
                  hidden
                  accept="image/*"
                  multiple
                  onChange={handleImageChange}
                />
              </Button>
              <ImageList cols={3} gap={8}>
                {previewUrls.map((url, index) => (
                  <ImageListItem key={index}>
                    <img
                      src={url}
                      alt={`Preview ${index + 1}`}
                      loading="lazy"
                      style={{ height: 200, objectFit: 'cover' }}
                    />
                  </ImageListItem>
                ))}
              </ImageList>
            </Box>
          </Box>
        </DialogContent>
        <DialogActions>
          <Button
            onClick={() => {
              setCreateDialogOpen(false);
              resetForm();
            }}
            disabled={isUploading}
          >
            Cancel
          </Button>
          <Button
            onClick={handleCreateSubmit}
            variant="contained"
            disabled={!selectedProperty || !featuredImage || isUploading}
          >
            {isUploading ? 'Creating...' : 'Create Listing'}
          </Button>
        </DialogActions>
      </Dialog>

      <Dialog
        open={deleteDialogOpen}
        onClose={() => setDeleteDialogOpen(false)}
      >
        <DialogTitle>Confirm Delete</DialogTitle>
        <DialogContent>
          <Typography>
            Are you sure you want to delete this listing? This action cannot be undone.
          </Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleteDialogOpen(false)}>Cancel</Button>
          <Button onClick={handleDeleteConfirm} color="error">
            Delete
          </Button>
        </DialogActions>
      </Dialog>

      <Dialog
        open={createPropertyDialogOpen}
        onClose={() => setCreatePropertyDialogOpen(false)}
        maxWidth="md"
        fullWidth
      >
        <DialogTitle>Create New Property</DialogTitle>
        <DialogContent>
          <Box sx={{ mt: 2 }}>
            <TextField
              fullWidth
              label="Street"
              value={newProperty.address?.street}
              onChange={(e) => setNewProperty({
                ...newProperty,
                address: { ...newProperty.address!, street: e.target.value }
              })}
              sx={{ mb: 2 }}
            />
            <TextField
              fullWidth
              label="City"
              value={newProperty.address?.city}
              onChange={(e) => setNewProperty({
                ...newProperty,
                address: { ...newProperty.address!, city: e.target.value }
              })}
              sx={{ mb: 2 }}
            />
            <TextField
              fullWidth
              label="Province/State"
              value={newProperty.address?.province}
              onChange={(e) => setNewProperty({
                ...newProperty,
                address: { ...newProperty.address!, province: e.target.value }
              })}
              sx={{ mb: 2 }}
            />
            <TextField
              fullWidth
              label="Country"
              value={newProperty.address?.country}
              onChange={(e) => setNewProperty({
                ...newProperty,
                address: { ...newProperty.address!, country: e.target.value }
              })}
              sx={{ mb: 2 }}
            />
            <TextField
              fullWidth
              label="Postal Code"
              value={newProperty.address?.postalCode}
              onChange={(e) => setNewProperty({
                ...newProperty,
                address: { ...newProperty.address!, postalCode: e.target.value }
              })}
              sx={{ mb: 2 }}
            />
            <TextField
              fullWidth
              label="Latitude"
              type="number"
              value={newProperty.address?.latitude}
              onChange={(e) => setNewProperty({
                ...newProperty,
                address: { ...newProperty.address!, latitude: Number(e.target.value) }
              })}
              sx={{ mb: 2 }}
            />
            <TextField
              fullWidth
              label="Longitude"
              type="number"
              value={newProperty.address?.longitude}
              onChange={(e) => setNewProperty({
                ...newProperty,
                address: { ...newProperty.address!, longitude: Number(e.target.value) }
              })}
              sx={{ mb: 2 }}
            />
            <TextField
              select
              fullWidth
              label="Property Type"
              value={newProperty.propertyType}
              onChange={(e) => setNewProperty({
                ...newProperty,
                propertyType: e.target.value as PropertyType
              })}
              sx={{ mb: 2 }}
            >
              <MenuItem value="APARTMENT">Apartment</MenuItem>
              <MenuItem value="HOUSE">House</MenuItem>
              <MenuItem value="VILLA">Villa</MenuItem>
              <MenuItem value="OFFICE">Office</MenuItem>
              <MenuItem value="RETAIL">Retail</MenuItem>
              <MenuItem value="INDUSTRIAL">Industrial</MenuItem>
              <MenuItem value="LAND">Land</MenuItem>
            </TextField>
            <TextField
              fullWidth
              label="Year Built"
              type="number"
              value={newProperty.yearBuilt}
              onChange={(e) => setNewProperty({
                ...newProperty,
                yearBuilt: Number(e.target.value)
              })}
              sx={{ mb: 2 }}
            />
            <TextField
              fullWidth
              label="Lot Size (m²)"
              type="number"
              value={newProperty.lotSize}
              onChange={(e) => setNewProperty({
                ...newProperty,
                lotSize: Number(e.target.value)
              })}
              sx={{ mb: 2 }}
            />
            <TextField
              fullWidth
              label="Parking Spaces"
              type="number"
              value={newProperty.parkingSpaces}
              onChange={(e) => setNewProperty({
                ...newProperty,
                parkingSpaces: Number(e.target.value)
              })}
              sx={{ mb: 2 }}
            />
            <TextField
              fullWidth
              label="Garage Size (m²)"
              type="number"
              value={newProperty.garageSize}
              onChange={(e) => setNewProperty({
                ...newProperty,
                garageSize: Number(e.target.value)
              })}
              sx={{ mb: 2 }}
            />
            <TextField
              fullWidth
              label="HOA Fee"
              type="number"
              value={newProperty.hoaFee}
              onChange={(e) => setNewProperty({
                ...newProperty,
                hoaFee: Number(e.target.value)
              })}
              sx={{ mb: 2 }}
            />
            <Box sx={{ mb: 2 }}>
              <Typography variant="subtitle2" gutterBottom>
                Amenities
              </Typography>
              <Autocomplete
                multiple
                options={amenityOptions}
                value={selectedAmenities}
                onChange={(_, newValue) => {
                  setSelectedAmenities(newValue);
                }}
                renderInput={(params) => (
                  <TextField
                    {...params}
                    variant="outlined"
                    placeholder="Select amenities"
                  />
                )}
                renderTags={(value, getTagProps) =>
                  value.map((option, index) => (
                    <Chip
                      label={option}
                      {...getTagProps({ index })}
                      size="small"
                    />
                  ))
                }
              />
            </Box>
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setCreatePropertyDialogOpen(false)}>Cancel</Button>
          <Button onClick={handleCreateProperty} variant="contained">
            Create Property
          </Button>
        </DialogActions>
      </Dialog>

      <Dialog
        open={soldDialogOpen}
        onClose={() => setSoldDialogOpen(false)}
      >
        <DialogTitle>Enter buyer/tenant information</DialogTitle>
        <DialogContent>
          <TextField
            label={soldDialogListing?.listingType === 'RENT' ? 'Tenant name' : 'Buyer name'}
            value={buyerName}
            onChange={e => setBuyerName(e.target.value)}
            fullWidth
            sx={{ mb: 2 }}
          />
          <TextField
            label={soldDialogListing?.listingType === 'RENT' ? 'Tenant ID/Passport' : 'Buyer ID/Passport'}
            value={buyerIdentity}
            onChange={e => setBuyerIdentity(e.target.value)}
            fullWidth
            sx={{ mb: 2 }}
          />
          {soldDialogListing?.listingType === 'RENT' && (
            <>
              <TextField
                label="Rental start date (YYYY-MM-DD)"
                value={startDate}
                onChange={e => setStartDate(e.target.value)}
                fullWidth
                sx={{ mb: 2 }}
                placeholder="2024-07-01"
              />
              <TextField
                label="Rental end date (YYYY-MM-DD)"
                value={endDate}
                onChange={e => setEndDate(e.target.value)}
                fullWidth
                sx={{ mb: 2 }}
                placeholder="2024-07-31"
              />
            </>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setSoldDialogOpen(false)} disabled={isSubmittingSold}>Cancel</Button>
          <Button
            onClick={async () => {
              if (!soldDialogListing) return;
              setIsSubmittingSold(true);
              try {
                if (soldDialogListing.listingType === 'SALE') {
                  await saleTransactionService.create({
                    listingId: soldDialogListing.id,
                    price: soldDialogListing.price,
                    agentId: soldDialogListing.agentId,
                    buyerName,
                    buyerIdentity,
                    status: 'COMPLETED',
                  });
                } else if (soldDialogListing.listingType === 'RENT') {
                  await rentalTransactionService.create({
                    listingId: soldDialogListing.id,
                    price: soldDialogListing.price,
                    agentId: soldDialogListing.agentId,
                    tenantName: buyerName,
                    tenantIdentity: buyerIdentity,
                    status: 'COMPLETED',
                    startDate: toISOString(startDate),
                    endDate: toISOString(endDate),
                  });
                }
                await listingService.update(soldDialogListing.id, { isSold: true });
                setSnackbar({
                  open: true,
                  message: 'Update sold status successfully',
                  severity: 'success',
                });
                setSoldDialogOpen(false);
                fetchListings();
              } catch (err) {
                setSnackbar({
                  open: true,
                  message: 'Failed to update sold status',
                  severity: 'error',
                });
              } finally {
                setIsSubmittingSold(false);
              }
            }}
            variant="contained"
            disabled={!buyerName || !buyerIdentity || (soldDialogListing?.listingType === 'RENT' && (!startDate || !endDate)) || isSubmittingSold}
          >
            Confirm
          </Button>
        </DialogActions>
      </Dialog>

      <Snackbar
        open={snackbar.open}
        autoHideDuration={6000}
        onClose={() => setSnackbar({ ...snackbar, open: false })}
      >
        <Alert
          onClose={() => setSnackbar({ ...snackbar, open: false })}
          severity={snackbar.severity}
          sx={{ width: '100%' }}
        >
          {snackbar.message}
        </Alert>
      </Snackbar>
    </Box>
  );
};

export default Listings;