$(document).ready(function () {

    // 체크박스 전체선택하는 함수
    $("#chk_all").bind("click", function () {
        if ($("#chk_all:checked").val() === "Y") {
            $('input[name="sno[]"]').prop("checked", true).change();
            $('input[name="game_codes[]"]').prop("checked", true).change();
            $('input[name="checked"]').val("true");
        } else {
            $('input[name="sno[]"]').prop("checked", false).change();
            $('input[name="game_codes[]"]').prop("checked", false).change();
            $('input[name="checked"]').val("false");
        }
        printFinalPrice();
    });

    // 각 체크박스를 클릭할 때마다 상태를 변경하는 함수
    $('input[name="sno[]"]').on("change", function () {
        // 현재 체크박스의 checked 상태를 가져옴
        let isChecked = $(this).prop("checked");
        // 현재 체크박스의 상태를 반전하여 checked input 의 값을 설정
        $(this).closest('tr').find('input[name="checked"]').val(isChecked ? "true" : "false");

        // game_codes[] 체크박스의 상태도 함께 변경
        $(this).closest('tr').find('input[name="game_codes[]"]').prop("checked", isChecked).change();
        printFinalPrice();
    });

    // 선택 삭제 버튼 클릭 시
    $("#all_delete_btn").click(function () {
        const btn = document.getElementById("all_delete_btn");
        disableTheBtn(btn);

        // 선택된 위시리스트번호 배열 초기화
        let wishNumbers = [];

        // 각 행에서 선택된 위시리스트번호 추출
        $("input[name='sno[]']:checked").each(function () {
            // 현재 행에서 위시리스트번호 추출하여 배열에 추가
            let wishNumber = $(this).closest('tr').find('td[name="sno[]"]').text();
            wishNumbers.push(wishNumber);
        });

        // 만약 선택된 위시리스트번호가 없다면 경고 메시지 출력 후 함수 종료
        if (wishNumbers.length === 0) {
            alert("선택된 위시리스트가 없습니다.");
            location.reload();
            return false;
        }

        // 배열 출력
        console.log(wishNumbers);

        const option = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(wishNumbers)
        };

        // 선택된 번호를 서버로 전송하는 fetch 요청
        fetch('/users/deleteWishlist', option)
            .then((res) => res.text())
            .then((data) => {
                if (data === "success") {
                    alert(`선택한 위시리스트가 장바구니에서 삭제됐습니다.`);
                    location.reload();
                } else {
                    alert(`위시리스트 삭제에 실패했습니다.`)
                    location.reload();
                }
            }).catch(error => console.error('Error:', error));
    });
});

// 장바구니 이동 버튼 이벤트 핸들러
document.getElementById("move_to_cart_btn").addEventListener("click", () => {
    const btn = document.getElementById("move_to_cart_btn");
    disableTheBtn(btn);
    // 폼 요소 가져오기
    let form = document.getElementById("wishlistForm");

    // 선택된 상품의 위시번호들을 담을 배열
    let selectedItems = [];
    // 선택된 상품의 게임코드들을 담을 배열
    let gameCodes = [];

    // 각 행에서 선택된 주문번호 추출
    $("input[name='sno[]']:checked").each(function () {
        // 현재 행에서 위시번호 추출하여 배열에 추가
        let orderNumber = $(this).closest('tr').find('td[name="sno[]"]').text();
        selectedItems.push(orderNumber);

        // 현재 행에서 게임코드 추출하여 배열에 추가
        let code = $(this).closest('tr').find('td[name="game_codes[]"]').text();
        gameCodes.push(code);
    });

    // 선택된 상품이 있는지 확인
    if (gameCodes.length > 0) {
        // 선택된 상품의 위시번호를 숨겨진 필드로 폼에 추가
        selectedItems.forEach(function (item) {
            let input = document.createElement("input");
            input.setAttribute("type", "hidden");
            input.setAttribute("name", "selectedItems[]");
            input.setAttribute("value", item);
            form.appendChild(input);
        });

        // 선택된 상품의 게임코드를 숨겨진 필드로 폼에 추가
        gameCodes.forEach(function (code) {
            let input = document.createElement("input");
            input.setAttribute("type", "hidden");
            input.setAttribute("name", "gameCodes[]");
            input.setAttribute("value", code);
            form.appendChild(input);
        });

        // 폼 전송
        form.submit();
    } else {
        // 선택된 위시가 없는 경우 사용자에게 알림 메시지 표시
        alert("장바구니로 이동할 위시를 선택해주세요.");
        location.reload();
    }
});

// 버튼을 비활성화 하는 함수
// const btn = document.getElementById();
function disableTheBtn(btn) {
    btn.disabled = true;
    btn.style.opacity = "50%";
}

// 합계들을 모두 합해 최종결제금액을 출력
function printFinalPrice() {
    console.log(`printFinalPrice() 호출됨`);
    let finalPrice = 0;

    // 각 체크박스 요소들을 가져오기
    let checkboxes = document.querySelectorAll('input[name="sno[]"]');

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

            console.log(price);
            finalPrice += price;
        }
    });

    // 최종결제금액 출력
    document.getElementById('finalPrice').innerText = finalPrice;
}
