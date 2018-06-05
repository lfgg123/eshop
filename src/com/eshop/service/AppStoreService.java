package com.eshop.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.eshop.model.Store;
import com.eshop.util.ConfigUtils;
import com.eshop.util.DistanceUtil;
import com.eshop.util.LogConst;
import com.jfinal.kit.StrKit;

public class AppStoreService {
	public final static AppStoreService service = new AppStoreService();
	
	private Logger logger = Logger.getLogger(LogConst.SERVER_LOG);
	
	private static Store dao = Store.dao;

	/**
	 * 获取919门店列表
	 */
	public List<Store> get919StoreInfoList() {
		return dao.find("select * from shop_store");
	}

	/**
	 * 根据经纬度获取919门店
	 * 
	 * @throws Exception
	 */
	public Store get919StoreByLatAndLon(String lat, String lon) throws Exception {
		if (StrKit.isBlank(lat)) {
			throw new Exception("纬度不能为空");
		}
		if (StrKit.isBlank(lon)) {
			throw new Exception("经度不能为空");
		}
		logger.info("lat=" + lat + ", lon=" + lon);
//		return dao.findFirst("select * from shop_store where lat=? and lon =?", lat, lon);
		return this.get919StoreInDeliveryArea(lat, lon).get(0);
	}

	/**
	 * 获取配送范围内的门店
	 * 
	 * @param double
	 *            lat
	 * @param double
	 *            lon
	 * @return List<Store>
	 * @throws Exception
	 * 
	 */

	public List<Store> get919StoreInDeliveryArea(String lat, String lon) throws Exception {
		if (StrKit.isBlank(lat)) {
			throw new Exception("纬度不能为空");
		}
		if (StrKit.isBlank(lon)) {
			throw new Exception("经度不能为空");
		}
		List<Store> result = null;
		List<Store> stores = dao.find("select * from shop_store");
		double deliveryArea = Double.valueOf(ConfigUtils.getProperty("delivery.area"));
		double latDouble = Double.valueOf(lat);	
		double lonDouble = Double.valueOf(lon);
		logger.info(latDouble + "::::" + lonDouble + ":::" + deliveryArea);
		if (stores != null && !stores.isEmpty()) {
			result = new ArrayList<Store>();
			for (Store store : stores) {
				double actualDistance = DistanceUtil.getDistance(lonDouble, latDouble, Double.valueOf(store.getLon()),
						Double.valueOf(store.getLat()));
				logger.info("actualDistance==" + actualDistance);
				if (actualDistance <= deliveryArea) {
					result.add(store);
				}
			}
		}
		logger.info("result.size()==="+result.size());
		return result;
	}

}
