import React, { useEffect, useState, useCallback, createRef } from 'react'
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Typography from '@mui/material/Typography';
import { Button, CardActionArea, Container, Grid, Rating } from '@mui/material';
import { styled } from '@mui/material/styles';
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import '../wwwroot/css/Global.css';
import { ViewAllService } from '../API';
import defaultImage from '../wwwroot/images/LogoYellow.png'



export default function Service() {
  const userToken = sessionStorage.getItem('user_token');
  const Item = styled(Paper)(({ theme }) => ({
    backgroundColor: theme.palette.mode === 'dark' ? '#1A2027' : '#fff',
    ...theme.typography.body2,
    padding: theme.spacing(1),
    textAlign: 'center',
    color: theme.palette.text.secondary,
  }));
  const [data, setData] = React.useState(null);
  useEffect(() => {
    ViewAllService({userToken}) // Pass the user token
    .then(result => {
      if (result && result.data && result.data.result) {
        setData(result.data.result.data);     
      }
    });// Set the data state variable
  }, [userToken]);

  // useEffect(() => {
  //   const fetchData = async () => {
  //     const result = await ViewAllService();
  //     setAllService(result);
  //   };
  
  //   fetchData();
  // }, []);
  return (
    <>
      
      
    
      <Box sx={{ p: 2 }}>
  <Container fixed>
    <Grid container spacing={5}>
      {data && data.map((item, index) => (
        <Grid item xs={12} md={4} sm={6} ms={4} key={index}>

<Card sx={{ 
      maxWidth: 345,
      height:310,
      backgroundColor: '#7E88DE',
      borderRadius: '15px',
      color: 'white',
      m: 'auto',
      mb: 3,
      p: 2
    }}>
      <Typography gutterBottom variant="h5" component="div">
        {item.title}
      </Typography>
      {Object.values(item.attachments).map((attachment, index) => {
                  let img = attachment ? attachment : process.env.PUBLIC_URL + '../../wwwroot/images/LogoYellow.png';
                  return (
                    <CardMedia
                      key={index}
                      component="img"
                      height="150"             
                      image={img} 
                      alt={item.title}
                      sx={{ borderRadius: "10px", objectFit: "cover" }} 
                    />
                  );
                })}
      <CardContent>
        <Typography variant="body2" color="text.secondary">
          {item.description}
        </Typography>
         <Box sx={{ display: 'flex',justifyContent: 'space-between', mt: 1 ,alignItems:'center',pb:1}}>
          <Typography variant="body2" color="text.secondary"  sx={{pr:4}}>
            Views: {item.view}
          </Typography>
          <Box sx={{ display: 'flex', justifyContent: 'space-between',alignItems:'center'}}>
          <Typography variant="body2" color="text.secondary">
               Rating: 
          </Typography>
          <Rating name="read-only" value={item.service_rate} readOnly />
          </Box>
        
           
          {/* <Button variant="contained" color="primary">
            ${item.price}
          </Button> */}
        </Box>
        <Box sx={{ display: 'flex',alignItems:'center',justifyContent: 'space-between'}}>
        <Typography variant="body2" color="text.secondary">
          Service type: {item.service_type}
        </Typography>
        <Button variant="contained" color="primary">
            ${item.price}
          </Button> 
          </Box>
        
      </CardContent>
    </Card>
  

          
          
        </Grid>
      ))}
    </Grid>
  </Container>
</Box>


     
      
    </>
  );
}