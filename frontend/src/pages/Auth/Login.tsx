import React from 'react';
import {
  Box,
  Typography,
  TextField,
  Button,
  Link,
  Divider,
} from '@mui/material';
import GoogleIcon from '@mui/icons-material/Google';
import AppleIcon from '@mui/icons-material/Apple';
import FacebookIcon from '@mui/icons-material/Facebook';
import { useFormik } from 'formik';
import * as yup from 'yup';
import { useNavigate } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import { setCredentials } from '../../store/slices/authSlice';
import { authService } from '../../services/auth.service';
import styles from '../../styles/Auth/AuthForm.module.css';
import logo from '../../assets/images/Proptech-logo.png';
import { LoginRequest, JwtResponse } from '../../types/auth.types';

const validationSchema = yup.object({
  username: yup
    .string()
    .required('Username is required'),
  password: yup
    .string()
    .min(5, 'Password must be at least 5 characters')
    .required('Password is required'),
});

export const Login: React.FC = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const formik = useFormik<LoginRequest>({
    initialValues: {
      username: '',
      password: '',
    },
    validationSchema: validationSchema,
    onSubmit: async (values) => {
      try {
        const response = await authService.login(values);
        if (response) {
          dispatch(setCredentials(response));
          navigate('/');
        }
      } catch (error) {
        console.error('Login error:', error);
      }
    },
  });

  return (
    <Box className={styles.authContainer}>
      {/* Left: Form */}
      <Box className={styles.formBox}>
        <Box className={styles.formInner}>
          {/* Logo */}
          <Box
            component="img"
            src={logo}
            alt="Proptech Logo"
            className={styles.logo}
          />
          <Typography className={styles.title} mb={2}>
            Sign in
          </Typography>
          <Box component="form" onSubmit={formik.handleSubmit} sx={{ mt: 2 }}>
            <TextField
              fullWidth
              id="username"
              name="username"
              label="Username*"
              value={formik.values.username}
              onChange={formik.handleChange}
              error={formik.touched.username && Boolean(formik.errors.username)}
              helperText={formik.touched.username && formik.errors.username}
              margin="normal"
              size="medium"
            />
            <TextField
              fullWidth
              id="password"
              name="password"
              label="Password*"
              type="password"
              value={formik.values.password}
              onChange={formik.handleChange}
              error={formik.touched.password && Boolean(formik.errors.password)}
              helperText={formik.touched.password && formik.errors.password}
              margin="normal"
              size="medium"
            />
            <Button
              type="submit"
              fullWidth
              variant="contained"
              color="primary"
              size="large"
              className={styles.submitBtn}
            >
              Continue
            </Button>
            <Box sx={{ mt: 2, textAlign: 'left' }}>
              <Typography variant="body2">
                New to Proptech?{' '}
                <Link href="/register" variant="body2" sx={{ fontWeight: 700 }}>
                  Create account
                </Link>
              </Typography>
            </Box>
            <Divider className={styles.divider}>OR</Divider>
            <Button
              key="google"
              fullWidth
              variant="outlined"
              startIcon={<GoogleIcon />}
              className={styles.socialBtn}
            >
              Continue with Google
            </Button>
            <Button
              key="apple"
              fullWidth
              variant="outlined"
              startIcon={<AppleIcon />}
              className={styles.socialBtn}
            >
              Continue with Apple
            </Button>
            <Button
              key="facebook"
              fullWidth
              variant="outlined"
              startIcon={<FacebookIcon />}
              className={styles.socialBtn}
            >
              Continue with Facebook
            </Button>
            <Typography variant="caption" color="text.secondary" sx={{ mt: 2, display: 'block' }}>
              By submitting, I accept Proptech's <Link href="#">terms of use</Link>
            </Typography>
          </Box>
        </Box>
      </Box>
      {/* Right: Image */}
      <Box
        className={styles.rightImage}
        sx={{
          backgroundImage:
            'url(https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=1500&q=80)',
        }}
      />
    </Box>
  );
}; 