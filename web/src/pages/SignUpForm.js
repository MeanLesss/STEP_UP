import React, { useEffect, useState } from 'react';
import { Button, TextField, Grid, Paper, Card, CardMedia, Typography, Containerm,FormControl, Container,CircularProgress  } from '@mui/material';
import LoingPicture from '../wwwroot/images/Mask group.png';
import { createRef } from 'react';
import { Select, MenuItem, InputLabel } from '@mui/material';
import Swal from 'sweetalert2';
import Dialog from '@mui/material/Dialog';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import { styled } from '@mui/system';
import { getUser } from '../API';
const SignUpForm = (props) => {


  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const jobStatuses = ['Student', 'Self Employment', 'Employee', 'Manager', 'Passionate Worker', 'Other'];
  const [jobStatus, setJobStatus] = useState('');
  const userToken = sessionStorage.getItem('user_token');
  const [success,setSuccess]=useState('');
 const userref = createRef();
 const emailref = createRef();
 const jobref = createRef();
 const phoneref = createRef();
 const passref = createRef();
 const cpassref = createRef();
 const [isLoading, setIsLoading] = useState(false);
 const [open, setOpen] = React.useState(false);
 //validate
 const validate = () => {
  
  
  if (!userref.current.value) {
    Swal.fire({
      icon: 'error',
      title: 'Oops...',
      text: 'Please enter a user name.',
    });
    return false;
  }

  if (!emailref.current.value) {
    Swal.fire({
      icon: 'error',
      title: 'Oops...',
      text: 'Please enter an email.',
    });
    return false;
  }

  if (!jobref.current.value) {
    Swal.fire({
      icon: 'error',
      title: 'Oops...',
      text: 'Please enter a job.',
    });
    return false;
  }

  if (!phoneref.current.value) {
    Swal.fire({
      icon: 'error',
      title: 'Oops...',
      text: 'Please enter a phone number.',
    });
    return false;
  }
  if(!passref.current.value){
    Swal.fire({
      icon: 'error',
      title: 'Oops...',
      text: 'Please enter password.',
    });
    return false;
  }
  if (passref.current.value !== cpassref.current.value) {
    Swal.fire({
      icon: 'error',
      title: 'Oops...',
      text: 'The passwords do not match!',
    });
    return false;
  }
  // If all checks pass:
  return true;
};
 useEffect(()=>{
  localStorage.clear();
},[])

 


  const handleSubmit = (event) => {
    event.preventDefault();
    setOpen(true);
    setIsLoading(true); 
    // if(validate()){
      var myHeaders = new Headers();
      myHeaders.append("Content-Type", "application/json");
      
      var raw = JSON.stringify({
        "guest": false,
        "freelancer": false,
        "name": userref.current.value,
        "email": emailref.current.value,
        "password":passref.current.value,
        "confirm_password": cpassref.current.value,
        "phone_number": phoneref.current.value,
        "job_type": jobStatus
      });
      
      var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: raw,
        redirect: 'follow'
     };
    
      fetch("/api/signup", requestOptions)
        .then(response => response.json())
        .then(result =>{
          console.log(result);
          if (result.status === "success") {     
            sessionStorage.setItem('user_token', result.data.user_token);       
            sessionStorage.setItem('user_success', result.status);     
            getUser(userToken)     
            .then(result => {
              if (result && result.data && result.data.user_info) {     
                sessionStorage.setItem('user_role',result.data.user_info.role)
              }
            });                    
            setIsLoading(false);
            handleClose();
            
          }else{
            setIsLoading(false);
            handleClose();
          }
          return result;
        })
        .catch(error =>{setIsLoading(false);  console.log('error', error)});
    }

    
  
    const handleClose = () => {
      setOpen(false);
    };

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
    width: 700,
    height:500,
    display: 'flex',
    justifyContent: 'center',
    paddingTop:'40px',
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
    <Paper elevation={3} sx={style} > 
    <Container >

   
     <form onSubmit={handleSubmit}>
    <Typography component="div" variant="h6" textAlign={'center'}>Continue with your email</Typography>
    <Grid container spacing={2}>
      <Grid item xs={12} sm={6}>
      <Typography sx={{color:'#faff00',paddingTop:'30px'}}>Username</Typography>
        <TextField
          hiddenLabel
          variant="outlined"
          fullWidth
          margin="normal"
          height='40px'
          inputRef={userref}
          required
          sx={styleTextField}
        />
        <Typography sx={{color:'#faff00'}}>Email</Typography>
        <TextField
          hiddenLabel
          variant="outlined"
          fullWidth
          margin="normal"
          inputRef={emailref}
          required
          sx={styleTextField}
        />
        <Typography sx={{color:'#faff00'}}>Job Status</Typography>
        <FormControl  variant="outlined">
        <Select
     value={jobStatus}  
  variant="outlined"
  size="large"
  sx={selectStyle}
  onChange={(e) => setJobStatus(e.target.value)}   
>
  {jobStatuses.map((status, index) => (
    <MenuItem value={status} key={index}>
      {status}
    </MenuItem>
  ))}
</Select>
        </FormControl>
       
      </Grid>
      <Grid item xs={12} sm={6}>
        <Typography sx={{color:'#faff00',paddingTop:'30px'}}>Phone</Typography>
        <TextField
          hiddenLabel
          variant="outlined"
          fullWidth
          margin="normal"
          inputRef={phoneref}
          type="text"   
          required
          sx={styleTextField}
        />
        <Typography sx={{color:'#faff00'}}>Password</Typography>
        <TextField
          hiddenLabel
          variant="outlined"
          fullWidth
          margin="normal"
          inputRef={passref}
          type="password"
          required
          sx={styleTextField}
        />
        <Typography sx={{color:'#faff00'}}>ConfirmPassword</Typography>
        <TextField
          hiddenLabel
          variant="outlined"
          fullWidth
          margin="normal"
          inputRef={cpassref}
          type="password"        
          required
          sx={styleTextField}
        />
      </Grid>
      <Grid item xs={12} sm={12} sx={{display:'flex',justifyContent:'center',paddingTop:'20px'}} spacing={4}>
      <Button variant="outlined"  onClick={props.closePopup} sx={{width:'100px',borderRadius:'9px',borderColor: '#FAFF00',color:'#FAFF00'}}>
                Back
            </Button>
            <Button variant="contained" type="submit" sx={{width:'100px',borderRadius:'9px',background:'#FAFF00',color:'black',}}>
         
                Sign UP
            </Button>
      </Grid>
    </Grid>
   
  </form>
  </Container>
  <Dialog open={open} onClose={handleClose} fullWidth PaperComponent={TransparentPaper}>
  <DialogTitle>  </DialogTitle>
  <DialogContent sx={{display:'flex', justifyContent:'center', alignContent:'center', alignItems:'center'}}>
    {isLoading && <CircularProgress size={24} />}
  </DialogContent>
</Dialog>
    </Paper>
  );
};

export default SignUpForm;
