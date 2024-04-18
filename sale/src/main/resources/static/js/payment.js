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

// 결제라인
let checkd = true;
function ordervalue(number){
    let payment = [];
    let gamePrice = [...document.querySelectorAll("#price")];
    let gameId = [...document.querySelectorAll("#gameId")];
    let cartId = [...document.querySelectorAll("#cartId")];
    let total = [...document.querySelectorAll("#total")];
    let index = parseInt(document.querySelector("#index").value);
    for(let i = 0; i < gameId.length; i+=1){
        let data = {};
        data.cartId = parseInt(cartId[i].value);
        data.gameId = parseInt(gameId[i].value);
        data.paymentPirce = parseInt(total[i].value);
        data.merchantUid = "IMP"+number;
        data.gamePrice = parseInt(gamePrice[i].value);
        payment.push(data);

    }
    return payment;
}
// 타입 체크된거 찾기
function orderType(){
    let type;
    let selete = document.getElementsByName("settlekind");
    for(let i = 0; i < selete.length; i+=1){
       if(selete[i].checked){
           type = selete[i].value;
           console.log(type);
           break;
       }
    }
    return type;
}
function requestPay() {
    if(!checkd){
        return;
    }
    let type;
    // 결제 타입
    if(!orderType()){
        alert("결제방법을 선택해주세요");
        return;
    }else{
        type = orderType();
    }
    let number = createnumber();
    let listTotal = parseInt(document.querySelector("#listtotal").value);
    // let listTotal = 300;
    let orderName = document.querySelector("#ordername").value;
    let username = document.querySelector("#username").value;
    let dataList = ordervalue(number);
    checkd = false;

    IMP.init("imp15605565");
    // 객체 초기화
    // buyer_tel
    IMP.request_pay(
        {
            pg: "danal_tpay", // 다날
            pay_method: type, //결제방식
            merchant_uid: "IMP" + number, // 주문번호(고유번호)
            name: orderName, // 상품이름
            amount: listTotal, // 결제가격
            buyer_tel: "010-1234-5678", // 다날 필수
            buyer_name : username,
        },
        function (rsp) {
            if (rsp.success) {
                $.ajax({
                    url : "/payment/processing",
                    type : "post",
                    data: {
                        impUid: rsp.imp_uid,
                        merchantUid: rsp.merchant_uid
                    },
                    success : function (data){
                        if(listTotal == data.response.amount){
                            // 저장 로직 또는 오류일경우 환불처리
                            fetch("/payment/paymentcheck", {
                                method: "POST",
                                headers: {
                                    "ConTent-Type": "application/json"
                                },
                                body: JSON.stringify(dataList)
                            })
                            .then(response => response.text())
                            .then(data => {
                                alert("결제완료 "+data);
                                checkd = true;
                                $('.overlay').removeClass('active');
                                location.href = "/";
                            })
                            .catch(error => {
                                console.error('Error:', error);
                                alert("서버문제로 인하여 환불처리되었습니다.");
                                checkd = true;
                                $('.overlay').removeClass('active');
                            });
                        }else{
                            // 만약 해당결제 금액이 다를경우
                            fetch("/payment/errorPayment", {
                                method: "POST",
                                headers: {
                                    "ConTent-Type": "application/json"
                                },
                                body: JSON.stringify(dataList)
                            })
                                .then(response => response.text())
                                .then(data => {
                                    alert("결제실패 "+data);
                                    checkd = true;
                                    $('.overlay').removeClass('active');
                                    location.href = "/";
                            })
                                .catch(error => {
                                    console.error('Error:', error);
                                    alert("서버문제로 인하여 환불처리되었습니다.");
                                    checkd = true;
                                    $('.overlay').removeClass('active');
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