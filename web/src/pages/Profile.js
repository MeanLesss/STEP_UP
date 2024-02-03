import React, { useEffect, useState } from 'react';
import { makeStyles } from '@mui/styles';
import { Card, CardContent, Typography, Avatar, Grid, Button, Box, TextField, IconButton } from '@mui/material';
import { getUser } from '../API';
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';

const useStyles = makeStyles({
  root: {
    minWidth: 275,
    maxWidth: '100%',
    backgroundColor: '#0D0C22', // Dark background
    color: '#FFFFFF', // White text
    border: '2px solid #FAFF00', // Yellow border
  },
  title: {
    fontSize: 14,
  },
  pos: {
    marginBottom: 12,
  },
  large: {
    width: 100,
    height: 100,
  },
});

const Profile = () => {
  const classes = useStyles();
  const userToken = sessionStorage.getItem('user_token');
  const [data, setData] = useState('');

useEffect(() => {    
    getUser(userToken)     
        .then(result => {
            if (result) {     
                setData(result.data)
                sessionStorage.setItem('user_role',result.data.user_info.role)
            }
        });
}, [userToken]);

const styleTextField = {
    '& .MuiOutlinedInput-root': {
        borderRadius: '9px',
        '& fieldset': {
            borderColor: '#FAFF00',
        },
        '&:hover fieldset': {
            borderColor: '#FAFF00',
        },
        '&.Mui-focused fieldset': {
            borderColor: '#FAFF00',
        },
        '& .MuiOutlinedInput-input': {
            color: '#FAFF00',
            height: '10px',
        },
    },
};

return (
  <Box sx={{ 
      bgcolor: '#0D0C22', 
      color: '#FAFF00', 
      p: 3, 
      display:'flex',
      flexDirection: 'column',
      justifyContent:'center',
      alignItems:'center' 
  }}>
      {data && data.user_info && data.user_detail && (
          <Card sx={{ 
              bgcolor: '#0D0C22', 
              color: '#0D0C22', 
              p: 2, 
              marginBottom: '20px',
              width: '80%',
              display: 'flex',
              flexDirection: 'column',
              justifyContent:'center',
              alignItems:'center' 
          }}>
              <Grid container spacing={2} alignItems="center">
                  <Grid item md={4}>
                      <Typography color={'#FAFF00'} sx={{pt:'5px'}}>Username</Typography>
                      <TextField variant="outlined" value={data.user_info.name} fullWidth sx={styleTextField } />
                      <Typography  color={'#FAFF00'} sx={{pt:'5px'}}>Email</Typography>
                      <TextField variant="outlined" value={data.user_info.email} fullWidth sx={styleTextField} />
                      <Typography  color={'#FAFF00'} sx={{pt:'5px'}}>Phone Number</Typography>
                      <TextField  variant="outlined" value={data.user_detail.phone} fullWidth sx={styleTextField} />
                      <Typography  color={'#FAFF00'} sx={{pt:'5px'}}>Job</Typography>
                      <TextField  variant="outlined" value={data.user_detail.job_type} fullWidth sx={styleTextField} />
                  </Grid>
                  <Grid item md={6}>
                      <div style={{ display: 'flex', justifyContent: 'space-around', padding: '20px' }}>
                          <Card sx={{
                              maxWidth: '90%',               
                              backgroundColor: '#0D0C22',
                              borderColor: '#FAFF00',
                              borderRadius: '15px',
                              color: '#FAFF00',
                              border: '2px solid',
                              m: 'auto',
                              mb: 2,
                              p:4}}>
                              <CardContent>
                                  <Typography variant="h6">
                                      Score
                                  </Typography>
                                  <Typography variant="h6">
                                      {data.user_detail.credit_score}/100
                                  </Typography>
                              </CardContent>
                          </Card>
                          <Card sx={{
                              maxWidth: '90%',                        
                              backgroundColor: '#0D0C22',
                              borderColor: '#FAFF00',
                              borderRadius: '15px',
                              color: '#FAFF00',
                              border: '2px solid',
                              m: 'auto',
                              mb: 2,
                              p: 4}}>
                              <CardContent sx={{display:'flex',justifyContent:'space-between',gap:'4'}}>
                                <div>
                                <Typography variant="h6">
                                      Balance
                                  </Typography>
                                  <Typography variant="h6">
                                      ${data.user_detail.balance.toFixed(2)}
                                  </Typography>
                                </div>
                                  
                                  <IconButton color="primary" aria-label="add to balance">
                                      <AddCircleOutlineIcon />
                                  </IconButton>
                              </CardContent>
                          </Card>
                      </div>
                  </Grid>
              </Grid>
          </Card>
      )}
  </Box>
);

};


export default Profile;
