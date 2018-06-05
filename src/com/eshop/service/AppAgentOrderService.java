package com.eshop.service;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Date;

import com.eshop.model.MemberReturn;
import com.eshop.model.Order;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;

public class AppAgentOrderService {
	public final static AppAgentOrderService service = new AppAgentOrderService();
	private final static AppAliSmsService msgService = new AppAliSmsService();

	/**
	 * 确认配送
	 * 
	 * @param String
	 *            member
	 * @param Integer
	 *            orderId
	 * @throws Exception
	 * @return boolean
	 * 
	 */
	public boolean confirmDelivery(final String member, final Integer orderId) throws Exception {
		if (StrKit.isBlank(member)) {
			throw new Exception("会员guid 不能为空");
		}
		if (null == orderId) {
			throw new Exception("订单Id不能为空");
		}

		boolean result = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				try {
					Order order = Order.dao.findById(orderId);
					if (null == order) {
						throw new Exception(MessageFormat.format("订单id为{0}的订单不存在", orderId));
					}

					// 修改订单配送状态
					order.setDisStatus(1);

					// 插入配送记录
					Record record = new Record();
					record.set("agent_guid", member);
					record.set("order_sn", order.getSn());
					record.set("type", 1);
					record.set("create_time", new Date());
					return order.update() && Db.save("shop_agent_order", record);
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				}
			}
		});
		if (result) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					Record customer = Db.findFirst("select * from shop_member where guid = ?", member);
					Record agent = Db.findFirst("select agent.name name,agent.tel tel from shop_agent agent"
							+ " left join shop_agent_order ao on ao.agent_guid = agent.guid"
							+ " left join shop_order o on o.sn = ao.order_sn" + ""
									+ "	where o.id = ?", orderId);
					String phoneNumber = customer.getStr("phone");
					String courier = agent.getStr("name");
					String courierPhoneNumber = agent.getStr("tel");
					if (!StrKit.isBlank(phoneNumber) && !StrKit.isBlank(courier)
							&& !StrKit.isBlank(courierPhoneNumber)) {
						try {
							msgService.sendDeliveryCode(phoneNumber, courier, courierPhoneNumber);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}).start();
		}
		return result;

	}

	/**
	 * 确认取货
	 * 
	 * @param String
	 *            member
	 * @param Integer
	 *            orderId
	 * @param String
	 *            remark
	 * @return boolean
	 * @throws Exception
	 * 
	 */
	public boolean confirmGetGoods(final String member, final Integer orderId, String remark) throws Exception {
		if (StrKit.isBlank(member)) {
			throw new Exception("会员guid 不能为空");
		}
		if (null == orderId) {
			throw new Exception("订单Id不能为空");
		}

		Order order = Order.dao.findById(orderId);
		if (null == order) {
			throw new Exception(MessageFormat.format("订单id为{0}的订单不存在", orderId));
		}
		return Db.update("update shop_agent_order set type = 2,bz=? where agent_guid = ? and order_sn=?", remark,
				member, order.getSn()) > 0 ? true : false;

	}

	/**
	 * 确认送达
	 * 
	 * @param String
	 *            member
	 * @param Integer
	 *            orderId
	 * @return boolean
	 * @throws Exception
	 * 
	 */
	public boolean confirmDelivered(final String member, final Integer orderId) throws Exception {
		if (StrKit.isBlank(member)) {
			throw new Exception("会员guid 不能为空");
		}
		if (null == orderId) {
			throw new Exception("订单Id不能为空");
		}
		boolean result = Db.tx(new IAtom() {

			@Override
			public boolean run() throws SQLException {
				try {
					Order order = Order.dao.findById(orderId);
					if (null == order) {
						throw new Exception(MessageFormat.format("订单id为{0}的订单不存在", orderId));
					}
					// 修改订单配送状态
					order.setDisStatus(2);
					// 若货到付款，设置 支付状态
					if (order.getPayType().equals("3")) {
						order.setPayStatus(1);
					}

					boolean agentOrderResult = Db.update(
							"update shop_agent_order set type =3 , finish_time = now() where agent_guid = ? and order_sn=?",
							member, order.getSn()) > 0 ? true : false;
					return order.update() && agentOrderResult;
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				}
			}
		});
		if (result) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					Record customer = Db.findFirst("select * from shop_member where guid = ?", member);
					String mobile = customer.getStr("phone");
					Record sysInfo = Db.findFirst("select * from shop_sys_info");
					String servicePhone = sysInfo.getStr("custerm");
					if (!StrKit.isBlank(mobile) && !StrKit.isBlank(servicePhone)) {
						try {
							msgService.sendOrderFinishedMessage(mobile, servicePhone);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}
			}).start();
		}
		return result;

	}

}
