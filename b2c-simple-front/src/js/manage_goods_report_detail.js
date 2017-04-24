define(["jquery", "components", "bootstrap", "manageCommon", "jqueryValidate","highcharts"], function (jquery, components, bootstrap, manageCommon, jqueryValidate,highcharts) {
    listUrl = apiUrl + "/admin/order/orderAdmin/getDetailReportByOrderDetailsAdminSearchVO";
	
	 var goodsName= components.GetQueryString("goodsName");
	 var createTime_le= components.GetQueryString("createTime_le");
	 var createTime_ge= components.GetQueryString("createTime_ge");
	//alert(goodsName+" "+createTime_le+" "+createTime_ge);
	if(goodsName == undefined ||goodsName=="" ||createTime_le == undefined ||createTime_le=="" ||createTime_ge == undefined ||createTime_ge==""){
		return;
	}else{
		initData(goodsName,createTime_le,createTime_ge);
	}
	 
	function initData(goodsName,createTime_le,createTime_ge) {
       	var data = "";	
			data=data+"goodsName="+goodsName+"&createTime_le="+createTime_le+"&createTime_ge="+createTime_ge;		
        var url = listUrl;
        components.getMsg(url, data, "post").done(function(msg) {
            var res = msg.res;
            if (res == 1) {
                msg.dataList = msg.list;
				if(msg.dataList != undefined && msg.dataList.length>0){
					//给时间定义了两个全局变量,方便下面字符串拼接
					var createTime = "";
					var createTimeTemp = "";
					//给销量数据定义了两个全局变量,方便下面字符串拼接
					var num = "";
					var numTemp = "";
					//给销售金额定义了两个全局变量,方便下面字符串拼接
					var detailsAmount="";
					var detailsAmountTemp="";
					var goodsName="";
					var unitName="";
					for (var j = 0; j < msg.dataList.length; j++) {
						msg.dataList[j].createTime = components.formatDate(msg.dataList[j].createTime);
						//取值
						var createTimeTemp=msg.dataList[j].createTime;
						var numTemp=msg.dataList[j].num;
						var detailsAmountTemp=msg.dataList[j].detailsAmount;
						unitName=msg.dataList[j].unitName;
						goodsName=msg.dataList[j].goodsName;
						//把单个数据循环从后台拿到的json数组里取出来，拼接成字符串
						createTime=createTime += ",'"+createTimeTemp+"'"; 
						num=num += ","+numTemp+"";
						detailsAmount=detailsAmount+=","+detailsAmountTemp/100+"";
					}
					createTime=createTime.substring(1); //前面拼接字符串，逗号是放在前面的，这个意思是截取掉第一个字符
					createTime="["+createTime+"]";  //到这一步是把字符串拼接成json数组的样式，但不是json数组
					num=num.substring(1);
					num="["+num+"]";
					detailsAmount=detailsAmount.substring(1);
					detailsAmount="["+detailsAmount+"]";					
					showDetailReport(goodsName,unitName,createTime,num,detailsAmount);//显示报表
				}
				/* for (var i = 0; i < msg.dataList.length; i++) {
                    msg.dataList[i].unitPrice = msg.dataList[i].unitPrice /100.00;
					msg.dataList[i].detailsAmount = msg.dataList[i].detailsAmount /100.00;
                }  */                
            } else {
                
            }
        });
    }
	
	
	//显示报表
	//goodsName 商品名称,unitName 规格名称,createTime 创建时间（x坐标）,num 销量,detailsAmount 销售金额
	function showDetailReport(goodsName,unitName,createTime,num,detailsAmount){
		if(goodsName==undefined ||unitName==undefined||createTime==undefined||num==undefined||detailsAmount==undefined){
			return;
		}
		$("#reportdetail").highcharts({ //图表展示容器，与div的id保持一致
            //默认是折线图，所以chart: {type:'line',},不用写
            title: { //头部
                text: goodsName+'('+unitName+')', //text：标题的文本
                x: -20,
            },
            credits: {
                text: '',
                href: ''
            },
            subtitle: { //副标题，写不写都行
                text: '',
                x: -20
            },
            xAxis: { //X坐标轴   categories类别
                categories: eval(createTime), 
                plotLines: [{  //plotLines：标示线
                    value: -1,  //定义在哪个值上显示标示线，这里是在x轴上刻度为3的值处垂直化一条线
                    width: 1,  //标示线的宽度，2px
                    dashStyle:'solid',  //默认值是solid实线，这里定义为虚线
                    color: 'red',//线的颜色，定义为红色
                }]
            },
            yAxis: { //Y坐标轴
                title: {
                    text: '销售量/销售额'
                },                
            },
            tooltip: { //数据提示框
                valueSuffix: '',  //highcharts 提供了 valuePrefix(前缀)、valueSuffix（后缀） 来给数据添加前缀及后缀
            },                        //比如说 valuePrefix: '￥', valueSuffix: '元'
            legend: { //图例
                layout: 'horizontal',  //图例内容布局方式，有水平布局及垂直布局可选，对应的配置值是： “horizontal(水平)”， “vertical(垂直)”
                align: 'center',  //图例在图表中的对齐方式，有 “left”, "center", "right" 可选
                verticalAlign: 'bottom',  //垂直对齐方式，有 'top'， 'middle' 及 'bottom' 可选
                borderWidth: 1 //边框宽度
            },
            series:[ //数据列
                {  //数据列中的 name 代表数据列的名字，并且会显示在数据提示框（Tooltip）及图例（Legend）中
                    name: '销售额',
                    data:eval(detailsAmount),
                },{
                    name: '销售量',
                    data:eval(num)
                },
                ]
        });
	}


});