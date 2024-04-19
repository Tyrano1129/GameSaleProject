const myPage = document.getElementById("myPage");
const questionForm = document.getElementById("questionForm");

// 폼에서 엔터키, 스페이스바 작동하지 않게하기, 특정 폼에서만 동작하게하기
myPage.addEventListener("keypress", (event) => {
    if (event.key === "Enter" || event.key === " ") {
        event.preventDefault();
    }
});

// 원하는 폼에서는 스페이스바 작동하게하기
if (questionForm) {
    questionForm.addEventListener("keypress", (event) => {
        if (event.key === " ") {
            event.stopPropagation(); // 해당 이벤트의 전파를 중지합니다.
        }
    });
}

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
    const btn = document.getElementById("userUpdateBtn");
    disableTheBtn(btn)
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
    const btn = document.getElementById("realResignBtn");
    disableTheBtn(btn);
    form.submit();
    alert(`회원탈퇴에 성공했습니다.`);
}

// 버튼을 비활성화 하는 함수
// const btn = document.getElementById();
function disableTheBtn(btn) {
    btn.disabled = true;
    btn.style.opacity = "50%";
}
