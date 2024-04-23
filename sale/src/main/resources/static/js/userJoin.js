let check = 0;
let pwCheck = -1;
const emailInput = document.getElementById("username")
const pwInput = document.getElementById("password");
const pwConfirmInput = document.getElementById("pwConfirm");
const emailDuplicatedBtn = document.getElementById("emailDuplicatedBtn");
const emailSendingBtn = document.getElementById("emailSendingBtn");
const codeInput = document.getElementById("codeInput");
const joinBtn = document.getElementById("joinBtn");
const joinForm = document.getElementById("userJoinForm");

// 폼에서 엔터키, 스페이스바 작동하지 않게하기, 특정 폼에서만 동작하게하기
joinForm.addEventListener("keypress", (event) => {
    if (event.key === "Enter" || event.key === " ") {
        event.preventDefault();
    }
});

// 입력값들의 유효성 체크
function validCheck(form) {
    if (!form.username.value.match(/[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$/)) {
        alert("이메일 형식이 다릅니다");
        form.username.value = "test@test.com";
        form.username.focus();
        return false;
    }
    if (!form.password.value.trim()) {
        alert("비밀번호를 입력해주세요");
        form.password.focus();
        return false;
    }
    if (!pwConfirmInput.value.trim()) {
        alert("비밀번호확인을 입력해주세요");
        pwConfirmInput.focus();
        return false;
    }
    if (!form.userNickname.value.trim()) {
        alert("닉네임을 입력해주세요");
        form.userNickname.focus();
        return false;
    }
    if (!form.userPhone.value.match(/010-\d{3,4}-\d{4}/)) {
        alert("전화번호 형식이 다릅니다");
        form.userPhone.value = "010-1234-1234";
        form.userPhone.focus();
        return false;
    }
    if (codeInput.value !== emailCode || emailCode === -1) {
        alert(`이메일로 보내드린 인증코드를 확인하세요.`)
        return false;
    }

    // submit 하기 전에 모든 조건들 체크
    if (check === 0) {
        alert('이메일 중복체크를 해주세요.');
        return false;
    } else if (check === -1) {
        alert('이메일 중복체크를 다시해주세요.');
        return false;
    }
    if (pwCheck !== 1) {
        pwConfirmInput.focus();
        alert(`비밀번호를 확인하세요.`)
        return false;
    }
    if (emailCode !== codeInput.value) {
        alert(`이메일 주소로 보내드린 인증코드를 확인하세요.`);
        return false;
    }

    // ***********
    disableTheBtn(joinBtn);
    form.submit();
    // ***********
}

// 버튼을 비활성화 하는 함수
// const btn = document.getElementById();
function disableTheBtn(btn) {
    btn.disabled = true;
    btn.style.opacity = "50%";
}

// 이메일 중복 체크하는 버튼 이벤트
emailDuplicatedBtn.addEventListener("click", () => {
    let username = document.getElementById("username").value.trim();

    if (username.length === 0) {
        alert("이메일을 입력해주세요");
        document.getElementById("username").focus();
        document.getElementById("username").style.border = "";
        return;
    }

    fetch("/users/emailDuplicated", {
        method: "POST",
        headers: {"Content-Type": "application/x-www-form-urlencoded; charset=UTF-8",},
        body: "username=" + username,
    })
        .then(response => response.text())
        .then(getResult)
        .catch(error => console.log("error= ", error));
});

function getResult(data) {
    if (data === "valid") {
        alert("사용 가능한 이메일입니다.");
        document.getElementById("password").focus();
        document.getElementById("username").style.border = "3px blue solid";
        emailDuplicatedBtn.style.visibility = "hidden";
        emailSendingBtn.style.visibility = "visible";
        codeInput.style.visibility = "visible";
        check = 1;
    } else if (data === "invalid") {
        alert("이미 사용중인 이메일입니다.");
        document.getElementById("username").value = "";
        document.getElementById("username").focus();
        document.getElementById("username").style.border = "3px red solid";
        check = -1;
    }
}

// 비밀번호와 비밀번호확인이 일치하는지 판단하는 함수
pwConfirmInput.addEventListener("keyup", () => {
    let pw = pwInput.value.trim();
    let pwConfirm = pwConfirmInput.value.trim();
    let pwNotCorresponding = document.getElementById("pwNotCorresponding");
    if (pw !== pwConfirm) {
        pwNotCorresponding.style.visibility = "visible";
        pwCheck = -1;
    } else {
        pwNotCorresponding.style.visibility = "hidden"; // 일치할 경우 숨김 처리
        pwCheck = 1;
    }
});

// 이메일 인풋을 수정했을 때 중복체크를 초기화하는 함수
emailInput.addEventListener("keyup", () => {
    check = -1;
    emailInput.style.border = "3px red solid";
    emailDuplicatedBtn.style.visibility = "visible";
    emailCode = -1;
});

// 이메일로 인증코드를 발송하는 함수
let emailCode;

function sendCodeToEmail() {
    let email = document.getElementById("username");
    console.log(email.value);
    if (!email.value.match(/[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$/)) {
        alert(`이메일 형식이 다릅니다.`);
        return false;
    }

    fetch("/users/sendCodeToEmail", {
        method: 'POST',
        headers: {
            "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8"
        },
        body: "email=" + email.value
    })
        .then(response => response.text())
        .then((data) => {
            emailCode = data;
            if (data) {
                alert(`인증코드가 메일로 발송되었습니다.`);

            } else {
                alert(`인증코드 발송에 실패했습니다.`)
            }
        }).catch(error => console.error('Error:', error));
}
