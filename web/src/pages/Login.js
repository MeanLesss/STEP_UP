
import React, { useState } from "react";
import { useTheme } from '@mui/material/styles';
import Box from '@mui/material/Box';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import SkipPreviousIcon from '@mui/icons-material/SkipPrevious';
import PlayArrowIcon from '@mui/icons-material/PlayArrow';
import SkipNextIcon from '@mui/icons-material/SkipNext';
import LoingPicture from '../wwwroot/images/Mask group.png';
import { Button, Paper } from '@mui/material';
import GoogleIcon from '@mui/icons-material/Google';
import PersonIcon from '@mui/icons-material/Person';
import MailIcon from '@mui/icons-material/Mail';
import Popup from "./components/PopUp.js";
import LoginForm from "./LoginForm.js";
import SignUp from "./SignUpForm.js";
import { warning, red } from '@mui/material/colors';
import SignUpForm from "./SignUpForm.js";

export default function Login(props) {
  const theme = useTheme();
  const [openPopup, setOpenPopup] = useState(false)
  const [openLogin, setOpenLogin] = useState(false)
  const [signIn, setSignIn] = useState(false); 
  const [showPopup, setShowPopup] = useState(false);
  const back = () => {
    setOpenPopup(false);   
  };

  const closePopup = (event, reason) => {
    if (reason === 'backdropClick') {
        setOpenPopup(false);      
    }
  };
  return (
    <>

        <Card sx={{ display: 'flex'   }} >
            <CardMedia
                component="img"
                sx={{ width: 350,  }}
                image={LoingPicture}
                alt="log in picture"
            />
            <Box sx={{ width: 350,
                        border: 1,borderColor: '#FAFF00',borderLeft: 0, background:'#0D0C22' }} >
                <CardContent sx={{ flex: '1 0 auto' }}>
                <Typography component="div" variant="h5" sx={{color:'white',paddingTop:'55px',textAlign:'center'}} >
                {props.isSignIn ? "Sign in to your account" : "Create a new account"}
                </Typography>
                <Typography variant="subtitle1" sx={{color:'white',textAlign:'center'}} component="div">
                    
                {props.isSignIn ? "Don't have an account?" : "Already have account?"}

                <span style={{color:'#FAFF00',cursor:"pointer"}} onClick={() => {setSignIn(true);}}> 
                    {props.isSignIn ? " sign up here! " : ""}
                    </span> 
                    <span style={{color:'#FAFF00',cursor:"pointer"}} onClick={() => {setSignIn(false);}}> 
                    {!props.isSignIn ? " sign in here!" : ""}
                    </span> 
                    
                </Typography>
                </CardContent>
                <Box sx={{ alignItems: 'center', px: 6, pt: 2 }}>
                    <Button variant="outlined" sx={{my:1,width:'250px',borderRadius:'12px'}} startIcon={<GoogleIcon />} color='warning' >
                        Continue with Google
                    </Button> 
                    <Button variant="outlined" startIcon={<MailIcon />} sx={{my:1,width:'250px',borderRadius:'12px'}} 
                    onClick={()=>{setOpenPopup(true);}}>
                        Continue with Email
                    </Button> 
                    {/* <Button variant="outlined" sx={{my:1,width:'250px',borderRadius:'12px'}} style={{color:"#FAFF00"}} startIcon={<PersonIcon />}>
                        Continue with Guest
                    </Button> */}
                </Box>
            </Box>
        
        </Card>
        
        <Popup
             
                openPopup={openPopup}
                setOpenPopup={setOpenPopup}
            >
                 {props.isSignIn ?<LoginForm closePopup={back} />: <SignUpForm closePopup={back} />}
                
            </Popup>

            <Popup
             
             openPopup={signIn}
             setOpenPopup={setSignIn}
         >
             <Login />
         </Popup>
    </>
  );
};