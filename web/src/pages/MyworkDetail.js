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
import ArrowRightIcon from '@mui/icons-material/ArrowRight';


function MyworkDetail() {
    const theme = useTheme();
    const [data, setData] = React.useState([]);
    const [isLoading, setIsLoading] = React.useState(true);
    const [openPopup, setOpenPopup] = React.useState(false);
    const [openCompletePopup, setOpenCompletePopup] = React.useState(false);
    const [startDate, setStartDate] = React.useState(null);
    const [endDate, setEndDate] = React.useState(null);
    const [open, setOpen] = React.useState(false);
    const describeref = React.createRef();
    const navigate = useNavigate();
    const [files, setFiles] = React.useState([]);
    const userToken = sessionStorage.getItem('user_token');
    const { serviceID } = useParams();
    React.useEffect(() => {
      const Service= async () => {
        try {          

            var myHeaders = new Headers();
            myHeaders.append("X-CSRF-TOKEN", "");
            myHeaders.append("Content-Type", "application/json");
            myHeaders.append("Authorization", "Bearer " + userToken);
    
            var requestOptions = {
                method: 'GET',
                headers: myHeaders,
                redirect: 'follow'
            };
            fetch(`/api/order-service/${serviceID}/view/false`, requestOptions)
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

    const handleUpload=()=>{
      var myHeaders = new Headers();
      myHeaders.append("Authorization", "Bearer "+userToken);
      
      var formdata = new FormData();
        formdata.append("order_id", "78");
        formdata.append("service_id", "49");
        Array.from(files).forEach((file, index) => {
          formdata.append(`attachment_files[${index}]`, file);
        });

        var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: formdata,
        redirect: 'follow'
        };
     
      fetch("/api/freelancer/submit-work", requestOptions)
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
      })
      .catch(error => console.log('error', error));                 
    }

    const handleAction = (action) => {
        var myHeaders = new Headers();
        myHeaders.append("X-CSRF-TOKEN", "");
        myHeaders.append("Content-Type", "application/json");
        myHeaders.append("Authorization", "Bearer "+userToken);     
        var raw = JSON.stringify({
          "isAccept": action 
        });
        
        var requestOptions = {
          method: 'POST',
          headers: myHeaders,
          body: raw,
          redirect: 'follow'
        };
      
        Swal.fire({
          title: 'Are you sure?',
          text: `Are you sure you want to ${action==='true'?'accept':'decline'}?`,
          icon: 'warning',
          showCancelButton: true,
          confirmButtonColor: '#3085d6',
          cancelButtonColor: '#d33',
          confirmButtonText: 'Confirm'
        }).then((result) => {
          if (result.isConfirmed) {
            setIsLoading(true);
            setOpen(true);
            fetch(`/api/order-service/${data.id}/accept`, requestOptions)
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

    const handleCencel = () => {
        var myHeaders = new Headers();
        myHeaders.append("Content-Type", "application/json");
        myHeaders.append("Authorization", "Bearer "+userToken);         
        var raw = JSON.stringify({
            "order_id": data.id,
            "service_id": data.service_id,
            "cancel_desc": describeref.current.value
        });
        var requestOptions = {
            method: 'POST',
            headers: myHeaders,
            body: raw,
            redirect: 'follow'
        };    
        fetch("/api/freelancer/cancellationBeforeDueDate", requestOptions)
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
    //data fomart
    let stDate = new Date(data.expected_start_date);
    let edDate = new Date(data.expected_end_date);
    let accepted_at = new Date(data.accepted_at);

    // Array of month names
    let monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];

    // Format the dates
    let formattedStartDate = `${stDate.getUTCDate()}-${monthNames[stDate.getUTCMonth()]}-${stDate.getUTCFullYear()}`;
    let formattedEndDate = `${edDate.getUTCDate()}-${monthNames[edDate.getUTCMonth()]}-${edDate.getUTCFullYear()}`;
    let formattedaccepted = `${accepted_at.getUTCDate()}-${monthNames[accepted_at.getUTCMonth()]}-${accepted_at.getUTCFullYear()}`;
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
                <Typography  variant='h4'>Mywork Details</Typography>
                <Button variant="outlined" startIcon={<KeyboardBackspaceIcon />} onClick={() => navigate(`/mywork`)}
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
                         
              <Box pt={2}>     
                               
              <Typography gutterBottom variant="h5" component="div" >
                {data.order_title? data.order_title: 'No Title'}
                </Typography>   
                <Typography variant="body" color="white" sx={{ pr: 4}}>
                    <span style={{color:'#FAFF00'}}>Description:</span> {data.order_description ? data.order_description : 'No Description'}
                    </Typography>
     
              </Box>
              <Card elevation={0} sx={{
             
                backgroundColor: '#0D0C22',
                color: '#7E88DE',
                borderRadius: '15px',
                border: '2px solid',
                padding:4,
                mt:4
            }}>
                 <Typography gutterBottom variant="h6" component="div" sx={{color:'#faff00'}}>
                Summary
                </Typography>     
                <Divider sx={{my:2,color:'#FAFF00',background:'grey'}}/>
                 <Grid container spacing={1}>
                    <Grid item xs={12} sm={6}>
                        <Typography sx={{color:'white',pl:5}}>Status:</Typography>                   
                        <Typography sx={{color:'white',pl:5}}>Order by:</Typography>              
                        <Typography sx={{color:'white',pl:5}}>Email:</Typography>                  
                        <Typography sx={{color:'white',pl:5}}>Expected date:</Typography>                   
                        <Typography sx={{color:'white',pl:5}}>Price:</Typography>                 
                        <Typography sx={{color:'white',pl:5}}>Service Type:</Typography>
                        <Typography sx={{color:'white',pl:5}}>Accepted at:</Typography>                  
                    </Grid> 
                    <Grid item xs={12} sm={6}>
                        <Typography sx={{color:'#faff00'}}>{data.stringStatus}</Typography>                   
                        <Typography sx={{color:'#faff00'}}>
                        {data.contact ? data.contact.name : 'No Name'}
                        </Typography>
            
                        <Typography sx={{color:'#faff00'}}>
                        {data.contact ? data.contact.email : 'No Email'}
                        </Typography>
                
                        <Box sx={{ display: 'flex', alignItems: 'center' }}>
                          <Typography variant="body2" sx={{color:'#faff00'}} >
                            {formattedStartDate}
                          </Typography>
                          <Typography variant="body2" sx={{color:'#faff00'}}>
                          <ArrowRightIcon/> 
                          </Typography>
                          <Typography variant="body2" sx={{color:'#faff00'}} >
                           {formattedEndDate}
                          </Typography>
                        </Box>              
                        <Typography sx={{color:'#faff00'}}>${data.service?data.service.price:'0.00'}</Typography>                             
                        <Typography sx={{color:'#faff00'}}>{data.service_order}</Typography>
                        <Typography sx={{color:'#faff00'}}>{data.accepted_at?formattedaccepted:'Not Yet Accepted'}</Typography>                  
                    </Grid> 
                 </Grid>
                   
                 <Box sx={{display:'flex',justifyContent:'center',mt:4}}>
                  {data.stringStatus!=='Fail'&& data.stringStatus !== 'InReview' &&(
                    <>
                    {data.accepted_at === null && (
                    <>
                    <Button variant="outlined" startIcon={<HighlightOffIcon />} 
                        sx={{borderRadius:'16px',borderColor: '#AF2A2A',color:'#AF2A2A',mr:5}} 
                        onClick={() => handleAction('false')}>
                        Decline
                    </Button>
                    <Button variant="contained" startIcon={<CheckCircleOutlineIcon />} 
                        sx={{borderRadius:'16px',color:'black',borderColor: '#7CFC00',background:'#faff00'}} 
                        onClick={() => handleAction('true')}>
                        Accept
                    </Button>

                      
                    </>
                    )}
                    {data.accepted_at !== null && (
                    <>
                        <Button variant="outlined" startIcon={<HighlightOffIcon />} 
                        sx={{borderRadius:'16px',borderColor: '#AF2A2A',color:'#AF2A2A',mr:5}} onClick={()=>{setOpenPopup(true);}}>
                        Cancel
                        </Button>
                        <Button variant="contained" startIcon={<CheckCircleOutlineIcon />} 
                        sx={{borderRadius:'16px',borderColor: '#7CFC00'}} onClick={()=>{setOpenCompletePopup(true);}}>
                        Complete
                        </Button>
                    </>
                    )}

                    </>
                  )}  
                 
               </Box>           
            </Card>
                
             
            </Card>
             </Grid>
            
                
        </Grid>
     
          </Box>
          </Container>

        </Box>
        {/* Cancel work */}
        <Popup
             
             openPopup={openPopup}
             setOpenPopup={setOpenPopup}
         ><Paper elevation={3} sx={style} >
            <Container >
              <Box p={3}>
             
                  <Typography component="div" variant="h6" textAlign={'center'}>Are you sure?</Typography>              
                  <Typography sx={{color:'#faff00'}} variant="caption">Cancel Description</Typography>
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
                  <Typography component="div" variant="caption" color={'#AF2A2A'} >If you cancel before the expected end date, your creadit score will be
                  reduced by 5 points</Typography>              

                <div style={{display:'flex',justifyContent:'space-between',paddingTop:'12px'}}>
                  <Button variant="outlined"  onClick={()=>setOpenPopup(false)} sx={{width:'100px',borderRadius:'9px',borderColor: '#FAFF00',color:'#FAFF00'}}>
                      Cancel
                  </Button>
                  <Button variant="contained" onClick={()=>handleCencel()} sx={{width:'100px',borderRadius:'9px',background:'#FAFF00',color:'black'}}>
                      Confirm
                  </Button>
                </div>
                
              
              </Box>             
        </Container>
        </Paper>
             
         </Popup>
          {/* Complete work */}
        <Popup
             
             openPopup={openCompletePopup}
             setOpenPopup={setOpenCompletePopup}
         ><Paper elevation={3} sx={style} >
            <Container >
              <Box p={3}>
             
                  <Typography component="div" variant="h6" textAlign={'center'}>Please select  a file(zip/rar)</Typography>              
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
                <div style={{display:'flex',justifyContent:'space-between',paddingTop:'12px'}}>
                  <Button variant="outlined"  onClick={()=>setOpenCompletePopup(false)} sx={{width:'100px',borderRadius:'9px',borderColor: '#FAFF00',color:'#FAFF00'}}>
                      Close
                  </Button>
                  <Button variant="contained" onClick={()=>handleUpload()} sx={{width:'100px',borderRadius:'9px',background:'#FAFF00',color:'black'}}>
                      Upload
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

export default MyworkDetail;
