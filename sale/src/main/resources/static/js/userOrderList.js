let code;

function showModal() {
    document.getElementById(`refundOverlay`).style.display = 'block';
    document.getElementById(`refundModal`).style.display = 'block';
}

function hideModal() {
    document.getElementById(`refundOverlay`).style.display = 'none';
    document.getElementById(`refundModal`).style.display = 'none';
}

$('.refund_btn').on('click', function () {
    code = $(this).attr('paymentOrdernum');
    showModal();
});

function doRefundReal(form) {
    if (form.reason.value === "") {
        form.reason.focus();
        alert(`환불 사유를 선택해주세요.`);
        return false;
    }
    let reason = "기타";
    if (form.reason.value === "1") {
        reason = "게임이 실행되지 않습니다.";
    } else if (form.reason.value === "2") {
        reason = "사용자의 단순 변심";
    } else if (form.reason.value === "3") {
        reason = "결제 프로세스가 중복으로 이루어졌습니다.";
    } else if (form.reason.value === "4") {
        reason = "상품가격 불만";
    } else if (form.reason.value === "5") {
        reason = "개인 정보유출 우려";
    } else if (form.reason.value === "6") {
        reason = "쇼핑몰의 신뢰도 불만";
    } else if (form.reason.value === "7") {
        reason = "쇼핑 기능 불만";
    } else if (form.reason.value === "8") {
        reason = "기타";
    }
    console.log(code);
    console.log(reason);

    // 폼 데이터 생성
    const formData = new FormData();
    formData.append('code', code);
    formData.append('reason', reason);

    const realRefundBtn =document.getElementById("realResignBtn");
    disableTheBtn(realRefundBtn);

    // 서버로 POST 요청 보내기
    fetch('/payment/refund', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.text();
        })
        .then(data => {
            console.log('Response:', data);
            location.reload();
            alert(`환불요청에 성공했습니다.`);
        })
        .catch(error => {
            console.error('Error:', error);
            location.reload();
            alert(`환불요청에 실패했습니다.`);
        });
}

// 버튼을 비활성화 하는 함수
// const btn = document.getElementById();
function disableTheBtn(btn) {
    btn.disabled = true;
    btn.style.opacity = "50%";
}
