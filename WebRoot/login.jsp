<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
	<title>ES系统后台登录</title>
	<link rel="icon" href="<%=path %>/image/default_3.png" type="image/x-icon">
	<!-- Bootstrap -->
	<link rel="stylesheet" href="<%=basePath%>bootstrap/css/bootstrap.css" >
	<!-- <link rel="stylesheet" href="<%=basePath%>bootstrap/css/dashboard.css" > -->
	<!-- <link rel="stylesheet" href="<%=basePath%>bootstrap/css/my-bootstrap.css" > -->
	<link rel="stylesheet" href="<%=basePath%>font-awesome/css/font-awesome.min.css" >

	<script type="text/javascript" src="<%=basePath%>bootstrap/js/angular.js"></script>
	<script type="text/javascript" src="<%=basePath%>bootstrap/js/my.js"></script>
	<script type="text/javascript" src="<%=basePath%>index.js"></script>
	<style type="text/css">
		body {
			background: url(<%=basePath%>bootstrap/css/login_bg.jpg) center center no-repeat;
			background-size: cover;
			position: relative;
			min-height: 100vh;
			font-family: "Segoe UI","Lucida Grande",Helvetica,Arial,"Microsoft YaHei",FreeSans,Arimo,"Droid Sans","wenquanyi micro hei","Hiragino Sans GB","Hiragino Sans GB W3",FontAwesome,sans-serif;
		}

		.sl-login {
			width: 500px;
			height: 360px;
			position: fixed;
			top: 50%;
			left: 50%;
			margin-left: -250px;
			margin-top: -280px;
			z-index: 100;
			_position: absolute; /*这里开始的2句代码是为ie6不兼容position:fixed;而写的*/
			_top: expression(eval(document.documentElement.scrollTop +160) );
			/*这里需要获取滚动高度+元素原本的高度*/
		}

		.sl-login .login-box {
			width: 500px;
			height: 300px;
			background: #fff;
			border-radius: 10px;
			display: block;
			box-shadow: 0 0 10px #0CC;
			padding: 50px 110px;
		}

		.sl-login h1 {
			width: 500px;
			color: #fff;
			text-align: center;
			font-size: 40px
		}

		.sl-login i {
			color: #60a7e5
		}

		.sl-login .am-alert {
			background-color: #a4d3fc;
			border: #66b0ea solid 1px;
		}

		.sl-login-coptright {
			width: 200px;
			margin: 0 auto;
			position: fixed;
			bottom: 30px;
			left: 50%;
			margin-left: -100px;
			text-align: center;
			color: #333
		}
		.text{background:url(http://d.lanrentuku.com/down/png/1211/blueberry/user_friend.png) no-repeat 0 center;}
		.text input{float:left;border:none;background:none;height:40px;line-height:30px;width:100%; text-indent:32px;}

	</style>
	<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
	<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
	<!--[if lt IE 9]>
	<script src="https://cdn.bootcss.com/html5shiv/3.7.3/html5shiv.min.js"></script>
	<script src="https://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
	<![endif]-->
</head>
<body>

<div class="sl-login" ng-app="eshop" ng-controller="esCtrl">
	<h1>ES后台管理系统</h1>
	<div class="login-box">
		<form class="form-horizontal">
			<div class="form-group">

				<div class="col-xs-12 input-group">
					<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
					<input type="username" class="form-control" name="account" ng-model="formData.account">
				</div>
			</div>
			<div class="form-group">

				<div class="col-xs-12 input-group">
					<span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
					<input type="password" class="form-control" name="password" ng-model="formData.password">
				</div>
			</div>

			<div class="form-group">
				<div class="row">
					<div class="col-xs-6">
						<button type="button" class="btn btn-primary" ng-click="login()">&nbsp;&nbsp;&nbsp;&nbsp;登录&nbsp;&nbsp;&nbsp;&nbsp;</button>
					</div>
					<div class="col-xs-6">
						<button type="button" class="btn btn-default pull-right">忘记密码^_^?</button>
					</div>
				</div>
			</div>
		</form>
		<div class="row" ng-show="msgRow">
			<div class="col-xs-12" style="padding-left:0; padding-right:0">
				<div class="bs-example bs-example-standalone" data-example-id="dismissible-alert-js">
					<div class="alert alert-info alert-dismissible fade in" role="alert">
						<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button>
						<strong>注意</strong> {{msg}}
					</div>
				</div>
			</div>
		</div>

	</div>
</div>

<div class="sl-login-coptright">
	<p>© 2017 eshop</p>
</div>


<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="<%=basePath%>bootstrap/js/jquery.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="<%=basePath%>bootstrap/js/bootstrap.min.js"></script>
<script src="<%=basePath%>js/viewer-jquery.min.js"></script>
<script>

</script>
</body>
</html>
