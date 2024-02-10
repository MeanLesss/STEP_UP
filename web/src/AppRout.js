import React from 'react'
import { Route, Routes } from 'react-router-dom'
import App from './App'
import Layout from './Layout'
import Service from './pages/Service'
import MyService from './pages/MyService'
import MyWork from './pages/MyWork'
import MyOrder from './pages/MyOrder'
import Header from './pages/Header'
import img from './wwwroot/images/step.jpg'
import Profile from './pages/Profile'
import ServiceDetail from './pages/ServiceDetail'
import MyworkDetail from './pages/MyworkDetail'
import MyOrderDetail  from './pages/MyOrderDetail'
import BuyingService from './pages/BuyingService'
import FreelancerSignUp from './pages/FreelancerSignUp'
import Popup from './pages/components/PopUp'
import { Box, Button, Typography } from '@mui/material'


export default function AppRout() {

     const role = sessionStorage.getItem('user_role');
     const [openRegister, setOpenRegister] = React.useState(false);
     const closePopup = (event, reason) => {
       if (reason === 'backdropClick') {
        setOpenRegister(false);
       }
     };
    return (
        <>
         <Header />
        <Routes>
        <Route path='/' element={<App />} />
        <Route path='/myservice' element={ role === '100' || role === 100 ? <MyService /> : 
            role === '101' || role === 101 ? (
                <>
               <Box display="flex" flexDirection="column" alignItems="center" justifyContent="center" height="100vh">
                    <Typography variant="h5" align="center">
                        Register as freelancer to use this feature
                    </Typography>
                    <Button sx={{ marginTop: "10px", background:"#7E88DE", borderRadius:"30px" }} variant="contained" onClick={()=>{setOpenRegister(true)}}>
                        Register
                    </Button>
               </Box>
                </>
            ) : 
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
                <img src={img} alt='logo'></img>
                <h2>Please sign up  to access these pages.</h2>
            </div>} 
                />
        <Route path='/profile' element={role === '100'|| role === '101'||role===101||role===100 ?<Profile />: 
                <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
                <img src={{img}} alt='logo'></img>
                <h2>Please sign up  to access these pages.</h2>
            </div>} 
                />
        <Route path='/mywork' element={
            role === '100' || role === 100 ? <MyWork /> : 
            role === '101' || role === 101 ? (
                <>
               <Box display="flex" flexDirection="column" alignItems="center" justifyContent="center" height="100vh">
                    <Typography variant="h5" align="center">
                        Register as freelancer to use this feature
                    </Typography>
                    <Button sx={{ marginTop: "10px", background:"#7E88DE", borderRadius:"30px" }} variant="contained" onClick={()=>{setOpenRegister(true)}}>
                        Register
                    </Button>
               </Box>
                </>
            ) : 
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
                <img src={img} alt='logo'></img>
                <h2>Please sign up  to access these pages.</h2>
            </div>
        } />

         <Route path='mywork/service/detail/:serviceID' element={role==='100'||role===100 ? <MyworkDetail/> : 
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
            <img src={{img}} alt='logo'></img>
            <h2>Please sign up  to access these pages.</h2>
            </div>
          } />
        <Route path='myservice/service/detail/:serviceID' element={role==='100'||role===100 ? <ServiceDetail/> : 
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
            <img src={{img}} alt='logo'></img>
            <h2>Please sign up  to access these pages.</h2>
        </div>
        } />
        <Route path='/service' element={<Service />} />
        <Route path='/myorder' element={role === '100'|| role === '101'||role===101||role===100 ? <MyOrder /> : 
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
            <img src={{img}} alt='logo'></img>
            <h2>Please sign up  to access these pages.</h2>
        </div>
        } />
        <Route path='/:serviceID' element={role==='100'||role===100 ? <MyOrderDetail/> : 
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
            <img src={{img}} alt='logo'></img>
            <h2>Please sign up to access these pages.</h2>
        </div>
        } />
         <Route path='/buyservice/:ID' element={role === '100'|| role === '101'||role===101||role===100 ? <BuyingService /> : 
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
            <img src={{img}} alt='logo'></img>
            <h2>Please sign up  to access these pages.</h2>
        </div>
       } />
        <Route path='*' element={<div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
            <img src={{img}} alt='logo'></img>
            <h1>NOT FOUND</h1>
        </div>}/>
        </Routes>
        <Popup
          title="Employee Form"
          openPopup={openRegister}
          setOpenPopup={setOpenRegister}
          closePopup={closePopup}
        >
          <FreelancerSignUp/>
        </Popup>
        </>
       
    )
}

{/* <Route path='/mywork' element={role === '101'||role===101 ? <MyWork /> :  */}