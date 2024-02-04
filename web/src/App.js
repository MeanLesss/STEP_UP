import logo from './logo.svg';
import './App.css';
import { Box, TextField, Button, Typography } from '@mui/material';
import { Outlet, useLocation, useNavigate } from 'react-router-dom'
import Header from './pages/Header'
import Profile from './pages/Profile';

function App() {
 
  return (
    <>
     
     <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',      
        color: '#fff',
        height: '100vh',
        justifyContent:'center'
 
       
      }}
    >
      <Typography variant="h4" component="div" gutterBottom>
        Find the right freelance service, right away
      </Typography>
      <TextField
        placeholder="Search for any service..."
        variant="outlined"
        
        sx={{ '& .MuiOutlinedInput-root': {
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
            color: '#FAFF00'        
          },
        },width:'80%',paddingBottom:"40px"}}
      />
      <Button variant="contained" color="primary">
        Search
      </Button>
    </Box>
    {/* <Profile/> */}
        
    </>

  );
}

export default App;
