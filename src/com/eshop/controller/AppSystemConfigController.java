package com.eshop.controller;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.util.log.Log;

import com.eshop.interceptor.AppLoginInterceptor;
import com.eshop.model.Banner;
import com.eshop.model.Bbs;
import com.eshop.model.MemberMessage;
import com.eshop.model.Store;
import com.eshop.service.AppAgentOrderService;
import com.eshop.service.AppAliSmsService;
import com.eshop.service.AppBannerService;
import com.eshop.service.AppBbsService;
import com.eshop.service.AppMemberMessageService;
import com.eshop.service.AppStoreService;
import com.eshop.util.JacksonUtils;
import com.eshop.vo.ResultJson;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class AppSystemConfigController extends Controller {
	private final static AppBbsService bbsService = new AppBbsService();
	private final static AppStoreService storeService = AppStoreService.service;
	private final static AppMemberMessageService messageService = AppMemberMessageService.service;
	private final static AppAgentOrderService agentOrderService = AppAgentOrderService.service;
	private final static AppBannerService bannerService = AppBannerService.service;

	/**
	 * 获取私人定制列表
	 */
	public void getBbsList() {
		List<Bbs> result = bbsService.getBbsList();
		if (null == result) {
			result = new ArrayList<>();
		}
		renderText(JacksonUtils.obj2json(ResultJson.success(result)));
	}

	/**
	 * 根据id获取私人定制详情
	 * 
	 * @param Long
	 *            bbsId
	 */
	public void getBbsById() {
		Long bbsId = getParaToLong("bbsId");
		Bbs result = bbsService.getBbsById(bbsId);
		if (null == result) {
			renderText(JacksonUtils.obj2json(ResultJson.fail(MessageFormat.format("bbsId:{0}的私人定制不存在", bbsId))));
		} else {
			renderText(JacksonUtils.obj2json(ResultJson.success(result)));
		}

	}

	/**
	 * 私人定制点赞
	 * 
	 * @param Long
	 *            bbsId
	 * @throws Exception
	 */
	public void upvoteBbs() throws Exception {
		Long bbsId = getParaToLong("bbsId");
		boolean result = bbsService.upvoteBbs(bbsId);
		if (result) {
			renderText(JacksonUtils.obj2json(ResultJson.success("点赞成功")));
		} else {
			renderText(JacksonUtils.obj2json(ResultJson.fail("操作失败")));
		}
	}

	/**
	 * 私人定制取消点赞
	 * 
	 * @param Long
	 *            bbsId
	 * @throws Exception
	 */
	public void downvoteBbs() throws Exception {
		Long bbsId = getParaToLong("bbsId");
		boolean result = bbsService.downvoteBbs(bbsId);
		if (result) {
			renderText(JacksonUtils.obj2json(ResultJson.success("取消点赞成功")));
		} else {
			renderText(JacksonUtils.obj2json(ResultJson.fail("操作失败")));
		}
	}

	/**
	 * 私人定制收藏
	 * 
	 * @param Long
	 *            bbsId
	 * @throws Exception
	 */
	public void collectBbs() throws Exception {
		Long bbsId = getParaToLong("bbsId");
		boolean result = bbsService.collectBbs(bbsId, getPara("TOKEN"));
		if (result) {
			renderText(JacksonUtils.obj2json(ResultJson.success("收藏成功")));
		} else {
			renderText(JacksonUtils.obj2json(ResultJson.fail("操作失败")));
		}
	}

	/**
	 * 私人定制回复
	 * 
	 * @param Long
	 *            bbsId
	 * @param String
	 *            content
	 * @throws Exception
	 */
	public void replyBbs() throws Exception {

		Long bbsId = getParaToLong("bbsId");
		String content = getPara("content");
		boolean result = bbsService.replyBbs(bbsId, getPara("TOKEN"), content);
		if (result) {
			renderText(JacksonUtils.obj2json(ResultJson.success("回复成功")));
		} else {
			renderText(JacksonUtils.obj2json(ResultJson.fail("操作失败")));
		}
	}

	/**
	 * 获取919门店信息列表
	 */
	@Clear(AppLoginInterceptor.class)
	public void get919StoreList() {
		List<Store> list = storeService.get919StoreInfoList();
		if (null == list) {
			list = new ArrayList<>();
		}
		renderText(JacksonUtils.obj2json(ResultJson.success(list)));
	}

	/**
	 * 根据消息类型获取用户消息列表
	 * 
	 */
	public void getMemberMessageListByType() {
		Integer type = getParaToInt("type");
		List<MemberMessage> messages = messageService.getMemberMessageByType(type, getPara("TOKEN"));
		if (null == messages) {
			messages = new ArrayList<>();
		}
		renderText(JacksonUtils.obj2json(ResultJson.success(messages)));
	}

	/**
	 * 确认配送
	 * 
	 * @throws Exception
	 * 
	 */
	public void confirmDelivery() throws Exception {
		Integer orderId = getParaToInt("orderId");
		boolean result = agentOrderService.confirmDelivery(getPara("TOKEN"), orderId);
		if (result) {

			renderText(JacksonUtils.obj2json(ResultJson.success("确认配送成功!")));
		} else {
			renderText(JacksonUtils.obj2json(ResultJson.fail("操作失败")));
		}
	}

	/**
	 * 确认取货
	 * 
	 * @throws Exception
	 */
	public void confirmGetGoods() throws Exception {
		Integer orderId = getParaToInt("orderId");
		String remark = getPara("remark", "");
		boolean result = agentOrderService.confirmGetGoods(getPara("TOKEN"), orderId, remark);
		if (result) {
			renderText(JacksonUtils.obj2json(ResultJson.success("确认取货成功!")));
		} else {
			renderText(JacksonUtils.obj2json(ResultJson.fail("操作失败")));
		}

	}

	/**
	 * 确认送达
	 * 
	 * @throws Exception
	 */
	public void confirmDelivered() throws Exception {
		Integer orderId = getParaToInt("orderId");
		boolean result = agentOrderService.confirmDelivered(getPara("TOKEN"), orderId);
		if (result) {
			renderText(JacksonUtils.obj2json(ResultJson.success("确认送达成功!")));
		} else {
			renderText(JacksonUtils.obj2json(ResultJson.fail("操作失败")));
		}
	}

	/**
	 * 获取社区轮播图列表
	 * 
	 **/
	public void getBannerList() {
		List<Banner> banners = bannerService.getBannerList();
		if (null == banners) {
			banners = new ArrayList<>();
		}
		renderText(JacksonUtils.obj2json(ResultJson.success(banners)));
	}

	/**
	 * 获取客服电话
	 * 
	 * @throws Exception
	 */
	public void getCustomerServerPhone() throws Exception {
		Record record = Db.findFirst("select * from shop_sys_info");
		if (record == null || StrKit.isBlank(record.getStr("custerm"))) {
			throw new Exception("系统错误，请稍后再试");
		}
		Map<String, String> result = new HashMap<>();
		result.put("custerm", record.getStr("custerm"));
		renderText(JacksonUtils.obj2json(ResultJson.success(result)));
	}

	/**
	 * 根据经纬度获得919门店
	 * 
	 * @throws Exception
	 * 
	 */
	@Clear(AppLoginInterceptor.class)
	public void get919StoreByLatAndLon() throws Exception {
		String lat = getPara("lat", "");
		String lon = getPara("lon", "");
		System.out.println(lat + ":" + lon);
		Store result = storeService.get919StoreByLatAndLon(lat, lon);
		if (result == null) {
			renderText(JacksonUtils.obj2json(ResultJson.fail("查询失败")));
		} else {
			renderText(JacksonUtils.obj2json(ResultJson.success(result)));
		}
	}

	/**
	 * 获取配送范围内的门店列表
	 */
	public void get919StoresInDeliveryArea() throws Exception {
		String lat = getPara("lat", "");
		String lon = getPara("lon", "");
		List<Store> result = storeService.get919StoreInDeliveryArea(lat, lon);
		if (result == null||result.isEmpty()) {
			renderText(JacksonUtils.obj2json(ResultJson.fail("超出配送范围")));
		} else {
			renderText(JacksonUtils.obj2json(ResultJson.success(result)));

		}

	}

}
