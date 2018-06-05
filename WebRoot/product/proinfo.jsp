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
	  <li><a href="#">商品管理</a></li>
	  <li><a href="#">商品信息</a></li>
	</ol>
	<div class="panel panel-default">
		<div class="panel-body" id="isQuery">
			<form class="form-inline J-form-line">
				<div class="form-group">
					<label for="" class="control-label">查询条件:</label> <input ng-model="keyword" name="keyword"
						id="keyword" type="text" class="form-control input-sm" placeholder="商品名称">
				</div>
				<div class="form-group">
					<button type="button" class="btn btn-success btn-sm isQuery" ng-click="search()">
						<i class="fa fa-search" aria-hidden="true"></i> 查询
					</button>
				</div>
			</form>
		</div>
	</div>
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="row">
				<div class="col-xs-8">
					<h4>
						商品信息 <small>商品信息操作</small>
					</h4>
				</div>
				<div class="col-xs-4">
					<div class="btn-group pull-right" role="group">
					  	<button type="button" class="btn btn-success" ng-click="clearHead();">新增</button>
					</div>
					
				</div>
			</div>
		</div>
		<div class="panel-body">
			
			<table class="table table-striped table-hover J-table1">
				<thead>
					<tr>
						<th class="text-center" width="50"></th>
						<!-- <th class="text-center">ID</th> -->
						<th>商品名称</th>
						<th>商品价格</th>
						<th>商品图片</th>
						<th>商品销量</th>
						<th>是否上架</th>
						<th>是否畅销</th>
						<th>商品库存</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
				<tr>
				<td><input type="checkbox" ng-model="checkstate" ng-change="selectRow()"></td>
					<td>江小白</td>
					<td>￥ 25-180</td>
					<td><img src="<%=basePath%>image/default_1.png"
							class="img-circle" height="38px"></td>
					<td>100</td>
					<td>是</td>
					<td>是</td>
					<td>250000</td>
					<td><a class="btn btn-success btn-xs" href="javascript:void(0)" ng-click="openAcc(datalist.id)" role="button">上下架</a>
							<a class="btn btn-danger btn-xs" href="javascript:void(0)" ng-click="downAcc(datalist.id)" role="button">畅销</a></td>
					
				</tr>
				
				<!-- 
					<tr ng-show="showTr">
						<td colspan="8" align="center" scope="row">※没有找到相关数据!</td>
					</tr>
					<tr ng-repeat="datalist in datalist" ng-class="rowClass(datalist)" ng-click="selectRowClick()">
						<td><input type="checkbox" ng-model="checkstate" ng-change="selectRow(datalist.id,checkstate)"></td>
						<td><img src="<%=basePath%>images/headImg/{{datalist.img}}"
							onerror="this.src='<%=basePath%>image/default_1.png'"
							class="img-circle" height="38px"></td>
						<td>{{datalist.phone}}</td>
						<td>{{datalist.nick}}</td>
						<td>{{datalist.sex}}</td>
						<td>{{datalist.age}}</td>
						<td>{{datalist.point}}</td>
						<td>{{datalist.state==1?'正常':'禁用'}}</td>
						<td><a class="btn btn-success btn-xs" href="javascript:void(0)" ng-click="openAcc(datalist.id)" role="button">启用</a>
							<a class="btn btn-danger btn-xs" href="javascript:void(0)" ng-click="downAcc(datalist.id)" role="button">禁用</a></td>
					</tr>
					 -->
				</tbody>
			</table>
		</div>
		<div class="panel-footer clearfix">
			<ul class="J-pagination pull-right form-inline"
				style="margin:0px; padding:0px;">
				<nav aria-label="Page navigation">
  <ul class="pagination">
    <li>
      <a href="#" aria-label="Previous">
        <span aria-hidden="true">&laquo;</span>
      </a>
    </li>
    <li><a href="#">1</a></li>
    <li><a href="#">2</a></li>
    <li><a href="#">3</a></li>
    <li><a href="#">4</a></li>
    <li><a href="#">5</a></li>
    <li>
      <a href="#" aria-label="Next">
        <span aria-hidden="true">&raquo;</span>
      </a>
    </li>
  </ul>
</nav>
			</ul>
		</div>
	</div>
</span>