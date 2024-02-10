import React, { useState } from 'react';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import { CardActionArea, Container, Dialog, DialogContent, Divider, FormControl, Grid, LinearProgress, MenuItem, Rating, Select, TextField } from '@mui/material';
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import '../wwwroot/css/Global.css';
import { ViewAllMyService } from '../API';
import defaultImage from '../wwwroot/images/LogoYellow.png'
import StarBorderIcon from '@mui/icons-material/StarBorder';
import AccessTimeIcon from '@mui/icons-material/AccessTime';
import ArrowRightIcon from '@mui/icons-material/ArrowRight';
import { useNavigate } from 'react-router-dom';
import ServiceDetail from './ServiceDetail';
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';

export default function MyService() {

  const userToken = sessionStorage.getItem('user_token');
  const [isLoading, setIsLoading] = React.useState(false);
  const [data, setData] = React.useState(null);
  const [open, setOpen] = React.useState(false);
  const serviceType = ['Software Development', 'Graphic Design'];
  const [service, setservice] = useState('');
  const [startDate, setStartDate] = React.useState(null);
  const [endDate, setEndDate] = React.useState(null);
  const [files, setFiles] = React.useState([]);
  const titleref = React.createRef();
  const describeref = React.createRef();
  const priceref = React.createRef();
  const navigate = useNavigate();
  React.useEffect(() => {
    setIsLoading(true);
    ViewAllMyService(userToken) // Pass the user token      
      .then(result => {
        if (result && result.data && result.data.result) {
          setIsLoading(false);
          setData(result.data.result);
        }
      });// Set the data state variable
  }, [userToken]);

  const handleSubmit = (event) => {
    
      var myHeaders = new Headers();
      myHeaders.append("X-CSRF-TOKEN", "");
      myHeaders.append("Pragma", "no-cache");
      myHeaders.append("Cache-Control", "no-cache");
      myHeaders.append("Authorization", "Bearer "+userToken);
      
      var formdata = new FormData();
      formdata.append("title", titleref.current.value);
      formdata.append("description", describeref.current.value);
      Array.from(files).forEach((file, index) => {
        formdata.append(`attachment_files[${index}]`, file);
      });
      formdata.append("price", priceref.current.value);
      formdata.append("service_type", service);
      formdata.append("start_date",startDate);
      formdata.append("end_date", endDate);
      
      var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: formdata,
        redirect: 'follow'
      };
      
      fetch("/api/service/create", requestOptions)
        .then(response => response.json())
        .then(result => {
          // if(result.)
          console.log(result)
        })
        .catch(error => console.log('error', error));
  }

  // style
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
  const style = {
    width: 700,
    height:500,
    display: 'flex',
    justifyContent: 'center',
    borderLeft: 0,
    background: '#0D0C22',
    color: 'white',
    
  };

  // const server=(serviceID)=>{
  //   if(serviceID){
  //     <ServiceDetail serviceID={serviceID}/>
  //     navigate={.=}
  //   }
  // }
  return (
    <>
    
    {isLoading ? (
        <LinearProgress sx={{ '.MuiLinearProgress-bar': { backgroundColor: '#FAFF00' }, marginBottom: '20px' }}/>
      ) : (
        <Box sx={{ p: 2 ,paddingTop:'4%'}}>
           <Container fixed maxWidth='false' >
          <Box sx={{ padding: '0% 4%' }}>
          <Box sx={{display:'flex',justifyContent:'space-between' }} >
                <Typography  variant='h4'>My Service</Typography>
                <Button variant="contained" type="submit" startIcon={<AddCircleOutlineIcon/>} sx={{borderRadius:'9px',background:'#FAFF00',color:'black'}} onClick={()=>setOpen(true)}>
              AddService
            </Button>              
              </Box>
              <Divider sx={{my:3,color:'#FAFF00',background:'grey'}}/>
          

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
                default:
                  borderColor = '#FAFF00';}

              let startDate = new Date(item.start_date);
              let endDate = new Date(item.end_date);
          
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
                          {item.title}
                        </Typography> 
                        <Typography variant="caption" color={borderColor}>
                          {item.stringStatus}<AccessTimeIcon fontSize='small'/>
                        </Typography>
                      </Box>
                      <CardContent>
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
                        <Typography variant="body2" color="white" style={{
                          display: '-webkit-box',
                          WebkitLineClamp: 3,
                          WebkitBoxOrient: 'vertical',
                          overflow: 'hidden',
                          textOverflow: 'ellipsis',
                        }}>
                          {item.description}
                        </Typography>                
                        <Typography variant="caption" color="white" sx={{ pr: 4 }}>
                          Service type: {item.service_type}
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
          ) : <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center',alignContent:'center' }}>
                  
                <h2>No Service </h2>
              </div>}

        </Grid>
          </Box>
          </Container>

        </Box>

       
       
      )}

{/* Popup AddService */}
                
     <Dialog open={open}  fullWidth maxWidth="md" PaperProps={{
          style: {
            borderRadius: '15px',
            background: "#0D0C22",
            backgroundColor: 'transparent'
          },
        }}>
            <DialogContent>
              <Card elevation={3} sx={{ bgcolor: '#0D0C22',border:1,borderColor:'#7E88DE' ,p: '2% 5%', width:700, overflowY: 'auto' }}>
              <Paper elevation={0} sx={style} > 
                  <Container >                
                  <form onSubmit={handleSubmit}>
                  <Typography component="div" variant="h6" textAlign={'center'}>Create Service</Typography>
                  <Grid container spacing={2}>
                    <Grid item xs={12} sm={6}>
                    <Typography sx={{color:'#faff00',paddingTop:'30px'}}>Title</Typography>
                      <TextField
                        hiddenLabel
                        variant="outlined"
                        fullWidth
                        margin="normal"
                        height='40px'
                        inputRef={titleref}
                        required
                        sx={styleTextField}
                      />
                      <Typography sx={{color:'#faff00'}}>Price</Typography>
                      <TextField
                        hiddenLabel
                        variant="outlined"
                        fullWidth
                        margin="normal"
                        inputRef={priceref}
                        required
                        sx={styleTextField}
                      />
                      <Typography sx={{color:'#faff00'}}>Select A Service Type</Typography>
                      <FormControl  variant="outlined">
                      <Select
                          value={service}  
                        variant="outlined"
                        size="large"
                        sx={selectStyle}
                        onChange={(e) => setservice(e.target.value)}   
                      >
                        {serviceType.map((status, index) => (
                          <MenuItem value={status} key={index}>
                            {status}
                          </MenuItem>
                        ))}
                      </Select>
                      </FormControl>
                    
                    </Grid>
                    <Grid item xs={12} sm={6}>
                    <Typography sx={{color:'#faff00',paddingTop:'30px'}}>Start Date</Typography>
                    <TextField
                      hiddenLabel
                      variant="outlined"
                      fullWidth
                      margin="normal"
                      type="date"
                      required
                      sx={styleTextField}
                      InputLabelProps={{
                        shrink: true,
                      }}
                      onChange={(event) => setStartDate(event.target.value)} 
                    />
                     
                      <Typography sx={{color:'#faff00'}}>End Date</Typography>  
                    <TextField
                      hiddenLabel
                      variant="outlined"
                      fullWidth
                      margin="normal"
                      type="date"
                      required
                      sx={styleTextField}
                      InputLabelProps={{
                        shrink: true,
                      }}
                      onChange={(event) => setEndDate(event.target.value)} 
                    />
                    <Typography sx={{color:'#faff00'}}>Attachment(Optional)</Typography>
                    <TextField
                      hiddenLabel
                      variant="outlined"
                      fullWidth
                      margin="normal"
                      type="file"
                      sx={styleTextField}
                      InputProps={{ inputProps: { multiple: true } }}
                      onChange={(event) => setFiles(event.target.files)} 
                    />
                    </Grid>
                    <Grid item xs={12} sm={12}>
                    <Typography sx={{color:'#faff00'}}>Description</Typography>
                      <TextField
                        id="description"
                        variant="outlined"
                        fullWidth
                        inputRef={describeref}
                        margin="normal"
                        hiddenLabel
                        multiline
                        rows={3}
                        required
                        sx={styleTextField}
                      />
                    </Grid>
                    <Grid item xs={12} sm={12} sx={{display:'flex',justifyContent:'center',paddingTop:'20px'}} spacing={4}>
                   
                  </Grid>
                </Grid>
                <div style={{display:'flex',justifyContent:'center',padding:'12px',pb:'20px'}}>
                      <Button variant="outlined"  onClick={()=>setOpen(false)} sx={{width:'100px',borderRadius:'9px',borderColor: '#FAFF00',color:'#FAFF00',marginRight:'3%'}}>
                          Close
                      </Button>
                      <Button variant="contained" type="submit" sx={{width:'100px',borderRadius:'9px',background:'#FAFF00',color:'black'}}>
                          Create
                      </Button>
                    </div>
                </form>
                </Container>
              
                  </Paper>
            </Card>
          </DialogContent>
        </Dialog>    


    </>
   
  );
}
