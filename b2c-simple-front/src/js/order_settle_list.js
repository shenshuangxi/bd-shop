define(['jquery', "components", "common", "template", "weui"], function (jquery, components, common, template, weui) {
    var $orderList = $("#order-box");
    var $loadmore = $("#weui-loadmore");
    var $noRec = $('#noRec');
    var _payState = null;
    var _paymentId = null;
    var _logisticsState = null;
    var money = 0;
    var num = 0;
    var orderList = {
        page: 0, //触发获取数据的数次(+1等于页码)
        size: 15, //每次触发取的记录条数
        isLoading: false, //列表是否加载中，避免重复触底加载
        url:apiUrl + "/front/order/order/getLJOrderByPage?pageSize=4",
        getMore: function (first) {
            if (orderList.isLoading) //取数过程中，先停止重复取数
                return;

            if (first) {
                orderList.page = 1;
                $('#noRec').hide();
                $orderList.html('');
            } else {
                orderList.page += 1;
            }
            $loadmore.show(); //显示加载框
            orderList.isLoading = true;
            setTimeout(orderList.d(orderList.page, orderList.size), 1000); //模拟延迟取数据
        },

        //异步获取文章列表
        d: function (page, size) {
            $.ajax({
                url: orderList.url,
                data: "pageNo=" + page + "&pageSize=" + size,
                type: 'GET',
                xhrFields: {
                    withCredentials: true
                },
                crossDomain: true,
                dataType: "json",
            }).done(function(msg) {
                var res = msg.res;
                if (res !== 0) {
                    msg = msg.obj;
                    for (var i = 0; i < msg.dataList.length; i++) {
                        msg.dataList[i].goodsName = msg.dataList[i].orderDetailsList[0].goodsName;
                        //msg.dataList[i].orderDetailsList[0].image = msg.dataList[i].orderDetailsList[0].image.split(",");
                        //msg.dataList[i].image = apiUrlPic + msg.dataList[i].orderDetailsList[0].image[0];
                        msg.dataList[i].totalAmount = msg.dataList[i].totalAmount / 100;
                        for (var j = 0; j < msg.dataList[i].orderDetailsList.length; j++) {
                            console.log(msg.dataList[i].orderDetailsList[j].image);
                            var img = msg.dataList[i].orderDetailsList[j].image;
                            var imgs;
                            if (img.length > 0) {
                                imgs = img.split(",");
                                if (imgs.length > 0) {
                                    msg.dataList[i].orderDetailsList[j].image = apiUrlPic + imgs[0];
                                }
                            }
                        }
                    }
                    var newsJson = msg.dataList;
                    var html = template('order-box-tpl', msg);
                   // $("#order-box").html(html);
                    for (var x in msg.dataList) {
                        $("#order-box").on("click", "#checkbox_" + x, function () {
                            getTotalAmount(this.id.substring(9));
                        });
                    }
                    ;
                    if (newsJson && newsJson.length > 0) {
                        orderList.isLoading = false;
                        $noRec.hide();
                    } else {
                        orderList.isLoading = true;
                        $noRec.show();
                    }
                    if (orderList.page > 1) {
                        $orderList.append(html);
                    } else {
                        $orderList.html(html);
                    }
                    $loadmore.hide(); //隐藏加载框
                } else {
                    $noRec.show();
                    $loadmore.hide();
                }

            });
        }
    };
    $(window).scroll(function () {
        var num1 = $(document).scrollTop() + $(window).height() + (50 + 50);
        var num2 = $(document).height();
        //滚动高度 + 窗口高度 + (底部导航高度 + 版权块高度) >= 文档高度，注意：文档高度不包括fixed定位的元素（分类导航、底部导航）
        if (num1 >= num2) {
            orderList.getMore(false); //获取数据
        }
    });
    orderList.getMore(true);

    function getUserInfo() {
        components.getMsg(apiUrl + "/front/user/user/getUserById").done(function (msg) {
            var res = msg.res;
            if (res == 1) {
                msg = msg.obj;
                var html = template('user-info-tpl', msg);
                $("#user-info").html(html);
            }
        });
    }

    $("#order-box").on("click", ".send-modal", function () {
        var _orderNumber = $(this).attr("oNum");
        url = apiUrl + "/front/order/order/buyGoodsAgain?orderNumber=" + _orderNumber;
        components.getMsg(url).done(function (msg) {
            var res = msg.res;
            if (res == 1) {
                window.location.href = '/page/order_submit.html';
            } else {
                $.toast("璐拱澶辫触", "text");
            }
        });
    });

    $("#order_submit").click(function () {
        if (money != null && money >= 10000) {
            $.toast("閲戦瓒呰繃10000", "text");
            return;
        }
        var url = apiUrl + "/front/order/order/joinOrder";
        var moduleCheckBox = document.getElementsByName("checkbox");
        var orderNumber = "";
        for (var i = 0; i < moduleCheckBox.length; i++) {
            if (moduleCheckBox[i].checked) {
                if (orderNumber.length != 0) {
                    orderNumber = orderNumber + ",";
                }
                orderNumber = orderNumber + (moduleCheckBox[i].value);
            }
        }
        var data = {
            "orderNumbers": orderNumber,
        }
        components.getMsg(url, data, "post").done(function (msg) {
            var res = msg.res;
            if (res == 1) {
                // msg = msg.obj;
                window.location.href = '/page/pay_check.html?orderNumber=' + msg.result + '&price=' + money;
            }
        });
    });
    function getTotalAmount(value) {
        var totalAmount = $("#totalAmount_" + value).val();
        var moduleCheckBox = document.getElementsByName("checkbox");
        money = $("#total-money").text();
        if (moduleCheckBox[value].checked) {
            money = parseInt(money) + parseInt(totalAmount);
            num++;
        } else {
            money = parseInt(money) - parseInt(totalAmount);
            num--;
        }
        $("#total-money").text(money);
        $("#num").text(num);
    }

    $("#total-money").text(money);
    $("#num").text(num);
});
