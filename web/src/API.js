export const ViewAllService = async () => {
  var myHeaders = new Headers();
  myHeaders.append("X-CSRF-TOKEN", "");
  myHeaders.append("Content-Type", "application/json");
  myHeaders.append("Authorization", "684|7lJzPuZ5RdbaIahgh7UXumrlunalmwBxAyJ30t66f9107451");
  myHeaders.append("Access-Control-Allow-Origin", "*");
  var formdata = new FormData();
  formdata.append("service_type", "");
  formdata.append("range", "10");
  formdata.append("page", "1");

  var requestOptions = {
    method: 'POST',
    mode: 'no-cors', // 'cors' by default
    headers: myHeaders,
    body: formdata,
    redirect: 'follow'
  };

  try {
      const response = await fetch("http://step-up-kh.duckdns.org:168/api/service/data", requestOptions);
      const result = await response.text();
      if(response.status==='success'){
        alert("res");
      }
      console.log(response);
      return result;
  } catch (error) {
      console.log('error', error);
  }
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


