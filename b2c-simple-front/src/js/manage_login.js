define(['jquery', "jqueryCookie", "components","manageCommon"], function(jquery, jqueryCookie, components, manageCommon) {
    // 全局配置ajax
    document.onkeydown = function(event) {
        var e = event || window.event || arguments.callee.caller.arguments[0];
        if (e && e.keyCode == 13) { // enter 键
            login();
        }
    };
    $("#login-btn").bind("click", function() {
        login();
    });
    //  登录
    function login() {
        var data = "&adminName=" + $("#user-name").val() + "&password=" + $("#password").val();
        var url = apiUrl + "/adminLogin/toLogin";
     //   alert(url)
        components.getMsg(url, data, "get").done(function(msg) {
       //     alert(111);
            var res = msg.res;
            msg = msg.obj;
            if (res == 1) {
                $.cookie("userName", msg.adminName);
                var href = $.cookie("preHrefB");
                if (href === undefined || href == "null") {
                    href = '/page/manage_goods_list.html';
                }
                $.cookie("preHrefB", "null", {
                    path: '/'
                });
                location.href = href;
            } else {
                alert("用户不存在或密码错误");
            }
        });
    }


});
