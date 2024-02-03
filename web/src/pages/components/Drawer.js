import React, { useState } from "react";
import Logo from "../../wwwroot/images/LogoYellow.png";
import {
  Drawer,
  IconButton,
  List,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Button,
  Paper,
} from "@mui/material";
import MenuIcon from "@mui/icons-material/Menu";
import { Link } from "react-router-dom";
import LogoutIcon from '@mui/icons-material/Logout';
const pages = [
  { label: "Home", path: "/" },
  { label: "Service", path: "/service" },
  { label: "My Service", path: "/myservice" },
  { label: "My Work", path: "/mywork" },
  { label: "My Order", path: "/myorder" }
];

const DrawerComp = () => {
  const [openDrawer, setOpenDrawer] = useState(false);
  return (
    <React.Fragment>
      <Drawer
        anchor="right"
        open={openDrawer}
        onClose={() => setOpenDrawer(false)}
        PaperProps={{
          sx: {
            width: "300px", // Set the width of the drawer to 200px
          }}}
      >
<Paper sx={{background:"#0D0C22",padding:"10px"}} elevation={0} square>

<img src={Logo} sx={{ transform: "scale(2)"}}></img>
  </Paper>

          {/* <Link>Sign in</Link> */}
        <List>         
          {pages.map((page, index) => (
            <ListItemButton key={index} component={Link} to={page.path}>
              <ListItemText>{page.label}</ListItemText>
            </ListItemButton>
          ))}
        </List>
        {/* <Button variant="contained" endIcon={<LogoutIcon />} fullWidth size="Large"
         sx={{ alignSelf: "center", position: "absolute", bottom: 0 }}>LOG OUT
        </Button> */}
       
      </Drawer>
      <IconButton
        sx={{ color: "white", marginLeft: "auto" }}
        onClick={() => setOpenDrawer(!openDrawer)}
      >
        <MenuIcon color="white" />
      </IconButton>
    </React.Fragment>
  );
};

export default DrawerComp;