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


export default function Service() {
  const Item = styled(Paper)(({ theme }) => ({
    backgroundColor: theme.palette.mode === 'dark' ? '#1A2027' : '#fff',
    ...theme.typography.body2,
    padding: theme.spacing(1),
    textAlign: 'center',
    color: theme.palette.text.secondary,
  }));
  const [allService, setAllService] = React.useState(null);
  useEffect((e) => {
    ViewAllService()
      .then(data => { setAllService(data); });
      console.log(ViewAllService);
  }, []);
  // useEffect(() => {
  //   const fetchData = async () => {
  //     const result = await ViewAllService();
  //     setAllService(result);
  //   };
  
  //   fetchData();
  // }, []);
  return (
    <>
      <Container sx={{py:'30px'}}>
      <div>
      {/* Check if allService is not null */}
      {allService && allService.map((item, index) => (
        <div key={index}>
          {/* Render your data here */}
          {/* This is just an example, adjust it according to your data structure */}
          <h1>{item.title}</h1>
          <p>{item.description}</p>
        </div>
      ))}
    </div>
        {/* <Card sx={{ maxWidth: 333 }}>
          <CardActionArea>
          <Typography gutterBottom variant="h5" component="div">
               Titlee
              </Typography>
            <CardMedia
              component="img"
              height="140"
              image="/static/images/cards/contemplative-reptile.jpg"
              alt="green iguana"
              crossOrigin="anonymous"
            />
            <CardContent>
             
              <Typography variant="body2" color="text.secondary">
               descrption
              </Typography>
              <Grid container spacing={2} > 
  <Grid xs={6} md={6}>
    <Item elevation={0}>Views:</Item>
  </Grid>
  <Grid xs={6} md={6}>
    <Item elevation={0}>
      <span className=''>Rate: </span><Rating name="read-only" value={4} readOnly />
    </Item>
  </Grid>
  <Grid xs={6} md={8}>
    <Item elevation={0}>allService</Item>
  </Grid>
  <Grid xs={6} md={4}>
    <Item elevation={0}><Button variant='contained' className=''>Price</Button></Item>
  </Grid>
</Grid>

            </CardContent>
          </CardActionArea>
        </Card> */}
      </Container>
    </>
  );
}