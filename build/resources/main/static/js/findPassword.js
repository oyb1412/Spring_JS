document.getElementById("find-password-form").addEventListener("submit", function(e){
    e.preventDefault();

    const formData = new FormData(e.target);

    const username = formData.get("username");
    const password = formData.get("password");
    const passwordConfirm = formData.get("passwordConfirm");

    const user ={
        username:username,
        password:password,
    }

    fetch(`/api/user/findPassword?passwordConfirm=${passwordConfirm}`,{
        method : "POST",
        headers :{
        "Content-Type" : "application/json",
        [window.csrfHeader] : window.csrfToken
        },
        body : JSON.stringify(user)
    })
        .then(ref => ref.json())
        .then(data => {
            if(data.error)
            {
                alert(data.error);
            }
            else
            {
                alert(data.success);
                location.href = "/"
            }
        })
        
})

document.getElementById("cancle-form").addEventListener("submit", function(e){
    e.preventDefault();

    location.href ="/";
})