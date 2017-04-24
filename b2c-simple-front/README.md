

## 此项目用到的技术
   jquery，artTemplate，require，gulp，sass

## 注意事项
   1、使用该项目时请先安装gulp：1、下载安装node.js; 2、在本目录下运行以下命令：
   $ npm install -g cnpm --registry=https://registry.npm.taobao.org
   $ npm install -g gulp 
   $ cnpm install
   $ gulp
   
   2、如何通过相应require加载相应页面的脚本：
	 <script id="script" src="/src/libs/require.min.js" defer async="true" require-module="./manage_goods_list" data-main="/src/js/config.js"></script>
    通过script标签属性require-module加载页面manage_goods_list.js文件；   
    详情请见demo
	
   3、请将所有的页面的资源路径请均采用绝对路径	   
   
## 目录结构

- src/sass/
    + 存放.scss 源码

- src/html/content/
    + 存放普通页面html文件

- src/html/manage/
    + 存放管理后台页面html文件
	
- src/font/
    + 存放 字体文件

- src/img/
    + 存放 图片文件

- src/js/
    + 存放js源码 

- src/lib/
    + 存放第三方依赖类库，如 zeptojs

- dist/
    + 发布后的文件

- gulpfile.js
    + gulp 配置文件
	
- src/js/config.js
    + require配置与共用的请求域名的配置文件
	
- src/js/common.js
    +前端页面共用js
	
- src/js/components.js
    +js组件存放处	
	
- src/js/merber_common.js
    +会员中心共用js	

- src/js/manage_common.js
    +管理中心共用js		
	

## gulp命令
   
   gulp            启动本地服务器，默认端口4865，支持热加载，如果改动共用的html文件，请重新启动本地服务器   
   gulp col        编译所有文件
   gulp html       编译html文件
   gulp htmlmin    发布html文件
   gulp js         发布js
   gulp sass       编译sass
   gulp css        发布css
   gulp clean      清理发布目录
   gulp deploy     发布所有文件到dist
   gulp help       命令说明


   


   
   
