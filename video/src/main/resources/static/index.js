let videoList = []

// 将获取到的video标签逐个展示到<div id="container">中
function displayVideoCard() {
    videoList = api_getVideoList("math");
    //console.log("list:"+list)
    let container = document.getElementById("video-container");
    let titleContainer = document.getElementById("title-container");
    // for (const video of videoList) {
    if (videoList.length === 0) {
        let h2 = document.createElement("h2");
        h2.innerText = "No video found";
        container.appendChild(h2);
        return
    }
    const video = videoList[0];
    let title = video["title"];
    let url = video["url"];
    let h2 = document.createElement("h2");
    h2.innerText = title;
    h2.id = "video-title"
    let videoN = document.createElement("video");
    videoN.height = 422
    videoN.controls = true
    // videoN.preload = 'none'
    videoN.src = url
    videoN.id = "video-player"
    titleContainer.appendChild(h2);
    container.appendChild(videoN);
    //console.log(`${title} ${url}`)
    // }
    // if (container.firstElementChild != null) {
    //     container.firstElementChild.nextSibling.preload = 'metadata'
    // }
}

function displayVideoSections() {
    let container = document.getElementById("sections-container");
    for (const video of videoList) {

        let btn = document.createElement("button");
        btn.innerText = video["title"];
        btn.classList.add("video-sections-item");
        btn.onclick = () => {
            let h2 = document.getElementById("video-title");
            let videoN = document.getElementById("video-player");
            h2.innerText = video["title"];
            videoN.src = video["url"];
        }

        container.appendChild(btn);
    }
}



function api_getVideoList(subject) {
    let api = new XMLHttpRequest();
    api.open("GET", "http://47.97.29.190:8080/user/video/"+subject, false);
    api.send();
    console.log(api.responseText);
    return JSON.parse(api.responseText);
}

window.onload = () => {
    displayVideoCard()
    displayVideoSections()
}