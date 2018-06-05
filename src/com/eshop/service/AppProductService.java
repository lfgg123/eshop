package com.eshop.service;

import com.eshop.model.Product;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;


import java.util.List;

public class AppProductService {
	private static Product dao = Product.dao;

	/**
	 * 猜你喜欢
	 * @param member
	 * @return
	 */
	public List<Product> guessike(String member){
		return dao.find("select * from shop_product where pro_class in " +
				"(select pro_class from shop_product where guid in " +
				"(select good_guid from shop_member_history where member = ?)) " +
				"and guid not in( " +
				"select good_guid from shop_member_history where member = ?) limit 20",member,member);
	}

	/**
	 * 单个商品
	 * @param guid
	 * @return Product
	 */
	public Product getProduct(String guid){
		return dao.findFirst("select sp.*,sg.guid good_guid,sgs.spec_name ,sgs.spec_value from shop_product sp left join shop_goods sg on sg.pro_guid = sp.guid left join shop_goods_spec sgs on sgs.good_guid = sg.guid where sp.guid = ?",guid);
	}

	/**
	 * 获取评价
	 * @param guid
	 * @return List<Record>
	 */
	public List<Record> getGoodEva(String guid){
		return Db.find("select sge.*,sm.guid m_guid,sm.nick,sm.img head_img from shop_good_eva sge left join shop_member sm on sge.member = sm.guid left join shop_goods sg on sg.guid = sge.good_guid and sg.pro_guid = ? order by sge.first_time desc",guid);
	}
	
	/**
	 * 根据proClass 获取 商品分类
	 * @param String proClass
	 * @return List<Product>
	 * */
	public List<Product> getProductListByProClass(String proClass){
		return dao.find("select * from shop_product where pro_class = ?",proClass);
	}
	
	/**
	 * 分类页根据proClass 获取商品分类
	 * @param String guid
	 * @return List<Proudct>
	 * @throws Exception 
	 * */
	public List<Record> getProductsByProClass(String proClass) throws Exception{
		if(StrKit.isBlank(proClass)) {
			throw new Exception("商品分类guid不能为空");
		}
		return Db.find("select id,guid,name,price_area as price,image,salenum,modify_time as modifyTime,create_time as createTime "
				+ " from shop_product where pro_class=?",proClass);
	}
	
	/**
	 * 根据名字搜索商品
	 * @param String productName
	 * @return Proudct
	 * */
	public Product searchProductByName(String productName) {
		
		return null;
	}
	
	

}
