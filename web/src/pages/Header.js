import React, { useEffect, useState } from "react";
import Popup from "./components/PopUp.js";
import Login from "./Login.js";
import { Link, Outlet } from 'react-router-dom';
import {
  AppBar,
  Button,
  Card,
  IconButton,
  Menu,
  MenuItem,
  Paper,
  Tab,
  Tabs,
  Toolbar,
  Typography,
  useMediaQuery,
  useTheme,
} from "@mui/material";
import Logo from "../wwwroot/images/LogoYellow.png";
import LightModeIcon from '@mui/icons-material/LightMode';
import DarkModeIcon from '@mui/icons-material/DarkMode';
import DrawerComp from "./components/Drawer.js";
import { AccountCircle } from "@mui/icons-material";
import  { ColorModeContext } from "./components/Theme.js";
import { ThemeProvider, createTheme } from '@mui/material/styles';
import Brightness4Icon from '@mui/icons-material/Brightness4';
import Brightness7Icon from '@mui/icons-material/Brightness7';
import { getUser } from "../API.js";
// import IconButton from '@mui/material/IconButton';

const Header = () => {
  const [value, setValue] = useState();
  const theme = useTheme();
  const isMatch = useMediaQuery(theme.breakpoints.down("md"));
  const [openPopup, setOpenPopup] = useState(false);
  const [isSignIn, setIsSignIn] = useState(false); 
  const userToken = sessionStorage.getItem('user_token');
  /* const [isSignUp, setIsSignIn] = useState(false); /*  */
 
  const closePopup = (event, reason) => {
    if (reason === 'backdropClick') {
        setOpenPopup(false);
    }
  };
  const Logout = () => {
    sessionStorage.clear();
  };
 
///theme
const ColorModeContext = React.createContext({ toggleColorMode: () => {} });
  const colorMode = React.useContext(ColorModeContext);
  const [auth, setAuth] =useState(false);
 
//
const [anchorEl, setAnchorEl] = React.useState(null);
const handleMenu = (event) => {
  setAnchorEl(event.currentTarget);
};

const handleClose = () => {
  setAnchorEl(null);
};
useEffect(() => {
  if (userToken) {
    setAuth(true);
  } else {
    setAuth(false);
  }
}, [userToken]);
//

  return (
    <React.Fragment>
    <ColorModeContext.Provider value={colorMode}>
      <ThemeProvider theme={theme}>   
<AppBar sx={{ background: "#0D0C22"}} position="static">
  <Toolbar >
      {isMatch ? (
        <>
          <img src={Logo} sx={{ transform: "scale(2)" }} alt="logo"></img>
          <DrawerComp />
        </>
      ) : (
        <>
        <img src={Logo} sx={{ transform: "scale(2)"}}></img>
         <Tabs
  sx={{ marginLeft: "auto" }}
  indicatorColor="secondary"
  textColor="inherit"
  value={value}
  onChange={(e, value) => setValue(value)}
>

  <Tab label="Home" component={Link} to="/" sx={{paddingLeft:"0",color:"#FAFF00"}}/>
  <Tab label="Service" component={Link} to="/service" sx={{color:"#FAFF00"}}/>
  <Tab label="My Service" component={Link} to="/myservice" sx={{color:"#FAFF00"}}/>
  <Tab label="My Work" component={Link} to="/mywork" sx={{color:"#FAFF00"}}/>
  <Tab label="My Order" component={Link} to="/myorder" sx={{paddingRight:"0",color:"#FAFF00"}}/>
</Tabs>
        
          <div style={{marginLeft:"auto"}} >
          {auth && (
            <div>
              <IconButton
                size="large"
                aria-label="account of current user"
                aria-controls="menu-appbar"
                aria-haspopup="true"
                onClick={handleMenu}
                color="inherit"
              >
                <AccountCircle />
              </IconButton>
              <Menu
  id="menu-appbar"
  anchorEl={anchorEl}
  anchorOrigin={{
    vertical: 'top',
    horizontal: 'right',
  }}
  transformOrigin={{
    vertical: 'top',
    horizontal: 'right',
  }}
  open={Boolean(anchorEl)}
  onClose={handleClose}
  sx={{ opacity: 1,
    transform: 'none',
    transition: 'opacity 232ms cubic-bezier(0.4, 0, 0.2, 1) 0ms, transform 155ms cubic-bezier(0.4, 0, 0.2, 1) 0ms',
    top: '50px',
    right: '16px',
    transformOrigin: '-16px -16px',}}
>
<MenuItem onClick={handleClose} component={Link} to="/profile">Profile</MenuItem>
  <MenuItem onClick={Logout}>Logout</MenuItem>
</Menu>

            </div>
          )}
            {!auth && (
              <div>
                <Button variant="contained" onClick={()=>{setOpenPopup(true);setIsSignIn(true);}} sx={{background:"#7E88DE",borderRadius:"30px"}}>
                  Login
                </Button>
                <Button sx={{ marginLeft: "10px",background:"#7E88DE",borderRadius:"30px" }} variant="contained"  onClick={()=>{setOpenPopup(true);setIsSignIn(false);}}>
                  SignUp
                </Button>
              </div>
            )}
          </div>
        </>
      )}
  </Toolbar>
</AppBar>
       
      </ThemeProvider>
    </ColorModeContext.Provider>



      <Popup
                title="Employee Form"
                openPopup={openPopup}
                setOpenPopup={setOpenPopup}
                closePopup={closePopup}
            >
                <Login isSignIn={isSignIn} setOpenPopup={setOpenPopup} />

            </Popup>
            <Outlet/>
    </React.Fragment>
  );
};

export default Header;