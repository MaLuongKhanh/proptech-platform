import React, { useState, useEffect } from 'react';
import {
  Box,
  Grid,
  Paper,
  Typography,
  Button,
  TextField,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  List,
  ListItem,
  ListItemText,
  ListItemIcon,
  Divider,
  Chip,
  CircularProgress,
  Alert,
  Card,
  CardContent,
} from '@mui/material';
import {
  AccountBalance as AccountBalanceIcon,
  Add as AddIcon,
  Remove as RemoveIcon,
  Payment as PaymentIcon,
  History as HistoryIcon,
  QrCode as QrCodeIcon,
} from '@mui/icons-material';
import { motion } from 'framer-motion';
import { paymentService } from '../../../services/payment.service';
import { Wallet as WalletType, Transaction } from '../../../types/payment.types';

const Wallet: React.FC = () => {
  const [wallet, setWallet] = useState<WalletType | null>(null);
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [depositDialogOpen, setDepositDialogOpen] = useState(false);
  const [withdrawDialogOpen, setWithdrawDialogOpen] = useState(false);
  const [amount, setAmount] = useState('');
  const [qrCodeUrl, setQrCodeUrl] = useState<string | null>(null);
  const [showQrCode, setShowQrCode] = useState(false);

  useEffect(() => {
    const userString = localStorage.getItem('user');
    if (userString) {
      try {
        const user = JSON.parse(userString);
        const userId = user.id;
        if (userId) {
          fetchWalletAndTransactions(userId);
        } else {
          setError('User ID not found');
          setLoading(false);
        }
      } catch (error) {
        console.error('Error parsing user data:', error);
        setError('Invalid user data');
        setLoading(false);
      }
    } else {
      setError('Please login to view wallet information');
      setLoading(false);
    }
  }, []);

  const fetchWalletAndTransactions = async (userId: string) => {
    try {
      setLoading(true);
      setError(null);
      const walletData = await paymentService.getWalletByUserId(userId);
      setWallet(walletData);
      const transactionsData = await paymentService.getTransactions({
        walletId: walletData.id,
      });
      setTransactions(transactionsData);
    } catch (err) {
      setError('Unable to load wallet information. Please try again later.');
      console.error('Error fetching wallet:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleDeposit = async () => {
    if (!amount) return;
    
    // Generate VietQR code URL
    const bankId = 'vietcombank';
    const accountNo = '1031572387';
    const template = 'compact2';
    const description = `Deposit to wallet ${wallet?.id}`;
    const accountName = 'MA LUONG KHANH';
    
    const qrUrl = `https://img.vietqr.io/image/${bankId}-${accountNo}-${template}.jpg?amount=${amount}&addInfo=${encodeURIComponent(description)}&accountName=${encodeURIComponent(accountName)}`;
    
    setQrCodeUrl(qrUrl);
    setShowQrCode(true);
  };

  const handleCompleteDeposit = async () => {
    try {
      if (!wallet || !amount) return;
      const updatedWallet = await paymentService.topUpWallet(wallet.id, amount);
      setWallet(updatedWallet);
      await fetchWalletAndTransactions(wallet.userId);
      setDepositDialogOpen(false);
      setShowQrCode(false);
      setQrCodeUrl(null);
      setAmount('');
    } catch (err) {
      setError('Unable to complete deposit. Please try again later.');
      console.error('Error completing deposit:', err);
    }
  };

  const handleWithdraw = async () => {
    try {
      if (!wallet || !amount) return;
      const updatedWallet = await paymentService.paymentWallet(wallet.id, amount);
      setWallet(updatedWallet);
      await fetchWalletAndTransactions(wallet.userId);
      setWithdrawDialogOpen(false);
      setAmount('');
    } catch (err) {
      setError('Unable to withdraw. Please try again later.');
      console.error('Error withdrawing:', err);
    }
  };

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND',
    }).format(amount);
  };

  const getTransactionIcon = (type: string) => {
    switch (type) {
      case 'TOPUP':
        return <AddIcon color="success" />;
      case 'SERVICE_PAYMENT':
        return <RemoveIcon color="error" />;
      default:
        return <HistoryIcon />;
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
      <Grid container spacing={3}>
        <Grid item xs={12} md={4}>
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.3 }}
          >
            <Paper sx={{ p: 3, textAlign: 'center' }}>
              <AccountBalanceIcon sx={{ fontSize: 48, color: 'primary.main', mb: 2 }} />
              <Typography variant="h4" gutterBottom>
                {wallet ? formatCurrency(wallet.balance) : '0 ₫'}
              </Typography>
              <Typography variant="body2" color="text.secondary" gutterBottom>
                Current Balance
              </Typography>
              <Box sx={{ mt: 3, display: 'flex', gap: 2, justifyContent: 'center' }}>
                <Button
                  variant="contained"
                  startIcon={<AddIcon />}
                  onClick={() => setDepositDialogOpen(true)}
                >
                  Top up
                </Button>
              </Box>
            </Paper>
          </motion.div>
        </Grid>

        <Grid item xs={12} md={8}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>
              Transaction History
            </Typography>
            <List>
              {transactions.map((transaction, index) => (
                <React.Fragment key={transaction.id}>
                  <ListItem>
                    <ListItemIcon>
                      {getTransactionIcon(transaction.type)}
                    </ListItemIcon>
                    <ListItemText
                      primary={transaction.description}
                      secondary={new Date(transaction.createdAt).toLocaleString('vi-VN')}
                    />
                    <Box sx={{ textAlign: 'right' }}>
                      <Typography
                        variant="body1"
                        color={
                          transaction.type === 'TOPUP'
                            ? 'success.main'
                            : transaction.type === 'SERVICE_PAYMENT'
                            ? 'error.main'
                            : 'text.primary'
                        }
                      >
                        {transaction.type === 'TOPUP' ? '+' : '-'}
                        {formatCurrency(transaction.amount)}
                      </Typography>
                      <Chip
                        label={
                          transaction.status === 'SUCCESS'
                            ? 'Hoàn thành'
                            : transaction.status === 'PENDING'
                            ? 'Đang xử lý'
                            : 'Thất bại'
                        }
                        size="small"
                        color={
                          transaction.status === 'SUCCESS'
                            ? 'success'
                            : transaction.status === 'PENDING'
                            ? 'warning'
                            : 'error'
                        }
                        sx={{ mt: 1 }}
                      />
                    </Box>
                  </ListItem>
                  {index < transactions.length - 1 && <Divider />}
                </React.Fragment>
              ))}
            </List>
          </Paper>
        </Grid>
      </Grid>

      {/* Deposit Dialog */}
      <Dialog 
        open={depositDialogOpen} 
        onClose={() => {
          setDepositDialogOpen(false);
          setShowQrCode(false);
          setQrCodeUrl(null);
        }}
        maxWidth="sm"
        fullWidth
      >
        <DialogTitle>
          {showQrCode ? 'Scan QR Code to Deposit' : 'Deposit to Wallet'}
        </DialogTitle>
        <DialogContent>
          {!showQrCode ? (
            <TextField
              autoFocus
              margin="dense"
              label="Amount"
              type="number"
              fullWidth
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
              InputProps={{
                startAdornment: <Typography sx={{ mr: 1 }}>₫</Typography>,
              }}
            />
          ) : (
            <Box sx={{ textAlign: 'center', py: 2 }}>
              <Card sx={{ maxWidth: 300, mx: 'auto', mb: 2 }}>
                <CardContent>
                  <img 
                    src={qrCodeUrl || ''} 
                    alt="VietQR Code" 
                    style={{ width: '100%', height: 'auto' }}
                  />
                </CardContent>
              </Card>
              <Typography variant="body2" color="text.secondary" gutterBottom>
                Scan this QR code with your banking app to complete the deposit
              </Typography>
            </Box>
          )}
        </DialogContent>
        <DialogActions>
          <Button 
            onClick={() => {
              setDepositDialogOpen(false);
              setShowQrCode(false);
              setQrCodeUrl(null);
            }}
          >
            Cancel
          </Button>
          {!showQrCode ? (
            <Button 
              onClick={handleDeposit} 
              variant="contained"
              disabled={!amount}
            >
              Next
            </Button>
          ) : (
            <Button 
              onClick={handleCompleteDeposit} 
              variant="contained"
              color="success"
            >
              Complete
            </Button>
          )}
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default Wallet; 