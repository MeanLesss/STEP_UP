import * as React from 'react';
import { useTheme } from '@mui/material/styles';
import Box from '@mui/material/Box';
import MobileStepper from '@mui/material/MobileStepper';
import Paper from '@mui/material/Paper';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import KeyboardArrowLeft from '@mui/icons-material/KeyboardArrowLeft';
import KeyboardArrowRight from '@mui/icons-material/KeyboardArrowRight';
import SwipeableViews from 'react-swipeable-views';
import { autoPlay } from 'react-swipeable-views-utils';
import './App.css';
import defaultImage from './wwwroot/images/LogoYellow.png'
import { responsiveFontSizes } from '@mui/material';


import { Grid, Card, CardActionArea, CardContent, CardMedia, Container, LinearProgress,createMuiTheme , TextField } from '@mui/material';
import { GuestSignUp, ViewAllService, getUser } from './API';

const AutoPlaySwipeableViews = autoPlay(SwipeableViews);
let themes = createMuiTheme();
themes = responsiveFontSizes(themes);

const ResponsiveTypography = (props) => {
  const theme = useTheme();

  return (
    <Typography 
      variant="h4" 
      component="div" 
      gutterBottom 
      sx={{ 
        [theme.breakpoints.down('sm')]: { fontSize: 'h6.fontSize' },
        [theme.breakpoints.down('md')]: { fontSize: 'h6.fontSize' },
        [theme.breakpoints.down('xs')]: { display: 'none' }  // Hide on xs screens
      }}
    >
      {props.children}
    </Typography>
  );
};


function App() {
  const theme = useTheme();
  const [activeStep, setActiveStep] = React.useState(0);
  const [data, setData] = React.useState([]);
  const [isLoading, setIsLoading] = React.useState(true);

  React.useEffect(() => {
  const signUpAndFetchUser = async () => {
    try {
      let userToken = sessionStorage.getItem('user_token');
      if (!userToken) {
        const signUpResult = await GuestSignUp();
        if (signUpResult.status === "success") {
          userToken = signUpResult.data.user_token;
          sessionStorage.setItem('user_token', userToken);
        }
      }

      const userResult = await getUser(userToken);
      if (userResult && userResult.data && userResult.data.user_info) {
        sessionStorage.setItem('user_role', userResult.data.user_info.role);
      }

      const serviceResult = await ViewAllService(userToken);
      if (serviceResult && serviceResult.data && serviceResult.data.result) {
        setIsLoading(false);
        setData(serviceResult.data.result.data);
      }
    } catch (error) {
      console.error(error);
    }
  };

  signUpAndFetchUser();
}, []);
  const maxSteps = Math.ceil(data.length / 4);

  const handleNext = () => {
    setActiveStep((prevActiveStep) => prevActiveStep + 1);
  };

  const handleBack = () => {
    setActiveStep((prevActiveStep) => prevActiveStep - 1);
  };

  const handleStepChange = (step) => {
    setActiveStep(step);
  };
  const uniqueServices = data.reduce((unique, item) => {
    return unique.includes(item.service_type) ? unique : [...unique, item.service_type];
  }, []);

  return (
<>
    <Box
    sx={{
      display: 'flex',
      flexDirection: 'column',
      justifyContent: 'center',
      alignItems: 'center',
      pt:'9em'
    }}
  >
      <ResponsiveTypography  >
      Find the right freelance service, right away
    </ResponsiveTypography>
    <TextField
      placeholder="Search for any service..."
      variant="outlined"
      sx={{ '& .MuiOutlinedInput-root': {
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
          color: '#FAFF00'        
        },
      },  padding: '1em', width: '85%',paddingBottom:'0' }}
    />
  

  </Box>
    <Box sx={{ p: 2,pt:4 }}>      
    <Container fixed maxWidth='false' > 
        <Box sx={{ padding: '0% 4%',paddingTop:'1%' }} > 
        <Box sx={{display:'flex', alignItems: 'center',pb:2}}>
          <ResponsiveTypography>
            <Typography variant='body2' pt={1} color={'#FAFF00'}> Our Service : </Typography>     
          </ResponsiveTypography>
          {uniqueServices.map((service, index) => {
            return (
              <Grid item xs={12} sm={12} spacing={2} key={index}>
                <Button 
                  variant="outlined"   
                  size="small"
                  sx={{
                    borderRadius:'15px',
                    borderColor: '#FAFF00',
                    color:'white',
                    mr:1,ml:2
                  }}
                >
                  {service}
                </Button>
              </Grid>
            )
          })}
        </Box>
        
          <AutoPlaySwipeableViews
            axis={theme.direction === 'rtl' ? 'x-reverse' : 'x'}
            index={activeStep}
            onChangeIndex={handleStepChange}
            enableMouseEvents
          >
            {Array.from({length: maxSteps}).map((_, step) => (
              <div key={step}>
                <Grid container spacing={3}>
                  {data.slice(step * 4, (step + 1) * 4).map((item, index) => (
                    <Grid item xs={12} md={3} sm={6} ms={4} key={index}>
                      <Card sx={{
                        maxWidth: '90%',
                        backgroundColor: '#0D0C22',
                        borderColor: '#FAFF00',
                        borderRadius: '15px',
                        color: '#FAFF00',
                        border: '2px solid',
                        m: 'auto',
                        mb: 4,
                        p: 1
                      }}>
                        <CardActionArea>
                          <Box sx={{display:'flex' ,justifyContent:'space-between',padding:'7px 7px 0px 7px'}}>
                            <Typography gutterBottom variant="h5" component="div">
                              {item.title}
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
                            <Typography variant="caption" color="white" sx={{ pr: 4 }}>
                              Service type: {item.service_type}
                            </Typography>                       
                          </CardContent>
                        </CardActionArea>
                      </Card>
                    </Grid>
                  ))}
                </Grid>
              </div>
            ))}
          </AutoPlaySwipeableViews>
          <MobileStepper
            steps={maxSteps}
            position="static"
            activeStep={activeStep}
            sx={{ bgcolor: '#0D0C22' , color: 'yellow'}} 
            nextButton={
              <Button
                size="small"
                onClick={handleNext}
                disabled={activeStep === maxSteps - 1}
                sx={{ color: 'yellow' }} 
              >
                Next
                {theme.direction === 'rtl' ? (
                  <KeyboardArrowLeft />
                ) : (
                  <KeyboardArrowRight />
                )}
              </Button>
            }
            backButton={
              <Button
                size="small"
                onClick={handleBack}
                disabled={activeStep === 0}
                sx={{ color: 'yellow' }} 
              >
                {theme.direction === 'rtl' ? (
                  <KeyboardArrowRight />
                ) : (
                  <KeyboardArrowLeft />
                )}
                Back
              </Button>
            }
          />
        </Box>
      </Container>
    </Box>
  
    </>
  );
}

export default App;
