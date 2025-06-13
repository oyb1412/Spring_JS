fetch("/api/user/data")
        .then(ref => ref.json())
        .then(data => {
            if(data.error)
            {
                alert(data.error);
            }
            else if(data.success)
            {
                const username =  document.getElementById("username");
                const writer = document.getElementById("writer");
                username.value = data.user.username;
                writer.value = data.user.writer;
            }
        })

document.getElementById("userdata-modify-form").addEventListener("submit", function(e){
    e.preventDefault();

    const formData = new FormData(e.target);
    const username = formData.get("username");
    const writer = formData.get("writer");
    const password = formData.get("password");
    console.log(username);
    console.log(writer);
    console.log(password);

    const user ={
        username : username,
        password : password,
        writer : writer
    }

    fetch("api/userdata/modify", {
        method : "POST",
        headers : {
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
            else if(data.success)
            {
                alert(data.success);
                location.href ="/";
            }
        })
})

document.getElementById("cancel-form").addEventListener("submit", function(e){
    e.preventDefault();

    location.href = "/";
})