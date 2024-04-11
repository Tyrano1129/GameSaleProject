const emailInput = document.getElementById("username");

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

    form.submit()
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
        alert(`내부 시스템 문제로 로그인 요청을 처리할 수 없습니다.`);
    } else if (error === "003") {
        alert(`아이디(이메일)과 비밀번호를 확인하세요.`);
    } else if (error === "004") {
        alert(`인증 요청이 거부되었습니다.`);
    } else {
        alert(`알 수 없는 오류로 로그인 요청을 처리할 수 없습니다. 관리자에게 문의하세요.`);
    }
}