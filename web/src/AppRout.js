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


export default function AppRout() {
     const role = sessionStorage.getItem('role'); 
    return (
        <>
         <Header />
        <Routes>
        <Route path='/' element={<App />} />
        <Route path='/myservice' element={role === 'freelancer' ? <MyService /> : 
  
  <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
    <img src={img} alt='logo'></img>
  <h2>Please sign up as a freelancer to access these pages.</h2>
</div>} 
        />
        <Route path='/mywork' element={role === 'freelancer' ? <MyWork /> : 
         <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
         <img src={{img}} alt='logo'></img>
         <h2>Please sign up as a freelancer to access these pages.</h2>
     </div>
        } />
        <Route path='/service' element={<Service />} />
        <Route path='/myorder' element={role === 'client'|| role === 'freelancer' ? <MyOrder /> : 
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

