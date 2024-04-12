// 로그인 중이면 true, 그렇지 않으면 false 를 반환합니다.
function isLoggedIn(callback) {
    fetch("/users/checkLog", {
        method: "POST",
        headers: {"Content-Type": "application/x-www-form-urlencoded; charset=UTF-8"},
        body: "test=test",
    })
        .then(response => response.text())
        .then(data => {
            const logTrueFalse = data === "valid";
            callback(logTrueFalse);
        })
        .catch(error => {
            console.log("error: ", error);
            callback(false); // 에러 발생 시 로그인 상태를 false로 설정
        });
}

// 로그인 상태를 확인하고 장바구니로 이동하거나 경고 메시지를 표시합니다.
function showMyCart() {
    console.log(`장바구니로`);
    isLoggedIn(logTrueFalse => {
        if (logTrueFalse) {
            window.location.href = "/cart/myCart"
        } else {
            alert("로그인을 하셔야 이용이 가능합니다.");
        }
    });
}
