function requestRefund(paymentId) {

    let logId = parseInt(paymentId);

    // fetch를 사용하여 paymentId를 서버로 전송
    fetch('/payment/refund', {
        method: 'POST',
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: "paymentId=" + paymentId,
    })
        .then(response => response.text())
        .then((data) => {
            if (data) {
                alert(`환불요청에 성공했습니다.`);
                location.reload();
            } else {
                alert(`환불요청에 실패했습니다.`)
            }
        }).catch(error => console.error('Error:', error));
}
