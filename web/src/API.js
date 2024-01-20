

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


