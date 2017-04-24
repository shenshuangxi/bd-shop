define(['zepto', "components", "common", "weui", "touch", "template"], function(zepto, components, common, weui, touch, template) {
    getData();

    function getData() {
        components.getMsg(apiUrl + "/front/receive/receive/getReceiveByPage").done(function(msg) {
            var res = msg.res;
            if (res == 1) {
                var html = template('address-list-tpl', msg);
                document.getElementById('address-list').innerHTML = html;
                setDefultAddress();
                addEffect();
            }else{
              document.getElementById('address-list').innerHTML = '<div class="weui-loadmore weui-loadmore_line"><span class="weui-loadmore__tips">暂无数据</span></div>';
            }
        });
    }

    function setDefultAddress() {
        $("#address-list li").click(function(e) {
            var rid = $(this).attr("rid");
            if (!($(e.target).hasClass("delete") || $(e.target).hasClass("icon-delete"))) {
                components.getMsg(apiUrl + "/front/receive/receive/setDefaultReceive", "receiveId=" + rid, "post").done(function(msg) {
                    var res = msg.res;
                    if (res == 1) {
                        //history.go(-1);
                        //location.reload();
                        location.href=document.referrer;
                    } else {
                        console.log("false");
                    }
                });
            } else {
                $.confirm({
                    text: '确定要删除此地址吗？',
                    onOK: function() {
                        components.getMsg(apiUrl + "/front/receive/receive/delReceiveById?receiveId=" + rid).done(function(msg) {
                            var res = msg.res;
                            if (res == 1) {
                                $.toast("删除成功", "text");
                                getData();
                            }
                        });
                    },
                });
            }
        });
    }


    function addEffect() {
        var $addressItem = $('#address-list li');
        $addressItem.on("swipeLeft", function() {
            var $this = $(this);
            $this.find(".edit").addClass("hide");
            $this.find(".delete").removeClass("hide");
            event.preventDefault();
        });
        $addressItem.on("swipeRight", function() {
            var $this = $(this);
            $this.find(".edit").removeClass("hide");
            $this.find(".delete").addClass("hide");
            event.preventDefault();
        });
    }

});
