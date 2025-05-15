import React, { useState } from 'react';
import { 
  Box, 
  Container, 
  Typography, 
  Button, 
  Grid, 
  Card, 
  CardContent, 
  CardMedia, 
  Divider,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  FormControl,
  InputAdornment,
  TextField,
  Paper,
  Accordion,
  AccordionSummary,
  AccordionDetails,
  Link
} from '@mui/material';
import { styled } from '@mui/material/styles';
import { 
  Home, 
  Payments, 
  HelpOutline,
  MonetizationOn, 
  Assignment,
  ArrowForward, 
  Check,
  SearchOutlined,
  ExpandMore,
  PersonOutline,
  AttachMoney,
  ArrowRightAlt,
  KeyboardArrowRight
} from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';

// Import CSS Module
import styles from '../../styles/Sell/Sell.module.css';
import sellImage from '../../assets/images/Sell_My_Home.webp';
import offerImage from '../../assets/images/offer-image.png';


// Selling options data
const sellingOptions = [  {
    title: 'Sell with an agent',
    description: 'Get the most value and least stress by selling with a local real estate agent.',
    icon: <Home fontSize="large" color="primary" />,
    benefits: [
      'Local market expertise',
      'Professional home marketing',
      'Pricing strategy assistance',
      'Negotiation support'
    ],
    primaryAction: 'Find an agent',
    path: '/find-agent'
  },
  {
    title: 'Sell directly to PropTech',
    description: 'Get a competitive cash offer from PropTech with no showings and flexible closing.',
    icon: <Payments fontSize="large" color="primary" />,
    benefits: [
      'Skip repairs and showings',
      'Choose your closing date',
      'Cash offer within 24 hours',
      'Move on your timeline'
    ],
    primaryAction: 'Request an offer',
    path: '/request-offer'
  },
  {
    title: 'Sell For Sale By Owner',
    description: 'List your property directly on PropTech and take control of the selling process.',
    icon: <Assignment fontSize="large" color="primary" />,
    benefits: [
      'No agent commission',
      'Full control of the process',
      'Direct buyer communication',
      'Support tools and resources'
    ],
    primaryAction: 'Post your home',
    path: '/post-property'
  }
];

// Steps to sell house
const sellSteps = [
  {
    number: '1',
    title: 'Prepare your home',
    description: 'Make necessary repairs, declutter, and enhance curb appeal to attract buyers.'
  },
  {
    number: '2',
    title: 'Set the right price',
    description: 'Research comparable homes and set a competitive price based on market conditions.'
  },
  {
    number: '3',
    title: 'Market your property',
    description: 'Create appealing listings with professional photos and promote across platforms.'
  },
  {
    number: '4',
    title: 'Negotiate offers',
    description: 'Review and respond to buyer offers, negotiating terms that work for both parties.'
  },
  {
    number: '5',
    title: 'Close the deal',
    description: 'Complete paperwork, inspections, and legal requirements for a smooth transaction.'
  }
];

export const SellPage: React.FC = () => {
  const navigate = useNavigate();
  const [address, setAddress] = useState('');

  return (
    <Box>      
      {/* Hero Section */}
      <Box
        className={styles.heroSection}
        sx={{
          backgroundImage: 'linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.7)), url(https://images.unsplash.com/photo-1560518883-ce09059eeffa?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2000&q=80)'
        }}
      >
        <Container maxWidth="md" className={styles.heroContent}>
          <Typography variant="h2" className={styles.heroTitle} gutterBottom>
            Sell your home with confidence
          </Typography>
          <Box className={styles.searchContainer}>
            <TextField
              fullWidth
              variant="outlined"
              placeholder="Enter your home address"
              value={address}
              onChange={(e) => setAddress(e.target.value)}
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                    <SearchOutlined />
                  </InputAdornment>
                ),
              }}
              sx={{ 
                bgcolor: '#fff', 
                borderRadius: '4px',
                '& .MuiOutlinedInput-root': {
                  borderRadius: '4px',
                }
              }}
            />
            <Button 
              variant="contained" 
              color="primary"
              className={styles.searchButton}
              onClick={() => navigate('/home-valuation')}
            >
              Check your home estimate
            </Button>
          </Box>
          <Typography variant="subtitle2" sx={{ color: '#fff', mt: 1 }}>
            Get connected to a local agent. No obligation.
          </Typography>
        </Container>
      </Box>      {/* Selling Options Section */}
      <Box sx={{ py: 4, bgcolor: 'background.default' }}>
        <Container maxWidth="lg">
          <Box className={styles.optionsHeader}>
            <Typography variant="h4" fontWeight={600} gutterBottom>
              Sell with a Proptech partner agent or get a cash offer
            </Typography>
          </Box>
          <Grid container spacing={3}>
            <Grid item xs={12} md={6}>
              <Card className={styles.compareCard} elevation={2}>
                <Box display="flex" alignItems="center" mb={2}>
                  <Box className={styles.mobileIconContainer}>
                    <img
                      src={sellImage}
                      alt="Proptech Agent"
                      className={styles.mobileIcon}
                    />
                  </Box>
                  <Typography variant="h6" fontWeight={600}>
                    You can sell directly to OpenDoor
                  </Typography>
                </Box>
                <Typography variant="body2" paragraph>
                  Selling to OpenDoor means getting a competitive cash offer with a flexible closing date and avoiding the hassle of showings and open houses.
                </Typography>
                <Button 
                  variant="contained" 
                  color="primary" 
                  fullWidth
                  className={styles.optionButton}
                  onClick={() => navigate('/request-cash-offer')}
                >
                  Request a cash offer
                </Button>
              </Card>
            </Grid>
            <Grid item xs={12} md={6}>
              <Card className={styles.compareCard} elevation={2}>
                <Box display="flex" alignItems="center" mb={2}>
                  <Box className={styles.mobileIconContainer}>
                    <img
                      src={offerImage}
                      alt="Cash Offer"
                      className={styles.mobileIcon}
                    />
                  </Box>
                  <Typography variant="h6" fontWeight={600}>
                    You can sell for more money with a Proptech partner agent
                  </Typography>
                </Box>
                <Typography variant="body2" paragraph>
                  Selling with a Proptech partner agent means personalized service, professional marketing, and negotiation expertise to help you get the best price.
                </Typography>
                <Button 
                  variant="outlined" 
                  color="primary" 
                  fullWidth
                  className={styles.optionButton}
                  onClick={() => navigate('/find-agent')}
                >
                  Find an agent
                </Button>
              </Card>
            </Grid>
          </Grid>
        </Container>
      </Box>      {/* Sell Traditionally Section */}
      <Divider sx={{ my: 4 }} />
      
      <Box sx={{ py: 4, bgcolor: 'background.paper' }}>
        <Container maxWidth="lg">
          <Box mb={5}>
            <Typography variant="h4" fontWeight={600} gutterBottom>
              Sell traditionally with an agent
            </Typography>
            
            <Grid container spacing={4} mt={2}>
              <Grid item xs={12} md={6}>
                <Box className={styles.traditionallySection}>
                  <Typography variant="h6" fontWeight={600} gutterBottom>
                    Why sell traditionally?
                  </Typography>
                  <Box sx={{ mt: 2 }}>
                    <Box display="flex" gap={2} mb={2}>
                      <Check color="primary" />
                      <Typography variant="body2">
                        <strong>Get more money for your home.</strong> On average, homes sold via agents sell for more than those sold by owner.
                      </Typography>
                    </Box>
                    <Box display="flex" gap={2} mb={2}>
                      <Check color="primary" />
                      <Typography variant="body2">
                        <strong>Marketing expertise.</strong> Agents handle home photography, listing descriptions, and broad marketing exposure.
                      </Typography>
                    </Box>
                    <Box display="flex" gap={2}>
                      <Check color="primary" />
                      <Typography variant="body2">
                        <strong>Negotiation and paperwork</strong> support when selling, handling all the complex details for you.
                      </Typography>
                    </Box>
                  </Box>
                  
                  <Button 
                    variant="outlined" 
                    color="primary"
                    size="small"
                    sx={{ mt: 3 }}
                    endIcon={<KeyboardArrowRight />}
                    onClick={() => navigate('/why-agent')}
                  >
                    Learn more about agents
                  </Button>
                </Box>
              </Grid>
              
              <Grid item xs={12} md={6}>
                <Box className={styles.traditionallySection}>
                  <Typography variant="h6" fontWeight={600} gutterBottom>
                    How to sell traditionally
                  </Typography>
                  <Box sx={{ mt: 2 }}>
                    <Box display="flex" gap={2} mb={2}>
                      <Typography variant="body2" sx={{ fontWeight: 600, width: 25 }}>1.</Typography>
                      <Typography variant="body2">
                        <strong>Find the right agent</strong> who knows your local market and has a history of successful sales.
                      </Typography>
                    </Box>
                    <Box display="flex" gap={2} mb={2}>
                      <Typography variant="body2" sx={{ fontWeight: 600, width: 25 }}>2.</Typography>
                      <Typography variant="body2">
                        <strong>Determine your home's value</strong> with a free home estimate to set the right price.
                      </Typography>
                    </Box>
                    <Box display="flex" gap={2}>
                      <Typography variant="body2" sx={{ fontWeight: 600, width: 25 }}>3.</Typography>
                      <Typography variant="body2">
                        <strong>List your home with your agent</strong>, then field offers and negotiate the best deal through closing.
                      </Typography>
                    </Box>
                  </Box>
                  
                  <Button 
                    variant="contained" 
                    color="primary"
                    size="small"
                    sx={{ mt: 3 }}
                    endIcon={<KeyboardArrowRight />}
                    onClick={() => navigate('/home-selling-steps')}
                  >
                    See all the steps through closing
                  </Button>
                </Box>
              </Grid>
            </Grid>
          </Box>
        </Container>
      </Box>      {/* FSBO Section */}
      <Divider sx={{ my: 4 }} />
      
      <Box sx={{ py: 4, bgcolor: 'background.default' }}>
        <Container maxWidth="lg">
          <Box mb={5}>
            <Typography variant="h4" fontWeight={600} gutterBottom>
              Sell your home yourself
            </Typography>
            
            <Grid container spacing={4} mt={2}>
              <Grid item xs={12} md={6}>
                <Box className={styles.traditionallySection}>
                  <Typography variant="h6" fontWeight={600} gutterBottom>
                    Why sell FSBO?
                  </Typography>
                  <Box sx={{ mt: 2 }}>
                    <Box display="flex" gap={2} mb={2}>
                      <Check color="primary" />
                      <Typography variant="body2">
                        <strong>Save money on commission.</strong> Keep more of your home's sale price by avoiding agent fees.
                      </Typography>
                    </Box>
                    <Box display="flex" gap={2} mb={2}>
                      <Check color="primary" />
                      <Typography variant="body2">
                        <strong>Maintain complete control</strong> over the entire selling process, from pricing to negotiations.
                      </Typography>
                    </Box>
                    <Box display="flex" gap={2}>
                      <Check color="primary" />
                      <Typography variant="body2">
                        <strong>Flexibility in scheduling</strong> showings and open houses on your own timeline.
                      </Typography>
                    </Box>
                  </Box>
                  
                  <Button 
                    variant="outlined" 
                    color="primary"
                    size="small"
                    sx={{ mt: 3 }}
                    endIcon={<KeyboardArrowRight />}
                    onClick={() => navigate('/why-fsbo')}
                  >
                    Learn more about FSBO
                  </Button>
                </Box>
              </Grid>
              
              <Grid item xs={12} md={6}>
                <Box className={styles.traditionallySection}>
                  <Typography variant="h6" fontWeight={600} gutterBottom>
                    How to sell FSBO
                  </Typography>
                  <Box sx={{ mt: 2 }}>
                    <Box display="flex" gap={2} mb={2}>
                      <Typography variant="body2" sx={{ fontWeight: 600, width: 25 }}>1.</Typography>
                      <Typography variant="body2">
                        <strong>Prepare your home for sale</strong> with repairs, cleaning, and staging to attract buyers.
                      </Typography>
                    </Box>
                    <Box display="flex" gap={2} mb={2}>
                      <Typography variant="body2" sx={{ fontWeight: 600, width: 25 }}>2.</Typography>
                      <Typography variant="body2">
                        <strong>Determine your listing price</strong> based on comparable properties in your area.
                      </Typography>
                    </Box>
                    <Box display="flex" gap={2}>
                      <Typography variant="body2" sx={{ fontWeight: 600, width: 25 }}>3.</Typography>
                      <Typography variant="body2">
                        <strong>Create and manage your listing</strong>, arrange showings, field offers, and handle all paperwork.
                      </Typography>
                    </Box>
                  </Box>
                  
                  <Button 
                    variant="contained" 
                    color="primary"
                    size="small"
                    sx={{ mt: 3 }}
                    endIcon={<KeyboardArrowRight />}
                    onClick={() => navigate('/fsbo-guide')}
                  >
                    View FSBO guide
                  </Button>
                </Box>
              </Grid>
            </Grid>
          </Box>
        </Container>
      </Box>      {/* Resources Section */}
      <Box sx={{ py: 6, bgcolor: '#f0f6ff' }}>
        <Container maxWidth="lg">
          <Typography variant="h4" fontWeight={600} gutterBottom align="center">
            Go-to resources for a successful sale
          </Typography>
          
          <Box 
            sx={{ 
              backgroundImage: 'url(https://images.unsplash.com/photo-1560518883-ce09059eeffa?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2000&q=80)',
              height: 300,
              backgroundSize: 'cover',
              backgroundPosition: 'center',
              borderRadius: 2,
              position: 'relative',
              mt: 4,
              mb: 8,
              display: 'flex',
              flexDirection: 'column',
              justifyContent: 'center',
              alignItems: 'center'
            }}
          >
            <Box sx={{ 
              position: 'absolute', 
              top: 0, 
              left: 0, 
              width: '100%', 
              height: '100%', 
              bgcolor: 'rgba(0,0,0,0.5)',
              borderRadius: 2
            }} />
            
            <Box sx={{ position: 'relative', textAlign: 'center', color: 'white', zIndex: 1 }}>
              <Typography variant="h5" fontWeight={600} gutterBottom>
                Explore your home's value
              </Typography>
              
              <Button
                variant="contained"
                color="primary"
                sx={{ mt: 2 }}
                onClick={() => navigate('/home-estimate')}
              >
                Get started
              </Button>
            </Box>
          </Box>
          
          <Typography variant="h5" fontWeight={600} gutterBottom align="center" sx={{ mb: 4 }}>
            Get acquainted with the process
          </Typography>
          
          <Typography variant="subtitle1" align="center" mb={4}>
            We help make selling a home as simple as possible, from expert tips to a smooth closing.
          </Typography>
          
          <Grid container spacing={3}>
            <Grid item xs={12} md={6}>
              <Card sx={{ mb: 3 }}>
                <CardContent>
                  <Typography variant="h6" fontWeight={600} gutterBottom>
                    Need to sell a house for a life's needs
                  </Typography>
                  <Typography variant="body2" color="text.secondary" paragraph>
                    When there's a need to sell your home due to life changes—like marriage, divorce, or growing family—we have expert-backed guidance to help with the transition.
                  </Typography>
                  <Button 
                    variant="text" 
                    color="primary"
                    endIcon={<KeyboardArrowRight />}
                    onClick={() => navigate('/life-changes')}
                  >
                    Read more
                  </Button>
                </CardContent>
              </Card>
            </Grid>
            
            <Grid item xs={12} md={6}>
              <Card sx={{ mb: 3 }}>
                <CardContent>
                  <Typography variant="h6" fontWeight={600} gutterBottom>
                    No time to show your home? Try instant offers
                  </Typography>
                  <Typography variant="body2" color="text.secondary" paragraph>
                    For sellers who want convenience over maximum price, our cash offer partners can close on your timeline without the hassle of showings or repairs.
                  </Typography>
                  <Button 
                    variant="text" 
                    color="primary"
                    endIcon={<KeyboardArrowRight />}
                    onClick={() => navigate('/instant-offers')}
                  >
                    Read more
                  </Button>
                </CardContent>
              </Card>
            </Grid>
            
            <Grid item xs={12} md={6}>
              <Card sx={{ mb: 3 }}>
                <CardContent>
                  <Typography variant="h6" fontWeight={600} gutterBottom>
                    How to list for success
                  </Typography>
                  <Typography variant="body2" color="text.secondary" paragraph>
                    Learn the key strategies for creating a listing that attracts the right buyers, from pricing your home correctly to staging that highlights its best features.
                  </Typography>
                  <Button 
                    variant="text" 
                    color="primary"
                    endIcon={<KeyboardArrowRight />}
                    onClick={() => navigate('/listing-tips')}
                  >
                    Read more
                  </Button>
                </CardContent>
              </Card>
            </Grid>
            
            <Grid item xs={12} md={6}>
              <Card sx={{ mb: 3 }}>
                <CardContent>
                  <Typography variant="h6" fontWeight={600} gutterBottom>
                    Steps to selling a house
                  </Typography>
                  <Typography variant="body2" color="text.secondary" paragraph>
                    Follow our comprehensive guide to selling your home, from preparing your property for showings to navigating negotiations and closing successfully.
                  </Typography>
                  <Button 
                    variant="text" 
                    color="primary"
                    endIcon={<KeyboardArrowRight />}
                    onClick={() => navigate('/selling-steps')}
                  >
                    Read more
                  </Button>
                </CardContent>
              </Card>
            </Grid>
          </Grid>
          
          <Box sx={{ textAlign: 'center', mt: 4 }}>
            <Button
              variant="outlined"
              color="primary"
              onClick={() => navigate('/sell-guide')}
            >
              View all guides
            </Button>
          </Box>
        </Container>
      </Box>

      {/* FAQ Section */}
      <Box sx={{ py: 6, bgcolor: 'background.paper' }}>
        <Container maxWidth="md">
          <Typography variant="h4" fontWeight={600} gutterBottom align="center" mb={4}>
            Frequently Asked Questions
          </Typography>
          
          <Accordion>
            <AccordionSummary expandIcon={<ExpandMore />}>
              <Typography fontWeight={500}>What is the Price Comparison Policy?</Typography>
            </AccordionSummary>
            <AccordionDetails>
              <Typography variant="body2">
                Our Price Comparison Policy ensures transparency in home valuations. We provide estimates based on the features of your home, recent comparable sales in your area, and current market conditions.
              </Typography>
            </AccordionDetails>
          </Accordion>
          
          <Accordion>
            <AccordionSummary expandIcon={<ExpandMore />}>
              <Typography fontWeight={500}>What is price matching?</Typography>
            </AccordionSummary>
            <AccordionDetails>
              <Typography variant="body2">
                A price match is a promise to sell or list a property without being undercut by competitors. When using our partner agents, if you receive a better commission rate from a verified competitor, we'll match it.
              </Typography>
            </AccordionDetails>
          </Accordion>
          
          <Accordion>
            <AccordionSummary expandIcon={<ExpandMore />}>
              <Typography fontWeight={500}>What is an iBuyer?</Typography>
            </AccordionSummary>
            <AccordionDetails>
              <Typography variant="body2">
                An iBuyer is a company that uses technology to make quick cash offers on homes. iBuyers provide a convenient selling option for homeowners who want to avoid the traditional selling process.
              </Typography>
            </AccordionDetails>
          </Accordion>
          
          <Accordion>
            <AccordionSummary expandIcon={<ExpandMore />}>
              <Typography fontWeight={500}>Is the fair purchase of homes at public prices valid as of DATE, or, as a policy?</Typography>
            </AccordionSummary>
            <AccordionDetails>
              <Typography variant="body2">
                Our commitment to fair pricing is an ongoing policy rather than tied to a specific date. We regularly update our valuation models to reflect current market conditions and ensure fair offers.
              </Typography>
            </AccordionDetails>
          </Accordion>
          
          <Accordion>
            <AccordionSummary expandIcon={<ExpandMore />}>
              <Typography fontWeight={500}>When selling my home, where should I move?</Typography>
            </AccordionSummary>
            <AccordionDetails>
              <Typography variant="body2">
                This depends on your personal circumstances. Consider factors like job location, lifestyle preferences, school districts, and budget. Our relocation specialists can help you find the right area based on your needs.
              </Typography>
            </AccordionDetails>
          </Accordion>
          
          <Accordion>
            <AccordionSummary expandIcon={<ExpandMore />}>
              <Typography fontWeight={500}>How long does it take to sell a house?</Typography>
            </AccordionSummary>
            <AccordionDetails>
              <Typography variant="body2">
                The time to sell varies by market conditions, location, price point, and property condition. On average, homes currently take 30-45 days to sell, from listing to accepting an offer, with another 30-45 days to close.
              </Typography>
            </AccordionDetails>
          </Accordion>
        </Container>
      </Box>
    </Box>
  );
};
