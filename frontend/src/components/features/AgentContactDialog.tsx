import React, { useEffect, useState } from 'react';
import { Dialog, DialogContent, Box, Typography, Avatar, Button, CircularProgress } from '@mui/material';
import StarIcon from '@mui/icons-material/Star';
import StarHalfIcon from '@mui/icons-material/StarHalf';
import { userService } from '../../services/user.service';

interface AgentContactDialogProps {
  open: boolean;
  onClose: () => void;
  agentId: string;
  agentName: string;
  agentPhone?: string;
  agentEmail?: string;
  agentTeam?: string;
}

const AgentContactDialog: React.FC<AgentContactDialogProps> = ({ open, onClose, agentId, agentName, agentPhone, agentEmail, agentTeam }) => {
  const [avatarUrl, setAvatarUrl] = useState<string | undefined>(undefined);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (open && agentId) {
      setLoading(true);
      userService.getById(agentId).then((user) => {
        setAvatarUrl(user?.avatarUrl || '');
      }).finally(() => setLoading(false));
    }
  }, [open, agentId]);

  const firstName = agentName ? agentName.split(' ')[0] : 'Agent';

  return (
    <Dialog open={open} onClose={onClose} maxWidth="xs" fullWidth>
      <DialogContent>
        <Typography variant="caption" color="text.secondary" sx={{ mb: 1, display: 'block' }}>Listed by</Typography>
        <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', mb: 2 }}>
          {loading ? <CircularProgress /> : <Avatar src={avatarUrl} sx={{ width: 72, height: 72, mb: 1 }} />}
          <Typography variant="h6" sx={{ fontWeight: 700 }}>{agentName}</Typography>
          {agentTeam && <Typography variant="subtitle2" color="text.secondary">{agentTeam}</Typography>}
          <Box sx={{ display: 'flex', alignItems: 'center', mt: 1, mb: 1 }}>
            <StarIcon sx={{ color: '#1976d2' }} />
            <StarIcon sx={{ color: '#1976d2' }} />
            <StarIcon sx={{ color: '#1976d2' }} />
            <StarIcon sx={{ color: '#1976d2' }} />
            <StarHalfIcon sx={{ color: '#1976d2' }} />
            <Typography variant="body2" color="primary" sx={{ ml: 1 }}>22 Reviews</Typography>
          </Box>
        </Box>
        <Box sx={{ mb: 2 }}>
          {agentPhone && <Typography variant="body2">Phone: {agentPhone}</Typography>}
          {agentEmail && <Typography variant="body2">Email: {agentEmail}</Typography>}
        </Box>
        <Button variant="outlined" color="primary" fullWidth>Contact {firstName}</Button>
      </DialogContent>
    </Dialog>
  );
};

export default AgentContactDialog; 