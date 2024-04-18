const emailInput = document.getElementById("username");
const loginForm = document.getElementById("userLoginForm");

// 폼에서 엔터키, 스페이스바 작동하지 않게하기, 특정 폼에서만 동작하게하기
loginForm.addEventListener("keypress", (event) => {
    if (event.key === "Enter" || event.key === " ") {
        event.preventDefault();
    }
});

alertError();

function doLogin(form) {
    if (!form.username.value.trim()) {
        alert("이메일을 입력해주세요.");
        form.username.focus();
        return false;
    }
    if (!form.password.value.trim()) {
        alert("비밀번호를 입력해주세요.");
        form.password.focus();
        return false;
    }

    const btn = document.getElementById("loginBtn");
    disableTheBtn(btn);
    form.submit()
}

// 버튼을 비활성화 하는 함수
// const btn = document.getElementById();
function disableTheBtn(btn) {
    btn.disabled = true;
    btn.style.opacity = "50%";
}

function alertError() {

    const error = new URL(window.location.href).searchParams.get("error");

    if (!error) {
        return false;
    }

    emailInput.focus();

    if (error === "001") {
        alert(`존재하지 않는 아이디 입니다.`);
    } else if (error === "002") {
        alert(`내부 시스템 에러 : 만약 SNS로 회원가입 하셨다면 SNS로그인을 이용해주세요.`);
    } else if (error === "003") {
        alert(`아이디(이메일)과 비밀번호를 확인하세요.`);
    } else if (error === "004") {
        alert(`인증 요청이 거부되었습니다.`);
    } else {
        alert(`알 수 없는 오류로 로그인 요청을 처리할 수 없습니다. 관리자에게 문의하세요.`);
    }
}
