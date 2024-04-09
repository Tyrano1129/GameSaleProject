var IMP = window.IMP;
// 현재 시간 넣기
function createnumber(){
    $('.overlay').addClass('active');
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

let checkd = true;
function ordervalue(){
    let order = {};
    order.gameName = $('.gamename').val();
    order.gamePrice = $('.gameprice').val();
    order.paymentPrice = $('.paymentprice').val();
    order.paymentOrderNum ="IMP" + createnumber();
    return order;
}
function requestPay() {
    if(!checkd){
        return;
    }
    checkd = false;

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
                                    checkd = true;
                                    $('.overlay').removeClass('active');
                                },
                                error :  function(request, status, error) {
                                    alert("주문정보 저장을 실패");
                                    checkd = true;
                                    $('.overlay').removeClass('active');
                                }
                            });
                        }
                    },
                    error :  function(request, status, error) {
                        console.log(error);
                        checkd = true;
                        $('.overlay').removeClass('active');
                    }
                })
            } else {
                checkd = true;
                alert("결제에 실패하였습니다. 에러 내용:" + rsp.error_msg);
                $('.overlay').removeClass('active');
            }
        });
}