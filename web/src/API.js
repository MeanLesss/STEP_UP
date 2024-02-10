export const getUser = async(userToken) => {
  var myHeaders = new Headers();
  myHeaders.append("X-CSRF-TOKEN", "");
  myHeaders.append("Content-Type", "application/json");
  myHeaders.append("Authorization", "Bearer "+ userToken);

  var requestOptions = {
    method: 'GET',
    headers: myHeaders,
    redirect: 'follow'
  };

  return fetch(`/api/user`, requestOptions)
  .then(response => response.json())
  .then(result =>  {console.log(result); return result; })
  .catch(error => console.log('error', error));
}

export const GuestSignUp = async()=>{

  var myHeaders = new Headers();
  myHeaders.append("Content-Type", "application/json");

  var raw = JSON.stringify({
    "guest": true,
    "freelancer": false,
    "name": ""
  });

  var requestOptions = {
    method: 'POST',
    headers: myHeaders,
    body: raw,
    redirect: 'follow'
  };

  return fetch("/api/signup", requestOptions)
    .then(response => response.json())
    .then(result => {
      return result;
    })
    .catch(error => console.log('error', error));
}
export const ViewAllService = async (userToken,page) => { // Destructure userToken from the argument
  var myHeaders = new Headers();
  myHeaders.append("X-CSRF-TOKEN", "");
  myHeaders.append("Content-Type", "application/json");
  myHeaders.append("Authorization","Bearer "+ userToken); // Use userToken directly

  var raw = JSON.stringify({
    "service_type": "",
    "range": "10",
    "page": "1"
  });

  var requestOptions = {
    method: 'POST',
    headers: myHeaders,
    body: raw,
    redirect: 'follow'
  };

  return fetch(`/api/service/data`, requestOptions)
    .then(response => response.json())
    .then(result =>  {console.log(result); return result; })
    .catch(error => console.log('error', error));
}

export const Mywork = async ({ userToken }) => {
  var myHeaders = new Headers();
  myHeaders.append("Authorization", "Bearer "+userToken);
  
  var raw = "";
  
  var requestOptions = {
    method: 'GET',
    headers: myHeaders,
    body: raw,
    redirect: 'follow'
  };
  
  fetch("/api/service/ordered/freelancer/false", requestOptions)
    .then(response => response.text())
    .then(result => console.log(result))
    .catch(error => console.log('error', error));
}
//Myorder
export const MyOrderList = async(userToken) => {
  var myHeaders = new Headers();
  myHeaders.append("Authorization", "Bearer "+ userToken);

  var requestOptions = {
    method: 'GET',
    headers: myHeaders,
    redirect: 'follow'
  };

  return fetch("/api/service/ordered/freelancer/true", requestOptions)
    .then(response => response.json())
    .then(result => {
      console.log(result); // Debug line
      return result;
    })
    .catch(error => {
      console.error('Error Fetching Data:', error); // Debug line
      throw error;
    });
}
export const TopUp = async(userToken,amount)=>{
  var myHeaders = new Headers();
  myHeaders.append("Content-Type", "application/json");
  myHeaders.append("Authorization", "Bearer "+userToken);

  var raw = JSON.stringify({
    "balance": amount
  });

  var requestOptions = {
    method: 'POST',
    headers: myHeaders,
    body: raw,
    redirect: 'follow'
  };

  return fetch("/api/balance/top-up", requestOptions)
    .then(response => response.json())
    .then(result =>{ return result;})
    .catch(error => console.log('error', error));

}



export const ViewServiceProduct = async()=>{}
export const GetSummaryProduct = async()=>{}

export const GetAgreement = async(userToken)=>{

  var myHeaders = new Headers();
  myHeaders.append("Content-Type", "application/json");
  myHeaders.append("Authorization", "Bearer "+ userToken);

  var requestOptions = {
    method: 'GET',
    headers: myHeaders,
    redirect: 'follow'
  };

  return fetch("/api/service/agreement", requestOptions)
    .then(response => response.json())
    .then(result => {
      return result;
    })
    .catch(error => console.log('error', error));
}
export const ViewAllMyService = async(userToken)=>{
  var myHeaders = new Headers();
  myHeaders.append("X-CSRF-TOKEN", "");
  myHeaders.append("Content-Type", "application/json");
  myHeaders.append("Authorization", "Bearer "+userToken);

  var requestOptions = {
    method: 'GET',
    headers: myHeaders,
    redirect: 'follow'
  };

  return fetch("/api/my-service/view", requestOptions)
    .then(response => response.json())
    .then(result => {   
      console.log(result);
      return result;
    })
    .catch(error => console.log('error', error));
}
export const ViewALLMywork= async(userToken)=>{

  var myHeaders = new Headers();
  myHeaders.append("Authorization", "Bearer "+userToken);

  var requestOptions = {
    method: 'GET',
    headers: myHeaders,
    redirect: 'follow'
  };

  return fetch("/api/service/ordered/freelancer/false", requestOptions)
    .then(response => response.json())
    .then(result =>{
      return result; 
      console.log(result);
    })
    .catch(error => console.log('error', error));
}
export const GetPurchaseConfirm = async()=>{}


