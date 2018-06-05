package com.eshop.config;

import com.eshop.controller.*;
import com.eshop.interceptor.AppLoginInterceptor;
import com.eshop.interceptor.EshopAppExceptionHandler;
import com.eshop.model._MappingKit;
import com.eshop.util.ConfigUtils;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;

/**
 * Jfinal配置信息
 * @author 
 * 2017/10/11
 */
public class Config extends JFinalConfig {

	@Override
	public void configConstant(Constants me) {
		 
		PropKit.use("jdbc_config.txt");
//	    me.setMaxPostSize(1024000000);
		me.setDevMode(PropKit.getBoolean("devMode", false));
		
		me.setViewType(ViewType.JSP);
		me.setBaseUploadPath(ConfigUtils.getProperty("file.path"));
//		me.setUploadedFileSaveDirectory(ConfigUtils.getProperty("file.path").toString());
		
	}

	@Override
	public void configEngine(Engine arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void configHandler(Handlers me) {
		// TODO Auto-generated method stub
//		me.add(new ContextPathHandler("path"));

	}

	@Override
	public void configInterceptor(Interceptors arg0) {
		arg0.add(new AppLoginInterceptor());
		arg0.add(new EshopAppExceptionHandler());

	}

	@Override
	public void configPlugin(Plugins me) {
		C3p0Plugin c3p0Plugin = createDBPlugin();
		me.add(c3p0Plugin);
		
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		boolean devMode = PropKit.getBoolean("devMode", true);
		if(devMode)
			arp.setShowSql(true);	
		me.add(arp);
		_MappingKit.mapping(arp);
		c3p0Plugin.start();
		arp.start();

	}

	@Override
	public void configRoute(Routes me) {
		me.add("/app/user", LoginManagerController.class);
		me.add("/app/goods", AppGoodsController.class);
		me.add("/app/member", AppMemberController.class);
		me.add("/app/systemconfig",AppSystemConfigController.class);
		me.add("/app/purchase", AppPurchaseController.class);
		me.add("/app/order", AppOrderController.class);
		me.add("/pay", AppPayController.class);
	}
	


	public static C3p0Plugin createDBPlugin() {
		return new C3p0Plugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim(),PropKit.get("driverClassName"));
	}
	
	public static void main(String args[]) {
		JFinal.start("WebRoot",8180,"/");
		
	}

}
