function requestRefund(paymentId) {
    // fetch를 사용하여 paymentId를 서버로 전송
    fetch('/payment/refund', {
        method: 'GET',
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: "paymentId=" + paymentId,
    })
        .then(function (response) {
            if (response.ok) {
                // 요청이 성공적으로 처리되었을 때 실행할 코드
                console.log('환불 요청이 성공적으로 전송되었습니다.');
            } else {
                // 오류 응답 처리
                console.error('환불 요청이 실패하였습니다.');
            }
        })
        .catch(function (error) {
            console.error('네트워크 오류:', error);
        });
}
