// gameForm


var filesArr = [];
var fileNo = 0;

let files = document.querySelector("#files");
if(files){
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
function headerFiles(obj){
    const fileType = [
        "application/pdf",
        "image/jpeg",
        "image/png",
        "image/bmp",
    ];
    let files = obj.files[0];
    console.log(files.type);
    if (obj.files.size > 100 * 1024 * 1024) {
        alert("최대 파일 용량인 100MB를 초과한 파일은 제외되었습니다.");
        return false;

    }else if (!fileType.includes(files.type)) {
        alert("첨부가 불가능한 파일은 제외되었습니다.");
        return false;
    } else {
        document.querySelector(".upload-name").value = obj.value;
        return true;
    }
}
function validation(obj) {
    const fileType = [
        "application/pdf",
        "image/jpeg",
        "image/png",
        "image/bmp",
    ];
    console.log(obj.type);
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
}

let editorMin;
let editorRcm;
let content = [...document.querySelectorAll("#contents")];
ClassicEditor.create(document.querySelector("#minRequirements"), {
    language: "ko",
    removePlugins: ["Heading"],
})
    .then((newEditor) => {
        editorMin = newEditor;
    })
    .then((editorMin) => {
        window.editor = editorMin;
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
        editorRcm = newEditor;
    })
    .then((editorRcm) => {
        window.editor = editorRcm;
    })
    .catch((error) => {
        console.error("Oops, something went wrong!");
        console.error(
            "Please, report the following error on https://github.com/ckeditor/ckeditor5/issues with the build id and the error stack trace:"
        );
        console.warn("Build id: g64ljk55ssvc-goqlohse75uw");
        console.error(error);
    });
function result(form){
    form.minRequirements.value = editorMin.getData().toString();
    form.rcmRequirements.value = editorRcm.getData().toString();
    console.log(form.minRequirements.value);
    console.log(form.rcmRequirements.value);
    if(!form.stock.value.trim()){
        alert("수량 확인해주세요");
        return false;
    }
    if(!form.name.value.trim()){
        alert("이름 확인해주세요");
        return false;
    }
    if(!form.supportedLanguages.value.trim()){
        alert("언어 확인해주세요");
        return false;
    }
    if(!form.price.value.trim()){
        alert("금액 확인해주세요");
        return false;
    }
    if(!form.developers.value.trim()){
        alert("개발사 확인해주세요");
        return false;
    }
    if(!form.releaseDate.value.trim()){
        alert("날짜 확인해주세요");
        return false;
    }
    if(!form.genres.value.trim()){
        alert("장르 확인해주세요");
        return false;
    }
    if(!form.minRequirements.value.trim()){
        alert("최소사양 확인해주세요");
        return false;
    }
    if(!form.rcmRequirements.value.trim()){
        alert("권장사양 확인해주세요");
        return false;
    }
    if(!form.headerFile.value.trim()){
        alert("헤더 이미지 확인해주세요");
        return false;
    }
    if(!form.platform.value.trim()){
        alert("플랫폼 확인해주세요");
        return false;
    }
    if(!form.screenFile.value.trim()){
        alert("소개 이미지 확인해주세요");
        return false;
    }

    // const input = document.querySelector("#resultbtn");

    form.submit();

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
