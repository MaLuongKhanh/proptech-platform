import React, { useEffect, useState } from 'react';
import { Box, Paper, Typography, Button, Grid, Chip, Dialog, DialogTitle, DialogContent, DialogActions, List, ListItem, ListItemText, Divider, Alert } from '@mui/material';
import { motion } from 'framer-motion';
import AddIcon from '@mui/icons-material/Add';
import HistoryIcon from '@mui/icons-material/History';
import { paymentService } from '../../../services/payment.service';
import { Transaction } from '../../../types/payment.types';

const PACKAGES = [
  {
    id: 'basic',
    name: 'Basic Package',
    quota: 10,
    price: 0,
    desc: 'Post 10 listings/month, free',
  },
  {
    id: 'pro',
    name: 'Pro Package',
    quota: 50,
    price: 200000,
    desc: 'Post 50 listings/month, special for agents',
  },
];

const getUserId = () => {
  try {
    const user = JSON.parse(localStorage.getItem('user') || '{}');
    return user.id || 'guest';
  } catch {
    return 'guest';
  }
};

const getStorageKey = (userId: string) => `user_package_${userId}`;
const getHistoryKey = (userId: string) => `user_package_history_${userId}`;

const ServicePackage: React.FC = () => {
  const userId = getUserId();
  const [selected, setSelected] = useState('basic');
  const [remaining, setRemaining] = useState(0);
  const [openBuy, setOpenBuy] = useState(false);
  const [history, setHistory] = useState<{ action: string; time: string; value: number }[]>([]);
  const [alert, setAlert] = useState<string | null>(null);
  const [wallet, setWallet] = useState<any>(null);
  const [walletError, setWalletError] = useState<string | null>(null);

  useEffect(() => {
    // Lấy gói và số lượt còn lại từ localStorage
    const data = JSON.parse(localStorage.getItem(getStorageKey(userId)) || '{}');
    setSelected(data.selected || 'basic');
    setRemaining(data.remaining ?? PACKAGES[0].quota);
    setHistory(JSON.parse(localStorage.getItem(getHistoryKey(userId)) || '[]'));
    fetchWallet();
  }, [userId]);

  const fetchWallet = async () => {
    try {
      const walletData = await paymentService.getWalletByUserId(userId);
      setWallet(walletData);
    } catch (err) {
      console.error('Error fetching wallet:', err);
      setWalletError('Không thể tải thông tin ví');
    }
  };

  const saveState = (sel: string, rem: number, his: any[]) => {
    localStorage.setItem(getStorageKey(userId), JSON.stringify({ selected: sel, remaining: rem }));
    localStorage.setItem(getHistoryKey(userId), JSON.stringify(his));
  };

  const handleBuy = async (pkgId: string) => {
    const pkg = PACKAGES.find(p => p.id === pkgId);
    if (!pkg) return;

    if (pkg.price > 0) {
      if (!wallet || wallet.balance < pkg.price) {
        setAlert('Số dư ví không đủ để mua gói này!');
        return;
      }

      try {
        // Trừ tiền từ ví
        await paymentService.paymentWallet(wallet.id, pkg.price.toString());

        // Cập nhật số dư ví
        await fetchWallet();
      } catch (err) {
        console.error('Error processing payment:', err);
        setAlert('Có lỗi xảy ra khi thanh toán!');
        return;
      }
    }

    setSelected(pkgId);
    setRemaining(rem => rem + pkg.quota);
    const newHistory = [
      { action: `Mua ${pkg.name}`, time: new Date().toLocaleString('vi-VN'), value: pkg.quota },
      ...history,
    ];
    setHistory(newHistory);
    saveState(pkgId, remaining + pkg.quota, newHistory);
    setAlert(`Đã mua thành công ${pkg.name}, cộng thêm ${pkg.quota} lượt đăng!`);
    setOpenBuy(false);
  };

  return (
    <Box>
      {walletError && (
        <Alert severity="error" sx={{ mb: 2 }} onClose={() => setWalletError(null)}>
          {walletError}
        </Alert>
      )}
      <motion.div initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} transition={{ duration: 0.3 }}>
        <Paper sx={{ p: 3, mb: 3 }}>
          <Typography variant="h6" gutterBottom>Your Service Package</Typography>
          <Grid container spacing={2} alignItems="center">
            <Grid item xs={12} md={6}>
              <Chip label={PACKAGES.find(p => p.id === selected)?.name} color="primary" sx={{ mr: 2 }} />
              <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>{PACKAGES.find(p => p.id === selected)?.desc}</Typography>
              <Typography variant="h4" color="success.main" sx={{ mt: 2 }}>{remaining} listings left</Typography>
              <Button variant="contained" color="primary" sx={{ mt: 2 }} onClick={() => setOpenBuy(true)} startIcon={<AddIcon />}>Buy More Listings</Button>
            </Grid>
            <Grid item xs={12} md={6}>
              <Box sx={{ textAlign: 'center' }}>
                <HistoryIcon sx={{ fontSize: 48, color: 'info.main', mb: 1 }} />
                <Typography variant="subtitle1">Usage History</Typography>
                <List dense>
                  {history.slice(0, 5).map((h, idx) => (
                    <React.Fragment key={idx}>
                      <ListItem>
                        <ListItemText primary={h.action} secondary={h.time} />
                        <Typography color={h.value > 0 ? 'success.main' : 'error.main'}>{h.value > 0 ? `+${h.value}` : h.value}</Typography>
                      </ListItem>
                      {idx < 4 && <Divider />}
                    </React.Fragment>
                  ))}
                  {history.length === 0 && <ListItem><ListItemText primary="No history yet" /></ListItem>}
                </List>
              </Box>
            </Grid>
          </Grid>
        </Paper>
      </motion.div>
      <Dialog open={openBuy} onClose={() => setOpenBuy(false)} maxWidth="xs" fullWidth>
        <DialogTitle>Choose Service Package</DialogTitle>
        <DialogContent>
          {PACKAGES.map(pkg => (
            <Paper key={pkg.id} sx={{ p: 2, mb: 2, border: selected === pkg.id ? '2px solid #1976d2' : '1px solid #eee' }}>
              <Typography variant="h6">{pkg.name}</Typography>
              <Typography variant="body2" color="text.secondary">{pkg.desc}</Typography>
              <Typography variant="h5" color="primary.main" sx={{ mt: 1 }}>{pkg.price === 0 ? 'Free' : pkg.price.toLocaleString('vi-VN') + '₫'}</Typography>
              <Button 
                variant="contained" 
                color="success" 
                sx={{ mt: 2 }} 
                onClick={() => handleBuy(pkg.id)} 
                disabled={(selected === pkg.id && pkg.price === 0) || (pkg.id === 'basic' && selected === 'pro')}
              >
                {pkg.id === 'basic' && selected === 'pro' ? 'Cannot select basic package' : 'Buy this package'}
              </Button>
            </Paper>
          ))}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenBuy(false)}>Close</Button>
        </DialogActions>
      </Dialog>
      {alert && <Alert sx={{ mt: 2 }} severity="info" onClose={() => setAlert(null)}>{alert}</Alert>}
    </Box>
  );
};

export default ServicePackage; 