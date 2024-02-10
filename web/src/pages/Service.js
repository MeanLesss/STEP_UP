import React, { useEffect, useState, useCallback, createRef } from 'react'
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Typography from '@mui/material/Typography';
import { AppBar, Button, CardActionArea, Checkbox, Container, Dialog, DialogContent, DialogTitle, Divider, FormControl, Grid, LinearProgress, List, ListItem, ListItemText, MenuItem, Rating, Select, Slider, TextField } from '@mui/material';
import { styled } from '@mui/material/styles';
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import '../wwwroot/css/Global.css';
import { GetAgreement, ViewAllService } from '../API';
import defaultImage from '../wwwroot/images/LogoYellow.png'
import StarBorderIcon from '@mui/icons-material/StarBorder';
import { useNavigate } from 'react-router-dom';


export default function Service() {


  const userToken = sessionStorage.getItem('user_token');
  const [isLoading, setIsLoading] = useState(false);
  const [isTextTruncated, setIsTextTruncated] = useState(true);
  const [data, setData] = React.useState(null);
  const [price, setPrice] = useState(0);
  const [title, setTitle] = useState('');
  const [service, setService] = useState('');
  const [open, setOpen] = useState(false);//pop up when clik on card
  const [selectedItem, setSelectedItem] = useState(null);
  const navigate = useNavigate();
  useEffect(() => {
    setIsLoading(true);
    ViewAllService(userToken) // Pass the user token      
      .then(result => {
        if (result && result.data && result.data.result) {
          setIsLoading(false);
          setData(result.data.result.data);
        }
      });// Set the data state variable
  }, [userToken]);

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

  const distinctServices = data ? [...new Set(data.map(item => item.service_type))] : [""];
  // const filteredData = data ? data.filter(item => item.service_type === service) : [data];
  // Filter the data

  const handleReadMoreClick = () => {
    setIsTextTruncated(!isTextTruncated);
  };

  function valuetext(value) {
    return `${value}°C`;
  }

  const handleChange = (event) => {
    setService(event.target.value);
  };

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

  const handleOpen = (item) => {
    setSelectedItem(item);
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  //style
  const selectStyle = {
    width: '320px',
    borderRadius: '9px',
    marginTop: '15px',
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

      {/* List Service */}
      <Box sx={{ p: 2 }}>

        <Container fixed maxWidth='false' >
          <Box sx={{ padding: '1% 4%' }}>


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

              <Typography sx={{ color: '#faff00', paddingLeft: '10px' }}> Price: </Typography>
              <Slider
                sx={{ width: 300, marginLeft: '20px' }}
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
            {isLoading && <LinearProgress size={24} sx={{ '.MuiLinearProgress-bar': { backgroundColor: '#FAFF00' }, marginBottom: '20px' }} />}
            <Grid container spacing={3} paddingTop={4}>
              {filterData().map((item, index) => (
                <Grid item xs={12} md={3} sm={6} ms={4} key={index}>

                  <Card sx={{
                    maxWidth: '90%',
                    // height: 340,
                    backgroundColor: '#0D0C22',
                    borderColor: '#FAFF00',
                    borderRadius: '15px',
                    color: '#FAFF00',
                    border: '2px solid',
                    m: 'auto',
                    mb: 2,
                    p: 1
                  }}>
                    <CardActionArea key={index} onClick={() => navigate(`/buyservice/${item.id}`)}>
                      <Typography gutterBottom variant="h5" component="div" sx={{ padding: '7px 7px 0px 7px' }}>
                        {item.title}
                      </Typography>
                      {Object.keys(item.attachments).length === 0 ? (
                          <CardMedia
                            component="img"
                            height="125"
                            image={defaultImage}
                            alt={item.title}
                            sx={{ borderRadius: "10px", objectFit: "cover" }}
                          />
                        ) : (
                          <CardMedia
                            component="img"
                            height="125"
                            image={Object.values(item.attachments)[0]} // Select the first attachment
                            alt={item.title}
                            sx={{ borderRadius: "10px", objectFit: "cover" }}
                          />
                        )}
                      <CardContent>
                        {/* {Object.values(item.description).map((description, index) => {
                          const text = isTextTruncated ? description.slice(0, 100) + '...' : description;
                          return (
                            <Typography variant="body2" color="white" key={index}>
                              {text}
                              <Button color="primary" onClick={handleReadMoreClick}>
                                {isTextTruncated ? 'See More' : 'See Less'}
                              </Button>
                            </Typography>
                          );
                        })} */}
                        <Typography variant="body2" color="white" style={{
                          display: '-webkit-box',
                          WebkitLineClamp: 3,
                          WebkitBoxOrient: 'vertical',
                          overflow: 'hidden',
                          textOverflow: 'ellipsis',
                        }}>
                          {item.description}
                        </Typography>  
                        <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 1, alignItems: 'center', pb: 1 }}>
                          <Typography variant="caption" color="white" sx={{ pr: 4 }}>
                            Views: {item.view}
                          </Typography>
                          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                            <Typography variant="caption" color="white">
                              Rating:
                            </Typography>
                            <Rating 
                                name="read-only" 
                                value={item.service_rate} 
                                readOnly 
                                size='small'
                                emptyIcon={<StarBorderIcon style={{ color: 'white' }} size='small' />} 
                              />

                          </Box>
                        </Box>
                        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                          <Typography variant="caption" color="white">
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
            <div>
      {/* Render your data here */}
      {/* <Pagination count={10} page={page} onChange={handleChange} /> */}
    </div>
            {/* <button onClick={handlePrev}>Previous</button>
      <button onClick={handleNext}>Next</button> */}
          </Box>
        </Container>
      </Box>

  

    </>
  );
}