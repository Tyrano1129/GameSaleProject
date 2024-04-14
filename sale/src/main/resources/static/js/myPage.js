// 폼에서 엔터, 스페이스 작동하지 않게하기
const myPage = document.getElementById("myPage");
myPage.addEventListener("keypress", (event) => {
    if (event.key === "Enter" || event.key === " ") {
        event.preventDefault();
    }
});

function doUpdate(form) {
    if (!form.password.value.trim()) {
        alert("비밀번호를 입력해주세요.");
        form.password.value = null;
        form.password.focus();
        return false;
    }
    if (!form.pwConfirm.value.trim()) {
        alert("비밀번호확인을 입력해주세요.");
        form.pwConfirm.value = null;
        form.pwConfirm.focus();
        return false;
    }
    if (!form.userNickname.value.trim()) {
        alert("닉네임을 입력해주세요.");
        form.userNickname.focus();
        return false;
    }
    if (!form.userPhone.value.match(/010-\d{3,4}-\d{4}/)) {
        alert("전화번호 형식이 다릅니다");
        form.userPhone.value = "010-1234-1234";
        form.userPhone.focus();
        return false;
    }
    if (form.password.value.trim() !== form.pwConfirm.value.trim()) {
        alert(`비밀번호가 일치하지 않습니다.`);
        form.password.value = null;
        form.pwConfirm.value = null;
        form.password.focus();
        return false;
    }
    form.submit();
    alert(`회원 정보를 수정했습니다.`);
}

// 탈퇴하기
function doResign(form) {
    if (form.reason.value === "") {
        form.reason.focus();
        alert(`탈퇴 사유를 선택해주세요.`);
        return false;
    }
    showModal();
}

function showModal() {
    document.getElementById(`resignOverlay`).style.display = 'block';
    document.getElementById(`resignModal`).style.display = 'block';
}

function hideModal() {
    document.getElementById(`resignOverlay`).style.display = 'none';
    document.getElementById(`resignModal`).style.display = 'none';
}

function doResignReal(form) {
    form.submit();
    alert(`회원탈퇴에 성공했습니다.`);
}
