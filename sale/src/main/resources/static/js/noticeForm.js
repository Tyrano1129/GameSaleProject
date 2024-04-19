// window.onpopstate = function(e){
//     fetch("/imageDelete",{
//         headers: {
//             "Content-Type" : "application/x-www-form-urlencoded",
//         },
//         body: "img="+img
//     })
//     .then(response => response.text())
//     .then(data => {
//         if(data ==="ok"){
//             alert("이미지가 삭제되었습니다.");
//         }
//     })
//     .catch(error =>{
//         console.error("error",error);
//     });
// }

let editor;
let content = document.querySelector("#contents");
ClassicEditor.create(document.querySelector("#editor"), {
    language: "ko",
    removePlugins: ["Heading"],
    ckfinder: {
        uploadUrl: "/notice/image",
        withCredentials: true,
    },
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
function testText(form) {
    if(!editor.getData()){
        alert("내용을 입력해주세요.");
        return false;
    }
    if(!form.title.value.trim()){
        alert("제목을 입력해주세요");
        return false;
    }
    if(!form.writer.value.trim()){
        form.writer.value = "관리자";
    }
    form.content.value = editor.getData().toString();
    console.log(form.content.value);
    form.submit();
}