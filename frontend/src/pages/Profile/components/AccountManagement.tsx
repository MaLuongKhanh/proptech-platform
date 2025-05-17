import React, { useState, useEffect } from 'react';
import {
  Box,
  Paper,
  Typography,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  IconButton,
  Chip,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
  MenuItem,
  Alert,
  CircularProgress,
} from '@mui/material';
import {
  Block as BlockIcon,
  CheckCircle as CheckCircleIcon,
  Edit as EditIcon,
  Add as AddIcon,
} from '@mui/icons-material';
import { motion } from 'framer-motion';
import { userService } from '../../../services/user.service';
import { paymentService } from '../../../services/payment.service';
import { User } from '../../../types/user.types';

const AccountManagement: React.FC = () => {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [selectedUser, setSelectedUser] = useState<User | null>(null);
  const [editDialogOpen, setEditDialogOpen] = useState(false);
  const [confirmDialogOpen, setConfirmDialogOpen] = useState(false);
  const [actionType, setActionType] = useState<'enable' | 'disable' | 'addRole' | 'removeRole' | null>(null);
  const [selectedRole, setSelectedRole] = useState<string>('');
  const [alert, setAlert] = useState<{ type: 'success' | 'error', message: string } | null>(null);

  const availableRoles = ['USER', 'AGENT', 'ADMIN'];

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      setLoading(true);
      const response = await userService.getAll();
      if (response) {
        setUsers(Array.isArray(response) ? response : [response]);
      }
    } catch (err) {
      setError('Không thể tải danh sách người dùng');
      console.error('Error fetching users:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleAction = async () => {
    if (!selectedUser) return;

    try {
      let success = false;
      switch (actionType) {
        case 'enable':
          success = await userService.enable(selectedUser.id);
          break;
        case 'disable':
          success = await userService.disable(selectedUser.id);
          break;
        case 'addRole':
          success = await userService.addRole(selectedUser.id, selectedRole);
          if (success && selectedRole === 'AGENT') {
            try {
              await paymentService.createWallet({
                userId: selectedUser.id,
                currency: 'VND',
                balance: 0
              });
              console.log('Created wallet for new agent');
            } catch (error) {
              console.error('Failed to create wallet for agent:', error);
            }
          }
          break;
        case 'removeRole':
          success = await userService.removeRole(selectedUser.id, selectedRole);
          break;
      }

      if (success) {
        setAlert({
          type: 'success',
          message: 'Thao tác thành công'
        });
        await fetchUsers();
      } else {
        throw new Error('Thao tác thất bại');
      }
    } catch (err) {
      setAlert({
        type: 'error',
        message: 'Có lỗi xảy ra khi thực hiện thao tác'
      });
      console.error('Error performing action:', err);
    } finally {
      setConfirmDialogOpen(false);
      setActionType(null);
      setSelectedRole('');
    }
  };

  const getStatusColor = (enabled: boolean) => {
    return enabled ? 'success' : 'error';
  };

  const getRoleColor = (role: string) => {
    switch (role) {
      case 'ROLE_ADMIN':
        return 'error';
      case 'ROLE_AGENT':
        return 'warning';
      default:
        return 'default';
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
    <Box>
      {alert && (
        <Alert 
          severity={alert.type} 
          sx={{ mb: 2 }} 
          onClose={() => setAlert(null)}
        >
          {alert.message}
        </Alert>
      )}

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Username</TableCell>
              <TableCell>Email</TableCell>
              <TableCell>Họ tên</TableCell>
              <TableCell>Số điện thoại</TableCell>
              <TableCell>Vai trò</TableCell>
              <TableCell>Trạng thái</TableCell>
              <TableCell>Thao tác</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {users.map((user) => (
              <TableRow key={user.id}>
                <TableCell>{user.username}</TableCell>
                <TableCell>{user.email}</TableCell>
                <TableCell>{user.fullName}</TableCell>
                <TableCell>{user.phoneNumber || '-'}</TableCell>
                <TableCell>
                  <Box sx={{ display: 'flex', gap: 0.5, flexWrap: 'wrap' }}>
                    {user.roles.map((role) => (
                      <Chip
                        key={role}
                        label={role.replace('ROLE_', '')}
                        size="small"
                        color={getRoleColor(role)}
                        onDelete={role !== 'ROLE_USER' ? () => {
                          setSelectedUser(user);
                          setActionType('removeRole');
                          setSelectedRole(role);
                          setConfirmDialogOpen(true);
                        } : undefined}
                      />
                    ))}
                  </Box>
                </TableCell>
                <TableCell>
                  <Chip
                    label={user.enabled ? 'Đang hoạt động' : 'Đã khóa'}
                    size="small"
                    color={getStatusColor(user.enabled)}
                  />
                </TableCell>
                <TableCell>
                  <Box sx={{ display: 'flex', gap: 1 }}>
                    <IconButton
                      size="small"
                      onClick={() => {
                        setSelectedUser(user);
                        setActionType(user.enabled ? 'disable' : 'enable');
                        setConfirmDialogOpen(true);
                      }}
                    >
                      {user.enabled ? <BlockIcon color="error" /> : <CheckCircleIcon color="success" />}
                    </IconButton>
                    <IconButton
                      size="small"
                      onClick={() => {
                        setSelectedUser(user);
                        setActionType('addRole');
                        setConfirmDialogOpen(true);
                      }}
                    >
                      <AddIcon color="primary" />
                    </IconButton>
                  </Box>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      <Dialog
        open={confirmDialogOpen}
        onClose={() => {
          setConfirmDialogOpen(false);
          setActionType(null);
          setSelectedRole('');
        }}
      >
        <DialogTitle>
          {actionType === 'enable' && 'Xác nhận mở khóa tài khoản'}
          {actionType === 'disable' && 'Xác nhận khóa tài khoản'}
          {actionType === 'addRole' && 'Thêm vai trò'}
          {actionType === 'removeRole' && 'Xóa vai trò'}
        </DialogTitle>
        <DialogContent>
          {actionType === 'addRole' && (
            <TextField
              select
              fullWidth
              label="Chọn vai trò"
              value={selectedRole}
              onChange={(e) => setSelectedRole(e.target.value)}
              sx={{ mt: 2 }}
            >
              {availableRoles
                .filter(role => !selectedUser?.roles.includes(role))
                .map((role) => (
                  <MenuItem key={role} value={role}>
                    {role.replace('ROLE_', '')}
                  </MenuItem>
                ))}
            </TextField>
          )}
          {actionType === 'removeRole' && (
            <TextField
              select
              fullWidth
              label="Chọn vai trò cần xóa"
              value={selectedRole}
              onChange={(e) => setSelectedRole(e.target.value)}
              sx={{ mt: 2 }}
            >
              {selectedUser?.roles
                .filter(role => role !== 'ROLE_USER') // Không cho phép xóa ROLE_USER
                .map((role) => (
                  <MenuItem key={role} value={role}>
                    {role.replace('ROLE_', '')}
                  </MenuItem>
                ))}
            </TextField>
          )}
          {actionType === 'enable' && (
            <Typography>
              Bạn có chắc chắn muốn mở khóa tài khoản {selectedUser?.username}?
            </Typography>
          )}
          {actionType === 'disable' && (
            <Typography>
              Bạn có chắc chắn muốn khóa tài khoản {selectedUser?.username}?
            </Typography>
          )}
        </DialogContent>
        <DialogActions>
          <Button
            onClick={() => {
              setConfirmDialogOpen(false);
              setActionType(null);
              setSelectedRole('');
            }}
          >
            Hủy
          </Button>
          <Button
            onClick={handleAction}
            variant="contained"
            color={actionType === 'disable' ? 'error' : 'primary'}
          >
            Xác nhận
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default AccountManagement;