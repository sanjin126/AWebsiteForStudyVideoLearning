// 将获取到的video标签逐个展示到<div id="container">中
function displayVideoCard() {
    let list = api_getVideoList("math");
    //console.log("list:"+list)
    let container = document.getElementById("container");
    for (const video of list) {
        let title = video["title"];
        let url = video["url"];
        let h2 = document.createElement("h2");
        h2.innerText = title;
        let videoN = document.createElement("video");
        videoN.height = 422
        videoN.controls = true
        videoN.preload = 'none'
        videoN.src = url
        container.appendChild(h2);
        container.appendChild(videoN);
        //console.log(`${title} ${url}`)
    }
    if (container.firstElementChild != null) {
        container.firstElementChild.nextSibling.preload = 'metadata'
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
    displayVideoCard();
}