import React from 'react';
import { Box, Container, Typography, IconButton } from '@mui/material';
import FacebookIcon from '@mui/icons-material/Facebook';
import TwitterIcon from '@mui/icons-material/Twitter';
import InstagramIcon from '@mui/icons-material/Instagram';
import AppleIcon from '@mui/icons-material/Apple';
import AndroidIcon from '@mui/icons-material/Android';
import styles from '../../styles/Footer/Footer.module.css';

const footerLinks = [
  {
    title: 'Real Estate',
    links: ['Buy', 'Rent', 'Sell', 'Home Loans', 'Agent Finder'],
  },
  {
    title: 'Rentals',
    links: ['Apartments for Rent', 'Houses for Rent', 'All Rental Listings', 'All Rental Buildings'],
  },
  {
    title: 'Mortgage Rates',
    links: ['Current Mortgage Rates', 'Refinance Rates', 'Mortgage Calculator', 'Affordability Calculator'],
  },
  {
    title: 'Browse Homes',
    links: ['New Homes', 'Open Houses', 'Luxury', 'Price Cuts', 'Recently Sold'],
  },
];

const subLinks = [
  'About', 'Zestimates', 'Research', 'Careers', 'Privacy', 'Terms', 'Cookie Preference', 'Help', 'Advertise', 'Mobile Apps'
];

export const Footer: React.FC = () => {
  return (
    <Box component="footer" className={styles.footerRoot}>
      <Container maxWidth="lg">
        {/* Main footer links */}
        <div className={styles.footerGrid}>
          {footerLinks.map((col) => (
            <div key={col.title}>
              <Typography className={styles.footerColTitle} gutterBottom>
                {col.title}
              </Typography>
              {col.links.map((link) => (
                <a
                  href="#"
                  key={link}
                  className={styles.footerLink}
                >
                  {link}
                </a>
              ))}
            </div>
          ))}
        </div>

        {/* Sub links */}
        <div className={styles.footerSubLinks}>
          {subLinks.map((link, idx) => (
            <React.Fragment key={link}>
              <a href="#" className={styles.footerLink} style={{ display: 'inline', margin: '0 8px' }}>
                {link}
              </a>
              {idx < subLinks.length - 1 && <span style={{ color: '#bbb' }}>|</span>}
            </React.Fragment>
          ))}
        </div>

        {/* Social & App store */}
        <div className={styles.footerSocialApp}>
          <Box>
            <IconButton href="#" color="primary"><FacebookIcon /></IconButton>
            <IconButton href="#" color="primary"><TwitterIcon /></IconButton>
            <IconButton href="#" color="primary"><InstagramIcon /></IconButton>
          </Box>
          <Box sx={{ mt: { xs: 2, sm: 0 } }}>
            <IconButton href="#" color="primary"><AppleIcon /></IconButton>
            <IconButton href="#" color="primary"><AndroidIcon /></IconButton>
          </Box>
        </div>

        {/* Copyright */}
        <div className={styles.footerCopyright}>
          <Typography variant="body2">
            © {new Date().getFullYear()} Proptech. All rights reserved.
          </Typography>
        </div>
      </Container>
    </Box>
  );
}; 