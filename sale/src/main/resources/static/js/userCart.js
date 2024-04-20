// 체크박스 전체선택하는 함수
$("#chk_all").bind("click", function () {
    if ($("#chk_all:checked").val() === "Y") {
        $('input[name="goods_code[]"]').prop("checked", true);
        $('input[name="checked"]').val("true");
    } else {
        $('input[name="goods_code[]"]').prop("checked", false);
        $('input[name="checked"]').val("false");
    }
    printFinalPrice();
});
// 각 체크박스를 클릭할 때마다 상태를 변경하는 함수
$('input[name="goods_code[]"]').on("click", function () {
    // 현재 체크박스의 checked 상태를 가져옴
    let isChecked = $(this).prop("checked");
    // 현재 체크박스의 상태를 반전하여 checked input의 값을 설정
    $(this).closest('tr').find('input[name="checked"]').val(isChecked ? "true" : "false");
    printFinalPrice();
});

// 합계들을 모두 합해 최종결제금액을 출력
function printFinalPrice() {
    console.log(`printFinalPrice() 호출됨`);
    let finalPrice = 0;

    // 각 체크박스 요소들을 가져오기
    let checkboxes = document.querySelectorAll('input[name="goods_code[]"]');

    // 각 체크박스 요소의 가격을 합함
    checkboxes.forEach((obj) => {

        // 체크된 체크박스만 처리
        if (obj.checked) {
            // 변경된 체크박스를 포함하는 tr 요소 가져오기
            let closestTr = obj.closest('tr');

            // 해당 tr 요소에서 가격을 가져오기
            let priceElement = closestTr.querySelector('span[name="total"]');

            // 체크 여부에 따라 합계에 추가하기
            let price = parseInt(priceElement.innerText);

            finalPrice += price;
        }
    });

    // 최종결제금액 출력
    document.getElementById('finalPrice').innerText = finalPrice;
}

$(document).ready(function () {
    // 선택 삭제 버튼 클릭 시
    $("#btn_select_delete").click(function () {
        const delBtn = document.getElementById("btn_select_delete");
        disableTheBtn(delBtn);
        // 선택된 주문번호 배열 초기화
        let orderNumbers = [];
        // 각 행에서 선택된 주문번호 추출
        $("input[name='goods_code[]']:checked").each(function () {
            // 현재 행에서 주문번호 추출하여 배열에 추가
            let orderNumber = $(this).closest('tr').find('td[name="goods_code[]"]').text();
            orderNumbers.push(orderNumber);
        });
        // 만약 선택된 주문번호가 없다면 경고 메시지 출력 후 함수 종료
        if (orderNumbers.length === 0) {
            location.reload();
            alert("선택된 주문이 없습니다.");
            return false;
        }
        // 배열 출력
        console.log(orderNumbers);
        const option = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(orderNumbers)
        };
        // 선택된 주문번호를 서버로 전송하는 fetch 요청
        fetch('/cart/delete', option)
            .then((res) => res.text())
            .then((data) => {
                if (data === "success") {
                    location.reload();
                    alert(`선택한 게임이 장바구니에서 삭제됐습니다.`);
                } else {
                    location.reload();
                    alert(`장바구니 삭제에 실패했습니다.`)
                }
            }).catch(error => console.error('Error:', error));
    });
});
// 주문하기 버튼 클릭 이벤트 핸들러
document.getElementById("btn_order").addEventListener("click", (target) => {
    const btnOrder = document.getElementById("btn_order");
    disableTheBtn(btnOrder);
    // 체크된 상품의 주문번호를 담을 배열
    let selectedItems = [];
    // 각 행에서 선택된 주문번호 추출
    $("input[name='goods_code[]']:checked").each(function () {
        // 현재 행에서 주문번호 추출하여 배열에 추가
        let orderNumber = $(this).closest('tr').find('td[name="goods_code[]"]').text();
        selectedItems.push(orderNumber);
        console.log(orderNumber);
    });
    // 선택된 상품이 있는지 확인
    if (selectedItems.length > 0) {
        // 폼 요소 가져오기
        let form = document.getElementById("cartFrm");
        // 선택된 상품의 주문번호를 숨겨진 필드로 폼에 추가
        selectedItems.forEach(function (item) {
            let input = document.createElement("input");
            input.setAttribute("type", "hidden");
            input.setAttribute("name", "selectedItems");
            input.setAttribute("value", item);
            form.appendChild(input);
        });
        // 폼 전송
        disableTheBtn(btnOrder);
        form.submit();
    } else {
        // 선택된 상품이 없는 경우 사용자에게 알림 메시지 표시
        alert("주문할 상품을 선택해주세요.");
        location.reload();
    }
});
// 버튼을 비활성화 하는 함수
// const btn = document.getElementById();
function disableTheBtn(btn) {
    btn.disabled = true;
    btn.style.opacity = "50%";
}
