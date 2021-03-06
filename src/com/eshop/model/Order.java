package com.eshop.model;

import java.util.List;

import com.eshop.model.base.BaseOrder;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Order extends BaseOrder<Order> {
	public static final Order dao = new Order().dao();
	List<OrderItem> orderItems;
	
	private String payTypeStr = "";
	
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItemsBySql() {
		this.orderItems = OrderItem.dao.find("select * from shop_order_item i where i.order=?",getId());
	}

	/**
	 * 用来前端展示的反射方法
	 * 1:支付宝 2:微信 3:货到付款
	 * @return
	 */
	public String getPayTypeStr(){
		if(this.getPayType() == null){
			return "[未知]";
		}
		if(this.getPayType() == 1){
			return "[支付宝]";
		}else if(this.getPayType() == 1){
			return "[微信]";
		}else{
			return "[货到付款]";
		}
	}
}
