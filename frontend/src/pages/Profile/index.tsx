import React, { useState, useEffect } from 'react';
import {
  Box,
  Container,
  Typography,
  Paper,
  Tabs,
  Tab,
  Grid,
  Card,
  CardContent,
} from '@mui/material';
import { styled } from '@mui/material/styles';
import { useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import {
  Home as HomeIcon,
  AttachMoney as MoneyIcon,
  TrendingUp as TrendingUpIcon,
} from '@mui/icons-material';
import { listingService } from '../../services/listing.service';
import { paymentService } from '../../services/payment.service';

// Components
import PersonalInfo from './components/PersonalInfo';
import Listings from './components/Listings';
import Wallet from './components/Wallet';
import ServicePackage from './components/ServicePackage';
import AccountManagement from './components/AccountManagement';

const ProfileContainer = styled(Container)(({ theme }) => ({
  paddingTop: theme.spacing(4),
  paddingBottom: theme.spacing(4),
}));

const StyledTabs = styled(Tabs)(({ theme }) => ({
  borderBottom: `1px solid ${theme.palette.divider}`,
  marginBottom: theme.spacing(3),
  '& .MuiTab-root': {
    textTransform: 'none',
    fontWeight: 600,
    fontSize: '1rem',
    minWidth: 120,
  },
}));

const StatCard = styled(Card)(({ theme }) => ({
  height: '100%',
  display: 'flex',
  flexDirection: 'column',
  justifyContent: 'center',
  alignItems: 'center',
  padding: theme.spacing(2),
  textAlign: 'center',
  transition: 'transform 0.2s',
  '&:hover': {
    transform: 'translateY(-4px)',
  },
}));

export const ProfilePage: React.FC = () => {
  const [activeTab, setActiveTab] = useState(0);
  const [userRoles, setUserRoles] = useState<string[]>([]);
  const [stats, setStats] = useState({
    totalListings: 0,
    newListingsThisMonth: 0,
    monthlyDeposits: 0
  });
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const userString = localStorage.getItem('user');
    if (userString) {
      try {
        const user = JSON.parse(userString);
        setUserRoles(user.roles || []);
        if (user.roles?.includes('ROLE_AGENT')) {
          fetchAgentStats(user.id);
        } else if (user.roles?.includes('ROLE_ADMIN')) {
          fetchAdminStats();
        }
      } catch (error) {
        console.error('Error parsing user data:', error);
      }
    }
  }, []);

  const fetchAgentStats = async (userId: string) => {
    try {
      setLoading(true);
      const [listingStats, monthlyDeposits] = await Promise.all([
        listingService.getAgentStats(userId),
        paymentService.getMonthlyDeposits(userId)
      ]);

      setStats({
        totalListings: listingStats.total,
        newListingsThisMonth: listingStats.newThisMonth,
        monthlyDeposits
      });
    } catch (error) {
      console.error('Error fetching agent stats:', error);
    } finally {
      setLoading(false);
    }
  };

  const fetchAdminStats = async () => {
    try {
      setLoading(true);
      const [listings, transactions] = await Promise.all([
        listingService.getAll(),
        paymentService.getTransactions({
          type: 'TOPUP',
          startDate: new Date(new Date().getFullYear(), new Date().getMonth(), 1).toISOString(),
          endDate: new Date().toISOString()
        })
      ]);

      const startOfMonth = new Date();
      startOfMonth.setDate(1);
      startOfMonth.setHours(0, 0, 0, 0);

      const newThisMonth = listings.filter(listing => 
        new Date(listing.updatedAt) >= startOfMonth
      ).length;

      const monthlyDeposits = transactions.reduce((total, transaction) => 
        total + (transaction.status === 'SUCCESS' ? transaction.amount : 0), 0
      );

      setStats({
        totalListings: listings.length,
        newListingsThisMonth: newThisMonth,
        monthlyDeposits
      });
    } catch (error) {
      console.error('Error fetching admin stats:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleTabChange = (event: React.SyntheticEvent, newValue: number) => {
    setActiveTab(newValue);
  };

  const isAgent = userRoles.includes('ROLE_AGENT');
  const isAdmin = userRoles.includes('ROLE_ADMIN');

  return (
    <ProfileContainer maxWidth="lg">
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
      >
        <Grid container spacing={3} sx={{ mb: 3 }}>
          <Grid item xs={12} md={4}>
            <StatCard>
              <HomeIcon sx={{ fontSize: 40, color: 'primary.main', mb: 1 }} />
              <Typography variant="h4" gutterBottom>
                {loading ? '...' : stats.totalListings}
              </Typography>
              <Typography variant="body2" color="text.secondary">
                {isAdmin ? 'Total System Listings' : 'Total Listings'}
              </Typography>
            </StatCard>
          </Grid>
          <Grid item xs={12} md={4}>
            <StatCard>
              <TrendingUpIcon sx={{ fontSize: 40, color: 'success.main', mb: 1 }} />
              <Typography variant="h4" gutterBottom>
                {loading ? '...' : stats.newListingsThisMonth}
              </Typography>
              <Typography variant="body2" color="text.secondary">
                {isAdmin ? 'New System Listings This Month' : 'New Listings This Month'}
              </Typography>
            </StatCard>
          </Grid>
          <Grid item xs={12} md={4}>
            <StatCard>
              <MoneyIcon sx={{ fontSize: 40, color: 'warning.main', mb: 1 }} />
              <Typography variant="h4" gutterBottom>
                {loading ? '...' : new Intl.NumberFormat('vi-VN', {
                  style: 'currency',
                  currency: 'VND'
                }).format(stats.monthlyDeposits)}
              </Typography>
              <Typography variant="body2" color="text.secondary">
                {isAdmin ? 'Total System Deposits This Month' : 'Deposited This Month'}
              </Typography>
            </StatCard>
          </Grid>
        </Grid>

        <Paper sx={{ p: 3, borderRadius: 2 }}>
          <StyledTabs
            value={activeTab}
            onChange={handleTabChange}
            aria-label="profile tabs"
          >
            <Tab label="Personal Information" />
            {isAgent && <Tab label="My Listings" />}
            {isAgent && <Tab label="My Wallet" />}
            {isAgent && <Tab label="Service Package" />}
            {isAdmin && <Tab label="Account Management" />}
          </StyledTabs>

          {activeTab === 0 && <PersonalInfo />}
          {isAgent && activeTab === 1 && <Listings />}
          {isAgent && activeTab === 2 && <Wallet />}
          {isAgent && activeTab === 3 && <ServicePackage />}
          {isAdmin && activeTab === 1 && <AccountManagement />}
        </Paper>
      </motion.div>
    </ProfileContainer>
  );
};
 