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
window.onbeforeunload = function(e) {

    return "변경된 내용이 있습니다. 페이지를 떠나시겠습니까?";
};

let editor;
let content = document.querySelector("#contents");
ClassicEditor.create(document.querySelector("#editor"), {
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
