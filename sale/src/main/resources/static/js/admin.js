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

function listOn(b) {
    if (!document.querySelector(".usercontainer")) {
        location.href = "/admin";
    }
    document.querySelector(".usercontainer").style.display = "none";
    document.querySelector(".qnacontainer").style.display = "none";
    document.querySelector(".refundcontainer").style.display = "none";
    document.querySelector(".gamecontainer").style.display = "none";
    // document.querySelector(".reviewcontainer").style.display="none";
    if (b.id === "user") {
        document.querySelector(".usercontainer").style.display = "block";
    } else if (b.id === "game") {
        document.querySelector(".gamecontainer").style.display = "block";
    } else if (b.id === "qna") {
        document.querySelector(".qnacontainer").style.display = "block";
    } else if (b.id === "refund") {
        document.querySelector(".refundcontainer").style.display = "block";
    } else if (b.id === "review") {
        // document.querySelector(".reviewcontainer").style.display="block";
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

function userDelete(value, url) {
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


// modal
let sub = document.querySelector(".subject");

function refundCheck(subject, result, data) {
    let id = "";
    if (data) {
        id = data.getAttribute("data-delete");
    }
    document.querySelector(".modal").classList.add("active");
    document.querySelector(".overlay").classList.add("active");
    sub.innerHTML = subject;
    sub.setAttribute("data-id", result);
    sub.setAttribute("data-delete", id);
}

function refundAccept() {
    let id = sub.getAttribute("data-id");
    if (id === "userupdate") {
        console.log(id);
        userListRoleUpdate()
    } else if (id === "userdelete") {
        userDelete(sub.getAttribute("data-delete"), "userOneDelete");
    } else if (id === "gamedelete") {
        userDelete(sub.getAttribute("data-delete"), "gameOneDelete");
    } else if (id === "paymentDelete") {
        console.log(sub.getAttribute("data-delete"));
        // userDelete(sub.getAttribute("data-delete"));
    }
    document.querySelector(".modal").classList.remove("active");
    document.querySelector(".overlay").classList.remove("active");
    location.href = "#";
}

function refundCancel() {
    document.querySelector(".modal").classList.remove("active");
    document.querySelector(".overlay").classList.remove("active");
    location.href = "#";
}

// gameForm


var filesArr = [];
var fileNo = 0;

let files = document.querySelector("#files");
files.addEventListener("change", (e) => {
    document.querySelector(".upload-name").value = e.target.value;
});
function addFile(obj) {
    for (const file of obj.files) {
        // 첨부파일 검증
        if (validation(file)) {
            // 파일 배열에 담기
            var reader = new FileReader();
            reader.onload = function () {
                filesArr.push(file);
            };
            reader.readAsDataURL(file);

            // 목록 추가
            let htmlData = "";
            htmlData += '<div id="file' + fileNo + '" class="filebox">';
            htmlData += '   <p class="name">' + file.name + "</p>";
            htmlData +=
                '   <a class="delete" onclick="deleteFile(' + fileNo + ');"> x </a>';
            htmlData += "</div>";
            document.querySelector(".file-list").innerHTML += htmlData;
            fileNo++;
        } else {
            continue;
        }
    }
}

function validation(obj) {
    const fileType = [
        "application/pdf",
        "image/gif",
        "image/jpeg",
        "image/png",
        "image/bmp",
    ];
    if (obj.size > 100 * 1024 * 1024) {
        alert("최대 파일 용량인 100MB를 초과한 파일은 제외되었습니다.");
        return false;
    } else if (obj.name.lastIndexOf(".") == -1) {
        alert("확장자가 없는 파일은 제외되었습니다.");
        return false;
    } else if (!fileType.includes(obj.type)) {
        alert("첨부가 불가능한 파일은 제외되었습니다.");
        return false;
    } else {
        return true;
    }
}

/* 첨부파일 삭제 */
function deleteFile(num) {
    document.querySelector("#file" + num).remove();
    filesArr[num].is_delete = true;
}
function result(form){
    // const input = document.querySelector("#resultbtn");
    for(let i = 0; i < form.screenshots.files.length; i+=1){
        const file = form.screenshots.files[i];
        console.log('Selected file:', file.name);
    }
    // if(input.value ==="추가"){
    //     fetch('/admin/gameInsert',{
    //         headers:{
    //             "Content-Type" : "application/json"
    //         },
    //         body: JSON.stringify(form),
    //     })
    //         .then(response => response.text())
    //         .then(data => {
    //             if(data === "ok"){
    //                 alert("추가완료되었습니다.");
    //                 location.href="/admin";
    //             }
    //         })
    //         .catch(error => {
    //             console.error("error:",error);
    //         });
    // }
    // else if(input.value ==="수정"){}
}

let editor;
let content = document.querySelector("#contents");
ClassicEditor.create(document.querySelector("#minRequirements"), {
    language: "ko",
    removePlugins: ["Heading"],
})
    .then((newEditor) => {
        editor = newEditor;
    })
    .then((editor) => {
        window.editor = editor;
    })
    .catch((error) => {
        console.error("Oops, something went wrong!");
        console.error(
            "Please, report the following error on https://github.com/ckeditor/ckeditor5/issues with the build id and the error stack trace:"
        );
        console.warn("Build id: g64ljk55ssvc-goqlohse75uw");
        console.error(error);
    });

ClassicEditor.create(document.querySelector("#rcmRequirements"), {
    language: "ko",
    removePlugins: ["Heading"],
})
    .then((newEditor) => {
        editor = newEditor;
    })
    .then((editor) => {
        window.editor = editor;
    })
    .catch((error) => {
        console.error("Oops, something went wrong!");
        console.error(
            "Please, report the following error on https://github.com/ckeditor/ckeditor5/issues with the build id and the error stack trace:"
        );
        console.warn("Build id: g64ljk55ssvc-goqlohse75uw");
        console.error(error);
    });