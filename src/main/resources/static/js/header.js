window.csrfHeader = "";
window.csrfToken = "";
 
 //서버에 Get형식으로 json타입을 요청.
  fetch("/api/csrf-token", {
    method : "Get",
    headers : {
      "Content-Type" : "application/json"
    }
  })
  .then(res => res.json())
  .then(data => {
      window.csrfHeader = data.headerName;
      window.csrfToken = data.token;
  });