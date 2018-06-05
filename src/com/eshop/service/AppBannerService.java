package com.eshop.service;

import java.util.List;

import com.eshop.model.Banner;

public class AppBannerService {
	public final static AppBannerService service = new AppBannerService();	
	private static Banner dao = Banner.dao;
	
	/**
	 * 获取轮播图列表
	 * @return List<Banner>
	 * **/
	public List<Banner> getBannerList(){
		return dao.find("select * from shop_banner where is_show = 1");
	}

}
