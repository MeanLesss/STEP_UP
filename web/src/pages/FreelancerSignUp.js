import React, { useEffect, useState } from 'react';
import { Button, TextField, Grid, Paper, Card, CardMedia, Typography, Containerm,FormControl, Container,CircularProgress, Box  } from '@mui/material';
import LoingPicture from '../wwwroot/images/Mask group.png';
import { createRef } from 'react';
import { Select, MenuItem, InputLabel } from '@mui/material';
import Swal from 'sweetalert2';
import Dialog from '@mui/material/Dialog';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import { styled } from '@mui/system';
import { getUser } from '../API';
const FreelancerSignUp = (props) => {

  const idStatuses = ['National ID', 'Passport'];
  const [idStatus, setidStatus] = useState('');
  const idref = createRef();
  const [isLoading, setIsLoading] = useState(false);
  const [open, setOpen] = React.useState(false);
  const userToken = sessionStorage.getItem('user_token');
  const CryptoJS = require("crypto-js");
  // let secretKey = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
  let storedPassword = sessionStorage.getItem('web');
  // let bytes  = CryptoJS.AES.decrypt(storedPassword, secretKey);
  // let originalPassword = bytes.toString(CryptoJS.enc.Utf8);

  const [data, setData] = useState('');
  

 //validate
 useEffect(() => {    
  getUser(userToken)     
      .then(result => {
          if (result) {     
              setData(result.data)
              if (result.data.user_info && result.data.user_info.role) {
                  sessionStorage.setItem('user_role', result.data.user_info.role)
              } else {
                  console.error('Role is undefined');
              }
          }
      })
      .catch(error => console.error('An error occurred:', error));
}, [userToken]);

  const handleSubmit = (event) => {
    event.preventDefault(); 
  
      setIsLoading(true);
      setOpen(true);
      var myHeaders = new Headers();
      myHeaders.append("Content-Type", "application/json");
      
      var raw = JSON.stringify({
        "guest": false,
        "freelancer": true,
        "name": data.user_info.name,
        "email": data.user_info.email,
        "password": storedPassword,
        "confirm_password": storedPassword,
        "phone_number": data.user_detail.phone,
        "id_number": idref.current.value,
        "job_type": data.user_detail.job_type
      });
      
      var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: raw,
        redirect: 'follow'
      };
    
    //  signup
      fetch("/api/signup", requestOptions)
        .then(response => response.json())
        .then(result =>{
          console.log(result);
          if (result.status === "success") {     
            sessionStorage.setItem('user_token', result.data.user_token);                        
            setIsLoading(false);
            window.location.reload();
            
          }else{
            setIsLoading(false);
            setOpen(false);
            // Swal.fire({
            //   icon: 'error',
            //   title: result.error_msg.email,  

            // });
            Swal.fire({
              icon: result.status,
              title: result.status,
              text: result.error_msg,
            });
          }
          return result;
        })
        .catch(error =>{setIsLoading(false); setOpen(false); console.log('error', error)});
    
  }
    
  const TransparentPaper = styled(Paper)(({ theme }) => ({
    backgroundColor: 'transparent',
  }));
  
  ///style
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

  const style = {
    width: 350,
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    border: 1,
    borderColor: '#FAFF00',
    borderLeft: 0,
    background: '#0D0C22',
    color: 'white',
    
  };
  const selectStyle={
    width:'320px',
    borderRadius: '9px',
    marginTop:'15px',
    borderColor: '#FAFF00',
    color: '#FAFF00',
    '& .MuiOutlinedInput-input': {
      padding: '10px 14px', 
    },
    '& .MuiOutlinedInput-notchedOutline': {
      borderColor: '#FAFF00',
    },
  }
  //
  return (
    <Card sx={{ display: 'flex'   }} >
    <CardMedia
        component="img"
        sx={{ width: 350,  }}
        image={LoingPicture}
        alt="log in picture"
    />
  <Paper elevation={3} sx={style} >
    <Container > 
  
    <form onSubmit={handleSubmit}>
      <Typography component="div" variant="h6" textAlign={'center'} pb={3}>We need additional info to register you as a freelancer</Typography>
                 
          <Typography sx={{color:'#faff00'}}>Please select an option</Typography>
          <FormControl  variant="outlined">
          <Select
              value={idStatus}  
            variant="outlined"
            size="large"
            sx={selectStyle}
            required
            onChange={(e) => setidStatus(e.target.value)}   
          >
            {idStatuses.map((status, index) => (
              <MenuItem value={status} key={index}>
                {status}
              </MenuItem>
            ))}
          </Select>
          </FormControl>
          <Typography sx={{color:'#faff00',paddingTop:'30px'}}>Please Enter your info below</Typography>
          <TextField
            hiddenLabel
            variant="outlined"
            fullWidth
            margin="normal"
            inputRef={idref}
            type="text"   
            required
            sx={styleTextField}
          />
       
     <Box sx={{display:'flex',justifyContent:'center'}}>
      <Button variant="contained" type="submit" sx={{width:'100px',borderRadius:'9px',background:'#FAFF00',color:'black',}}>
          
          Register
      </Button>
     </Box>
         
            
        
      
   
  </form>

  </Container>
{/* popuploainding */}
      <Dialog open={open}  fullWidth PaperComponent={TransparentPaper}>
      <DialogTitle>  </DialogTitle>
      <DialogContent sx={{display:'flex', justifyContent:'center', alignContent:'center', alignItems:'center'}}>
        {isLoading && <CircularProgress size={24} />}
      </DialogContent>
    </Dialog>
    </Paper>
    </Card>
  );
};

export default FreelancerSignUp;
