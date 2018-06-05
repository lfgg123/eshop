<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!doctype html>
<html class="no-js fixed-layout">
<head>
<title>首页</title>
<link rel="icon" href="<%=path %>/image/default_3.png" type="image/x-icon">
<meta name="description" content="首頁">
<meta name="keywords" content="index">
<%@ include file="/comm_css_libs.jsp"%>
<style type="text/css">
	body { padding-bottom: 60px; }
</style>
</head>
<body data-type="index" ng-app="myApp" ng-controller="myCtrl">

	<nav class="navbar navbar-default navbar-fixed-top">
		<div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
					aria-expanded="false">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#"><strong>ES商城系统管理平台</strong>&nbsp;&nbsp;<small>欢迎您，${USERINFO.account}</small></a>
			</div>

			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">

				<ul class="nav navbar-nav navbar-right">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown" role="button" aria-haspopup="true"
						aria-expanded="false">商品管理 <span class="caret"></span></a>
						<ul class="dropdown-menu">
							<li><a href="javascript:void(0)" ui-sref="proinfo" >商品信息</a></li>
							<li><a href="javascript:void(0)" ui-sref="classinfo" >分类信息</a></li>
							<li><a href="javascript:void(0)" ui-sref="specinfo" >规格信息</a></li>
							<li><a href="javascript:void(0)" ui-sref="timebuy" >限时抢购</a></li>
							<li><a href="javascript:void(0)" ui-sref="choice" >精选商品</a></li>
							<li><a href="javascript:void(0)" ui-sref="runinfo" >跑腿商品</a></li>
							<li><a href="javascript:void(0)" ui-sref="enough_cut" >满减活动</a></li>
						</ul>
					</li>
					<li class="dropdown"><a href="javascript:void(0)" class="dropdown-toggle"
						data-toggle="dropdown" role="button" aria-haspopup="true"
						aria-expanded="false">订单管理 <span class="caret"></span></a>
						<ul class="dropdown-menu">
							<li><a href="javascript:void(0)" ui-sref="order1" >待支付</a></li>
							<li><a href="javascript:void(0)" ui-sref="order2">待发货</a></li>
							<li><a href="javascript:void(0)" ui-sref="order3">待收货</a></li>
							<li><a href="javascript:void(0)" ui-sref="order4">已完成</a></li>
							<li><a href="javascript:void(0)" ui-sref="order5">退款/货</a></li>
							<li><a href="javascript:void(0)" ui-sref="order6">待评价</a></li>
						</ul></li>
					<li><a href="javascript:void(0)" ui-sref="myself">私人定制</a></li>
					<li class="dropdown">
						<a href="javascript:void(0)" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
						aria-expanded="false" >会员管理 <span class="caret"></span></a>
						<ul class="dropdown-menu">
							<li><a href="javascript:void(0)" ui-sref="member">会员信息</a></li>
							<li><a href="javascript:void(0)" ui-sref="wine">酒币信息</a></li>
							<li><a href="javascript:void(0)" ui-sref="bbs">会员社区</a></li>
						</ul>
					</li>
					<!--  <li><a href="javascript:void(0)">退款管理</a></li>-->
					<li class="dropdown"><a href="javascript:void(0)" class="dropdown-toggle"
						data-toggle="dropdown" role="button" aria-haspopup="true"
						aria-expanded="false">系统管理 <span class="caret"></span>
					</a>
					<ul class="dropdown-menu">
						<li><a href="javascript:void(0)" ui-sref="sysset" >系统设置</a></li>
						<li><a href="javascript:void(0)" ui-sref="account" >管理账号</a></li>
						<li><a href="javascript:void(0)" ui-sref="module">平台模块</a></li>
						<li><a href="javascript:void(0)" ui-sref="role">角色管理</a></li>
						<li><a href="javascript:void(0)" ui-sref="stores">门店设置</a></li>
						<li><a href="javascript:void(0)" ui-sref="dope">消息设置</a></li>
						<li><a href="javascript:void(0)" ui-sref="substance">内容设置</a></li>
						<!--  <li><a href="javascript:void(0)" ng-click="setRate()">平台费率 {{rate!=null?rate+'%':'未设置'}}</a></li>-->
						<li role="separator" class="divider"></li>
						<li><a href="<%=basePath%>login.jsp">退出系统</a></li>
					</ul></li>
				</ul>
			</div>
			<!-- /.navbar-collapse -->
		</div>
		<!-- /.container-fluid -->
	</nav>
	<div class="container-fluid" ui-view="">
		
	</div>
	<div ui-view="front" class="container-fluid">
		<!-- front page -->
	</div>
	<div ui-view="footer">
		<!-- front page -->
	</div>
	<div id="sysConfigModal" class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel">
     	<div class="modal-dialog modal-sm" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">
						平台费率 <small>设置平台费率</small>
					</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal">
						<input type="hidden" id="id" name="id" ng-model="data.id" />
						<div class="form-group">
							<div class="col-sm-12">
								<div class="input-group">
									<input type="text" class="form-control" id="rate"
										ng-model="rate" placeholder="费率">
									<span class="input-group-addon">%</span>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary button_save" ng-click="saveRate()">提交</button>
				</div>
			</div>
		</div>
	</div>
	<%@ include file="/comm_js_libs.jsp"%>
</body>
</html>

