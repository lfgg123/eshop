	package com.eshop.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alipay.api.internal.util.StringUtils;
import com.eshop.exception.EshopAppGetParaException;
import com.eshop.model.Order;
import com.eshop.model.OrderReturn;
import com.eshop.service.AppOrderReturnService;
import com.eshop.service.AppOrderService;
import com.eshop.util.IoFileUtil;
import com.eshop.util.JacksonUtils;
import com.eshop.vo.ResultJson;
import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;

public class AppOrderController extends Controller {
	private AppOrderService orderService = new AppOrderService();
	private AppOrderReturnService orderReturnService = AppOrderReturnService.service;
	

	/**
	 * 获取当前用户全部订单列表
	 * 
	 * @throws Exception
	 * 
	 */
	public void getMemberAllOrderList() throws Exception {
		List<Order> orders = orderService.getAllOrderListByMemberGuid(getPara("TOKEN"));
		if (null == orders) {
			orders = new ArrayList<>();
		}else {
			for(Order order:orders) {
				order.setOrderItemsBySql();
			}
		}
		renderJson(JacksonUtils.obj2json(ResultJson.success(orders)));
	}

	/**
	 * 获取当前用户待付款订单列表
	 * 
	 * @throws Exception
	 * 
	 */
	public void getMemberWaitToPayOrderList() throws Exception {
		List<Order> orders = orderService.getWaitToPayOrderListByMemberGuid(getPara("TOKEN"));	
		if (null == orders) {
			orders = new ArrayList<>();
		}
		renderJson(JacksonUtils.obj2json(ResultJson.success(orders)));
	}
	
	
	/**
	 * 获取当前用户待配送订单列表
	 * 
	 * @throws Exception
	 * 
	 */
	public void getMemberWaitToDeliveryOrderList() throws Exception {
		List<Order> orders = orderService.getWaitToDeliveryOrderListByMemberGuid(getPara("TOKEN"));	
		if (null == orders) {
			orders = new ArrayList<>();
		}
		renderJson(JacksonUtils.obj2json(ResultJson.success(orders)));
	}

	/**
	 * 获取当前用户配送中订单列表
	 * 
	 * @throws Exception
	 * 
	 */
	public void getMemberBeingDeliveredOrderList() throws Exception {
		List<Order> orders = orderService.getBeingDeliveredOrderListByMemberGuid(getPara("TOKEN"));
		if (null == orders) {
			orders = new ArrayList<>();
		}else {
			for(Order order:orders) {
				order.setOrderItemsBySql();
			}
		}
		renderJson(JacksonUtils.obj2json(ResultJson.success(orders)));
	}
	
	/**
	 * 获取当前用户配送完成订单列表
	 * 
	 * @throws Exception
	 */
	public void getCurrentUserCompletedDeliveryOrderList() throws Exception {
		List<Order> orders = orderService.getCompletedDeliveryOrderListByMemberGuid(getPara("TOKEN"));
		if (null == orders) {
			orders = new ArrayList<>();
		}
		renderJson(JacksonUtils.obj2json(ResultJson.success(orders)));
	}

	/**
	 * 获取当前用户待评价订单列表
	 * 
	 * @throws Exception
	 */
	public void getMemberWaitToCommentOrderList() throws Exception {
		List<Order> orders = orderService.getCurrentUserWaitToCommentOrderList(getPara("TOKEN"));
		if (null == orders) {
			orders = new ArrayList<>();
		}else {
			for(Order order:orders) {
				order.setOrderItemsBySql();
			}
		}
		renderJson(JacksonUtils.obj2json(ResultJson.success(orders)));
	}

	/**
	 * 获取当前用户售后订单列表
	 * 
	 * @throws Exception
	 */
	public void getMemberAfterSaleOrderList() throws Exception {
		List<Order> orders = orderService.getCurrentUserAfterSaleOrderList(getPara("TOKEN"));
		if (null == orders) {
			orders = new ArrayList<>();
		}else {
			for(Order order:orders) {
				order.setOrderItemsBySql();
			}
		}
		renderJson(JacksonUtils.obj2json(ResultJson.success(orders)));
	}

	/**
	 * 获取新任务列表
	 * 
	 */
	public void getNewTaskList() {
		List<Order> orderList = orderService.getNewTaskList();
		if (null == orderList) {
			orderList = new ArrayList<>();
		}
		renderText(JacksonUtils.obj2json(ResultJson.success(orderList)));

	}

	/**
	 * 获取待取货任务列表
	 */
	public void getWaitToFetchTaskList() {
		List<Order> orderList = orderService.getWaitToFetchTaskList();
		if (null == orderList) {
			orderList = new ArrayList<>();
		}
		renderText(JacksonUtils.obj2json(ResultJson.success(orderList)));
	}

	/**
	 * 获取配送中任务列表
	 */
	public void getBeingDeliveredTaskList() {
		List<Order> orderList = orderService.getBeingDeliveredTaskList();
		if (null == orderList) {
			orderList = new ArrayList<>();
		}
		renderText(JacksonUtils.obj2json(ResultJson.success(orderList)));

	}

	/**
	 * 获取已完成任务列表
	 */
	public void getCompletedTaskList() {
		List<Order> orderList = orderService.getCompletedTaskList();
		if (null == orderList) {
			orderList = new ArrayList<>();
		}
		renderText(JacksonUtils.obj2json(ResultJson.success(orderList)));
	}

	/**
	 * 提交售后申请
	 * @throws Exception 
	 */
	public void saveOrderReturn() throws Exception {
		UploadFile img = getFile();
		List<UploadFile> list=new ArrayList<UploadFile>();
		list.add(img);
		IoFileUtil.saveImages(list);
		OrderReturn orderReturn = getBean(OrderReturn.class,"");
		orderReturn.setUpimage(img.getFileName());
		boolean result = orderReturnService.saveOrderReturn(orderReturn);
		if (result) {	
			renderText(JacksonUtils.obj2json(ResultJson.success("提交申请成功!")));
		} else {
			renderText(JacksonUtils.obj2json(ResultJson.fail("操作失败")));
		}
	}

	/**
	 * 根据类型获取用户售后列表
	 * @throws Exception 
	 */
	public void getOrderReturnSuccessListByType() throws Exception {
		Integer type = getParaToInt("type");
		List<Map> orderReturns = orderReturnService.getOrderReturnSuccessList(type, getPara("TOKEN"));
		if(null == orderReturns) {
			orderReturns = new ArrayList<>();
		}
		renderText(JacksonUtils.obj2json(ResultJson.success(orderReturns)));
	}

}
