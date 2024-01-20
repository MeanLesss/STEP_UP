import React, { useState } from "react";
import Popup from "./components/PopUp.js";
import Login from "./Login.js";
import { Link, Outlet } from 'react-router-dom';
import {
  AppBar,
  Button,
  Card,
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

const Header = () => {
  const [value, setValue] = useState();
  const theme = useTheme();
  const isMatch = useMediaQuery(theme.breakpoints.down("md"));
  const [openPopup, setOpenPopup] = useState(false);

  const closePopup = (event, reason) => {
    if (reason === 'backdropClick') {
        setOpenPopup(false);
    }
  };


  return (
    <React.Fragment>
<AppBar sx={{ background: "#535A9D"}} position="static">
  <Toolbar >
      {isMatch ? (
        <>
          <img src={Logo} sx={{ transform: "scale(2)" }} alt="logo"></img>
          <DrawerComp />
        </>
      ) : (
        <>
         <Tabs
  sx={{ marginLeft: "0" ,marginRight:"auto"}}
  indicatorColor="secondary"
  textColor="inherit"
  value={value}
  onChange={(e, value) => setValue(value)}
>
  <Tab label="Service" component={Link} to="/service" sx={{paddingLeft:"0"}}/>
  <Tab label="My Service" component={Link} to="/myservice" />
  <Tab label="My Work" component={Link} to="/mywork" />
  <Tab label="My Order" component={Link} to="/myorder" sx={{paddingRight:"0"}}/>
</Tabs>
          <img src={Logo} sx={{ transform: "scale(2)"}} style={{alignItems:"center",justifyContent:"center"}} alt="logo"></img>
          <div style={{marginLeft:"auto"}} >
            
            <Button variant="contained" onClick={()=>{setOpenPopup(true);}} sx={{background:"#472874",borderRadius:"30px"}}>
              Login
            </Button>
            <Button sx={{ marginLeft: "10px",background:"#472874",borderRadius:"30px" }} variant="contained" >
              SignUp
            </Button>
          </div>
        </>
      )}
  </Toolbar>
</AppBar>


      <Popup
                title="Employee Form"
                openPopup={openPopup}
                setOpenPopup={setOpenPopup}
                closePopup={closePopup}
            >
                <Login/>
            </Popup>
            <Outlet/>
    </React.Fragment>
  );
};

export default Header;