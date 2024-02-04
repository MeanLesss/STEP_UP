import React, { useEffect, useState, useCallback, createRef } from 'react'
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Typography from '@mui/material/Typography';
import { Button, CardActionArea, Container, FormControl, Grid, LinearProgress, MenuItem, Rating, Select, Slider, TextField } from '@mui/material';
import { styled } from '@mui/material/styles';
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import '../wwwroot/css/Global.css';
import { ViewAllService } from '../API';
import defaultImage from '../wwwroot/images/LogoYellow.png'
import StarBorderIcon from '@mui/icons-material/StarBorder';
import StarBorderPurple500Icon from '@mui/icons-material/StarBorderPurple500';

export default function Service() {
  const userToken = sessionStorage.getItem('user_token');
  const [isLoading, setIsLoading] = useState(false);
  const Item = styled(Paper)(({ theme }) => ({
    backgroundColor: theme.palette.mode === 'dark' ? '#1A2027' : '#fff',
    ...theme.typography.body2,
    padding: theme.spacing(1),
    textAlign: 'center',
    color: theme.palette.text.secondary,
  }));
  const [data, setData] = React.useState(null);

  useEffect(() => {
    setIsLoading(true);
    ViewAllService({ userToken }) // Pass the user token      
      .then(result => {
        if (result && result.data && result.data.result) {     
          setIsLoading(false);
          setData(result.data.result.data);
        }
      });// Set the data state variable
  }, [userToken]);
  const [isTextTruncated, setIsTextTruncated] = useState(true);
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

  const handleReadMoreClick = () => {
    setIsTextTruncated(!isTextTruncated);
  };
  const [price, setPrice] = useState(0);
  const [title, setTitle] = useState('');
  function valuetext(value) {
    return `${value}°C`;
  }
  const [service, setService] = useState('');

  const handleChange = (event) => {
    setService(event.target.value);
  };
  const distinctServices = data ? [...new Set(data.map(item => item.service_type))] : [""];
  // const filteredData = data ? data.filter(item => item.service_type === service) : [data];
  // Filter the data
  const filterData = () => {
    if (!data) return [];
    let filteredData = [...data];
    if (service) {
      filteredData = filteredData.filter(item => item.service_type === service);
    }
    if (price) {
      filteredData = filteredData.filter(item => item.price <= price);
    }
    if (title) {
      filteredData = filteredData.filter(item => item.title.toLowerCase().includes(title.toLowerCase()));
    }
    return filteredData;
  };
  const selectStyle={
    width:'320px',
    borderRadius: '9px',
    marginTop:'15px',
    borderColor: '#FAFF00',
    color: '#FAFF00',
    '& .MuiOutlinedInput-input': {
      padding: '10px 14px', 
    },
    '& .MuiOutlinedInput-notchedOutline': {
      borderColor: '#FAFF00',
    },
  }
  return (
    <>
       

      <Box sx={{ p: 2 }}>
    
        <Container fixed maxWidth='false' >
          <Box sx={{ padding: '0% 4%' }}>
          

            <Box sx={{ display: 'flex', justifyContent: 'center', mt: 1, alignItems: 'center', pb: 1 }}>
              <Typography sx={{ color: '#faff00', padding: '0 10px' }}>Service Type : </Typography>
              <FormControl>
                <Select
                  labelId="demo-simple-select-label"
                  placeholder='Choose....'
                  id="demo-simple-select"
                  value={service}
                  onChange={handleChange}
                  sx={selectStyle}
                >              
                  {distinctServices.map((serviceType, index) => (                   
                    <MenuItem value={serviceType} key={index}>
                      {serviceType}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
              <Typography sx={{ color: '#faff00', padding: '0 10px' }}>Service Title : </Typography>
              <TextField            
  hiddenLabel
  variant="outlined"
  margin="normal"
  type="text" 
  onChange={(e) => setTitle(e.target.value)}           
  sx={styleTextField}></TextField>

            </Box>

            <Box sx={{ mt: 1, pb: 1, width: '100%', display: 'flex', alignItems: 'center' }}>

<Typography sx={{ color: '#faff00', paddingRight: '10px'}}>Price: </Typography>

<Slider
  aria-label="Price range"
  defaultValue={0}
  getAriaValueText={valuetext}
  step={1}
  marks
  min={0}
  max={5000}
  valueLabelDisplay="auto"
  value={price} // Set the value to the price state variable
  onChange={(event, newValue) => setPrice(newValue)} // Update the price state variable when the slider value changes
/>    

</Box>



        {isLoading && <LinearProgress size={24} sx={{'.MuiLinearProgress-bar': {backgroundColor: '#FAFF00'},marginBottom:'20px'}}/>}
            <Grid container spacing={3} >
            {filterData().map((item, index) => (
                <Grid item xs={12} md={3} sm={6} ms={4} key={index}>

                  <Card sx={{
                    maxWidth: '90%',
                    height: 310,
                    backgroundColor: '#0D0C22',
                    borderColor: '#FAFF00',
                    borderRadius: '15px',
                    color: '#FAFF00',
                    border: '2px solid',
                    m: 'auto',
                    mb: 2,
                    p: 3
                  }}>
                    <CardActionArea>
                      <Typography gutterBottom variant="h5" component="div">
                        {item.title}
                      </Typography>
                      {Object.values(item.attachments).map((attachment, index) => {
                        let img = attachment ? attachment : process.env.PUBLIC_URL + '/LogoYellow.png';
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
                        {Object.values(item.description).map((description, index) => {
                          const text = isTextTruncated ? description.slice(0, 100) + '...' : description;
                          return (
                            <Typography variant="body2" color="white" key={index}>
                              {text}
                              <Button color="primary" onClick={handleReadMoreClick}>
                                {isTextTruncated ? 'See More' : 'See Less'}
                              </Button>
                            </Typography>
                          );
                        })}
                        <Typography variant="body2" color="white">
                          {item.description}
                        </Typography>
                        <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 1, alignItems: 'center', pb: 1 }}>
                          <Typography variant="caption" color="white" sx={{ pr: 4 }}>
                            Views: {item.view}
                          </Typography>
                          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                            <Typography variant="body2" color="white">
                              Rating:
                            </Typography>
                            <Rating name="read-only" value={item.service_rate} readOnly
                              emptyIcon={<StarBorderIcon style={{ color: 'white' }} />} />
                          </Box>
                        </Box>
                        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                          <Typography variant="body2" color="white">
                            Service type: {item.service_type}
                          </Typography>
                          <Button variant="contained" sx={{ backgroundColor: '#7E88DE' }}>
                            ${item.price}
                          </Button>
                        </Box>

                      </CardContent>
                    </CardActionArea>

                  </Card>




                </Grid>
              ))}
            </Grid>
            {/* <button onClick={handlePrev}>Previous</button>
      <button onClick={handleNext}>Next</button> */}
          </Box>
        </Container>
      </Box>




    </>
  );
}