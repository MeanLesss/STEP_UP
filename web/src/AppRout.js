import React from 'react'
import { Route, Routes } from 'react-router-dom'
import App from './App'
import Layout from './Layout'
import Service from './pages/Service'
import MyService from './pages/MyService'
import MyWork from './pages/MyWork'
import MyOrder from './pages/MyOrder'
import Header from './pages/Header'


export default function AppRout() {
    return (
        <>
         <Header />
        <Routes>
            <Route index path='/' element={<App />} />
            <Route path='/service' element={<Service />}/>
            <Route path='/myservice' element={<MyService />}/>
            <Route path='/mywork' element={<MyWork />}/>
            <Route path='/myorder' element={<MyOrder />}/>            
            <Route path='*' element={<h1>Not Found!</h1>}/>

        </Routes>
        </>
       
    )
}

