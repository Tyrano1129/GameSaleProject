
let checkd = false;

// user

function userListRoleUpdate(roleList) {
    if (!roleList) {
        return;
    }
    let dataList = [];
    roleList.forEach((role) => {
        if (role.getAttribute("data-setting") === "on") {
            let data = {};
            data.id = role.getAttribute("data-id");
            data.role = role.value;
            console.log(data);
            dataList.push(data);
        }
    });

    fetch("/admin/userListRoleUpdate", {
        method: "post",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(dataList),
    })
        .then(response => response.text())
        .then(data => {
            if (data === 'ok') {
                location.href = "/admin";
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function valuesDelete(value, url) {
    let id = parseInt(value);
    console.log(id);
    fetch("/admin/" + url, {
        method: "delete",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: "id=" + id,
    })
        .then(response => response.text())
        .then(data => {
            if (data === 'ok') {
                location.href = "/admin";
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}
function valueOneUpdate(value,url){
    const textList = [...document.querySelectorAll("#qnaAnwerContent")];
    let qna = {};
    qna.id = value; // id

    for(let i = 0; i < textList.length; i+=1){
        let id = textList[i].getAttribute("data-id");
        if(id === value){
            qna.qnaAnwerContent = textList[i].value;
            break;
        }
    }
    if(!qna.qnaAnwerContent.trim()){
        alert("답변 내용을 작성해주세요!");
        return false;
    }
    qna.qnaRespondent = "관리자";
    fetch("/admin/" + url, {
        method: "put",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(qna)
    })
        .then(response => response.text())
        .then(data => {
            if (data === 'ok') {
                location.href = "/admin";
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });

}
// modal
let sub = document.querySelector(".subject");

function valuesCheck(subject, result, data) {
    let id = "";
    if (data) {
        id = data.getAttribute("data-value");
    }
    document.querySelector(".modal").classList.add("active");
    document.querySelector(".overlay").classList.add("active");
    sub.innerHTML = subject;
    sub.setAttribute("data-id", result);
    sub.setAttribute("data-value", id);
}

function valuesAccept() {
    if(checkd){
        return;
    }
    checkd = true;
    let id = sub.getAttribute("data-id");
    if (id === "userupdate") {
        console.log(id);
        const roleList = [...document.querySelectorAll("#roleselect")];
        userListRoleUpdate(roleList);
    } else if (id === "userdelete") {
        valuesDelete(sub.getAttribute("data-value"), "userOneDelete");
    } else if (id === "gamedelete") {
        valuesDelete(sub.getAttribute("data-value"), "gameOneDelete");
    } else if (id === "paymentDelete") {
        console.log(sub.getAttribute("data-value"));
        valuesDelete(sub.getAttribute("data-value"),"paymentOneDelete");
    } else if(id ==="reviewdelete"){
        valuesDelete(sub.getAttribute("data-value"),"reviewOneDelete")
    } else if(id ==="qnaupdate"){
        valueOneUpdate(sub.getAttribute("data-value"),"qnaOneUpdate");
    }
    document.querySelector(".modal").classList.remove("active");
    document.querySelector(".overlay").classList.remove("active");
    location.href = "#";
    checkd = false;
}

function valuesCancel() {
    document.querySelector(".modal").classList.remove("active");
    document.querySelector(".overlay").classList.remove("active");
    location.href = "#";
}

function valueList(value){
    if(checkd){
        return;
    }
    console.log(value);
    let data = "";
    fetch("/admin/list?type="+value,{
        method: "GET"
    })
    .then(response => response.json())
    .then(data => {
        const dataList = data;
        console.log(dataList);
        if(value ==="userList"){
            userListView(dataList);
        }else if(value === "qnaList"){
            qnaList(dataList);
        }else if(value === "gameList"){
            gameList(dataList);
        }else if(value === "refundList"){
            refundList(dataList);
        }else if(value === "reviewList"){
            reviewList(dataList);
        }
    })
    .catch(error => {
     console.error('Error:', error);
    });
}
function userListView(data){
    let main = document.querySelector(".adminmain");
    let userheader = "<div class=\"usercontainer\">" +
        "<h1>유저 관리 리스트</h1>" +
        "<form action=\"/admin\" method=\"post\">" +
        "<div class=\"userButton\">" +
        "<input type=\"button\" onclick=\"valuesCheck('유저를 수정 하시겠습니까?','userupdate')\" value=\"수정하기\" />" +
        "</div>" +
        "<table class=\"userList\">" +
        "<tr>" +
        "<th>번호</th>" +
        "<th>이메일</th>" +
        "<th>닉네임</th>" +
        "<th>직책</th>" +
        "<th>전화번호</th>" +
        "<th>삭제</th>" +
        "</tr>" ;
    let list = ""
    data.contnet.forEach((value) => {
        list += "<tr>" +
            "<td>" +
            `<p class="userid">${value.id}</p>` +
            "</td>" +
            "<td>" +
            `<p>${value.username}</p>` +
            "</td>" +
            "<td>" +
            `<p>${value.userNickname}</p>` +
            "</td>" +
            "<td>" +
            `<select name="roleselect" id="roleselect" data-id="${value.id}">` +
            `<option value="MANAGER"`;
        if(value.userRole === "ROLE_MANAGER"){
            list += `selected>매니저</option>`
        }else{
            list += `>매니저</option>`;
        }
            list += `<option value="USER"`;
        if(value.userRole ==="ROLE_USER"){
            list += `selected>유저</option>`;
        }else{
            list += `>유저</option>`;
        }
        list += "</select>" +
            "</td>" +
            "<td>" +
            `<p>${value.userPhone}</p>` +
            "</td>" +
            "<td>" +
            `<input type="button" data-value="${value.id}" onclick="valuesCheck('유저를 삭제 하시겠습니까?','userdelete',this)" value="삭제">` +
            "</td>" +
            "</tr>";
    });
    let pageing =  "</table>" +
        "</form>";

    pageing +=
        "<div class='paging-admin'>" +
        "    <ul>";

    if(data.start >= 5){
      pageing+=  "      <li>" +
        `       <a href='javascript:userPageList(${data.start-1},${5})'><</a>` +
        "      </li>" ;
    }else if(data.start < 5){
      pageing+=  "      <li>" +
        `       <a href='javascript:return false'><</a>` +
        "      </li>" ;
    }
    for(let i = data.start; i < data.end; i+=1){
        let j = i + 1;
        pageing +=
            "      <li>" +
            `       <a href='javascript:userPageList(${i},${5})'>${j}</a>` +
            "      </li>" ;
    }

    if(data.end < data.total){
        pageing+=  "      <li>" +
            `       <a href='javascript:userPageList(${data.end},${5})'>></a>` +
            "      </li>" ;
    }else if(data.end >= data.total){
        pageing+=  "      <li>" +
            `       <a href='javascript:return false'>></a>` +
            "      </li>" ;
    }

    pageing +=
        "    </ul>" +
        "  </div>"+
        "</div>";
    main.innerHTML = userheader + list + pageing;

    const roleList = [...document.querySelectorAll("#roleselect")];
    roleList.forEach((role) => {
        role.addEventListener("change", () => {
            role.setAttribute("data-setting", "on");
        });
    })

}

function userPageList(page,size){
    let data = "";
    fetch("/admin/userPageList?page="+page+"&size="+size,{
        method: "GET"
    })
        .then(response => response.json())
        .then(data => {
            const dataList = data;
            console.log(dataList);
            userListView(dataList);
        })
        .catch(error => {
            console.error('Error:', error);
        });
}
function qnaList(data){
    let main = document.querySelector(".adminmain");
    let qna = "<div class=\"qnacontainer\">" +
        "<h1>문의 관리 리스트</h1>" +
        "<div class=\"qnatable\">" +
        "<table class=\"qnalist\">" +
        "<tr>" +
        "<th>번호</th>" +
        "<th>닉네임</th>" +
        "<th>문의제목</th>" +
        "<th>답변여부</th>" +
        "<th>답변자</th>" +
        "</tr>";
        data.contnet.forEach((q) =>{
            qna +=
            `<tr class="qnatitle" data-id="${q.qnaId}">` +
            "<td>" +
            `<p>${q.qnaId}</p>` +
            "</td>" +
            "<td>" +
            `<p>${q.users.userNickname}</p>` +
            "</td>" +
            "<td>" +
            `<a>${q.qnaTitle}</a>` +
            "</td>" +
            "<td>" +
            `<p>${q.qnaIsAnswered}</p>` +
            "</td>" +
            "<td>" +
            `<p>${q.qnaRespondent}</p>` +
            "</td>" +
            "</tr>" +
            `<tr class="qnatextarray" data-id="${q.qnaId}">` +
            "<td id=\"qnausercontent\" colspan=\"5\">" +
            "<div class=\"qancontnetarray\">" +
            `<span>${q.qnaContent}</span>` +
            "</div>" +
            "</td>" +
            "</tr>" +
            `<tr class="qnatextarray" data-id="${q.qnaId}">` +
            "<td colspan=\"5\">" +
            "<div class=\"qnatext\">" +
            "<label>답변 : </label>";
            if(!q.qnaIsAnswered){
               qna +=  "<textarea " +
                `name="qnaAnwerContent" ` +
                `id="qnaAnwerContent" ` +
                `data-id="${q.qnaId}" ` +
                `cols="50" ` +
                `rows="10"></textarea>`;
            }else {
                qna +="<textarea " +
                    `name="qnaAnwerContent" ` +
                    `data-id="${q.qnaId}" ` +
                    `cols="50" ` +
                    `rows="10" ` +
                    `disabled>` +
                    `${q.qnaAnwerContent}</textarea>`;
            }
            qna+= "<div class=\"button-qna\">";
            if(!q.qnaIsAnswered){
               qna+= `<button class="btn-qna-text" data-value="${q.qnaId}" onclick="valuesCheck('답변을하겠습니까?','qnaupdate',this)">올리기</button>`;
            }else{
               qna += `<button class="btn-qna-text" data-value="${q.qnaId}" onclick="return false">올리기</button>`;
            }
            qna +=
            "</div>" +
            "</div>" +
            "</td>" +
            "</tr>";
        });
        qna += "</table>" +
        "</div>" ;
    let pageing = "<div class='paging-admin'>" +
        "    <ul>";
    if(data.start >= 5){
        pageing+=  "      <li>" +
            `       <a href='javascript:qnApageList(${data.start-1},${5})'><</a>` +
            "      </li>" ;
    }else if(data.start < 5){
        pageing+=  "      <li>" +
            `       <a href='javascript:return false'><</a>` +
            "      </li>" ;
    }
    for(let i = data.start; i < data.end; i+=1){
        let j = i + 1;
        pageing +=
            "      <li>" +
            `       <a href='javascript:qnApageList(${i},${5})'>${j}</a>` +
            "      </li>" ;
    }

    if(data.end < data.total){
        pageing+=  "      <li>" +
            `       <a href='javascript:qnApageList(${data.end},${5})'>></a>` +
            "      </li>" ;
    }else if(data.end >= data.total){
        pageing+=  "      <li>" +
            `       <a href='javascript:return false'>></a>` +
            "      </li>" ;
    }

    pageing +=
        "    </ul>" +
        "  </div>"+
        "</div>";

    main.innerHTML = qna + pageing;

    const title = [...document.querySelectorAll(".qnatitle")];
    const number = [...document.querySelectorAll(".qnatextarray")];
    const body = document.querySelector("body");
    console.log(title);
    console.log(number);
    title.forEach((t) => {
        t.addEventListener("click", () => {
            let data = t.getAttribute("data-id");
            qnaTextOn(data);
        });
    });
    function qnaTextOn(id) {
        number.forEach((num) => {
            let number = num.getAttribute("data-id");
            if (id === number) {
                num.style.display = "table-row";
            } else {
                num.style.display = "none";
            }
        });
    }
}
function qnApageList(page,size){
    fetch("/admin/qnaPageList?page="+page+"&size="+size,{
        method: "GET"
    })
        .then(response => response.json())
        .then(data => {
            const dataList = data;
            console.log(dataList);
            qnaList(dataList);
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function gameList(data){
    let main = document.querySelector(".adminmain");
    let game = "<div class=\"gamecontainer\">" +
        "<h1>게임 관리 리스트</h1>" +
        "<div class=\"gameButton\">" +
        "<input type=\"button\" onclick=\"location.href='/admin/gameForm'\" value=\"추가하기\" />" +
        "</div>" +
        "<table class=\"gameList\">" +
        "<tr>" +
        "<th>번호</th>" +
        "<th>이름</th>" +
        "<th>개발자사</th>" +
        "<th>가격</th>" +
        "<th>발매일</th>" +
        "<th>장르</th>" +
        "<th>수량</th>" +
        "<th>삭제</th>" +
        "</tr>";
        data.contnet.forEach((g) =>{
            game +="<tr>" +
            "<td>" +
            `<p>${g.steamAppid}</p>` +
            "</td>" +
            "<td>" +
            `<a href="/admin/gameUpdateForm?id=${g.steamAppid}">${g.name}</a>` +
            "</td>" +
            "<td>" +
            `<p>${g.developers}</p>` +
            "</td>" +
            "<td>" +
            `<p>${g.priceView}</p>` +
            "</td>" +
            "<td>" +
            `<p>${g.gameDate}</p>` +
            "</td>" +
            "<td>" +
            `<p>${g.genres}</p>` +
            "</td>" +
            "<td>" +
            `<p>${g.stock}</p>` +
            "</td>" +
            "<td>" +
            `<button class="btn delete" data-value="${g.steamAppid}" onclick="valuesCheck('게임을 삭제 하시겠습니까?','gamedelete',this)">삭제</button>` +
            "</td>" +
            "</tr>";
        });
    game += "</table>";
    let pageing = "<div class='paging-admin'>" +
        "    <ul>";
    if(data.start >= 5){
        pageing+=  "      <li>" +
            `       <a href='javascript:gamePageList(${data.start-1},${5})'><</a>` +
            "      </li>" ;
    }else if(data.start < 5){
        pageing+=  "      <li>" +
            `       <a href='javascript:return false'><</a>` +
            "      </li>" ;
    }
    for(let i = data.start; i < data.end; i+=1){
        let j = i + 1;
        pageing +=
            "      <li>" +
            `       <a href='javascript:gamePageList(${i},${5})'>${j}</a>` +
            "      </li>" ;
    }

    if(data.end < data.total){
        pageing+=  "      <li>" +
            `       <a href='javascript:gamePageList(${data.end},${5})'>></a>` +
            "      </li>" ;
    }else if(data.end >= data.total){
        pageing+=  "      <li>" +
            `       <a href='javascript:return false'>></a>` +
            "      </li>" ;
    }

    pageing +=
        "    </ul>" +
        "  </div>"+
        "</div>";

    main.innerHTML = game + pageing;
}

function gamePageList(page,size){
    fetch("/admin/gamePageList?page="+page+"&size="+size,{
        method: "GET"
    })
        .then(response => response.json())
        .then(data => {
            const dataList = data;
            console.log(dataList);
            gameList(dataList);
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function refundList(data){
    let main = document.querySelector(".adminmain");
    let refund = "<div class=\"refundcontainer\">" +
        "<h1>환불 관리 리스트</h1>" +
        "<div class=\"refundtable\">" +
        "<table class=\"refundlist\">" +
        "<tr>" +
        "<th>번호</th>" +
        "<th>결제명</th>" +
        "<th>환불사유</th>" +
        "<th>환불신청날짜</th>" +
        "<th>환불여부</th>" +
        "<th>환불버튼</th>" +
        "</tr>";
    data.contnet.forEach((r) =>{
        refund +=
        "<tr>" +
        "<td>" +
        `<p>${r.refundId}</p>` +
        "</td>" +
        "<td>" +
        "<p>";
        refund += r.paymentList[0].gameName;
        if(r.paymentList.length != 1){
            refund += "외 " +(r.paymentList.length-1);
        }
        refund +=
        "</p>" +
        "</td>" +
        "<td>" +
        `<p>${r.refundReason}</p>` +
        "</td>" +
        "<td>" +
        `<p>${r.refundViewDate}</p>` +
        "</td>" +
        "<td>" +
        `<p>${r.refundWhether}</p>` +
        "</td>" +
        "<td>";
        if(r.refundWhether){
           refund += `<button onclick="return false">환불</button>`;
        }else{
           refund +=  `<button data-value="${r.refundId}" onclick="valuesCheck('환불 처리 하시겠습니까?','paymentDelete',this)">환불</button>`;
        }
        refund +=
        "</td>" +
        "</tr>";
    })
        refund +=
        "</table>" +
        "</div>";

    let pageing = "<div class='paging-admin'>" +
        "    <ul>";
    if(data.start >= 5){
        pageing+=  "      <li>" +
            `       <a href='javascript:refundPageList(${data.start-1},${5})'><</a>` +
            "      </li>" ;
    }else if(data.start < 5){
        pageing+=  "      <li>" +
            `       <a href='javascript:return false'><</a>` +
            "      </li>" ;
    }
    for(let i = data.start; i < data.end; i+=1){
        let j = i + 1;
        pageing +=
            "      <li>" +
            `       <a href='javascript:refundPageList(${i},${5})'>${j}</a>` +
            "      </li>" ;
    }

    if(data.end < data.total){
        pageing+=  "      <li>" +
            `       <a href='javascript:refundPageList(${data.end},${5})'>></a>` +
            "      </li>" ;
    }else if(data.end >= data.total){
        pageing+=  "      <li>" +
            `       <a href='javascript:return false'>></a>` +
            "      </li>" ;
    }

    pageing +=
        "    </ul>" +
        "  </div>"+
        "</div>";

    main.innerHTML = refund + pageing;


}
function refundPageList(page,size){
    fetch("/admin/refundPageList?page="+page+"&size="+size,{
        method: "GET"
    })
        .then(response => response.json())
        .then(data => {
            const dataList = data;
            console.log(dataList);
            refundList(dataList);
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function reviewList(data){
    let main = document.querySelector(".adminmain");
    let review = "<div class=\"reviewcontainer\">" +
        "<h1>신고리뷰 관리 리스트</h1>" +
        "<table class=\"reviewList\">" +
        "<tr>" +
        "<th>번호</th>" +
        "<th>게임이름</th>" +
        "<th>이름</th>" +
        "<th>내용</th>" +
        "<th>날짜</th>" +
        "<th>좋아요수</th>" +
        "<th>삭제</th>" +
        "</tr>";
        data.contnet.forEach((view) =>{
            review +=
            "<tr>" +
            "<td id=\"number\">" +
            `<p>${view.reviewId}</p>` +
            "</td>" +
            "<td id=\"gamename\">" +
            `<p> ${view.game.name}</p>` +
            "</td>" +
            "<td id=\"username\">" +
            `<p>${view.users.userNickname}</p>` +
            "</td>" +
            "<td id=\"subject\">" +
            `<p>${view.content}</p>` +
            "</td>" +
            "<td id=\"date\">" +
            `<p>${view.reviewDateView}</p>` +
            "</td>" +
            "<td id=\"count\">" +
            `<p>${view.voteCnt}</p>` +
            "</td>" +
            "<td id=\"삭제\">" +
            `<a class="btn delete" data-value="${view.reviewId}" onclick="valuesCheck('리뷰을 삭제 하시겠습니까?','reviewdelete',this)">삭제</a>` +
            "</td>" +
            "</tr>";
        })
    review +=
        "</table>";
    let pageing = "<div class='paging-admin'>" +
        "    <ul>";

    if(data.start >= 5){
        pageing+=  "      <li>" +
            `       <a href='javascript:reviewPageList(${data.start-1},${5})'><</a>` +
            "      </li>" ;
    }else if(data.start < 5){
        pageing+=  "      <li>" +
            `       <a href='javascript:return false'><</a>` +
            "      </li>" ;
    }
    for(let i = data.start; i < data.end; i+=1){
        let j = i + 1;
        pageing +=
            "      <li>" +
            `       <a href='javascript:reviewPageList(${i},${5})'>${j}</a>` +
            "      </li>" ;
    }

    if(data.end < data.total){
        pageing+=  "      <li>" +
            `       <a href='javascript:reviewPageList(${data.end},${5})'>></a>` +
            "      </li>" ;
    }else if(data.end >= data.total){
        pageing+=  "      <li>" +
            `       <a href='javascript:return false'>></a>` +
            "      </li>" ;
    }

    pageing +=
        "    </ul>" +
        "  </div>"+
        "</div>";

    main.innerHTML = review + pageing;
}
function reviewPageList(page,size){
    fetch("/admin/getReviewPageList?page="+page+"&size="+size,{
        method: "GET"
    })
        .then(response => response.json())
        .then(data => {
            const dataList = data;
            console.log(dataList);
            reviewList(dataList);
        })
        .catch(error => {
            console.error('Error:', error);
        });
}