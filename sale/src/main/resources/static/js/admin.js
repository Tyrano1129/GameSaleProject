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

function listOn(b){
    document.querySelector(".usercontainer").style.display="none";
    document.querySelector(".qnacontainer").style.display="none";
    document.querySelector(".refundcontainer").style.display="none";
    // document.querySelector(".gamecontainer").style.display="none";
    // document.querySelector(".reviewcontainer").style.display="none";
    if(b.id === "user"){
        document.querySelector(".usercontainer").style.display="block";
    }else if(b.id === "game"){
        // document.querySelector(".gamecontainer").style.display="block";
    }else if(b.id === "qna") {
        document.querySelector(".qnacontainer").style.display = "block";
    }else if(b.id === "refund"){
        document.querySelector(".refundcontainer").style.display="block";
    }else if(b.id === "review"){
        // document.querySelector(".reviewcontainer").style.display="block";
    }
}


// modal
function refundCheck() {
    document.querySelector(".modal").classList.add("active");
    document.querySelector(".overlay").classList.add("active");
}
function refundAccept() {
    document.querySelector(".modal").classList.remove("active");
    document.querySelector(".overlay").classList.remove("active");
    location.href = "#";
}
function refundCancel() {
    document.querySelector(".modal").classList.remove("active");
    document.querySelector(".overlay").classList.remove("active");
    location.href = "#";
}