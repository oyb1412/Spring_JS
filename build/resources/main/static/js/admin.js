const userSection = document.getElementById("user-section");


fetch("/api/admin/data")
    .then(ref => ref.json())
    .then(data =>{
        //유저데이터 가져와서 돌리기
        userSection.innerHTML = "";
        data.userList.forEach(user => {
            let html = "";
            let tr = document.createElement("tr");
            tr.style = "cursor:pointer";
            html += `
                <td>${user.idx}</td>
                <td>${user.username}</td>
                <td>${user.writer}</td>
                <td>${user.indate}</td>
            `

            if(user.ban)
            {
                html += `
                    <td>O</td>
                `
            }
            else
            {
                html += `
                    <td>X</td>
                `
            }

            html += `
            <td>
                <form class="ban-form" method="post">
                    <input type="hidden" id="idx" name="idx" value="${user.idx}">
                    <select name="ban">
                        <option value="false" ${!user.ban ? 'selected' : ''}>밴 해제</option>
                        <option value="true" ${user.ban ? 'selected' : ''}>밴</option>
                    </select>
                 </form>
            </td>
            `
            tr.innerHTML = html;
            userSection.appendChild(tr);
        });

        const banFormList = document.querySelectorAll(".ban-form");

    banFormList.forEach(form =>{
    const select = form.querySelector("select"); 

    select.addEventListener("change", function(e)
    {
        e.preventDefault();
        const form = this.closest("form")
        const formData = new FormData(form);
        console.log("1");
        const userIdx = formData.get("idx");
        const banValue = this.value;

        fetch(`/api/admin/ban?idx=${userIdx}&ban=${banValue}`, {
            method : "POST",
            headers : {
                [window.csrfHeader] : window.csrfToken
            },
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
            }
        })
    })
})
    })

