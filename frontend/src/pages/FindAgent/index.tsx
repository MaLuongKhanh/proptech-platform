import React, { useState } from 'react';
import {
  Box,
  Container,
  Typography,
  TextField,
  InputAdornment,
  Grid,
  Button,
  Tabs,
  Tab,
  Card,
  CardContent,
  CardMedia,
  Avatar,
  Divider,
  Accordion,
  AccordionSummary,
  AccordionDetails,
  Link,
} from '@mui/material';
import {
  Search as SearchIcon,
  ExpandMore,
  KeyboardArrowRight,
  StarOutlined,
  Check,
} from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
// Import CSS Module - we'll create this next
import styles from '../../styles/FindAgent/FindAgent.module.css';
import FirstAvatar from '../../assets/agents/Lan.jpg';
import SecondAvatar from '../../assets/agents/Marin.jpg';
import ThirdAvatar from '../../assets/agents/Piche.jpg';
import FourthAvatar from '../../assets/agents/Rick.jpg';
import FifthAvatar from '../../assets/agents/Tim-Yew.jpg';

// Sample agent data
const agents = [
  {
    id: 1,
    name: 'Matt Laricy',
    company: 'Americorp Real Estate',
    image: FirstAvatar,
    rating: 4.9,
    reviewCount: 140,
    sales: 435,
    yearsExperience: 12,
    averagePrice: '$1.1M - $3.8M',
    recentSales: 119,
    location: 'Chicago',
    featured: true,
    team: true
  },
  {
    id: 2,
    name: 'Patrick Shino',
    company: 'The SK Group',
    subCompany: 'FULTON GRACE',
    image: SecondAvatar,
    rating: 5.0,
    reviewCount: 93,
    sales: 320,
    yearsExperience: 8,
    averagePrice: '$500K - $2.3M',
    recentSales: 83,
    location: 'Chicago',
    featured: true,
    team: true
  },
  {
    id: 3,
    name: 'Sam Shaffer',
    company: 'Chicago Properties',
    image: ThirdAvatar,
    rating: 4.8,
    reviewCount: 127,
    sales: 267,
    yearsExperience: 10,
    averagePrice: '$450K - $1.9M',
    recentSales: 76,
    location: 'Chicago',
    featured: false,
    team: true
  },
  {
    id: 4,
    name: 'Vesta Preferred Realty',
    company: 'Vesta Properties, LLC',
    image: FourthAvatar,
    rating: 4.9,
    reviewCount: 71,
    sales: 529,
    yearsExperience: 7,
    averagePrice: '$740K - $2.8M',
    recentSales: 218,
    location: 'Chicago',
    featured: false,
    team: true
  },
  {
    id: 5,
    name: 'Bari Levine',
    company: 'Berkshire Hathaway',
    image: FifthAvatar,
    rating: 4.9,
    reviewCount: 59,
    sales: 162,
    yearsExperience: 6,
    averagePrice: '$190K - $1.8M',
    recentSales: 106,
    location: 'Chicago',
    featured: false,
    team: false
  },
];

// FAQ questions
const faqs = [
  {
    question: 'How to find a good real estate agent near me?',
    answer: 'To find a good real estate agent near you, start by checking our curated list of local experienced agents, read their reviews, look at their sales history, and interview a few before deciding. Our platform makes it easy to filter agents by area of expertise, experience level, and client ratings.'
  },
  {
    question: 'How to pick a real estate agent?',
    answer: 'When choosing a real estate agent, look for someone with local market knowledge, strong negotiation skills, and good communication. Consider their experience, check reviews from past clients, and ask about their strategy for your specific needs. It is also important to find someone you trust and feel comfortable working with.'
  },
  {
    question: 'How to contact a real estate agent?',
    answer: 'You can contact a real estate agent directly through our platform by clicking on their profile and using the "Contact Agent" button. Most agents can be reached by phone, email, or through our messaging system. Our agents typically respond within 24 hours.'
  },
  {
    question: 'How do I leave a review for a real estate agent?',
    answer: 'After working with an agent you found through our platform, you can leave a review by navigating to their profile page and clicking on "Write a Review." You will need to have an account and verify that you worked with the agent. Your honest feedback helps other users make informed decisions.'
  },
  {
    question: 'What is the difference between an agent and a broker?',
    answer: 'A real estate agent is licensed to facilitate real estate transactions and works under a broker. A broker has additional education, can work independently, and may manage a team of agents. Brokers typically have more experience and responsibilities in the real estate transaction process.'
  },
];

export const FindAgentPage: React.FC = () => {
  const [searchType, setSearchType] = useState<string>('location');
  const [searchQuery, setSearchQuery] = useState<string>('');
  const navigate = useNavigate();

  const handleSearchTypeChange = (event: React.SyntheticEvent, newValue: string) => {
    setSearchType(newValue);
  };

  return (
    <Box>
      {/* Hero Section */}
      <Box className={styles.heroSection}>
        <Container maxWidth="lg" sx={{ textAlign: 'center' }}>
          <Typography variant="h2" fontWeight={600} gutterBottom sx={{ textAlign: 'center' }}>
            A great agent makes all the difference
          </Typography>
          
          {/* Search Tabs */}
          <Box className={styles.searchContainer}>
            <Tabs
              value={searchType}
              onChange={handleSearchTypeChange}
              indicatorColor="primary"
              textColor="primary"
              sx={{ bgcolor: '#fff', borderTopLeftRadius: 2, borderTopRightRadius: 2 }}
            >
              <Tab 
                label="Location" 
                value="location" 
                sx={{ fontSize: '16px', fontWeight: 500 }}
              />
              <Tab 
                label="Name" 
                value="name" 
                sx={{ fontSize: '16px', fontWeight: 500 }}
              />
            </Tabs>
            
            {/* Search Bar */}
            <Box className={styles.searchBar}>
              <TextField
                fullWidth
                variant="outlined"
                placeholder={searchType === 'location' ? 'City, neighborhood, or ZIP code' : 'Agent name'}
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                InputProps={{
                  endAdornment: (
                    <InputAdornment position="end">
                      <Button
                        variant="contained"
                        color="primary"
                        sx={{ height: 40 }}
                      >
                        <SearchIcon />
                      </Button>
                    </InputAdornment>
                  ),
                  sx: { bgcolor: 'white', borderRadius: 0 }
                }}
              />
            </Box>
          </Box>
        </Container>
      </Box>
      
      {/* Main Content */}
      <Container maxWidth="lg">
        {/* Location Header */}
        <Box mt={4} mb={3} sx={{ textAlign: 'center' }}>
          <Typography variant="h4" fontWeight={600} gutterBottom sx={{ textAlign: 'center' }}>
            Real Estate Agents in Chicago, IL
          </Typography>
          <Typography variant="body1" color="text.secondary" sx={{ textAlign: 'center' }}>
            With over a million agents from all the top brokerages, a local agent knows your market and will guide you through the process from start to finish.
          </Typography>
        </Box>
        
        {/* Agents List */}
        <Box mb={5}>
          {agents.map((agent) => (
            <Card key={agent.id} className={styles.agentCard}>
              <Grid container spacing={2}>
                {/* Agent Image */}
                <Grid item xs={12} sm={3} md={2}>
                  <Box className={styles.agentImageContainer}>
                    <CardMedia
                      component="img"
                      image={agent.image}
                      alt={agent.name}
                      className={styles.agentImage}
                    />
                    {agent.team && (
                      <Typography 
                        variant="caption" 
                        className={styles.teamBadge}
                      >
                        TEAM
                      </Typography>
                    )}
                  </Box>
                </Grid>
                
                {/* Agent Details */}
                <Grid item xs={12} sm={9} md={6}>
                  <CardContent sx={{ pt: 2, pb: 2, px: 0 }}>
                    <Box display="flex" alignItems="center" gap={1} mb={1}>
                      <Typography variant="body2" className={styles.agentFeatured}>
                        {agent.featured ? 'FEATURED' : ''}
                      </Typography>
                      <Box display="flex" alignItems="center">
                        <StarOutlined sx={{ fontSize: 18, color: '#ffb400' }} />
                        <Typography variant="body1" fontWeight={600} ml={0.5}>
                          {agent.rating}
                        </Typography>
                      </Box>
                      <Typography variant="body2" color="text.secondary">
                        ({agent.reviewCount} reviews)
                      </Typography>
                    </Box>
                    
                    <Typography variant="h5" fontWeight={600}>
                      {agent.name}
                    </Typography>
                    
                    <Typography variant="body2" gutterBottom>
                      {agent.company} 
                      {agent.subCompany && 
                        <Typography component="span" variant="body2" color="text.secondary">
                          {' • '}{agent.subCompany}
                        </Typography>
                      }
                    </Typography>
                    
                    <Typography variant="body2" color="text.secondary">
                      {agent.sales} sales • price range {agent.averagePrice}
                    </Typography>
                    
                    <Typography variant="body2" color="text.secondary">
                      {agent.recentSales} recent sales in {agent.location}
                    </Typography>
                  </CardContent>
                </Grid>
                
                {/* Contact Button */}
                <Grid item xs={12} md={4} display="flex" alignItems="center" justifyContent="flex-end">
                  <Box px={3}>
                    <Button 
                      variant="outlined" 
                      color="primary" 
                      fullWidth
                      onClick={() => navigate(`/agent/${agent.id}`)}
                      sx={{ textTransform: 'none', fontWeight: 600 }}
                    >
                      Contact agent
                    </Button>
                  </Box>
                </Grid>
              </Grid>
            </Card>
          ))}
          
          <Box mt={3} textAlign="center">
            <Button 
              variant="contained" 
              color="primary"
            >
              View more
            </Button>
          </Box>
        </Box>        {/* Finding Agent Section with Background Image */}
        <Box className={styles.agentHelpSection} mt={8} mb={5}>
          <Container maxWidth="lg">
            <Box className={styles.agentHelpContent}>
              <Typography variant="h3" fontWeight={700} gutterBottom color="white">
                Get help finding an agent
              </Typography>
              <Typography variant="subtitle1" paragraph color="white" sx={{ maxWidth: '600px', mb: 4, fontSize: '1.1rem' }}>
                We'll pair you with a Premier Agent who has the inside scoop on your market.
              </Typography>
              <Button 
                variant="contained"
                size="large"
                sx={{ 
                  textTransform: 'none', 
                  px: 3, 
                  py: 1.2, 
                  borderRadius: 1,
                  bgcolor: 'white',
                  color: '#0061df',
                  fontWeight: 600,
                  '&:hover': {
                    bgcolor: '#f5f5f5'
                  }
                }}
              >
                Connect with a local agent
              </Button>
            </Box>
          </Container>
        </Box>
        
        {/* FAQ Section */}
        <Box mt={8} mb={5}>
          <Typography variant="h4" fontWeight={600} gutterBottom sx={{ textAlign: 'center', paddingBottom: '20px' }}>
            Frequently asked questions
          </Typography>
          
          {faqs.map((faq, index) => (
            <Accordion key={index}>
              <AccordionSummary
                expandIcon={<ExpandMore />}
                aria-controls={`panel${index}-content`}
                id={`panel${index}-header`}
              >
                <Typography fontWeight={500}>{faq.question}</Typography>
              </AccordionSummary>
              <AccordionDetails>
                <Typography variant="body2">
                  {faq.answer}
                </Typography>
              </AccordionDetails>
            </Accordion>
          ))}
        </Box>
      </Container>
      
      {/* Footer Links */}
      <Box sx={{ bgcolor: '#f9f9f9', py: 4 }}>
        <Container maxWidth="lg">
          <Typography variant="h6" fontWeight={600} gutterBottom>
            Are you a real estate agent?
          </Typography>
          
          <Typography variant="body2" paragraph>
            Access more detailed resources you will need at our <Link href="/agent-resources">Premier Agent® Resource Center</Link>, covering everything 
            from <Link href="/business-plan">business plan templates</Link> to complete guides on <Link href="/real-estate-marketing">real estate marketing</Link>. 
            As a Premier Agent you'll find out how we can help you get more leads.
          </Typography>
          
          <Grid container spacing={3} mt={4}>
            <Grid item xs={6} sm={3}>
              <Typography variant="subtitle2" fontWeight={600} gutterBottom>
                Real Estate Agents
              </Typography>
              <Link href="/chicago-real-estate-agents" variant="body2" display="block" mb={1}>
                Chicago Real Estate Agents
              </Link>
              <Link href="/cicero-real-estate-agents" variant="body2" display="block" mb={1}>
                Cicero Real Estate Agents
              </Link>
              <Link href="/evanston-real-estate-agents" variant="body2" display="block" mb={1}>
                Evanston Real Estate Agents
              </Link>
              <Link href="/berwyn-real-estate-agents" variant="body2" display="block" mb={1}>
                Berwyn Real Estate Agents
              </Link>
              <Link href="/show-more" variant="body2" display="block" mb={1}>
                Show more
              </Link>
            </Grid>
            
            <Grid item xs={6} sm={3}>
              <Typography variant="subtitle2" fontWeight={600} gutterBottom>
                Mortgage Lenders
              </Typography>
              <Link href="/chicago-mortgage-lenders" variant="body2" display="block" mb={1}>
                Chicago Mortgage Lenders
              </Link>
              <Link href="/cicero-mortgage-lenders" variant="body2" display="block" mb={1}>
                Cicero Mortgage Lenders
              </Link>
              <Link href="/evanston-mortgage-lenders" variant="body2" display="block" mb={1}>
                Evanston Mortgage Lenders
              </Link>
              <Link href="/berwyn-mortgage-lenders" variant="body2" display="block" mb={1}>
                Berwyn Mortgage Lenders
              </Link>
              <Link href="/show-more" variant="body2" display="block" mb={1}>
                Show more
              </Link>
            </Grid>
            
            <Grid item xs={6} sm={3}>
              <Typography variant="subtitle2" fontWeight={600} gutterBottom>
                Home Improvement Pros
              </Typography>
              <Link href="/chicago-home-improvement" variant="body2" display="block" mb={1}>
                Chicago Home Improvement
              </Link>
              <Link href="/cicero-home-improvement" variant="body2" display="block" mb={1}>
                Cicero Home Improvement
              </Link>
              <Link href="/evanston-home-improvement" variant="body2" display="block" mb={1}>
                Evanston Home Improvement
              </Link>
              <Link href="/berwyn-home-improvement" variant="body2" display="block" mb={1}>
                Berwyn Home Improvement
              </Link>
              <Link href="/show-more" variant="body2" display="block" mb={1}>
                Show more
              </Link>
            </Grid>
            
            <Grid item xs={6} sm={3}>
              <Typography variant="subtitle2" fontWeight={600} gutterBottom>
                Property Managers
              </Typography>
              <Link href="/chicago-property-managers" variant="body2" display="block" mb={1}>
                Chicago Property Managers
              </Link>
              <Link href="/cicero-property-managers" variant="body2" display="block" mb={1}>
                Cicero Property Managers
              </Link>
              <Link href="/evanston-property-managers" variant="body2" display="block" mb={1}>
                Evanston Property Managers
              </Link>
              <Link href="/berwyn-property-managers" variant="body2" display="block" mb={1}>
                Berwyn Property Managers
              </Link>
              <Link href="/show-more" variant="body2" display="block" mb={1}>
                Show more
              </Link>
            </Grid>
          </Grid>
        </Container>
      </Box>
    </Box>
  );
};

export default FindAgentPage;
