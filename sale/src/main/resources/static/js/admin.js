// admin js
const bList = [...document.querySelectorAll(".adminmenu button")];
bList.forEach((b) => {
    b.addEventListener("click", () => {
        activereset(b);
        b.classList = "active";
        listOn(b);
    });
});

function activereset() {
    bList.forEach((b) => {
        b.classList = "";
    });
}
let checkd = false;
function listOn(b) {
    if (!document.querySelector(".usercontainer")) {
        location.href = "/admin";
    }
    document.querySelector(".usercontainer").style.display = "none";
    document.querySelector(".qnacontainer").style.display = "none";
    document.querySelector(".refundcontainer").style.display = "none";
    document.querySelector(".gamecontainer").style.display = "none";
    document.querySelector(".reviewcontainer").style.display="none";
    if (b.id === "user") {
        document.querySelector(".usercontainer").style.display = "block";
    } else if (b.id === "game") {
        document.querySelector(".gamecontainer").style.display = "block";
    } else if (b.id === "qna") {
        document.querySelector(".qnacontainer").style.display = "block";
    } else if (b.id === "refund") {
        document.querySelector(".refundcontainer").style.display = "block";
    } else if (b.id === "review") {
        document.querySelector(".reviewcontainer").style.display="block";
    }
}

// user
const roleList = [...document.querySelectorAll("#roleselect")];
roleList.forEach((role) => {
    role.addEventListener("change", () => {
        role.setAttribute("data-setting", "on");
    });
})

function userListRoleUpdate() {
    if (!roleList) {
        return;
    }
    let dataList = [];
    roleList.forEach((role) => {
        if (role.getAttribute("data-setting") === "on") {
            let data = {};
            data.id = role.getAttribute("data-id");
            data.role = role.value;
            console.log(data);
            dataList.push(data);
        }
    });

    fetch("/admin/userListRoleUpdate", {
        method: "post",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(dataList),
    })
        .then(response => response.text())
        .then(data => {
            if (data === 'ok') {
                location.href = "/admin";
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function valuesDelete(value, url) {
    let id = parseInt(value);
    console.log(id);
    fetch("/admin/" + url, {
        method: "delete",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: "id=" + id,
    })
        .then(response => response.text())
        .then(data => {
            if (data === 'ok') {
                location.href = "/admin";
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}
function valueOneUpdate(value,url){
    const textList = [...document.querySelectorAll("#qnaAnwerContent")];
    let qna = {};
    qna.id = value; // id

    for(let i = 0; i < textList.length; i+=1){
        let id = textList[i].getAttribute("data-id");
        if(id === value){
            qna.qnaAnwerContent = textList[i].value;
            break;
        }
    }
    qna.qnaRespondent = "관리자";
    fetch("/admin/" + url, {
        method: "put",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(qna)
    })
        .then(response => response.text())
        .then(data => {
            if (data === 'ok') {
                location.href = "/admin";
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });

}
// modal
let sub = document.querySelector(".subject");

function refundCheck(subject, result, data) {
    let id = "";
    if (data) {
        id = data.getAttribute("data-value");
    }
    document.querySelector(".modal").classList.add("active");
    document.querySelector(".overlay").classList.add("active");
    sub.innerHTML = subject;
    sub.setAttribute("data-id", result);
    sub.setAttribute("data-value", id);
}

function refundAccept() {
    if(checkd){
        return;
    }
    checkd = true;
    let id = sub.getAttribute("data-id");
    if (id === "userupdate") {
        console.log(id);
        userListRoleUpdate()
    } else if (id === "userdelete") {
        valuesDelete(sub.getAttribute("data-value"), "userOneDelete");
    } else if (id === "gamedelete") {
        valuesDelete(sub.getAttribute("data-value"), "gameOneDelete");
    } else if (id === "paymentDelete") {
        console.log(sub.getAttribute("data-value"));
        valuesDelete(sub.getAttribute("data-value"),"paymentOneDelete");
    } else if(id ==="reviewdelete"){
        valuesDelete(sub.getAttribute("data-value"),"reviewOneDelete")
    } else if(id ==="qnaupdate"){
        valueOneUpdate(sub.getAttribute("data-value"),"qnaOneUpdate");
    }
    document.querySelector(".modal").classList.remove("active");
    document.querySelector(".overlay").classList.remove("active");
    location.href = "#";
    checkd = false;
}

function refundCancel() {
    document.querySelector(".modal").classList.remove("active");
    document.querySelector(".overlay").classList.remove("active");
    location.href = "#";
}

const title = [...document.querySelectorAll(".qnatitle")];
const number = [...document.querySelectorAll(".qnatextarray")];

console.log(title);
console.log(number);
title.forEach((t) => {
    t.addEventListener("click", () => {
        let data = t.getAttribute("data-id");
        qnaTextOn(data);
    });
});
function qnaTextOn(id) {
    number.forEach((num) => {
        let number = num.getAttribute("data-id");
        if (id === number) {
            num.style.display = "table-row";
        } else {
            num.style.display = "none";
        }
    });
}
