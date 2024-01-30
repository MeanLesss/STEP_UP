import React, { useState } from "react";
import Popup from "./components/PopUp.js";
import Login from "./Login.js";
import { Link, Outlet } from 'react-router-dom';
import {
  AppBar,
  Button,
  Card,
  IconButton,
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
// import IconButton from '@mui/material/IconButton';

const Header = () => {
  const [value, setValue] = useState();
  const theme = useTheme();
  const isMatch = useMediaQuery(theme.breakpoints.down("md"));
  const [openPopup, setOpenPopup] = useState(false);
  const [isSignIn, setIsSignIn] = useState(false); 
  /* const [isSignUp, setIsSignIn] = useState(false); /*  */
 
  const closePopup = (event, reason) => {
    if (reason === 'backdropClick') {
        setOpenPopup(false);
    }
  };

///theme
const ColorModeContext = React.createContext({ toggleColorMode: () => {} });
  const colorMode = React.useContext(ColorModeContext);

//

//
  return (
    <React.Fragment>
    <ColorModeContext.Provider value={colorMode}>
      <ThemeProvider theme={theme}>
      {/* <AppBar  position="static">
  <Toolbar >
      {isMatch ? (
        <>
          <img src={Logo} sx={{ transform: "scale(2)" }} alt="logo"></img>
          <DrawerComp />
        </>
      ) : (
        <>
        <img src={Logo} sx={{ transform: "scale(2)"}} ></img>
       
        
          <div style={{marginLeft:"auto"}} >
            
          <IconButton
              size="large"
              edge="end"
              aria-label="account of current user"
              // aria-controls={menuId}
              aria-haspopup="true"
              // onClick={handleProfileMenuOpen}
              color="inherit"
            >
              
              <AccountCircle />
            </IconButton>
            <IconButton edge="end" color="inherit" onClick={colorMode.toggleColorMode}>
          {colorMode.mode === 'dark' ? <Brightness7Icon /> : <Brightness4Icon />}
        </IconButton>
            {theme.palette.mode} mode
      <IconButton sx={{ ml: 1 }} onClick={colorMode.toggleColorMode} color="inherit">
        {theme.palette.mode === 'dark' ? <Brightness7Icon /> : <Brightness4Icon />}
      </IconButton>
          </div>
        </>
      )}
  </Toolbar>
</AppBar> */}
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
            
            <Button variant="contained" onClick={()=>{setOpenPopup(true);setIsSignIn(true);}} sx={{background:"#472874",borderRadius:"30px"}}>
              Login
            </Button>
            <Button sx={{ marginLeft: "10px",background:"#472874",borderRadius:"30px" }} variant="contained"  onClick={()=>{setOpenPopup(true);setIsSignIn(false);}}>
              SignUp
            </Button>
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