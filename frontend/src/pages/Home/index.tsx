import React, { useState } from 'react';
import {
  Box,
  Container,
  Typography,
  Paper,
  InputBase,
  IconButton,
  Button,
  Skeleton,
  List,
  ListItem,
  ListItemText,
  Popper,
  ClickAwayListener,
} from '@mui/material';
import { Search as SearchIcon } from '@mui/icons-material';
import { ListingCard } from '../../components/features/ListingCard';
import { ListingCardSkeleton } from '../../components/features/Skeleton/ListingCardSkeleton';
import { useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { Swiper, SwiperSlide } from 'swiper/react';
import { Navigation } from 'swiper/modules';
import 'swiper/css';
import 'swiper/css/navigation';
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos';

import useAsync from '../../hooks/useAsync';
import useDebounce from '../../hooks/useDebounce';
import useWindowSize from '../../hooks/useWindowSize';
import { listingService} from '../../services/listing.service';
import styles from '../../styles/Home/Home.module.css';
import { Listing } from '../../types/listing.types';
import ListingDetailDialog from '../../components/features/ListingDetailDialog';

// Import images
import buyImage from '../../assets/images/homepage-spot-agent-lg-1.webp';
import sellImage from '../../assets/images/homepage-spot-sell-lg-1.webp';
import rentImage from '../../assets/images/homepage-spot-rent-lg-1.webp';
import haNoiImage from '../../assets/images/hanoi-background.jpg';
import tpHCMImage from '../../assets/images/hcm-background.jpg';
import daNangImage from '../../assets/images/danang-background.jpg';
import haiPhongImage from '../../assets/images/haiphong-background.jpg';
import canThoImage from '../../assets/images/cantho-background.jpg';
import bgImage from '../../assets/images/commercial-high-rise-buildings.webp';

const actionCards = [
    {
      title: 'Buy a home',
      desc: `Find your place with an immersive photo experience and the most listings, including things you won't find anywhere else.`,
      img: buyImage,
      btn: 'Browse homes',
      onClick: (navigate: any) => navigate('/listings?type=SALE'),
    },
    {
      title: 'Sell a home',
      desc: 'No matter what path you take to sell your home, we can help you navigate a successful sale.',
      img: sellImage,
      btn: 'See your options',
      onClick: () => {},
    },
    {
      title: 'Rent a home',
      desc: `We're creating a seamless online experience – from shopping on the largest rental network, to applying, to paying rent.`,
      img: rentImage,
      btn: 'Find rentals',
      onClick: (navigate: any) => navigate('/listings?type=RENT'),
    },
  ];
  
const provinceListings = [
    {
      name: 'Hà Nội',
      image: haNoiImage,
      path: '/listings?province=Hà Nội',
    },
    {
      name: 'TP. Hồ Chí Minh',
      image: tpHCMImage,
      path: '/listings?province=Hồ Chí Minh',
    },
    {
      name: 'Đà Nẵng',
      image: daNangImage,
      path: '/listings?province=Đà Nẵng',
    },
    {
      name: 'Hải Phòng',
      image: haiPhongImage,
      path: '/listings?province=Hải Phòng',
    },
    {
      name: 'Cần Thơ',
      image: canThoImage,
      path: '/listings?province=Cần Thơ',
    },
  ];

const provinces = [
  'Hà Nội',
  'Hồ Chí Minh',
  'Đà Nẵng',
  'Hải Phòng',
  'Cần Thơ',
  'An Giang',
  'Bà Rịa - Vũng Tàu',
  'Bắc Giang',
  'Bắc Kạn',
  'Bạc Liêu',
  'Bắc Ninh',
  'Bến Tre',
  'Bình Định',
  'Bình Dương',
  'Bình Phước',
  'Bình Thuận',
  'Cà Mau',
  'Cao Bằng',
  'Đắk Lắk',
  'Đắk Nông',
  'Điện Biên',
  'Đồng Nai',
  'Đồng Tháp',
  'Gia Lai',
  'Hà Giang',
  'Hà Nam',
  'Hà Tĩnh',
  'Hải Dương',
  'Hậu Giang',
  'Hòa Bình',
  'Hưng Yên',
  'Khánh Hòa',
  'Kiên Giang',
  'Kon Tum',
  'Lai Châu',
  'Lâm Đồng',
  'Lạng Sơn',
  'Lào Cai',
  'Long An',
  'Nam Định',
  'Nghệ An',
  'Ninh Bình',
  'Ninh Thuận',
  'Phú Thọ',
  'Phú Yên',
  'Quảng Bình',
  'Quảng Nam',
  'Quảng Ngãi',
  'Quảng Ninh',
  'Quảng Trị',
  'Sóc Trăng',
  'Sơn La',
  'Tây Ninh',
  'Thái Bình',
  'Thái Nguyên',
  'Thanh Hóa',
  'Thừa Thiên Huế',
  'Tiền Giang',
  'Trà Vinh',
  'Tuyên Quang',
  'Vĩnh Long',
  'Vĩnh Phúc',
  'Yên Bái',
];

export const Home: React.FC = () => {
  const navigate = useNavigate();
  const { width } = useWindowSize();
  const [searchTerm, setSearchTerm] = React.useState('');
  const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
  const [suggestions, setSuggestions] = React.useState<string[]>([]);
  
  const [favoriteIds, setFavoriteIds] = React.useState<string[]>([]);
  const debouncedSearchTerm = useDebounce(searchTerm, 500);

  // Sử dụng useAsync để quản lý việc fetch data
  const { execute: fetchListings, data: listings, loading, error } = useAsync<Listing[]>(listingService.getAll);

  const [selectedListing, setSelectedListing] = useState<Listing | null>(null);
  const [dialogOpen, setDialogOpen] = useState(false);

  React.useEffect(() => {
    fetchListings();
  }, [fetchListings]);

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    if (searchTerm.trim()) {
      navigate(`/search?q=${encodeURIComponent(searchTerm)}`);
    }
  };

  const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setSearchTerm(value);
    
    if (value.trim()) {
      const filtered = provinces.filter(province => 
        province.toLowerCase().includes(value.toLowerCase())
      );
      setSuggestions(filtered);
      setAnchorEl(e.currentTarget);
    } else {
      setSuggestions([]);
      setAnchorEl(null);
    }
  };

  const handleSuggestionClick = (province: string) => {
    setSearchTerm(province);
    setSuggestions([]);
    setAnchorEl(null);
    navigate(`/listings?province=${encodeURIComponent(province)}`);
  };

  const handleClickAway = () => {
    setSuggestions([]);
    setAnchorEl(null);
  };

  const handleFavorite = (id: string) => {
    setFavoriteIds((prev) =>
      prev.includes(id) ? prev.filter((fid) => fid !== id) : [...prev, id]
    );
  };

  const handleOpenDetail = (listing: Listing) => {
    setSelectedListing(listing);
    setDialogOpen(true);
  };

  const handleCloseDetail = () => {
    setDialogOpen(false);
    setSelectedListing(null);
  };

  return (
    <Box>
      {/* Hero Section */}
      <Box
        className={styles.heroSection}
        style={{
          backgroundImage:
            `url(${bgImage})`,
        }}
      >
        {/* Overlay */}
        <Box className={styles.heroOverlay} />
        <Container maxWidth="md" className={styles.heroContainer}>
          <Typography
            className={styles.heroTitle}
            component="h1"
            gutterBottom
          >
            Find your dream home
          </Typography>
          <Typography className={styles.heroSubtitle} gutterBottom>
            Discover thousands of quality real estate listings
          </Typography>

          <ClickAwayListener onClickAway={handleClickAway}>
            <Box sx={{ position: 'relative', width: '100%' }}>
              <Paper
                component="form"
                onSubmit={handleSearch}
                className={styles.searchForm}
              >
                <IconButton type="submit" sx={{ p: 1, color: '#006AFF' }} aria-label="search">
                  <SearchIcon />
                </IconButton>
                <InputBase
                  sx={{ ml: 1, flex: 1, fontSize: 18, pl: 1 }}
                  placeholder="Enter address, city, province..."
                  inputProps={{ 'aria-label': 'search' }}
                  value={searchTerm}
                  onChange={handleSearchChange}
                />
              </Paper>
              <Popper
                open={Boolean(anchorEl) && suggestions.length > 0}
                anchorEl={anchorEl}
                placement="bottom-start"
                style={{ width: anchorEl?.offsetWidth, zIndex: 1300 }}
              >
                <Paper elevation={3} sx={{ mt: 1, maxHeight: 300, overflow: 'auto' }}>
                  <List>
                    {suggestions.map((province, index) => (
                      <ListItem
                        key={index}
                        button
                        onClick={() => handleSuggestionClick(province)}
                        sx={{
                          '&:hover': {
                            backgroundColor: 'rgba(0, 106, 255, 0.08)',
                          },
                        }}
                      >
                        <ListItemText primary={province} />
                      </ListItem>
                    ))}
                  </List>
                </Paper>
              </Popper>
            </Box>
          </ClickAwayListener>
        </Container>
      </Box>

      {/* Province Slider Section */}
      <Box className={styles.provinceSliderSection}>
        <Typography className={styles.provinceSliderTitle}>
          Explore real estate by province
        </Typography>
        <Typography className={styles.provinceSliderDesc}>
          Easily search for houses, apartments, and villas in major cities nationwide
        </Typography>
        <Box className={styles.provinceSliderWrapper}>
          <Swiper
            modules={[Navigation]}
            spaceBetween={24}
            slidesPerView={1.2}
            navigation={false}
            breakpoints={{
              600: { slidesPerView: 2.2 },
              900: { slidesPerView: 3.2 },
              1200: { slidesPerView: 4.2 },
            }}
            style={{ padding: '8px 0 32px 0' }}
          >
            {provinceListings.map((province) => (
              <SwiperSlide key={province.name}>
                <Box className={styles.provinceSlideCard} sx={{ position: 'relative', minHeight: 220 }}>
                  <img
                    src={province.image}
                    alt={province.name}
                    loading="eager"
                    style={{ width: '100%', height: 180, objectFit: 'cover' }}
                  />
                  <Typography
                    sx={{
                      position: 'absolute',
                      top: 16,
                      left: 16,
                      color: 'white',
                      fontWeight: 700,
                      fontSize: '1.3rem',
                      textShadow: '0 2px 8px rgba(0,0,0,0.4)',
                    }}
                  >
                    {province.name}
                  </Typography>
                  <Button
                    variant="contained"
                    sx={{
                      position: 'absolute',
                      bottom: 16,
                      left: 16,
                      background: 'rgba(255,255,255,0.92)',
                      color: '#006AFF',
                      fontWeight: 700,
                      borderRadius: 8,
                      textTransform: 'none',
                    }}
                    onClick={() => navigate(province.path)}
                  >
                    View Homes
                  </Button>
                </Box>
              </SwiperSlide>
            ))}
          </Swiper>
        </Box>
      </Box>

      {/* Home Actions Section */}
      <Container maxWidth="lg" sx={{ my: 6 }}>
        <div className={styles.actionsGrid}>
          {actionCards.map((card, idx) => (
            <motion.div
              key={idx}
              initial={{ opacity: 0, y: 40 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true, amount: 0.3 }}
              transition={{ duration: 0.6, delay: idx * 0.15 }}
              style={{ height: '100%' }}
            >
              <Paper
                className={styles.actionCard}
                onClick={() => card.onClick(navigate)}
                elevation={2}
              >
                <img src={card.img} alt={card.title} className={styles.actionImg} />
                <Typography className={styles.actionTitle} component="div">
                  {card.title}
                </Typography>
                <Typography className={styles.actionDesc} component="div">
                  {card.desc}
                </Typography>
                <div style={{ marginTop: 'auto', width: '100%' }}>
                  <Button
                    variant="outlined"
                    size="large"
                    className={styles.actionBtn}
                    onClick={e => {
                      e.stopPropagation();
                      card.onClick(navigate);
                    }}
                  >
                    {card.btn}
                  </Button>
                </div>
              </Paper>
            </motion.div>
          ))}
        </div>
      </Container>

      {/* Popular Listed Homes */}
      <Container maxWidth="lg" className={styles.featuredContainer}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 4 }}>
          <Typography className={styles.featuredTitle} gutterBottom>
            Popular Listed Homes
          </Typography>
          <Button 
            variant="outlined" 
            endIcon={<ArrowForwardIosIcon />}
            onClick={() => navigate('/listings')}
            sx={{ 
              borderRadius: 2,
              textTransform: 'none',
              fontWeight: 600,
              px: 3
            }}
          >
            View all listings
          </Button>
        </Box>
        <Box className={styles.listingSliderWrapper}>
          <Swiper
            modules={[Navigation]}
            spaceBetween={24}
            slidesPerView={1.2}
            navigation={false}
            breakpoints={{
              600: { slidesPerView: 2.2 },
              900: { slidesPerView: 3.2 },
              1200: { slidesPerView: 4.2 },
            }}
            style={{ padding: '8px 0 32px 0' }}
          >
            {loading
              ? Array.from({ length: 4 }).map((_, idx) => (
                  <SwiperSlide key={idx}>
                    <ListingCardSkeleton />
                  </SwiperSlide>
                ))
              : listings && listings.slice(0, 10).map((listing) => (
                  <SwiperSlide key={listing.id}>
                    <ListingCard
                      listing={listing}
                      onFavorite={handleFavorite}
                      isFavorite={favoriteIds.includes(listing.id)}
                      onClick={() => handleOpenDetail(listing)}
                    />
                  </SwiperSlide>
                ))}
          </Swiper>
        </Box>
      </Container>

      <ListingDetailDialog open={dialogOpen} onClose={handleCloseDetail} listing={selectedListing} />
    </Box>
  );
}; 