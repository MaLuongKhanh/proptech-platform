import React from 'react';
import { Box, Container, Typography, Paper, Avatar, Button } from '@mui/material';
import { useSelector } from 'react-redux';
import type { RootState } from '../store/store';

export const Profile: React.FC = () => {
  const { user } = useSelector((state: RootState) => state.auth);

  if (!user) {
    return (
      <Container>
        <Typography>Please login to view your profile</Typography>
      </Container>
    );
  }

  return (
    <Container maxWidth="md">
      <Box sx={{ mt: 4 }}>
        <Paper elevation={3} sx={{ p: 4 }}>
          <Box sx={{ display: 'flex', alignItems: 'center', mb: 4 }}>
            <Avatar
              sx={{ width: 100, height: 100, mr: 3 }}
              src={user.avatarUrl || ''}
              alt={user.fullName}
            >
              {!user.avatarUrl && user.fullName?.charAt(0)}
            </Avatar>
            <Box>
              <Typography variant="h4" gutterBottom>
                {user.fullName}
              </Typography>
            </Box>
          </Box>

          <Box sx={{ mt: 4 }}>
            <Typography variant="h6" gutterBottom>
              Account Settings
            </Typography>
            <Button variant="outlined" sx={{ mr: 2 }}>
              Edit Profile
            </Button>
            <Button variant="outlined" color="error">
              Delete Account
            </Button>
          </Box>
        </Paper>
      </Box>
    </Container>
  );
}; 