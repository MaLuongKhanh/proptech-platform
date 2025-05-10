import React, { useEffect } from 'react';
import {
  AppBar,
  Toolbar,
  Button,
  IconButton,
  Box,
  Container,
  Menu,
  MenuItem,
  useMediaQuery,
  Avatar,
  Typography,
} from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import { useTheme } from '@mui/material/styles';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { logout } from '../../store/slices/authSlice';
import { authService } from '../../services/auth.service';
import styles from '../../styles/Header/Header.module.css';
import type { RootState } from '../../store/store';
import logo from '../../assets/images/Proptech-logo.png';

const menuItems = [
  { label: 'Buy', path: '/buy' },
  { label: 'Rent', path: '/rent' },
  { label: 'Sell', path: '/sell' },
  { label: 'Find an Agent', path: '/agents' },
];

export const Header: React.FC = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { user } = useSelector((state: RootState) => state.auth);
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('md'));

  const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
  const [userMenuAnchorEl, setUserMenuAnchorEl] = React.useState<null | HTMLElement>(null);
  const isMenuOpen = Boolean(anchorEl) || Boolean(userMenuAnchorEl);

  const handleMenuOpen = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const handleUserMenuOpen = (event: React.MouseEvent<HTMLElement>) => {
    setUserMenuAnchorEl(event.currentTarget);
  };

  const handleUserMenuClose = () => {
    setUserMenuAnchorEl(null);
  };

  const handleLogout = () => {
    authService.logout();
    dispatch(logout());
    navigate('/');
    handleUserMenuClose();
  };

  // Apply inert attribute to non-menu content when menu is open
  useEffect(() => {
    const mainContent = document.querySelector('main');
    if (isMenuOpen && mainContent) {
      mainContent.setAttribute('inert', '');
      mainContent.setAttribute('aria-hidden', 'true');
    } else if (mainContent) {
      mainContent.removeAttribute('inert');
      mainContent.removeAttribute('aria-hidden');
    }

    return () => {
      if (mainContent) {
        mainContent.removeAttribute('inert');
        mainContent.removeAttribute('aria-hidden');
      }
    };
  }, [isMenuOpen]);

  return (
    <AppBar position="sticky" color="default" elevation={1}>
      <Container maxWidth="lg">
        <Toolbar disableGutters className={styles.headerToolbar}>
          {/* Menu bên trái */}
          {isMobile ? (
            <>
              <IconButton color="inherit" onClick={handleMenuOpen}>
                <MenuIcon />
              </IconButton>
              <Menu
                anchorEl={anchorEl}
                open={Boolean(anchorEl)}
                onClose={handleMenuClose}
                disableScrollLock={true}
                slotProps={{
                  root: {
                    'aria-hidden': false,
                    BackdropProps: { invisible: true },
                  },
                }}
                anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
                transformOrigin={{ vertical: 'top', horizontal: 'right' }}
                PaperProps={{
                  sx: {
                    maxWidth: '100vw',
                    width: { xs: '90vw', sm: '300px' },
                    boxSizing: 'border-box',
                    mt: 1,
                  },
                }}
              >
                {menuItems.map((item) => (
                  <MenuItem
                    key={item.label}
                    onClick={() => {
                      navigate(item.path);
                      handleMenuClose();
                    }}
                    sx={{ whiteSpace: 'normal', wordBreak: 'break-word' }}
                  >
                    {item.label}
                  </MenuItem>
                ))}
              </Menu>
            </>
          ) : (
            <Box className={styles.menuBox} sx={{ justifyContent: 'flex-start' }}>
              {menuItems.map((item) => (
                <Button
                  key={item.label}
                  color="inherit"
                  className={styles.menuButton}
                  onClick={() => navigate(item.path)}
                >
                  {item.label}
                </Button>
              ))}
            </Box>
          )}

          {/* Logo ở giữa */}
          <Box
            component="img"
            src={logo}
            alt="Logo"
            className={styles.logo}
            onClick={() => navigate('/')}
            sx={{ 
              cursor: 'pointer', 
              maxHeight: { xs: '30px', md: '40px' },
              position: 'absolute',
              left: '50%',
              transform: 'translateX(-50%)'
            }}
          />

          {/* Login/Register bên phải */}
          <Box className={styles.rightBox}>
          {user ? (
            <>
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                <Typography
                  variant="body2"
                  sx={{
                    display: { xs: 'none', sm: 'block' },
                    fontWeight: 500,
                  }}
                >
                  Welcome, {user.fullName}!
                </Typography>

                <IconButton
                  onClick={handleUserMenuOpen}
                  size="small"
                  sx={{ ml: 1 }}
                  aria-controls={Boolean(userMenuAnchorEl) ? 'account-menu' : undefined}
                  aria-haspopup="true"
                  aria-expanded={Boolean(userMenuAnchorEl) ? 'true' : undefined}
                >
                  <Avatar
                    sx={{
                      width: { xs: 28, md: 32 },
                      height: { xs: 28, md: 32 },
                      bgcolor: 'primary.main',
                    }}
                    alt={user.fullName}
                    src={user.avatarUrl || undefined}
                  >
                    {!user.avatarUrl && user.fullName?.charAt(0)}
                  </Avatar>
                </IconButton>
              </Box>
              
              <Menu
                anchorEl={userMenuAnchorEl}
                id="account-menu"
                open={Boolean(userMenuAnchorEl)}
                onClose={handleUserMenuClose}
                onClick={handleUserMenuClose}
                slotProps={{
                  root: {
                    'aria-hidden': false,
                    BackdropProps: { invisible: true },
                  },
                }}
                transformOrigin={{ horizontal: 'right', vertical: 'top' }}
                anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
                PaperProps={{
                  sx: {
                    maxWidth: '100vw',
                    width: { xs: '90vw', sm: '200px' },
                    boxSizing: 'border-box',
                    mt: 1,
                  },
                }}
                disableScrollLock={true}
              >
                <MenuItem onClick={() => navigate('/profile')}>Profile</MenuItem>
                <MenuItem onClick={handleLogout}>Logout</MenuItem>
              </Menu>
            </>
            ) : (
              <>
                <Button
                  color="inherit"
                  onClick={() => navigate('/login')}
                  sx={{ fontSize: { xs: '0.8rem', md: '1rem' } }}
                >
                  Login
                </Button>
                <Button
                  variant="contained"
                  color="primary"
                  onClick={() => navigate('/register')}
                  className={styles.registerBtn}
                  sx={{ 
                    fontSize: { xs: '0.8rem', md: '1rem' }, 
                    px: { xs: 1, md: 2 },
                    display: { xs: 'none', sm: 'inline-flex' }
                  }}
                >
                  Register
                </Button>
              </>
            )}
          </Box>
        </Toolbar>
      </Container>
    </AppBar>
  );
};