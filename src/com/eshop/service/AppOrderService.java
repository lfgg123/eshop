package com.eshop.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.eshop.model.Cart;
import com.eshop.model.CartItem;
import com.eshop.model.Goods;
import com.eshop.model.Order;
import com.eshop.model.OrderItem;
import com.eshop.model.Product;
import com.eshop.util.BeanUtils;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class AppOrderService {

	private final static Order dao = Order.dao;

	/**
	 * 获取订单类型
	 * @param orderItems
	 * @param member
	 * @return
	 */
	public Integer getOrderType(List<OrderItem> orderItems,String member){
		Cart cart = Cart.dao.findFirst("select * from shop_cart where member = ?",member);
		for(OrderItem orderItem:orderItems){
			CartItem cartItem = CartItem.dao.findFirst("select * from shop_cart_item where cart_guid = ? and good_guid = ?",cart.getGuid(),orderItem.getGoodGuid());
			if(cartItem != null && 1 == cartItem.getPrdType())
				return 1;
		}
		return 0;
	}

	public boolean deleteOrder(String sn){
		Order order = dao.findFirst("select * from shop_order where sn = ?",sn);
		order.setOrderStatus(9);
		Record updateRecord = new Record().setColumns(order);
		return Db.update("shop_order","id",updateRecord);
	}

	public boolean updateOrder(Order order){
		List<OrderItem> orderItems = OrderItem.dao.find("select * from shop_order_item where sn = ?", order.getSn());
		if (null != orderItems && orderItems.size() > 0) {
			for (OrderItem orderItem : orderItems) {
				Product product = Product.dao.findFirst("select * from shop_product where guid = ( select pro_guid from shop_goods where guid = ? )", orderItem.getGoodGuid());
				product.setSalenum(product.getSalenum()==null?0:product.getSalenum() + orderItem.getQuantity());
				Record updateRecord = new Record().setColumns(product);
				Db.update("shop_product", "id", updateRecord);
			}
		}
		Record updateRecord = new Record().setColumns(order);
		return Db.update("shop_order","id",updateRecord);
	}

	/**
	 * 取消订单，货品的库存数恢复
	 * @param sn
	 * @return
	 */
	public boolean cancelOrder(String sn){
		Order order = dao.findFirst("select * from shop_order where sn = ? and order_status = 0",sn);
		if(null != order) {
			int result = Db.update("update shop_order set order_status = -1 where sn = ? and order_status = 0", sn);
			if (result > 0) {
				List<OrderItem> orderItems = OrderItem.dao.find("select * from shop_order_item where sn = ?", sn);
				if (null != orderItems && orderItems.size() > 0) {
					for (OrderItem orderItem : orderItems) {
						Goods goods = Goods.dao.findFirst("select * from shop_goods where guid = ?", orderItem.getGoodGuid());
						if(goods!=null){
							goods.setStock(goods.getStock() + orderItem.getQuantity());
							Record updateRecord = new Record().setColumns(goods);
							Db.update("shop_goods", "id", updateRecord);
						}
					}
				}
				return true;
			}
		}
		return false;
	}

	public Order findOrder(String sn){
		return dao.findFirst("select * from shop_order where sn = ?",sn);
	}

	/**
	 * 新增订单详情
	 * @param orderItem
	 */
	public void createOrderItem(OrderItem orderItem){
		OrderItem idao = new OrderItem();
		idao = BeanUtils.transferObject(orderItem,OrderItem.class);
		idao.save();
	}

	/**
	 * 新增订单
	 * @param order
	 * @return Order
	 * @throws Exception
	 */
	public Order createOrder(Order order) {
		Order odao = new Order();
		odao = BeanUtils.transferObject(order,odao.getClass());
		odao.save();
		order.setId((Integer)odao.get("id"));
		return order;
	}

	/**
     * 获取新任务:未配送,已支付
     * @return ArrayList
     */
    public ArrayList getTask(){
        return (ArrayList)dao.find("select soi.name,soi.price,soi.quantity,so.*  from shop_order so left join shop_order_item soi on soi.sn = so.sn where so.pay_status = 1 and so.dis_status = 0");
    }

	/**
	 * 根据membreGuid 获取全部订单列表
	 *
	 * @param String
	 *            memberGuid
	 * @return List<Order>
	 * @throws Exception
	 */
	public List<Order> getAllOrderListByMemberGuid(String memberGuid) throws Exception {
		if (StrKit.isBlank(memberGuid)) {
			throw new Exception("当前用户信息的guid 不能为空");
		}
		return dao.find("select * from shop_order where member=? and order_status <> 9 order by create_time desc", memberGuid);

	}

	/**
	 * 根据memberGuid 获取当前用户待支付订单列表
	 *
	 * @param String
	 *            memberGuid
	 * @return List<Order>
	 * @throws Exception
	 */
	public List<Order> getWaitToPayOrderListByMemberGuid(String memberGuid) throws Exception {
		if (StrKit.isBlank(memberGuid)) {
			throw new Exception("当前用户信息的guid 不能为空");
		}
		// 待支付订单= 订单状态:未完成 and 支付状态：未支付
		List<Order> orders = dao.find("select * from shop_order where order_status=0 and pay_status = 0 and member =? order by create_time desc", memberGuid);
		if(null!=orders && !orders.isEmpty()) {
			for(Order order:orders) {
				order.setOrderItemsBySql();	
			}
		}
		return orders;
	}
	
	
	/**
	 * 根据memberGuid 获取当前用户待配送订单列表
	 *
	 * @param String
	 *            memberGuid
	 * @return List<Order>
	 * @throws Exception
	 */
	public List<Order> getWaitToDeliveryOrderListByMemberGuid(String memberGuid) throws Exception {	
		if (StrKit.isBlank(memberGuid)) {
			throw new Exception("当前用户信息的guid 不能为空");
		}
		// 待配送订单= 订单状态:未完成 and 支付状态：未支付 或 已支付 配送状态为未配送
		List<Order> orders = dao.find("select * from shop_order"
				+ " where order_status=0 and dis_status=0 and ((pay_status=0 and pay_type=3)or (pay_status=1 and pay_type!=3)) and member =? order by create_time desc", memberGuid);
		if(null!=orders && !orders.isEmpty()) {
			for(Order order:orders) {
				order.setOrderItemsBySql();	
			}
		}
		return orders;
	}


	/**
	 * 根据memberGuid 获取当前用户配送中 订单列表
	 *
	 * @param String
	 *            memberGuid
	 * @return List<Order>
	 * @throws Exception
	 */
	public List<Order> getBeingDeliveredOrderListByMemberGuid(String memberGuid) throws Exception {
		if (StrKit.isBlank(memberGuid)) {
			throw new Exception("当前用户信息的guid 不能为空");
		}
		// 配送中= 订单状态:未完成 and 配送状态：配送中
		return dao.find("select * from shop_order where order_status=0 and dis_status=1 and member=? order by create_time desc", memberGuid);
	}

	/**
	 * 根据memberGuid 获取当前用户 配送完成 订单列表
	 *
	 * @param String
	 *            memberGuid
	 * @return List<Order>
	 * @throws Exception
	 */
	public List<Order> getCompletedDeliveryOrderListByMemberGuid(String memberGuid) throws Exception {
		if (StrKit.isBlank(memberGuid)) {
			throw new Exception("当前用户信息的guid 不能为空");
		}
		// 配送完成= 订单状态:（未完成 or 已完成 ） and 配送状态：配送中
		return dao.find("select * from shop_order where order_status=0 and dis_status=1 and member=? order by create_time desc", memberGuid);
	}

	/**
	 * 根据memberGuid 获取当前用户 待评论 订单列表
	 *
	 * @param String
	 *            memberGuid
	 * @return List<Order>
	 * @throws Exception
	 */
	public List<Order> getCurrentUserWaitToCommentOrderList(String memberGuid) throws Exception {
		if (StrKit.isBlank(memberGuid)) {
			throw new Exception("当前用户信息的guid 不能为空");
		}
		// 待评论= 订单状态：(已完成) and 评价状态：未评价
		return dao.find("select * from shop_order where order_status= 1 and eva_status = 0 order by create_time desc");
	}

	/**
	 * 根据memberGuid 获取当前用户 售后订单列表
	 * 
	 * @param String
	 *            memberGuid
	 * @return List<Order>
	 * @throws Exception
	 */
	public List<Order> getCurrentUserAfterSaleOrderList(String memberGuid) throws Exception {
		if (StrKit.isBlank(memberGuid)) {
			throw new Exception("当前用户信息的guid 不能为空");
		}
		// 售后列表=订单状态：(未完成 or 已完成)
		return dao.find("select * from shop_order where order_status =0 or order_status =1 order by create_time desc");
	}

	/**
	 * 获取新任务列表
	 * 
	 * @return List<Order>
	 */
	public List<Order> getNewTaskList() {
		// 订单未完成；未配送；(在线支付已付款+货到付款未付款)
		List<Order> orderList = dao.find("select * from shop_order"
				+ " where order_status=0 and dis_status=0 and ((pay_status=0 and pay_type=3)or (pay_status=1 and pay_type!=3)) order by create_time desc");
		if (null != orderList) {
			for (Order order : orderList) {
				order.setOrderItemsBySql();
			}
		}
		return orderList;

	}

	/**
	 * 获取待取货订单列表
	 * 
	 * @return List<Order>
	 **/
	public  List<Order> getWaitToFetchTaskList() {
		List<Order> orderList = dao.find("select * from shop_order o "
				+ " left join shop_agent_order ao on ao.order_sn = o.sn"
				+ " where o.order_status = 0 and o.dis_status = 1 and((o.pay_status=0 and o.pay_type=3) or (o.pay_status=1 and o.pay_type!=3))  and ao.type = 1 order by o.create_time desc");
		if (null != orderList) {
			for (Order order : orderList) {
				order.setOrderItemsBySql();
			}
		}
		return orderList;
	}

	/**
	 * 获取配送中订单列表
	 * 
	 * @return List<Order>
	 */
	public  List<Order> getBeingDeliveredTaskList() {
		List<Order> orderList = dao.find("select * from shop_order o"
				+ " left join shop_agent_order ao on ao.order_sn = o.sn"
				+ " where o.order_status = 0 and o.dis_status = 1 and ((o.pay_status=0 and o.pay_type=3) or (o.pay_status=1 and o.pay_type!=3))  and ao.type = 2 order by o.create_time desc");
		if (null != orderList) {
			for (Order order : orderList) {
				order.setOrderItemsBySql();
			}
		}
		return orderList;
	}
	/**
	 * 获取已完成订单列表
	 * @return List<Order>
	 */
	public  List<Order> getCompletedTaskList(){
		List<Order> orderList = dao.find("select * from shop_order o"
				+ " left join shop_agent_order ao on ao.order_sn = o.sn"
				+ " where o.order_status = 0 and o.dis_status = 2 and o.pay_status=1 and ao.type = 3 order by o.create_time desc");
		if (null != orderList) {
			for (Order order : orderList) {
				order.setOrderItemsBySql();
			}
		}
		return orderList;
	}

	public boolean confirmGetGoods(String sn) {
		try{
			Order order = dao.findFirst("select * from shop_order where sn = ? and order_status = 0",sn);
			order.setFinishTime(new Date());//设置订单完成时间
			order.setOrderStatus(1);
			order.setDisStatus(2);
			Record updateRecord = new Record().setColumns(order);
			return Db.update("shop_order","id",updateRecord);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
}
