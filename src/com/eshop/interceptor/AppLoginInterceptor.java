package com.eshop.interceptor;

import java.util.Map;

import org.apache.log4j.Logger;

import com.eshop.model.Member;
import com.eshop.util.JacksonUtils;
import com.eshop.util.LogConst;
import com.eshop.vo.ResultJson;
import com.fasterxml.jackson.core.type.TypeReference;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;

public class AppLoginInterceptor implements Interceptor {
	private Logger logger = Logger.getLogger(LogConst.SERVER_LOG);
	@Override
	public void intercept(Invocation arg0) {
		Controller controller = arg0.getController();
		String contentType = controller.getRequest().getContentType();
		if (!StrKit.isBlank(contentType) && contentType.contains("multipart/form-data")) {
			controller.getFiles();
		}
//		// 调试阶段先写死 TO DELETE
//		String token = "abc";
		logger.info("arg0.getMethodName()=" + arg0.getMethodName());
		
		if("notifyUrl".equals(arg0.getMethodName())){
			arg0.invoke();
			return;
		}
		 String token = controller.getPara("TOKEN");
		if (StrKit.isBlank(token)) {
			Map<String, Object> map = JacksonUtils.json2Obj(HttpKit.readData(controller.getRequest()),
					new TypeReference<Map<String, Object>>() {	
					});
			token = (String) map.get("TOKEN");
		}
		if (StrKit.isBlank(token)) {
			controller.renderText(JacksonUtils.obj2json(ResultJson.fail("请先登录")));
		} else if (Db.findFirst("select * from shop_member where guid=?", token) == null&&Db.findFirst("select * from shop_agent where guid =?",token)==null) {
			controller.renderText(JacksonUtils.obj2json(ResultJson.fail("请先登录")));
		} else {
			arg0.invoke();
		}
	}

}
