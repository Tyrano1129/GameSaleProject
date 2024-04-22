
$(document).ready(function(){
    initKakao();
});

function initKakao(){
    Kakao.init('6a5f485741b87f4303214d8ba7957cb3');
   /* Kakao.Link.createDefaultButton({
        container: '#btn_kakao',
        objectType: 'feed',
        content:{
            title: '더 좋은 게임즈',
            description: snsTitle,
            imageUrl:thisUrl,
            link:{
                mobileWebUrl:thisUrl,
                webUrl:thisUrl
            }
        }
    });*/
}

function fn_sendFB(sns,game,headerImg) {
    console.log('fn_sendFB');
    var thisUrl = document.URL;
    var snsTitle = "같이 "+game +"해요! with 더 좋은 게임즈";
    if (sns == 'facebook') {
        var url = "http://www.facebook.com/sharer/sharer.php?u=" + encodeURIComponent(thisUrl);
        window.open(url, "", "width=486, height=286");
    } else if (sns == 'twitter') {
        var url = "http://twitter.com/share?url=" + encodeURIComponent(thisUrl) + "&text=" + encodeURIComponent(
            snsTitle);
        window.open(url, "tweetPop", "width=486, height=286,scrollbars=yes");
    } else if (sns == 'kakao') {
        Kakao.Link.sendDefault({
            objectType: 'feed',
            content:{
                title: '더 좋은 게임즈',
                description: snsTitle,
                imageUrl:headerImg,
                link:{
                    mobileWebUrl:thisUrl,
                    webUrl:thisUrl
                }
            }
        });
        //카카오 앱 자바스크립트 키
    }else if (sns == 'band'){
        var url = "http://www.band.us/plugin/share?body=" + encodeURIComponent(snsTitle) + "&route=" +
            encodeURIComponent(
                thisUrl);
        window.open(url, "shareBand", "width=40, height=500, resizable=yes");
    }

}