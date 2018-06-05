package com.eshop.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eshop.model.Goods;
import com.eshop.model.OrderReturn;
import com.jfinal.kit.StrKit;


public class AppOrderReturnService {
	public final static AppOrderReturnService service = new AppOrderReturnService();
	private OrderReturn dao = OrderReturn.dao;

	/**
	 * 保存售后申请
	 * @param OrderReturn orderReturn
	 * @return boolean 
	 * @throws Exception 
	 */
	public boolean saveOrderReturn(OrderReturn orderReturn) throws Exception {
		// 判断订单是否已经申请退货
		OrderReturn isExist = dao.findFirst("select * from shop_order_return where sn = ?",orderReturn.getSn());
		if(null!=isExist) {
			throw new Exception(MessageFormat.format("订单编号为{0}的订单已经提交售后申请，请勿重复操作",orderReturn.getSn()));
		}
		orderReturn.setState(0);
		orderReturn.setCreateTime(new Date());
		return orderReturn.save();
	}

	/**
	 * 根据类型获取用户售货申请列表
	 * 
	 * @param String
	 *            member
	 * @param Integer
	 *            type
	 * @return List<OrderReturn>
	 * @throws Exception
	 */
	public List<Map> getOrderReturnSuccessList(Integer type, String member) throws Exception {
		if (null == type) {	
			throw new Exception("申请类型不能为空");
		}
		if (StrKit.isBlank(member)) {
			throw new Exception("用户guid 不能为空");
		}
		List<OrderReturn> orderReturn = dao.find("select re.* from shop_order_return re"
				+ " left join shop_order o on o.sn = re.sn" + " where re.state = 1 and re.type = ? and o.member=?",
				type, member);
		List<Map> result = null;
		if (null != orderReturn && !orderReturn.isEmpty()) {
			result = new ArrayList<>();
			for (OrderReturn reObject : orderReturn) {
				Map localMap = new HashMap<>();
				Goods goods = Goods.dao.findFirst("select * from shop_goods where guid = ?", reObject.getGoodGuid());
				if (null == goods) {
					throw new Exception(MessageFormat.format("货品guid为{0}的记录不存在", reObject.getGoodGuid()));
				}
				localMap.put("goods", goods);
				localMap.put("quantity", reObject.getQuantity());
				localMap.put("price", reObject.getPrice());
				localMap.put("type", reObject.getType());
				result.add(localMap);
			}
		}
		return result;
	}

}
