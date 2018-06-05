package com.eshop.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eshop.model.Clazz;
import com.eshop.model.Goods;
import com.eshop.model.Product;
import com.eshop.model.TimeBuy;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class AppGoodsService {
	private static Goods dao = Goods.dao;

	/**
	 * 根据guid获取货品
	 * 
	 * @param goodGuid
	 * @param quantity
	 * @return
	 */
	public Goods getGood(String goodGuid, Integer quantity) {
		Goods goods = dao.findFirst("select * from shop_goods where guid = ? for update", goodGuid);
		if (goods != null) {
			goods.setGoodSpecList(goods.findGoodSpec());
			if (null != quantity)
				goods.setQuantity(quantity);
		}
		return goods;
	}

	/**
	 * 919 产品列表
	 * 
	 * @return List<Goods>
	 */
	public List<Goods> get919GoodsList() {
		// 不畅销 and 上架的产品
		return dao.find("select * from shop_goods where is_hot = 0 and is_ground = 1");
	}

	/**
	 * 商品列表(按分类)
	 * 
	 * @param proClass
	 * @return List<Goods>
	 */
	public List<Goods> getGoodsListByProClass(String proClass) {
		return dao.find("select * from shop_goods goods" + " left join shop_product prd on prd.guid = goods.pro_guid"
				+ " where pro_class = ?", proClass);
	}

	/**
	 * 搜索商品
	 * 
	 * @param String
	 *            productName
	 * @param Integer
	 *            main
	 * @param Integer
	 *            salesnum
	 * @param Integer
	 *            price
	 * @return List<Record>
	 */
	public List<Record> getProductListByName(String name, Integer main, Integer salesnum, Integer price) {
		String likeReg = "";
		if (StrKit.isBlank(name)) {
			likeReg = "'%%'";
		} else {
			likeReg = "'%" + name + "%'";
		}
		List<Record> goods = Db.find(
				"select id,guid,name,image,salenum,price_area as price,modify_time as modifyTime,create_time as createTime from shop_product where name like "
						+ likeReg);
		if (goods != null && !goods.isEmpty()) {
			sortGoodsListByThreeSelections(goods, main, salesnum, price);
		}
		return goods;
	}

	/**
	 * 根据商品获取货品
	 * 
	 * @param productGuid
	 * @return
	 */
	public List<Goods> getGoods(String productGuid) {
		List<Goods> goodsList = dao.find(
				"select sg.*,sgs.spec_name ,sgs.spec_value from shop_goods sg left join shop_goods_spec sgs on sgs.good_guid = sg.guid where sg.pro_guid = ?",
				productGuid);
		for (Goods goods : goodsList) {
			goods.setGoodSpecList(goods.findGoodSpec());
		}
		return goodsList;
	}

	/**
	 * 获取限时购买商品列表
	 * 
	 * @return List<Record>
	 * @throws Exception
	 * 
	 */
	public List<TimeBuy> getTimeBuyGoodsList() throws Exception {
		List<Record> records = Db.find(
				"select good_guid as goodGuid,end_time as endTime,isuse as isUse,num from shop_time_buy where isuse =1");
		List<TimeBuy> timeBuyList = new ArrayList<TimeBuy>();
		if (null != records) {
			for (Record record : records) {
				TimeBuy localTimeBuy = new TimeBuy(record);
				if (localTimeBuy.getGoods() != null)
					timeBuyList.add(localTimeBuy);
			}
		}
		return timeBuyList;

	}

	/**
	 * 根据一级分类获取限时购买商品列表
	 * 
	 * @param string
	 *            guid
	 * @return List<Goods>
	 * @throws Exception
	 */
	public List<TimeBuy> getTimeBuyGoodsListByFirstClass(String guid) throws Exception {
		if (StrKit.isBlank(guid)) {
			throw new Exception("一级分类的guid 不能为空！");
		}
		// 查出所有3级分类
		List<Clazz> thirdClassList = Clazz.dao.find("select t.guid from shop_class t"
				+ " left join shop_class s on s.guid = t.parent" + " where t.grade =3 and s.grade = 2 and s.parent = ?",
				guid);
		if (null == thirdClassList || thirdClassList.isEmpty()) {
			throw new Exception(MessageFormat.format("guid 为{0} 的一级分类下不存在三级分类", guid));
		}

		List<TimeBuy> timeBuyList = new ArrayList<>();
		for (Clazz clazz : thirdClassList) {
			List<Record> records = Db.find(
					"select tb.good_guid as goodGuid,tb.end_time as endTime,tb.isuse as isUse,tb.num as num from shop_time_buy tb"
							+ " left join shop_goods goods on tb.good_guid=goods.guid "
							+ " left join shop_product prd on prd.guid = goods.pro_guid"
							+ " where goods.is_ground =1 and tb.isuse=1 and prd.pro_class = ?",
					clazz.getGuid());
			if (null != records) {
				for (Record record : records) {
					timeBuyList.add(new TimeBuy(record));
				}
			}
		}
		return timeBuyList;

	}

	/**
	 * 根据一级分类获取919畅销商品列表
	 * 
	 * @param String
	 *            guid
	 * @return List<Good>
	 * @throws Exception
	 */
	public List<Goods> get919HotGoodsListByFirstClass(String guid) throws Exception {
		if (StrKit.isBlank(guid)) {
			throw new Exception("一级分类的guid 不能为空！");
		}
		// 查出所有3级分类
		List<Clazz> thirdClassList = Clazz.dao.find("select t.guid from shop_class t"
				+ " left join shop_class s on s.guid = t.parent" + " where t.grade =3 and s.grade = 2 and s.parent = ?",
				guid);
		if (null == thirdClassList || thirdClassList.isEmpty()) {
			throw new Exception(MessageFormat.format("guid 为{0} 的一级分类下不存在三级分类", guid));
		}
		List<Goods> goods = new ArrayList<>();
		for (Clazz clazz : thirdClassList) {
			List<Goods> localGoods = dao.find("select * from shop_goods g"
					+ " left join shop_product prd on prd.guid = g.pro_guid" + " where prd.pro_class = ?",
					clazz.getGuid());
			if (null != localGoods && !localGoods.isEmpty())
				goods.addAll(localGoods);
		}
		return goods;

	}

	/**
	 * 获取919精选商品列表
	 * 
	 * @return List<Map>
	 * @throws Exception
	 */
	public List<Map> getChoiceGoodsList() throws Exception {
		List<Map> result = new ArrayList<>();
		try{
			List<Record> choice = Db.find("select * from shop_choice where isuse=1");
			if (null != choice && !choice.isEmpty()) {
				for (Record record : choice) {
					Map localMap = new HashMap<>();
					List<Goods> goods = null;
					if (!StrKit.isBlank(record.getStr("good_guids"))) {
						goods = new ArrayList<>();
						String[] goodGuidArray = record.getStr("good_guids").split(",");
						for (String guid : goodGuidArray) {
							Goods lGoods = dao.findFirst("select * from shop_goods where guid =?", guid.trim());
							if (null != lGoods) {
								goods.add(lGoods);
							}
						}
					}
					if (goods != null && !goods.isEmpty()) {
						localMap.put("goods", goods);
						localMap.put("goodGuids", record.getStr("good_guids"));
						localMap.put("isUse", record.getInt("isuse"));
						localMap.put("img", record.getStr("img"));
						localMap.put("createTime", record.getTimestamp("create_time"));
						localMap.put("modifyTime", record.getTimestamp("modify_time"));
						localMap.put("bz", record.getStr("bz"));
						result.add(localMap);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}

	/**
	 * 919畅销产品列表
	 * 
	 * @return List<Goods>
	 */
	public List<Goods> get919HotGoods() {
		return dao.find("select * from shop_goods where is_hot=1");
	}

	/**
	 * 根据guid获取货品
	 * 
	 * @param String
	 *            guid
	 * @param String
	 *            member
	 * @return Goods goods
	 */
	public Goods getGoodsByGuid(String guid, String member) {
		// 插入浏览记录表
		AppMemberHistoryService.service.saveMemberHistory(guid, member);
		return dao.findFirst("select * from shop_goods where guid = ?", guid);
	}

	/**
	 * 筛选商品
	 * 
	 * @throws Exception
	 * 
	 */
	public List<Record> filterGoodsByClass(String proClass, String commodity, Integer brand, Integer country,
			Double priceFrom, Double priceTo, final Integer main, final Integer saleNum, final Integer price)
			throws Exception {
		StringBuffer buffer = new StringBuffer();
		if (!StrKit.isBlank(proClass)) {
			buffer.append("'").append(proClass).append("'");
		} else if (!StrKit.isBlank(commodity)) {
			List<Clazz> clazzs = Clazz.dao
					.find("select c.guid from shop_class c" + " left join shop_class f on c.parent = f.guid"
							+ " where c.grade = 3 and f.grade = 2 and f.parent = ?", commodity);
			if (clazzs == null || clazzs.isEmpty()) {
				return null;
			}
			for (Clazz clazz : clazzs) {
				buffer.append("'").append(clazz.getGuid()).append("'");
				if (clazzs.indexOf(clazz) < clazzs.size() - 1) {
					buffer.append(",");
				}
			}

		} else {
			throw new Exception("商品分类不能为空");
		}
		// 根据分类 获取商品列表
		List<Record> products = Db.find(
				"select id,guid,name,image,salenum,price_area as price,modify_time as modifyTime,create_time as createTime"
						+ " from shop_product where pro_class in (" + buffer.toString() + ")");
		if (products == null || products.isEmpty()) {
			return null;
		}
		// 排序
		sortGoodsListByThreeSelections(products, main, saleNum, price);
		return products;
	}

	/**
	 * 判断属性是否符合
	 * 
	 * @param Product
	 *            product
	 * @param Integer
	 *            attrValueId
	 * @return boolean
	 * @throws Exception
	 */
	private boolean isAttrValueOk(Product product, Integer attrValueId) throws Exception {
		// 若 为空 则不进行筛选，直接返回true
		if (attrValueId == null) {
			return true;
		}
		Record record = Db.findById("shop_attr_value", attrValueId);
		if (record == null) {
			throw new Exception(MessageFormat.format("id:{0}的属性值信息为空", attrValueId));
		}
		if (StrKit.isBlank(product.getAttr())) {
			return false;
		}
		return product.getAttr().contains(record.getStr("value"));
	}

	/**
	 * 根据价格筛选商品
	 * 
	 * @param List<Product>
	 *            products
	 * @param Double
	 *            priceFrom
	 * @param Double
	 *            priceTo
	 * @return
	 */
	private List<Product> filterProductByPriceArea(List<Product> products, Double priceFrom, Double priceTo) {
		List<Product> filterResult = new ArrayList<>();
		for (Product product : products) {
			if (isInPriceArea(priceFrom, priceTo, product.getPriceArea())) {
				filterResult.add(product);
			}
		}
		return filterResult;
	}

	private boolean isInPriceArea(Double priceFrom, Double priceTo, String priceArea) {
		String[] priceArray = priceArea.split("-");
		Double prdFrom = Double.valueOf(priceArray[0]);
		Double prdTo = Double.valueOf(priceArray[1]);
		// 价格区间为空，默认不排序
		if (priceFrom == null && priceArea == null) {
			return true;
		} else if (priceFrom != null && prdTo == null) {
			if (priceFrom.compareTo(prdTo) <= 0)
				return true;
		} else if (priceFrom == null && priceTo != null) {
			if (priceTo.compareTo(prdFrom) >= 0)
				return true;
		} else {
			if ((prdFrom.compareTo(priceFrom) >= 0 && prdFrom.compareTo(priceTo) <= 0)
					|| (prdTo.compareTo(priceFrom) >= 0 && prdTo.compareTo(priceTo) <= 0))
				return true;
		}
		return false;
	}

	/**
	 * 判断价格是否符合
	 * 
	 * @param Goods
	 *            goods
	 * @param Long
	 *            priceFrom
	 * @param Long
	 *            priceTo
	 * @return boolean
	 */
	private boolean isPriceOk(Goods goods, Double priceFrom, Double priceTo) {
		Double price = goods.getPrice();
		return price.compareTo(priceFrom) >= 0 && price.compareTo(priceTo) <= 0;

	}

	/**
	 * 根据三个选择排序商品列表
	 * 
	 * @param Integer
	 *            main
	 * @param Integer	
	 *            salesnum
	 * @param Integer
	 *            price
	 * @return
	 */
	private void sortGoodsListByThreeSelections(List<Record> goods, final Integer main, final Integer salesnum,
			final Integer price) {
		// 主排序 降序
		Collections.sort(goods, new Comparator<Record>() {

			@Override
			public int compare(Record o1, Record o2) {
				// 非空的在前面
				if (o1 == null && o2 == null)
					return 0;
				if (o1 != null && o2 == null)
					return -1;
				if (o1 == null && o2 != null)
					return 1;
				switch (main) {
				// 综合排序
				case 0:
					;
					// 评价最好
				case 1:
					String guid1 = o1.getStr("guid");
					String guid2 = o2.getStr("guid");
					Record c1 = Db.findFirst("select count(distinct coll_guid) as num from shop_member_coll coll "
							+ " left join shop_goods goods on goods.guid = coll.coll_guid "
							+ "  where goods.pro_guid=?", guid1);
					Record c2 = Db.findFirst("select count(distinct coll_guid) as num from shop_member_coll coll "
							+ " left join shop_goods goods on goods.guid = coll.coll_guid "
							+ "  where goods.pro_guid=?", guid2);
					if (c1 == null && c2 == null && main == 1) {
						return 0;
					}
					if (c1 != null && c2 == null)
						return -1;
					if (c1 == null && c2 != null)
						return 1;

					Integer c1Num = c1.getInt("num");
					Integer c2Num = c2.getInt("num");
					if (c1Num == null && c2Num == null && main == 1) {
						return 0;
					}
					if (c1Num != null && c2Num == null)
						return -1;
					if (c1Num == null && c2Num != null)
						return 1;
					if (c1Num.equals(c2Num) && main == 1) {
						return 0;
					}
					if (c1Num.compareTo(c2Num) > 0) {
						return -1;
					} else {
						return 1;
					}

					// 最新商品
				case 2:
					Date o1CreateTime = o1.getDate("createTime");
					Date o2CreateTime = o2.getDate("createTime");
					if (o1CreateTime == null && o2CreateTime == null)
						return 0;
					if (o1CreateTime == null && o2CreateTime != null)
						return 1;
					if (o1CreateTime != null && o2CreateTime == null)
						return -1;
					return o1CreateTime.after(o2CreateTime) ? -1 : o1CreateTime.before(o2CreateTime) ? 1 : 0;
				default:
					return 0;
				}

			}

		});

		if (salesnum != 0) {
			// 销量 -1 降序 1 升序
			Collections.sort(goods, new Comparator<Record>() {

				@Override
				public int compare(Record o1, Record o2) {
					// 非空的在前面
					if (o1 == null && o2 == null)
						return 0;
					if (o1 != null && o2 == null)
						return -1;
					if (o1 == null && o2 != null)
						return 1;
					Integer salesnum1 = o1.getInt("salenum");
					Integer salesnum2 = o2.getInt("salenum");
					if (salesnum1 == null && salesnum2 == null) {
						return 0;
					}
					if (salesnum1 == null && salesnum2 != null) {
						return 1;
					}
					if (salesnum1 != null && salesnum2 == null) {
						return -1;
					}

					// 销量高的在前面
					if (salesnum < 0) {
						return salesnum1.equals(salesnum2) ? 0 : (salesnum1.compareTo(salesnum2) > 0 ? -1 : 1);
					} else {
						return salesnum1.equals(salesnum2) ? 0 : (salesnum1.compareTo(salesnum2) > 0 ? 1 : -1);
					}

				}
			});
		}

		if (price != 0) {
			// 价格 降序
			Collections.sort(goods, new Comparator<Record>() {

				@Override
				public int compare(Record o1, Record o2) {
					// 非空的排在前面
					if (o1 == null && o2 == null)
						return 0;
					if (o1 != null && o2 == null)
						return -1;
					if (o1 == null && o2 != null)
						return 1;
					boolean priceArea1IsNull = StrKit.isBlank(o1.getStr("price"));
					boolean priceArea2IsNull = StrKit.isBlank(o2.getStr("price"));
					if (priceArea1IsNull && priceArea2IsNull) {
						return 0;
					}
					if (priceArea1IsNull && !priceArea2IsNull) {
						return 1;
					}
					if (!priceArea1IsNull && priceArea2IsNull) {
						return -1;
					}
					Double price1 = Double.valueOf(o1.getStr("price").split("-")[0]);
					Double price2 = Double.valueOf(o2.getStr("price").split("-")[0]);
					// 价格高的排在前面
					if (price < 0) {
						return price1.equals(price2) ? 0 : price1.compareTo(price2) > 0 ? -1 : 1;
					} else {
						return price1.equals(price2) ? 0 : price1.compareTo(price2) > 0 ? 1 : -1;
					}
				}
			});
		}

	}
}
