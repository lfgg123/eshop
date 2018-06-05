<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<span ng-controller="userInfo">
	<br>
	<ol class="breadcrumb">
	  <li><a href="#">系统管理</a></li>
	  <li><a href="#">系统设置</a></li>
	</ol>
	
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="row">
				<div class="col-xs-8">
					<h4>
						系统设置 <small> - 系统基本信息操作</small>
					</h4>
				</div>
				
			</div>
		</div>
		<div class="panel-body">
			<form class="form-horizontal">
									    	  <div class="form-group">
											    <label for="exampleInputName2" class="col-sm-2 control-label">商城名称</label>
											    <div class="col-sm-5">
											      <input type="text" class="form-control" id="exampleInputName2" placeholder="请输入商城名称">
											    </div>
											  </div>
											 
											  <div class="form-group">
											      <label for="exampleInputFile" class="col-sm-2 control-label">LOGO</label>
											      <div class="col-sm-5" style="margin-top:5px">
											        <input type="file" id="exampleInputFile">
											        <img src="image/default_2.png" style="margin-top:10px" class="img-responsive">
											      </div>
											   
		                                      </div>
		                                     
		                                      <div class="form-group">
											    <label for="exampleInputEmail2" class="col-sm-2 control-label">描述</label>
											    <div class="col-sm-5">
											      <input type="email" class="form-control" id="exampleInputEmail2" placeholder="请输入商城描述">
											    </div>
											  </div>
											   <div class="form-group">
											    <label for="exampleInputEmail2" class="col-sm-2 control-label">关键词</label>
											    <div class="col-sm-5">
											      <input type="email" class="form-control" id="exampleInputEmail2" placeholder="请输入商城关键词">
											    </div>
											  </div>
											    <div class="form-group">
											    <label for="exampleInputEmail2" class="col-sm-2 control-label">标题</label>
											    <div class="col-sm-5">
											      <input type="email" class="form-control" id="exampleInputEmail2" placeholder="请输入商城标题">
											    </div>
											  </div>
											  <div class="form-group">
											    <label for="inputPassword3" class="col-sm-2 control-label">地址</label>
											    <div class="col-sm-5">
											      <input type="password" class="form-control" id="inputPassword3" placeholder="请输入联系地址">
											    </div>
											  </div>
											  
											   <div class="form-group">
											    <label for="exampleInputEmail2" class="col-sm-2 control-label">邮编</label>
											    <div class="col-sm-5">
											      <input type="email" class="form-control" id="exampleInputEmail2" placeholder="请输入商城邮编">
											    </div>
											  </div>
											   <div class="form-group">
											    <label for="exampleInputEmail2" class="col-sm-2 control-label">联系电话</label>
											    <div class="col-sm-5">
											      <input type="email" class="form-control" id="exampleInputEmail2" placeholder="请输入商城联系电话">
											    </div>
											  </div>
											   <div class="form-group">
											    <label for="exampleInputEmail2" class="col-sm-2 control-label">客服电话</label>
											    <div class="col-sm-5">
											      <input type="email" class="form-control" id="exampleInputEmail2" placeholder="请输入商城客服电话">
											    </div>
											  </div>
											   <div class="form-group">
											    <label for="exampleInputEmail2" class="col-sm-2 control-label">Email</label>
											    <div class="col-sm-5">
											      <input type="email" class="form-control" id="exampleInputEmail2" placeholder="请输入商城电子邮件">
											    </div>
											  </div>
		                                      <div class="form-group">
												  <div class="col-md-5 col-sm-offset-2" >
												  <button type="button" class="btn btn-success btn-lg">保存</button>   
												  </div>
											  </div>
		                                      

		                                    </form>
			
		</div>
		
	</div>
</span>