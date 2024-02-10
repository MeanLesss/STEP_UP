import * as React from 'react';
import { useTheme } from '@mui/material/styles';
import Box from '@mui/material/Box';
import MobileStepper from '@mui/material/MobileStepper';
import Paper from '@mui/material/Paper';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import defaultImage from '../wwwroot/images/LogoYellow.png'
import { CircularProgress, Dialog, DialogContent, Divider, Rating, responsiveFontSizes } from '@mui/material';
import { Grid, Card, CardActionArea, CardContent, CardMedia, Container, LinearProgress,createMuiTheme , TextField } from '@mui/material';
import { useParams } from 'react-router-dom';
import StarBorderIcon from '@mui/icons-material/StarBorder';
import KeyboardBackspaceIcon from '@mui/icons-material/KeyboardBackspace';
import HighlightOffIcon from '@mui/icons-material/HighlightOff';
import CheckCircleOutlineIcon from '@mui/icons-material/CheckCircleOutline';
import Swal from 'sweetalert2';
import Popup from './components/PopUp';
import { styled } from '@mui/system';
import { useNavigate } from 'react-router-dom';


function ServiceDetail() {
    const theme = useTheme();
    const [data, setData] = React.useState([]);
    const [isLoading, setIsLoading] = React.useState(true);
    const [openPopup, setOpenPopup] = React.useState(false);
    const [startDate, setStartDate] = React.useState(null);
    const [endDate, setEndDate] = React.useState(null);
    const [open, setOpen] = React.useState(false);
    const navigate = useNavigate();
    const userToken = sessionStorage.getItem('user_token');
    const { serviceID } = useParams();
    React.useEffect(() => {
      const Service= async () => {
        try {          
          var myHeaders = new Headers();
          myHeaders.append("X-CSRF-TOKEN", "");
          myHeaders.append("Pragma", "no-cache");
          myHeaders.append("Cache-Control", "no-cache");
          myHeaders.append("Authorization", "Bearer " + userToken);
  
          var requestOptions = {
            method: 'GET',
            headers: myHeaders,
            redirect: 'follow'
          };
          fetch(`/api/service/${serviceID}/view`, requestOptions)
            .then(response => response.json())
            .then(result => {
                setData(result.data.result);               
              console.log(result)
            })
            .catch(error => console.log('error', error));
        } catch (error) {
          console.error(error);
        }
      };
  
      Service();
    }, [userToken]); 

    const handleActivate=()=>{
      var myHeaders = new Headers();
      myHeaders.append("X-CSRF-TOKEN", "");
      myHeaders.append("Pragma", "no-cache");
      myHeaders.append("Cache-Control", "no-cache");
      myHeaders.append("Content-Type", "application/json");
      myHeaders.append("Authorization", "Bearer "+userToken);
      
      var raw = JSON.stringify({
        "service_id": data.id,
        "is_active": "true",
        "start_date": startDate,
        "end_date": endDate
      });
      
      var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: raw,
        redirect: 'follow'
      };
     
      fetch("/api/service/activation", requestOptions)
      .then(response => response.json())
      .then(result =>{
        if(result.status==='success'){
          Swal.fire({
            icon: result.status,
            title:  result.status,
            text: result.msg,
          });
          window.location.reload();
          console.log(result);
        }
        else{
          Swal.fire({
            icon: 'error',
            title:  'Ooop...',
            text: 'Please contact admin',
          });
          window.location.reload();
        }
        console.log(result)
      } )
      .catch(error => console.log('error', error));
                
      
    }
   const handleDiactivate=()=>{
      var myHeaders = new Headers();
      myHeaders.append("X-CSRF-TOKEN", "");
      myHeaders.append("Pragma", "no-cache");
      myHeaders.append("Cache-Control", "no-cache");
      myHeaders.append("Content-Type", "application/json");
      myHeaders.append("Authorization", "Bearer "+userToken);
      
      var raw = JSON.stringify({
        "service_id": data.id,
        "is_active": false
      });
      
      var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: raw,
        redirect: 'follow'
      };
      Swal.fire({
        title: 'Are you sure?',
        text: "Are you you want to deactivate?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Confirm'
      }).then((result) => {
        if (result.isConfirmed) {
          setIsLoading(true);
            setOpen(true);
          fetch("/api/service/activation", requestOptions)
          .then(response => response.json())
          .then(result =>{
            if(result.status==='success'){
              setIsLoading(false);
            setOpen(false);
              Swal.fire({
                icon: result.status,
                title:  result.status,
                text: result.msg,
              });
            }
            else{
              setIsLoading(false);
              setOpen(false);
              Swal.fire({
                icon: 'error',
                title:  'Ooop...',
                text: 'Please contact admin',
              });
            }
            window.location.reload();
          })
          .catch(error => console.log('error', error));
          
        }
      })
     
    }
    
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
    const style = {
      width: 350,
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      border: 1,
      borderColor: '#7E88DE',
      background: '#0D0C22',
      color: 'white',
    
      
    };
    const TransparentPaper = styled(Paper)(({ theme }) => ({
      backgroundColor: 'transparent',
    }));
 return (
    <>
   
   <Box sx={{ p: 2 ,pt:5}}>
           <Container fixed maxWidth='false' >
          
          <Box sx={{ padding: '0% 4%' }} >       
         
          <Grid container spacing={3} >
          
                <Grid item xs={12} md={12} sm={12} ms={12} >              
              <Box sx={{display:'flex',justifyContent:'space-between' }} >
                <Typography  variant='h4'>Service Details</Typography>
                <Button variant="outlined" startIcon={<KeyboardBackspaceIcon />} onClick={() => navigate(`/myservice`)}
                sx={{width:'100px',borderRadius:'16px',borderColor: '#FAFF00',color:'white'}}>
                    Back
                  </Button>                
              </Box>
              <Divider sx={{my:3,color:'#FAFF00',background:'grey'}}/>
                            <Card elevation={0} sx={{
              maxWidth: '90%',
              backgroundColor: '#0D0C22',
              color: '#FAFF00',
              m: 'auto',
              mb: 2,
              p: 1
            }}>
              <Typography gutterBottom variant="h5" component="div" >
                {data.title}
              </Typography>    
              <CardActionArea sx={{borderColor:"#FAFF00",border:'2px'}}>
                <CardContent>
                  {/* Display the first attachment */}
                  {data && data.attachments && Object.keys(data.attachments).length > 0 ? (
                    <CardMedia
                      component="img"
                      height="300"
                      image={Object.values(data.attachments)[0]} // Select the first attachment
                      alt={data.title}
                      sx={{ borderRadius: "10px", objectFit: "cover" }}
                    />
                  ) : (
                    <CardMedia
                      component="img"
                      height="300"
                      image={defaultImage}
                      alt="default"
                      sx={{ borderRadius: "10px", objectFit: "cover" }}
                    />
                  )}


                </CardContent>
              </CardActionArea>
              <Box pt={2}>
              
              <Rating 
                  name="read-only" 
                  value={data.service_rate} 
                  readOnly 
                  size='small'
                  emptyIcon={<StarBorderIcon style={{ color: 'white' }} size='small' />} 
                />

                <Box sx={{display:'flex',py:1}}>
                  <Typography variant="body" color="#FAFF00" sx={{ pr: 4 }}>
                    Service Type: {data.service_type}
                  </Typography>         
                  <Typography variant="body" color="#FAFF00" sx={{ pr: 4 }}>
                    Price: ${data.price}
                  </Typography>            
                </Box>                   
                <Typography variant="body" color="white" sx={{ pr: 4 }}>
                 <span style={{color:'#FAFF00'}}>Description:</span> {data.description}
                </Typography>      
              </Box>
              <Box sx={{display:'flex',justifyContent:'center',mt:4}}>
              
              {data.stringStatus !== "Pending" && (
                  <>
                    {data.status === 2 && (
                      <Button variant="contained" startIcon={<CheckCircleOutlineIcon />} 
                        sx={{borderRadius:'16px',borderColor: '#7CFC00'}} onClick={()=>{setOpenPopup(true);}}>
                        Activate Service
                      </Button>
                    )}
                    {data.status === 1 && (
                      <Button variant="outlined" startIcon={<HighlightOffIcon />} 
                        sx={{borderRadius:'16px',borderColor: '#AF2A2A',color:'#AF2A2A'}} onClick={()=>handleDiactivate()}>
                        Deactivate Service
                      </Button>
                    )}
                  </>
                )}

                </Box>              
             
            </Card>
             </Grid>
            
                
        </Grid>
     
          </Box>
          </Container>

        </Box>
        {/* Service active */}
        <Popup
             
             openPopup={openPopup}
             setOpenPopup={setOpenPopup}
         ><Paper elevation={3} sx={style} >
            <Container >
              <Box p={3}>
             
                  <Typography component="div" variant="h6" textAlign={'center'}>Reactivate Service</Typography>
                  <Typography component="div" variant="caption" textAlign={'center'}>Please select start and end date</Typography>
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
                <div style={{display:'flex',justifyContent:'space-between',paddingTop:'12px'}}>
                  <Button variant="outlined"  onClick={()=>setOpenPopup(false)} sx={{width:'100px',borderRadius:'9px',borderColor: '#FAFF00',color:'#FAFF00'}}>
                      Cancel
                  </Button>
                  <Button variant="contained" onClick={()=>handleActivate()} sx={{width:'100px',borderRadius:'9px',background:'#FAFF00',color:'black'}}>
                      Confirm
                  </Button>
                </div>
                
              
              </Box>             
        </Container>
        </Paper>
             
         </Popup>
          {/* popuploainding */}
      <Dialog open={open}  fullWidth PaperComponent={TransparentPaper}>

      <DialogContent sx={{display:'flex', justifyContent:'center', alignContent:'center', alignItems:'center'}}>
        {isLoading && <CircularProgress size={24} />}
      </DialogContent>
    </Dialog>
    </>
  );
}

export default ServiceDetail;
