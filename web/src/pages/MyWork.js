import React, { useEffect, useState, useCallback, createRef } from 'react'
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Typography from '@mui/material/Typography';
import { Button, CardActionArea, Container, Dialog, DialogContent, DialogTitle, FormControl, Grid, LinearProgress, MenuItem, Rating, Select, Slider, TextField } from '@mui/material';
import { styled } from '@mui/material/styles';
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import '../wwwroot/css/Global.css';
import { MyOrderList, ViewALLMywork, ViewAllService } from '../API';
import AccessTimeIcon from '@mui/icons-material/AccessTime';
import ArrowRightIcon from '@mui/icons-material/ArrowRight';
import { useNavigate } from 'react-router-dom';

export default function Mywork() {
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
    ViewALLMywork(userToken)  
      .then(result => {
        if (result && result.data && result.data.result) {
          setIsLoading(false);
          setData(result.data.result);
        } else {
          setIsLoading(false);
        }
      })
      .catch(error => {
        console.error('Error Fetching Data:', error); 
        setIsLoading(false);
      });
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
      {isLoading ? (
        <LinearProgress sx={{ '.MuiLinearProgress-bar': { backgroundColor: '#FAFF00' }, marginBottom: '20px' }}/>
      ) : (
        <Box sx={{ p: 2 ,paddingTop:'5%'}}>
           <Container fixed maxWidth='false' >
          <Box sx={{ padding: '0% 4%' }}>

          <Grid container spacing={3} >
          {data?.length > 0 ? (
            data.map((item, index) => {

              //border status color
              let borderColor;
             
             switch (item.stringStatus) {
                case 'Pending':
                  borderColor = '#FF7816';
                  break;
                case 'Inprogress':
                  borderColor = '#A9BC33';
                  break;
                case 'Success':
                  borderColor = '#319B9B';
                  break;
                case 'Cancel':
                  borderColor = '#AF2A2A';
                  break;
                case 'Fail':
                  borderColor = '#AF2A2A';
                  break;
                default:
                  borderColor = '#FAFF00';}

              let startDate = new Date(item.expected_start_date);
              let endDate = new Date(item.expected_end_date);
          
              // Array of month names
              let monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
          
              // Format the dates
              let formattedStartDate = `${startDate.getUTCDate()}-${monthNames[startDate.getUTCMonth()]}-${startDate.getUTCFullYear()}`;
              let formattedEndDate = `${endDate.getUTCDate()}-${monthNames[endDate.getUTCMonth()]}-${endDate.getUTCFullYear()}`;
              return (
                <Grid item xs={12} md={3} sm={6} ms={4} key={index}>
                  <Card sx={{
                    maxWidth: '90%',
                    // height: 310,
                    backgroundColor: '#0D0C22',
                    borderColor: borderColor,
                    borderRadius: '15px',
                    color: borderColor,
                    border: '2px solid',
                    m: 'auto',
                    mb: 2,
                    p: 1
                  }}>
                    <CardActionArea key={index} onClick={() => navigate(`service/detail/${item.id}`)}>
                      <Box sx={{display:'flex' ,justifyContent:'space-between',padding:'7px 7px 0px 7px'}}>
                        <Typography gutterBottom variant="h5" component="div">
                          {item.order_title}
                        </Typography> 
                        <Typography variant="caption" color={borderColor}>
                          {item.stringStatus}<AccessTimeIcon fontSize='small'/>
                        </Typography>
                      </Box>
                      <CardContent>
                        <Typography variant="body2" color="white" style={{
                          display: '-webkit-box',
                          WebkitLineClamp: 3,
                          WebkitBoxOrient: 'vertical',
                          overflow: 'hidden',
                          textOverflow: 'ellipsis',
                        }}>
                          {item.order_description}
                        </Typography>                
                        <Typography variant="caption" color="white" sx={{ pr: 4,pt:2 }}>
                          Orderby: {item.contact.name}
                        </Typography>
                        <Box sx={{ display: 'flex', alignItems: 'center' }}>
                          <Typography variant="body2" color="white">
                            {formattedStartDate}
                          </Typography>
                          <Typography variant="body2" color="white">
                          <ArrowRightIcon/> 
                          </Typography>
                          <Typography variant="body2" color="white">
                           {formattedEndDate}
                          </Typography>
                        </Box>
                      </CardContent>
                    </CardActionArea>
                  </Card>
                </Grid>
              );
            })
          ) : <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
                  
                <h2>No Order</h2>
              </div>}

        </Grid>
          </Box>
          </Container>

        </Box>

       
       
      )}

    </>
  );
}