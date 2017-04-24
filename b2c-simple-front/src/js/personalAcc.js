define(['jquery', "components", "common", "template", "weui"], function(jquery, components, common, template, weui) {
    getUserInfo();
    // getOrderList();
    $("#index-menu li").eq(2).addClass("active");
    var headimgrul=null;
    var nickname=null;
    var phone=null;
    var $orderList = $("#order-box");
    function getUserInfo() {
        components.getMsg(apiUrl + "/front/user/user/getUserById").done(function(msg) {
            var res = msg.res;
            if (res == 1) {
                msg = msg.obj;
                var html = template('user-info-tpl', msg);
                $("#user-info").html(html);
            }
        });
    }
    upPic();
    function upPic() {
        $("#user-info").on("change", "#uploaderInput", function() {
            var formData = new FormData(); //构造空对象，下面用append 方法赋值。
            formData.append("param", "");
            formData.append("file", $("#uploaderInput")[0].files[0]);
            var url = apiUrl + "/upload/uploadimg/dydz/user";
            $.ajax({
                url: url,
                type: 'POST',
                dataType: "json",
                data: formData,
                processData: false,
                contentType: false,
                success: function(msg) {
                    updateHeadimgurl(msg.result);

                }

            });
        });
    }
    function updateHeadimgurl(headimgurl){
         headimgurl=apiUrlPic +headimgurl;
        var data={
            'headimgurl' :headimgurl,
        };
        var url =apiUrl + "/front/user/user/updateUser";
        components.getMsg(url,data,"POST").done(function (msg) {
            var res = msg.res;
            if (res == 1) {
                window.location.href = '/page/personalAcc.html';
            }
        });
    }
    $("#user-info").on("change", "#nickname", function() {
        var nickname =$("#nickname").val();
        var data={
            'nickname' :nickname,
        };
        var url =apiUrl + "/front/user/user/updateUser";
        components.getMsg(url,data,"POST").done();
    });
    $("#user-info").on("change", "#phone", function() {
        var phone=$("#phone").val();
        var data={
            'phone' :phone,
        };
        var url =apiUrl + "/front/user/user/updateUser";
        components.getMsg(url,data,"POST").done();
    });

});
