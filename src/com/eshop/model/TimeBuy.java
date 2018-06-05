package com.eshop.model;


import java.util.Date;

import com.jfinal.plugin.activerecord.Record;

public class TimeBuy {
	
	private String goodGuid;
	private Date endTime;
	private Integer isUse;
	private Integer num;
	private Goods goods;
	public TimeBuy(Record record) {
		this.goodGuid = record.getStr("goodGuid");
		this.endTime = record.getDate("endTime");
		this.isUse = record.getInt("isUse");
		this.num= record.getInt("num");
		setGoodsBySql();
		
	}
	public String getGoodGuid() {
		return goodGuid;
	}
	public void setGoodGuid(String goodGuid) {
		this.goodGuid = goodGuid;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getIsUse() {
		return isUse;
	}
	public void setIsUse(Integer isUse) {
		this.isUse = isUse;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Goods getGoods() {
		return goods;
	}
	
	private void setGoodsBySql() {
		this.goods = Goods.dao.findFirst("select * from shop_goods where guid = ?",getGoodGuid());
	}

}
