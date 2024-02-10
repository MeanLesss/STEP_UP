import * as React from 'react';
import { useTheme} from '@mui/material/styles';
import Box from '@mui/material/Box';
import MobileStepper from '@mui/material/MobileStepper';
import Paper from '@mui/material/Paper';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import defaultImage from '../wwwroot/images/LogoYellow.png'
import { CircularProgress, Dialog, DialogContent, Divider, FormControlLabel, Rating, responsiveFontSizes } from '@mui/material';
import { Grid, Card, CardActionArea, CardContent, CardMedia, Container, LinearProgress,createMuiTheme , TextField } from '@mui/material';
import { useParams } from 'react-router-dom';
import StarBorderIcon from '@mui/icons-material/StarBorder';
import KeyboardBackspaceIcon from '@mui/icons-material/KeyboardBackspace';
import HighlightOffIcon from '@mui/icons-material/HighlightOff';
import CheckCircleOutlineIcon from '@mui/icons-material/CheckCircleOutline';
import Swal from 'sweetalert2';
import Popup from './components/PopUp';
import { styled } from '@mui/system';
import { withStyles ,makeStyles  } from '@mui/styles';
import { useNavigate } from 'react-router-dom';
import Stepper from '@mui/material/Stepper';
import Step from '@mui/material/Step';
import StepLabel from '@mui/material/StepLabel';
import Checkbox from '@mui/material/Checkbox';
import { GetAgreement } from '../API';
import { Token } from '@mui/icons-material';

const useStyles = makeStyles({
    stepLabel: {
      color: 'red', // Set your desired color
      fontSize: '20px', // Set your desired font size
    },
  });

function BuyService() {
    const theme = useTheme();
    const [data, setData] = React.useState([]);
    const [agreement, setAgreement] = React.useState([]);
    const [isLoading, setIsLoading] = React.useState(true);
    const [openPopup, setOpenPopup] = React.useState(false);
    const [startDate, setStartDate] = React.useState(null);
    const [endDate, setEndDate] = React.useState(null);
    const [description, setDescription] = React.useState('');
    const [title, setTitle] = React.useState('');
    const [files, setFiles] = React.useState([]);
    const titleref = React.createRef();
    const [open, setOpen] = React.useState(false);
    const navigate = useNavigate();
    const userToken = sessionStorage.getItem('user_token');
    const { ID } = useParams();
    const [activeStep, setActiveStep] = React.useState(0);
    const classes = useStyles();
    const [agreementChecked, setAgreementChecked] = React.useState(false);
    const [summary, setSummary] = React.useState('');
    const steps = ['Service Detail', 'Agreement Service','Order Description','Order Summary'];
    React.useEffect(() => {
        const fetchData = async () => {
          var myHeaders = new Headers();
          myHeaders.append("X-CSRF-TOKEN", "");
          myHeaders.append("Pragma", "no-cache");
          myHeaders.append("Cache-Control", "no-cache");
          myHeaders.append("Content-Type", "application/json");
          myHeaders.append("Authorization", "Bearer "+userToken);
      
          var requestOptions = {
            method: 'GET',
            headers: myHeaders,
            redirect: 'follow'
          };
      
          try {
            const response = await fetch(`/api/service/${ID}/view`, requestOptions);
            const result = await response.json();
            setData(result.data.result);
            console.log(result);
      
            const serviceResult = await GetAgreement(userToken);
            if (serviceResult && serviceResult.msg) {
                setAgreement(serviceResult.msg);
            }
          } catch (error) {
            console.log('error', error);
          }
        };
        const getOrderSummary = async () => {
            var myHeaders = new Headers();
            myHeaders.append("X-CSRF-TOKEN", "");
            myHeaders.append("Content-Type", "application/json");
            myHeaders.append("Authorization", "Bearer "+userToken);
        
            var raw = JSON.stringify({
              "service_id": ID,
              "isAgreementAgreed": "1"
            });
        
            var requestOptions = {
              method: 'POST',
              headers: myHeaders,
              body: raw,
              redirect: 'follow'
            };
        
            try {
              const response = await fetch("/api/service/purchase-summary", requestOptions);
              const result = await response.json();
              if(result.status==="success"){
                setSummary(result.data.result);
              }
              console.log(result);
            } catch (error) {
              console.log('error', error);
            }
          }
        
        fetchData();
        getOrderSummary();
    }, [userToken]);
    //get order summary
   
    const handleOrder=async ()=>{
        setIsLoading(true);
        setOpen(true);
        var myHeaders = new Headers();
        myHeaders.append("X-CSRF-TOKEN", "");
        myHeaders.append("Authorization", "Bearer "+userToken);
        
        var formdata = new FormData();
        formdata.append("service_id", ID);
        formdata.append("order_title", title);
        formdata.append("order_description", description);
        Array.from(files).forEach((file, index) => {
            formdata.append(`attachment_files[${index}]`, file);
          });
        formdata.append("expected_start_date", startDate);
        formdata.append("expected_end_date", endDate);
        formdata.append("isAgreementAgreed", "1");
        
        var requestOptions = {
          method: 'POST',
          headers: myHeaders,
          body: formdata,
          redirect: 'follow'
        };
        
        fetch("/api/service/confirm-purchase", requestOptions)
          .then(response => response.json())
          .then(result =>{
            if(result.status==="success"){
                setIsLoading(false);
                setOpen(false);
                Swal.fire({
                    icon: result.status,
                    title:  result.status,
                    text: result.msg,
                  }).then(() => {
                    setTimeout(() => {
                      navigate('/myorder'); 
                    }, 3000); 
                  });
                
            }
            else{
                setIsLoading(false);
                setOpen(false);
                Swal.fire({
                    icon: 'error',
                    title:  'Ooop...',
                    text: result.msg,
                });
            }
            console.log(result)
          })
          .catch(error => console.log('error', error));
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
    const handleNext = () => {
        setActiveStep((prevActiveStep) => prevActiveStep + 1);
      };
      
    const handleBack = () => {
    setActiveStep((prevActiveStep) => prevActiveStep - 1);
    };
    const handleFinish = () => {
    // Your logic here
    console.log('Finished!');
    };
    const YellowCheckbox = withStyles({
    root: {
        color: '#FAFF00 !important',
        '&$checked': {
        color: '#FAFF00 !important',
        },
    },
    checked: {},
    })((props) => <Checkbox color="default" {...props} />);
   

    const handleCheckboxChange = (event) => {
      setAgreementChecked(event.target.checked);
    };
    ///Fomart price 
    // let price = parseFloat(summary.price.replace('$', ''));
    // let taxAmount = parseFloat(summary.taxAmount.replace('$', ''));
    // let totalPrice = parseFloat(summary.totalPrice.replace('$', ''));
    
    // if (!isNaN(price)) {
    //   price = price.toFixed(2);
    // } else {
    //   console.log('summary.price is not a number');
    // }
    
    // if (!isNaN(taxAmount)) {
    //   taxAmount = taxAmount.toFixed(2);
    // } else {
    //   console.log('summary.taxAmount is not a number');
    // }
    
    // if (!isNaN(totalPrice)) {
    //   totalPrice = totalPrice.toFixed(2);
    // } else {
    //   console.log('summary.totalPrice is not a number');
    // }
    
 return (
    <>
   
   <Box sx={{ p: 2 ,pt:5}}>
           <Container fixed maxWidth='false' >
          
          <Box sx={{ padding: '0% 4%' }} >       
          <Stepper activeStep={activeStep} alternativeLabel>
            {steps.map((label) => (
                <Step key={label}>
                <StepLabel><Typography  variant="h6" color="white">{label}</Typography></StepLabel>
                </Step>
            ))}
            </Stepper>
        {activeStep === 0 && (
       
       <Grid container spacing={3} >          
       <Grid item xs={12} md={12} sm={12} ms={12} >     
                
       <Card elevation={0} sx={{
        maxWidth: '75%',
        backgroundColor: '#0D0C22',
        color: '#FAFF00',
        m: 'auto',
        mb: 2,
        p: 4
      }}>
        <Typography gutterBottom variant="h6" component="div" pt={4}>
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

       
      </Card>
       </Grid>
      
          
  </Grid>
        )}
        {activeStep === 1 && (
        // Your code for Agreement Service (Step 2)
        <CardContent sx={{ flex: '1 0 auto',p: 4 }}>
       

       <Box sx={{ pb: '2%', display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center'}}>
        <Typography variant="h4" color="#FAFF00" sx={{ pr: "5%" }} align='center'>
            Terms and Agreement
        </Typography>
        <Box sx={{width:'850px',display:'flex', justifyContent: 'center', alignItems: 'center',}}>

            <Typography variant="h6" sx={{ pr: "5%" }} dangerouslySetInnerHTML={{ __html: agreement.replace(/\n/g, '<br />') }} align='center'/>
        </Box>
        <FormControlLabel
            control={<YellowCheckbox checked={agreementChecked} onChange={handleCheckboxChange} />}
            label="I have read and agreed to Term and Agreement"
        />
        </Box>

        

      </CardContent>

        )}
          {activeStep === 2 && (
        // Your code for Agreement Service (Step 2)
        <CardContent sx={{ flex: '1 0 auto' }}>
       

      
    <Grid container spacing={2} p={3} sx={{ maxHeight: '100vh', overflowY: 'auto'}} px={15}>
      <Grid item xs={12} sm={6}>
      <Typography sx={{color:'#faff00',paddingTop:'30px'}}>Title</Typography>
      <TextField
        hiddenLabel
        variant="outlined"
        fullWidth
        margin="normal"
        height='40px'
        value={title}
        onChange={(event) => setTitle(event.target.value)}
        required
        sx={styleTextField}
        />

      <Typography sx={{color:'#faff00'}}>Expected Start Date</Typography>
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
      </Grid>
      <Grid item xs={12} sm={6}>
        <Typography sx={{color:'#faff00',paddingTop:'30px'}}>Attachment(Optional)</Typography>
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
        <Typography sx={{color:'#faff00'}}>Expected End Date</Typography>
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
      </Grid>
      <Grid item xs={12} sm={12}>
      <Typography sx={{color:'#faff00'}}>Description</Typography>
      <TextField
        id="description"
        variant="outlined"
        fullWidth
        margin="normal"
        hiddenLabel
        multiline
        rows={3}
        required
        sx={styleTextField}
        onChange={(event) => setDescription(event.target.value)}
        />

      </Grid>
      
      
    </Grid>
   


      </CardContent>

        )}
        {activeStep === 3 && (
        // Your code for Agreement Service (Step 2)
        <CardContent sx={{ flex: '1 0 auto' }}>
       
       <Card elevation={0} sx={{
           
             backgroundColor: '#0D0C22',             
             mt:7,mx:15
         }}>
              <Typography gutterBottom variant="h5" component="div" sx={{color:'#faff00'}}>
             Summary
             </Typography> 
             <Typography gutterBottom  component="div" sx={{color:'white'}}>
            Title : {title && title? title: ''}
            </Typography> 
            <Typography gutterBottom  component="div" sx={{color:'white'}}>
            Description: {description}
            </Typography>
            <Typography gutterBottom  component="div" sx={{color:'white'}}>
            Attachment: 
            <div>
                {Array.from(files).map((file, index) => (
                    <p key={index}>{file.name}</p>
                ))}
            </div>
            </Typography>
         </Card>
      
       <Card elevation={0} sx={{
             
             backgroundColor: '#0D0C22',
             color: '#7E88DE',
             borderRadius: '15px',
             border: '2px solid',
             padding:4,
             mt:7,mx:15
         }}>
              <Typography gutterBottom  component="div" sx={{color:'#faff00'}}>
              {summary.tax}
             </Typography>     
             <Divider sx={{my:2,color:'#FAFF00',background:'grey'}}/>
              <Grid container spacing={1}>
                 <Grid item xs={12} sm={6}>
                                  
                     <Typography sx={{color:'white',pl:5}} >Start Date:</Typography>                   
                     <Typography sx={{color:'white',pl:5}}>End Date:</Typography>                 
                     <Typography sx={{color:'white',pl:5}}>Price:</Typography>                   
                     <Typography sx={{color:'white',pl:5}}>VAT:</Typography>              
                     <Typography sx={{color:'#FAFF00',pl:5}} variant='h6'>Total:</Typography>                     
                 </Grid>     
                 <Grid item xs={12} sm={6}>
                    <Typography sx={{color:'white',pl:5}} >{startDate}</Typography>                   
                    <Typography sx={{color:'white',pl:5}}>{endDate}</Typography>                 
                    <Typography sx={{color:'white',pl:5}}>{summary.price}</Typography>                   
                    <Typography sx={{color:'white',pl:5}}>{summary.taxAmount}</Typography>              
                    <Typography sx={{color:'#FAFF00',pl:5}} variant='h6'>{summary.totalPrice}</Typography>                     
                </Grid>               
              </Grid>
             
         </Card>
      </CardContent>

        )}

        <Box sx={{ display: 'flex', justifyContent: 'space-between' ,pt:5,px:15}}>
            {activeStep > 0 && (
                <Button onClick={handleBack} variant="outlined"  sx={{borderColor: '#7E88DE',width:'200px',borderRadius:'9px',color:'#FAFF00'}}>
                    Back
                </Button>
                )}

                {activeStep < steps.length - 1 && (
                <Button variant="contained" color="primary" onClick={handleNext} disabled={activeStep === 1 && !agreementChecked} 
                sx={{width:'200px',borderRadius:'9px',background:'#FAFF00',color:'black'}}>
                    Next
                </Button>
                )}

                {activeStep === steps.length - 1 && (
                <Button variant="contained" color="primary" onClick={()=>handleOrder()} sx={{width:'200px',borderRadius:'9px',background:'#FAFF00',color:'black'}}>
                    Place Order
                </Button>
                )}
        </Box>
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
                  <Button variant="outlined"  sx={{width:'100px',borderRadius:'9px',borderColor: '#FAFF00',color:'#FAFF00'}}>
                      Cancel
                  </Button>
                  <Button variant="contained" sx={{width:'100px',borderRadius:'9px',background:'#FAFF00',color:'black'}}>
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

export default BuyService;
