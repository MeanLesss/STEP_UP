import React, { useEffect, useState } from 'react';
import { Button, TextField, Grid, Paper, Card, CardMedia, Typography, Container } from '@mui/material';
import LoingPicture from '../wwwroot/images/Mask group.png';
import { createRef } from 'react';
import { getUser } from '../API';

const LoginForm = (props) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [userDetail, setUserDetail] = useState(0);
  const userToken = sessionStorage.getItem('user_token');
  
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
      headers: myHeaders,
      body: raw,
      redirect: 'follow'
    };
  
    fetch("/api/login ", requestOptions)
      .then(response => response.json())
      .then(result => {
        console.log(result);
        if (result.status === "success") {     
          sessionStorage.setItem('user_token', result.data.user_token);       
          getUser({userToken: sessionStorage.getItem('user_token')})     
          .then(result => {
            if (result && result.data && result.data.user_info) {     
              sessionStorage.setItem('user_role',result.data.user_info.role)
            }
          });  
        }
        return result;
      })
      .catch(error => console.log('error', error));
     
  };

  // const userToken = sessionStorage.getItem('user_token');
  
  // useEffect(() => {
  //   getUser({userToken}) // corrected here
  //   .then(data => {
  //     if (data.status === "success") {
  //       sessionStorage.setItem('user_detail', JSON.stringify(data));
  //       sessionStorage.setItem('role', data.user_info.role);
  //       sessionStorage.setItem('is_guest', data.user_info.isGuest);
  //     } 
  //   })
  // });

 
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
