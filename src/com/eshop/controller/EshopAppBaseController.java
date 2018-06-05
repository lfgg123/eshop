package com.eshop.controller;

import java.util.HashMap;
import java.util.Map;

import com.eshop.model.Member;
import com.eshop.util.Constants;
import com.eshop.util.JacksonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;

public class EshopAppBaseController extends Controller {

	private Map postParamMap = null;

	private final static String REQUEST_METHOD_POST = "POST";
	private final static String REQUEST_METHOD_GET = "GET";

	/**
	 * 重写 getPara(field) 方法 使其可以获得post body 中的参数
	 */
//	@Override
//	public String getPara(String name) {
//		try {
//			if (REQUEST_METHOD_POST.equals(getRequest().getMethod())) {
//				if (null == postParamMap || postParamMap.isEmpty()) {
//					postParamMap = JacksonUtils.json2Obj(HttpKit.readData(getRequest()),
//							new TypeReference<HashMap<String, String>>() {
//							});
//				}
//				String result = (String) postParamMap.get(name);
//				postParamMap.remove(name);
//				return result;
//			} else if (REQUEST_METHOD_GET.equals(getRequest().getMethod())) {
//				return super.getPara(name);
//			} else {
////				throw new EshopAppExceptionHandler("当前不支持该请求方式：" + getRequest().getMethod());
//			}
//		} catch (Exception e) {
////			throw new Exception("获取请求参数异常. 	报错信息:" + e.getMessage());
//		}
//		return "";
//	}

	/**
	 * 重写 getPara(index) 方法 使其可以获得post body 中的参数
	 */

	@Override
	public String getPara(int index) {
		return super.getPara(index);
	}
	
	/**
	 * 获取当前登录用户
	 * */ 
	protected Member getCurrentUser() {
	/*	Member member=new Member();
		member.setId(1);
		member.setGuid("");*/
		return (Member) getSession().getAttribute(Constants.USER);
	}

}
