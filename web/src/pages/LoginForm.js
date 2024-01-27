import React, { useEffect, useState } from 'react';
import { Button, TextField, Grid, Paper, Card, CardMedia, Typography, Container } from '@mui/material';
import LoingPicture from '../wwwroot/images/Mask group.png';
import { createRef } from 'react';

const LoginForm = (props) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
 //login
 const eref = createRef();
 const pref = createRef();
 useEffect(()=>{
  localStorage.clear();
},[])

  const handleEmailChange = (event) => {
    setEmail(event.target.value);
  };

  const handlePasswordChange = (event) => {
    setPassword(event.target.value);
  };


  const handleSubmit = (event) => {
    event.preventDefault();
    if (eref && pref) {
      alert('Logged in');
    } else {
      alert('Please enter both email and password');
    }
    var myHeaders = new Headers();
myHeaders.append("Pragma", "no-cache");
myHeaders.append("Cache-Control", "no-cache");
myHeaders.append("Content-Type", "application/json");
myHeaders.append("Access-Control-Allow-Origin", "*");

var raw = JSON.stringify({
  "email": eref.current.value,
  "password": pref.current.value
});

var requestOptions = {
  method: 'POST',
  // mode: 'no-cors', // 'cors' by default
  headers: myHeaders,
  body: raw,
  redirect: 'follow'
};

    var res =  fetch("http://step-up-kh.duckdns.org:168/api/login ", requestOptions)
      .then(response => response.json())
      .then(result => { return result })
      .catch(error => console.log('error', error));
     console.log(res);
    if (res.status === 'SUCCESS') {
      console.log('Go to  dashboard')
      // console.log('/' + res.user.role.toLowerCase())
      // localStorage.setItem('user', JSON.stringify(res.user))
      localStorage.getItem('user_token', res.user_token)
      // navigate('/' + res.user.role.toLowerCase(), { replace: true })
    }
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
    <Card sx={{ display: 'flex'   }} >
        <CardMedia
            component="img"
            sx={{ width: 350,  }}
            image={LoingPicture}
            alt="log in picture"
        />
      <Paper elevation={3} sx={style} >
        <Container>
        <form onSubmit={handleSubmit}>
            <Typography component="div" variant="h6" textAlign={'center'}>Continue with your email</Typography>
            <Typography sx={{color:'#faff00',paddingTop:'30px'}}>Email</Typography>
            <TextField
                hiddenLabel
                variant="outlined"
                fullWidth
                margin="normal"
                value={email}
                inputRef={eref}
                onChange={handleEmailChange}
                required
                sx={styleTextField}
                />
                  <Typography sx={{color:'#faff00'}}>Password</Typography>
                <TextField
                hiddenLabel
                variant="outlined"
                fullWidth
                margin="normal"
                inputRef={pref}
                type="password"
                value={password}
                onChange={handlePasswordChange}
                required
                sx={styleTextField}
                />
          <div style={{display:'flex',justifyContent:'space-between',paddingTop:'12px'}}>
            <Button variant="outlined"  onClick={props.closePopup} sx={{width:'100px',borderRadius:'9px',borderColor: '#FAFF00',color:'#FAFF00'}}>
                Back
            </Button>
            <Button variant="contained" type="submit" sx={{width:'100px',borderRadius:'9px',background:'#FAFF00',color:'black'}}>
                Login
            </Button>
          </div>
          
        </form>
        </Container>
        
      </Paper>
    </Card>
  );
};

export default LoginForm;
