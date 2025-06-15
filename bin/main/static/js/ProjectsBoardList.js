fetch("api/projects/list")
    .then(ref => ref.json())
    .then(data => {
        if(data.projectsList)
        {
            const projects = data.projectsList;
            let tbody = document.getElementById("list-section");
            tbody.innerHTML = "";
            projects.forEach(project => {
                const tr = document.createElement("tr");
                tr.style = "cursor:pointer";
                tr.onclick = () => {
                    location.href = `/projects-check-page?idx=${project.idx}`;
                }

                tr.innerHTML = `
                    <td>${project.thumbnail}</td>
                    <td>${project.title}</td>
                    <td>${project.introduce}</td>
                    <td>${project.indate}</td>
                `

                tbody.appendChild(tr);
            });
        }
    })