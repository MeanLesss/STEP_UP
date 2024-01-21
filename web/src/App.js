import logo from './logo.svg';
import './App.css';
import { Box, TextField, Button, Typography } from '@mui/material';
import { Outlet, useLocation, useNavigate } from 'react-router-dom'
import Header from './pages/Header'

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
        sx={{ width: '80%', marginBottom: 2 }}
      />
      <Button variant="contained" color="primary">
        Search
      </Button>
    </Box>
        
    </>

  );
}

export default App;
