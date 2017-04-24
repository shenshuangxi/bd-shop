define(['jquery', "components", "common", "template", "weui"], function (jquery, components, common, template, weui) {
    getUserInfo();
    // getOrderList();
    $("#index-menu li").eq(2).addClass("active");
    var $orderList = $("#order-box");

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

    $("#logout").click(function () {
        var url = apiUrl + "/front/user/user/logout";
        components.getMsg(url, null, "post").done(function (msg) {
            var res = msg.res;
            if (res == 1) {
                window.location.href = '/index.html';
            }
        });
    });
	
	addActiveClass(3);
	//底部导航选中方法，index表示选中第几个，从0开始
    function addActiveClass(index){		
		 var childrens=$("#footer_tabbar").children();
		 childrens.each(function(index1,element1){
			 $(element1).removeClass("weui-bar__item_on");			
			 var imgAndp=$(element1).children();
			 var src=imgAndp[0].src;
			 if(src.indexOf("-") > 0 )   
			{   
				src=src.substring(0,src.indexOf("-"))+".png";
			}  
			//imgAndp[0].src=src;
			$(imgAndp[0]).attr("src",src);
			if(index==index1){
				 //字体变色
				$(element1).addClass("weui-bar__item_on"); 
				//imgAndp[0].src=src.replace(".","-green.");
				var	srcsub=src.substr(0,src.lastIndexOf("."))+"-green.png";
				$(imgAndp[0]).attr("src",srcsub);
			}			
		 });
    }

});
