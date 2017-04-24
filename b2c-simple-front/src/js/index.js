$(function () {
    $("#index-menu li").eq(0).addClass("active");

    var apiUrl = "http://localhost:10001";
    var apiUrlPic = "http://localhost:10002";
    // var apiUrlPic = "http://localhost:10001";
    var tempInt = 1;

    //function changeMessage() {
    //    if (tempInt == 1) {
    //        $("#message").text("新鲜的大白菜上市咯");
    //        tempInt = 0;
    //    } else {
    //        $("#message").text("新鲜的大白菜上市...");
    //        tempInt = 1;
    //    }
    //}
    //
    //setInterval(changeMessage, 2000);
  //  getTest();
    function getTest() {
        $.ajax({
            url: apiUrl + "/front/user/user/test",
            type: 'GET',
            xhrFields: {
                withCredentials: true
            },
            crossDomain: true,
        }).done(function (msg) {
            var res = msg.res;
            if (res == 301) {
                // var url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=XXX&redirect_uri=http%3A%2F%2FXXXX.com%2FwxAuthor%2Fuser&response_type=code&scope=snsapi_userinfo&state=aaa&connect_redirect=1#wechat_redirect"
                // window.location = url;
            }
        });
    }

    var $goodsList = $("#product-list");
    var $noRec = $('#noRec');
    var $cloading = $('#cloading');
    var goodsList = {
        page: 0, //触发获取数据的数次(+1等于页码)
        size: 100, //每次触发取的记录条数
        isLoading: false, //列表是否加载中，避免重复触底加载
        url: apiUrl + "/front/productCategory/productCategory/getHomePageCategory", //数据api
        getMore: function (first) {
            if (goodsList.isLoading) //取数过程中，先停止重复取数
                return;

            if (first) {
                goodsList.page = 1;
                $('#noRec').hide();
                $goodsList.html('');
            } else {
                goodsList.page += 1;
            }
            $('#cloading').show(); //显示加载框
            goodsList.isLoading = true;
            setTimeout(goodsList.d(goodsList.page, goodsList.size), 1000); //模拟延迟取数据
        },

        //异步获取商品列表
        d: function (page, size) {
            $.ajax({
                url: goodsList.url,
                data: "pageNo=" + page + "&pageSize=" + size + "&recommend=1",
                type: 'GET',
                xhrFields: {
                    withCredentials: true
                },
                crossDomain: true,
                dataType: "json",
            }).done(function (msg) {
                var res = msg.res;
                if (res !== 0) {
                    var newsJson = msg.obj.dataList;
                    msg = msg.obj;
                    for (var i = 0; i < msg.dataList.length; i++) {
                        if (msg.dataList[i].image !== "") {
                            msg.dataList[i].image = msg.dataList[i].image.split(",");
                            for (var j = 0; j < msg.dataList[i].image.length; j++) {
                                msg.dataList[i].image[j] = apiUrlPic + msg.dataList[i].image[j];
                            }
                        }
                        //msg.dataList[i].price = msg.dataList[i].price / 100;
                    }
                    newsJson = msg.dataList;
                    var html = template('product-list-tpl', msg);
                    if (newsJson && newsJson.length > 0) {
                        goodsList.isLoading = false;
                        $noRec.hide();
                    } else {
                        goodsList.isLoading = true;
                        $noRec.show();
                    }
                    if (goodsList.page > 1) {
                        $goodsList.append(html);
                    } else {
                        $goodsList.html(html);
                    }
                    $cloading.hide(); //隐藏加载框
                    // $('#dowebok').fullpage({
                    //     scrollingSpeed: 500,
                    //     scrollOverflow: true
                    // });
                } else {
                    $noRec.show();
                    $cloading.hide();
                }
            });
        }
    };
    getArticle();
    $(window).scroll(function () {
        //滚动高度 + 窗口高度 + (底部导航高度 + 版权块高度) >= 文档高度，注意：文档高度不包括fixed定位的元素（分类导航、底部导航）
        if ($(document).scrollTop() + $(window).height() + (50 + 50) >= $(document).height()) {
            //goodsList.getMore(false); //获取数据
        }
    });

    goodsList.getMore(true);
    function getArticle() {
        var url = apiUrl + "/front/article/article/getArticleByPage";
        $.ajax({
            url: url,
            data: "pageNo=1 &pageSize=2",
            type: 'GET',
            crossDomain: true,
            dataType: "json",
        }).done(function (msg) {
            res = msg.res;
            if (res == 1) {
                msg = msg.obj;
                var html = template('article_list_tp', msg);
                $("#article_list").html(html);
            }
        });
    }

    getAd();
    function getAd() {
        var url = apiUrl + "/front/ad/ad/getAdByPage";
        $.ajax({
            url: url,
            data: "pageNo=1 &pageSize=5",
            type: 'GET',
            crossDomain: true,
            dataType: "json"
        }).done(function (msg) {
            res = msg.res;
            if (res == 1) {
                msg = msg.obj;
                console.log(msg)
                var listDom='';
                for (var i = 0; i < msg.dataList.length; i++) {
                    if (msg.dataList[i].image.length > 0) {
                        // msg.dataList[i].image = apiUrlPic + msg.dataList[i].image;
                        listDom+='<li><a class="pic" href="'+msg.dataList[i].url+'"><img src="'+ apiUrlPic + msg.dataList[i].image+'"></a></li>'
                    }
                }
                //var html = template('ad_list_tpl', msg);

                $("#ad_list").html(listDom);
                TouchSlide({
                    slideCell: "#slideBox",
                    titCell: ".hd ul", //开启自动分页 autoPage:true ，此时设置 titCell 为导航元素包裹层
                    mainCell: ".bd ul",
                    effect: "leftLoop",
                    autoPage: true,//自动分页
                    autoPlay: true ,//自动播放
                    interTime:5000
                });
            }
        });
    }

    getMessage();
    function getMessage() {
        var url = apiUrl + "/front/message/message/getMessageLevel";
        $.ajax({
            url: url,
            data: "",
            xhrFields: {
                withCredentials: true
            },
            type: 'GET',
            crossDomain: true,
            dataType: "json"
        }).done(function (msg) {
            res = msg.res;
            if (res == 1) {
                //msg = msg.obj;
                var html = template('msg_list_tpl', msg);
                $("#msg_list").html(html);

            }
        });
    }

    $("#msg_list").on("click", ".send-modal", function () {
        var _url = $(this).attr("url");
        var _userMsgId = $(this).attr("userMsgId");
        var _id=$(this).attr("id");
        if(_url.length > 0){
            url = _url;
        } else {
            url = "/page/stationMessageInfo.html?id="+ _id + "&userMessageId=" + _userMsgId;
        }
        window.location.href = url;
    });

});
