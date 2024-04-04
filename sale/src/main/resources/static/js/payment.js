var IMP = window.IMP;
// 현재 시간 넣기
function createnumber(){
    const today = new Date();
    const hours = today.getHours();
    const minutes = today.getMinutes();
    const seconds = today.getSeconds();
    const milliseconds = today.getMilliseconds();
    let makeMerchanUid = hours + "" + minutes + seconds + milliseconds;
    for(let i = 0; i < 5; i+=1){
        makeMerchanUid += Math.floor(Math.random() * 8);
    }
    return parseInt(makeMerchanUid);
}
function requestPay() {
    IMP.init("imp15605565");
    // 객체 초기화
    // buyer_tel
    IMP.request_pay(
        {
            pg: "danal_tpay", // 다날
            pay_method: "card", //결제방식
            merchant_uid: "IMP" + createnumber(), // 주문번호(고유번호)
            name: "게임이름", // 상품이름
            amount: 300, // 결제가격
            buyer_tel: "010-1234-5678", // 다날 필수
            buyer_name : "유저 이름",
        },
        function (rsp) {
            if (rsp.success) {
                $.ajax({
                    url : "/payment",
                    type : "post",
                    data: {
                        impUid: rsp.imp_uid,
                        merchantUid: rsp.merchant_uid
                    },
                    success : function (data){
                        if(300 == data.response.amount){
                            // 저장 로직 또는 오류일경우 환불처리
                            $.ajax({
                                url:"/paymenttest",
                                type:"post",
                                data:{
                                    impUid: rsp.imp_uid,
                                    merchantUid: rsp.merchant_uid
                                },
                                success : function (data){
                                    console.log(data);
                                },
                                error :  function(request, status, error) {
                                    alert("주문정보 저장을 실패");
                                }
                            });
                        }
                    },
                    error :  function(request, status, error) {
                        console.log(error);
                    }
                })
            } else {
                alert("결제에 실패하였습니다. 에러 내용:" + rsp.error_msg);
            }
        });
}