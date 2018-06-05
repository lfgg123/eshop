package com.eshop.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eshop.model.Goods;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class AppMemberHistoryService {
	public final static AppMemberHistoryService service = new AppMemberHistoryService();

	/**
	 * 获取用户浏览历史
	 * 
	 * @param String
	 *            member
	 * @return List
	 */
	public List getMemberBrowsingHistory(String member) {
		List<Map> result = null;
		List<Record> browsingHistory = Db
				.find("select * from shop_member_history where member=? order by create_time desc", member);
		if (null != browsingHistory) {
			result = new ArrayList<>();
			for (Record record : browsingHistory) {
				Goods goods = Goods.dao.findFirst("select * from shop_goods where guid = ?",
						record.getStr("good_guid"));
				if (null != goods) {
					Map<String, Object> localMap = new HashMap<String, Object>();
					localMap.put("member", record.getStr("member"));
					localMap.put("goodGuid", record.getStr("good_guid"));
					localMap.put("createTime", record.getDate("create_time"));
					localMap.put("goods", goods);
					result.add(localMap);
				}
			}
		}
		return result;
	}

	/**
	 * 保存用户浏览记录
	 * 
	 * @param String
	 *            goodGuid
	 * @param String
	 *            member
	 * @return boolean
	 */
	public boolean saveMemberHistory(String goodGuid, String member) {
		Record record = new Record();
		record.set("good_guid", goodGuid);
		record.set("member", member);
		record.set("create_time", new Date());
		return Db.save("shop_member_history", record);
	}

	/**
	 * 清除用户浏览数据
	 * 
	 * @param String
	 *            member
	 * @return boolean
	 */
	public boolean clearMemberHistory(String member) {
		Record record = Db.findFirst("select * from shop_member_history where member=?", member);
		if (record == null) {
			return true;
		}
		return Db.update("delete from shop_member_history where member=?", member) > 0 ? true : false;
	}

}
