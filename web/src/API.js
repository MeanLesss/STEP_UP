export const getUser = async(userToken) => {
  var myHeaders = new Headers();
  myHeaders.append("X-CSRF-TOKEN", "");
  myHeaders.append("Content-Type", "application/json");
  myHeaders.append("Authorization", userToken);

  var requestOptions = {
    method: 'GET',
    headers: myHeaders,
    redirect: 'follow'
  };

  return fetch("/api/user", requestOptions)
  .then(response => response.json())
  .then(result =>  {console.log(result); return result })
  .catch(error => console.log('error', error));
}


export const ViewAllService = async ({ userToken }) => { // Destructure userToken from the argument
  var myHeaders = new Headers();
  myHeaders.append("X-CSRF-TOKEN", "");
  myHeaders.append("Content-Type", "application/json");
  myHeaders.append("Authorization", userToken); // Use userToken directly

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

  return fetch("/api/service/data", requestOptions)
    .then(response => response.json())
    .then(result =>  {console.log(result); return result })
    .catch(error => console.log('error', error));
}


  
export const GetUserProduct = async()=>{}
export const ViewServiceProduct = async()=>{}
export const GetSummaryProduct = async()=>{}
export const GetPurchaseConfirm = async()=>{}
export const ShowAllOrderService = async()=>{}
export const ViewOrder = async()=>{}
export const ViewAllMyService = async()=>{}
// export const ViewAllService = async()=>{}

// export const GetSummary = async (summary) => {
//     var myHeaders = new Headers();
//     myHeaders.append("X-CSRF-TOKEN", "");
//     myHeaders.append("Authorization", "Bearer 1");
    
//     var formdata = new FormData();
//     formdata.append("service_id", "35");
//     formdata.append("order_title", "test 32432453");
//     formdata.append("order_description", "test");
//     formdata.append("attachment_files[]", fileInput.files[0], "/D:/IT STEP ACADEMY/Side Web Project/Scripting project/supProcess.py");
//     formdata.append("attachment_files[]", fileInput.files[0], "/D:/IT STEP ACADEMY/Side Web Project/Scripting project/supProcess.py");
//     formdata.append("expected_start_date", "2023-12-20");
//     formdata.append("expected_end_date", "20234-12-20");
//     formdata.append("isAgreementAgreed", "1");
    
//     var requestOptions = {
//       method: 'POST',
//       headers: myHeaders,
//       body: formdata,
//       redirect: 'follow'
//     };
    
//     fetch("http://192.168.1.18:8000/api/service/purchase-summary/", requestOptions)
//       .then(response => response.text())
//       .then(result => console.log(result))
//       .catch(error => console.log('error', error));

// }


