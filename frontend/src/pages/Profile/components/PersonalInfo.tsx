import React, { useState, useEffect } from 'react';
import {
  Box,
  Grid,
  TextField,
  Button,
  Typography,
  Paper,
  Avatar,
  IconButton,
  CircularProgress,
  Alert,
} from '@mui/material';
import { PhotoCamera } from '@mui/icons-material';
import { styled } from '@mui/material/styles';
import { userService } from '../../../services/user.service';
import { User, UpdateUserRequest } from '../../../types/user.types';

const Input = styled('input')({
  display: 'none',
});

const PersonalInfo: React.FC = () => {
  const [isEditing, setIsEditing] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [user, setUser] = useState<User | null>(null);
  const [formData, setFormData] = useState<UpdateUserRequest>({
    email: '',
    fullName: '',
    phoneNumber: '',
  });
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [previewUrl, setPreviewUrl] = useState<string | null>(null);

  useEffect(() => {
    fetchUserData();
  }, []);

  const fetchUserData = async () => {
    try {
      setLoading(true);
      setError(null);
      const userString = localStorage.getItem('user');
      if (userString) {
        const parsedUser = JSON.parse(userString);
        const userData = await userService.getById(parsedUser.id);
        if (userData) {
          setUser(userData);
          setFormData({
            email: userData.email,
            fullName: userData.fullName,
            phoneNumber: userData.phoneNumber || '',
          });
          setPreviewUrl(userData.avatarUrl);
        }
      }
    } catch (err) {
      setError('Không thể tải thông tin người dùng. Vui lòng thử lại sau.');
      console.error('Error fetching user data:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      const file = e.target.files[0];
      setSelectedFile(file);
      setPreviewUrl(URL.createObjectURL(file));
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!user) return;

    try {
      setLoading(true);
      setError(null);

      const updateData: UpdateUserRequest = {
        ...formData,
      };

      if (selectedFile) {
        updateData.avatar = selectedFile;
      }

      const updatedUser = await userService.update(user.id, updateData);
      if (updatedUser) {
        setUser(updatedUser);
        setIsEditing(false);
        // Cập nhật lại thông tin user trong localStorage
        const userString = localStorage.getItem('user');
        if (userString) {
          const parsedUser = JSON.parse(userString);
          localStorage.setItem('user', JSON.stringify({
            ...parsedUser,
            ...updatedUser,
          }));
        }
      }
    } catch (err) {
      setError('Không thể cập nhật thông tin. Vui lòng thử lại sau.');
      console.error('Error updating user:', err);
    } finally {
      setLoading(false);
    }
  };

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
    <Box component="form" onSubmit={handleSubmit}>
      <Grid container spacing={3}>
        <Grid item xs={12} md={4}>
          <Paper sx={{ p: 3, textAlign: 'center' }}>
            <Box sx={{ position: 'relative', display: 'inline-block' }}>
              <Avatar
                sx={{ width: 150, height: 150, mb: 2 }}
                src={previewUrl || undefined}
              />
              {isEditing && (
                <label htmlFor="avatar-upload">
                  <Input
                    accept="image/*"
                    id="avatar-upload"
                    type="file"
                    onChange={handleFileChange}
                  />
                  <IconButton
                    color="primary"
                    aria-label="upload picture"
                    component="span"
                    sx={{
                      position: 'absolute',
                      bottom: 0,
                      right: 0,
                      backgroundColor: 'white',
                      '&:hover': { backgroundColor: 'white' },
                    }}
                  >
                    <PhotoCamera />
                  </IconButton>
                </label>
              )}
            </Box>
            <Typography variant="h6" gutterBottom>
              {user?.fullName}
            </Typography>
            <Typography variant="body2" color="text.secondary">
              {user?.email}
            </Typography>
          </Paper>
        </Grid>

        <Grid item xs={12} md={8}>
          <Paper sx={{ p: 3 }}>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 3 }}>
              <Typography variant="h6">Thông tin cá nhân</Typography>
              <Button
                variant="outlined"
                onClick={() => setIsEditing(!isEditing)}
              >
                {isEditing ? 'Hủy' : 'Chỉnh sửa'}
              </Button>
            </Box>

            <Grid container spacing={3}>
              <Grid item xs={12} sm={6}>
                <TextField
                  fullWidth
                  label="Họ và tên"
                  name="fullName"
                  value={formData.fullName}
                  onChange={handleChange}
                  disabled={!isEditing}
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  fullWidth
                  label="Email"
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                  disabled={!isEditing}
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  fullWidth
                  label="Số điện thoại"
                  name="phoneNumber"
                  value={formData.phoneNumber}
                  onChange={handleChange}
                  disabled={!isEditing}
                />
              </Grid>
            </Grid>

            {isEditing && (
              <Box sx={{ mt: 3, display: 'flex', justifyContent: 'flex-end' }}>
                <Button
                  type="submit"
                  variant="contained"
                  color="primary"
                  disabled={loading}
                >
                  {loading ? <CircularProgress size={24} /> : 'Lưu thay đổi'}
                </Button>
              </Box>
            )}
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
};

export default PersonalInfo; 