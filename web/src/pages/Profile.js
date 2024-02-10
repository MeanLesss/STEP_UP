import React, { useEffect, useState } from 'react';
import { makeStyles } from '@mui/styles';
import { Card, CardContent, Typography, Avatar, Grid, Button, Box, TextField, IconButton, Dialog, DialogContent, Slider } from '@mui/material';
import { TopUp, getUser } from '../API';
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';
import Swal from 'sweetalert2';

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
  const [amount, setAmount] = useState(0);
  const [open, setOpen] = useState(false);
  const amounts = [5, 10, 20, 50, 100, 200];

  useEffect(() => {    
      getUser(userToken)     
          .then(result => {
              if (result) {     
                  setData(result.data)
                  sessionStorage.setItem('user_role',result.data.user_info.role)
              }
          });
  }, [userToken]);

  const handleTopUp = () => {
    TopUp(userToken, amount).then(result => {   
        // setIsLoading(false);
        if(result.status==="success"){
          Swal.fire({
            icon: result.status,
            title: result.status,
            text: result.msg,
          });
          window.location.reload();
        }      
   
    });
  };

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

function valuetext(value) {
  return `${value}°C`;
}
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
                                  
                                <IconButton color="primary" aria-label="add to balance" onClick={() => setOpen(true)}>
                                    <AddCircleOutlineIcon />
                                </IconButton>
                              </CardContent>
                          </Card>
                      </div>
                  </Grid>
              </Grid>
          </Card>
      )}

       <Dialog open={open} fullWidth maxWidth="md" PaperProps={{ style: { borderRadius: '15px', background: "#0D0C22", backgroundColor: 'transparent' } }}>
      <DialogContent>
        <Card elevation={3} 
          sx={{ 
            bgcolor: '#535A9D', 
            borderRadius: '15px',
            border: 2, 
            borderColor: '#0D0C22', 
            p: '5%', 
            overflowY: 'auto' 
          }}>
          <Typography variant="h4" color={"#faff00"}>Top Up Balance</Typography>
          <Typography variant="h5" align='center' color={"#faff00"}>${amount}</Typography>

          <Slider
            sx={{ width: '100%', marginLeft: '20px' }}
            aria-label="Price range"
            defaultValue={0}
            step={1}
            marks
            min={0}
            max={500}
            valueLabelDisplay="auto"
            value={amount}
            onChange={(event, newValue) => setAmount(newValue)}
          />

          <Grid container spacing={2}>
            {amounts.map((value, index) => (
              <Grid item xs={4} key={index}>
                <Button
                  variant="outlined"
                  sx={{
                    width: '100%',
                    borderRadius: '9px',
                    borderColor: '#FAFF00',
                    color: '#FAFF00',
                  }}
                  onClick={() => setAmount(value)}
                >
                  {value}$
                </Button>
              </Grid>
            ))}
          </Grid>
          <Box sx={{display:'flex' ,justifyContent:'center'}}>
            <Button 
              variant="outlined" 
              onClick={()=>setOpen(false)} 
              sx={{
                width: '20%',
                borderRadius: '9px',
                borderColor: '#FAFF00',
                color: '#0D0C22', 
                bgcolor: '#FAFF00',            
                mt:4,
                mr:4,
              }}
            >
              Close
            </Button>
            <Button 
              variant="contained" 
              onClick={handleTopUp} 
              sx={{
                width: '20%',
                borderRadius: '9px',
                borderColor: '#FAFF00',
                color: '#0D0C22', 
                bgcolor: '#FAFF00',            
                mt:4,
              }}
            >
              Top Up
            </Button>
          </Box>    
        </Card>
      </DialogContent>
    </Dialog>
  </Box>
);

};


export default Profile;
