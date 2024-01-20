import React, { useEffect } from 'react'
import { Outlet, useLocation, useNavigate } from 'react-router-dom'

export default function Layout() {

//   const navigate = useNavigate()
//     const user = localStorage.getItem('user')
//     const location = useLocation()
//   useEffect(() => {
//     if(user == null) return navigate('/',{replace:true})
//     //prevent switch role by url
//     const userObj = JSON.parse(user)
//     if(location.pathname.split('/')[1] !==userObj.role.toLowerCase()){
//             navigate('/'+userObj.role.toLowerCase())
//         }
//     return () => {}
//   }, [])


  return (
    <div> 
        <br/>
        <Outlet/>
    </div>
  )
}
