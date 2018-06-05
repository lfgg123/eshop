var curWwwPath=window.document.location.href; 
		//获取主机地址之后的目录如：/Tmall/index.jsp 
		var pathName=window.document.location.pathname; 
		var pos=curWwwPath.indexOf(pathName); 
		//获取主机地址，如： http://localhost:8080 
		var localhostPaht=curWwwPath.substring(0,pos); 
		//获取带"/"的项目名，如：/Tmall 
		var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1); 
		var path = localhostPaht+projectName+"/";
		//path="http://47.93.18.21/eshop/";
		//path="http://127.0.0.1:8087/";
		//图片地址
		var proPath="http://47.93.18.21:80/";
		var productPath = "http://47.93.18.21:80"+projectName+"/image/";
		//图片地址
		var proImg=proPath+"em/es_pro/";
		var praImg=proPath+ "em/es_myself/";
		var pruImg=proPath+"nmw/";
		//用户头像path
		var thumbPath = proPath+"nmw/thumb/";
		//论坛图片path
		var communityPath = proPath+"nmw/community/";
		//var userPath = proPath+"nmw/user_img/";
		var userPath = proPath+"nmw/";
		var storeImg= proPath+"em/es_store/";
		var privateImg=proPath+"em/es_myself";
		
		
		//工具方法， 从url里获取参数
		function getUrlParam(key){
			var url = window.location.href;
			var params = url.split("?")[1].split("&");
			for(var i=0; i< params.length; i++){
				if(key == params[i].split("=")[0]){
					return params[i].split("=")[1];
				}
			}
			//没找到此参数
			return "";
		}