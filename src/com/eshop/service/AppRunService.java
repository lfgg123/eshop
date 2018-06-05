package com.eshop.service;

import java.util.List;

import com.eshop.model.Goods;
import com.eshop.model.Run;

public class AppRunService {
	private Run dao = Run.dao;
	
	/**
	 * 获取代跑腿商品列表
	 * @param int isUse 
	 * */
	public List<Goods> getRunList(int isUse){
		// 业务修改：商品中 有个代跑腿 根据product获取goods列表 
		return Goods.dao.find("select goods.* from shop_goods goods "
				+ " left join shop_product prd on prd.guid = goods.pro_guid"
				+ " where prd.name like '%代跑腿%'");
	}

}
