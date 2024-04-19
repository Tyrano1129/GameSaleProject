// 체크박스 전체선택하는 함수
$("#chk_all").bind("click", function () {
    if ($("#chk_all:checked").val() === "Y") {
        $('input[name="goods_code[]"]').prop("checked", true);
        $('input[name="checked"]').val("true");
    } else {
        $('input[name="goods_code[]"]').prop("checked", false);
        $('input[name="checked"]').val("false");
    }
});

// 각 체크박스를 클릭할 때마다 상태를 변경하는 함수
$('input[name="goods_code[]"]').on("click", function () {
    // 현재 체크박스의 checked 상태를 가져옴
    let isChecked = $(this).prop("checked");
    // 현재 체크박스의 상태를 반전하여 checked input의 값을 설정
    $(this).closest('tr').find('input[name="checked"]').val(isChecked ? "true" : "false");
});

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
