fetch('api/resume')
    .then(ref => ref.json())
    .then(data => {
        const content = document.getElementById("resume-content");
        content.innerHTML = data.resume.content;
    })