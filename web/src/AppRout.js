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


export default function AppRout() {

     const role = sessionStorage.getItem('user_role');
    return (
        <>
         <Header />
        <Routes>
        <Route path='/' element={<App />} />
        <Route path='/myservice' element={role === '101' ? <MyService /> : 
  
  <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
    <img src={img} alt='logo'></img>
  <h2>Please sign up as a freelancer to access these pages.</h2>
</div>} 
        />
        <Route path='/profile' element={role === '100'|| role === '101' ? <Profile /> : 
  
  <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
    <img src={{img}} alt='logo'></img>
  <h2>Please sign up to access these pages.</h2>
</div>} 
        />
        <Route path='/mywork' element={role === '101' ? <MyWork /> : 
         <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
         <img src={{img}} alt='logo'></img>
         <h2>Please sign up as a freelancer to access these pages.</h2>
     </div>
        } />
        <Route path='/service' element={<Service />} />
        <Route path='/myorder' element={role === '100'|| role === '101' ? <MyOrder /> : 
        <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
        <img src={{img}} alt='logo'></img>
        <h2>Please sign up as a client to access these pages.</h2>
    </div>
       } />
        <Route path='*' element={<div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
        <img src={{img}} alt='logo'></img>
        <h1>NOT FOUND</h1>
    </div>}/>
        </Routes>
        </>
       
    )
}

