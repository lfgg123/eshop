package com.eshop.service;

import java.util.List;

import com.eshop.model.Content;

public class AppContentService {
	private static Content dao = Content.dao;
	
	/**
	 * 获取广播消息
	 * @return List<Content>
	 * */
	public List<Content> getBroadCastContent(){
		return dao.find("select * from shop_con where type=1 ");
	}
	

}
