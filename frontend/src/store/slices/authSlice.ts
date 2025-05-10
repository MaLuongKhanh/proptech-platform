import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { AuthState, JwtResponse } from '../../types/auth.types';


// Lấy thông tin user từ localStorage khi khởi tạo
const getUserFromStorage = (): JwtResponse | null => {
  const userStr = localStorage.getItem('user');
  if (userStr) {
    try {
      return JSON.parse(userStr);
    } catch (error) {
      console.error('Error parsing user from localStorage:', error);
      return null;
    }
  }
  return null;
};

const initialState: AuthState = {
  user: getUserFromStorage(),
  error: null,
};


const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    setCredentials: (
      state,
      action: PayloadAction<JwtResponse>
    ) => {
      state.user = action.payload;
      state.error = null;
      // Lưu user vào localStorage khi set user mới
      if (action.payload) {
        localStorage.setItem('accessToken', action.payload.accessToken);
        localStorage.setItem('refreshToken', action.payload.refreshToken);
        const user = {
          id: action.payload.id,
          fullName: action.payload.fullName,
          avatarUrl: action.payload.avatarUrl,
          roles: action.payload.roles
        };
        localStorage.setItem('user', JSON.stringify(user));
      } else {
        localStorage.removeItem('user');
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
      }
    },
    setError: (state, action: PayloadAction<string | null>) => {
      state.error = action.payload;
    },
    logout: (state) => {
      state.user = null;
      state.error = null;
      localStorage.removeItem('user');
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');  
    },
  },
});

export const { setCredentials, logout, setError } = authSlice.actions;
export default authSlice.reducer; 