package com.eshop.service;

import com.eshop.model.CartItem;
import com.eshop.model.EnoughCut;
import com.eshop.model.Goods;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppEnoughCutService {
	private static EnoughCut dao = EnoughCut.dao;
	private static Goods gdao = Goods.dao;
	
	/**
	 * 获取轮播图列表
	 * @return List<EnoughCut>
	 * */
	public List<Record> getEnoughCutList(){
		return Db.find("select * from shop_banner where is_show=1");
	}

	/**
	 * 满减送商品列表
	 * @return
	 */
	public ArrayList getEnoughCutProductList(EnoughCut enoughCut){
		ArrayList list = new ArrayList();
		String[] goodGuids = enoughCut.getGoodGuids().split(",");
		if(goodGuids.length>0) {
			for (int i = 0; i < goodGuids.length; i++) {
				if(goodGuids[i].length()>0) {
					Goods goods = gdao.findFirst("select sg.* from shop_goods sg where guid = ?", goodGuids[i]);
					goods.setGoodSpecList(goods.findGoodSpec());
					list.add(goods);
				}
			}
		}
		return list;
	}

	/**
	 * 获取满减送
	 * @return
	 */
	public EnoughCut getEnoughCut(){
		return dao.findFirst("select * from shop_enough_cut where isuse=1");
	}

	/**
	 * 获取满减金额
	 * @return
	 */
	public int getEnoughCutAmount(List<CartItem> cartItems){
		EnoughCut enoughCut =  getEnoughCut();
		if(null != enoughCut){
			double totalAmount = 0.0;
			for(CartItem cartItem:cartItems){
				if(isExist(enoughCut,cartItem.getGoodGuid())){
					totalAmount += (cartItem.getPrice() * cartItem.getQuantity());
				}
			}
			if(totalAmount>=enoughCut.getEnought()){
				return enoughCut.getCut();
			}else{
				return 0;
			}
		}else{
			return 0;
		}
	}

    /**
     * 查看货品是否在满减送中
     * @param goodGuid
     * @return
     */
	public boolean isExist(EnoughCut enoughCut ,String goodGuid){
		String[] goodGuids = enoughCut.getGoodGuids().split(",");
		List<String> guids = Arrays.asList(goodGuids);
		if(guids.contains(goodGuid)){
			return true;
		}
		return false;
	}
}
