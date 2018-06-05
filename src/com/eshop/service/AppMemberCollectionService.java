package com.eshop.service;

import com.eshop.model.Bbs;
import com.eshop.model.Goods;
import com.eshop.model.MemberPublish;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import java.text.MessageFormat;
import java.util.*;

public class AppMemberCollectionService {
	public static final AppMemberCollectionService service = new AppMemberCollectionService();
 	
	/**
	 * 根据收藏类型获取用户收藏列表
	 * @param String member
	 * @param Integer type
	 * @return List<Record>
	 * */
	public List getMemberCollectionListByType(String member,Integer type) {
		List<Map> result = new ArrayList<Map>();
		List<Record> collList = Db.find("select * from shop_member_coll where member = ? and type=?",member,type);
		if(null!=collList) {
			for(Record record:collList) {
			Map<String,Object> resObject = new HashMap<String,Object>();
			resObject.put("type", record.getInt("type"));
			resObject.put("collGuid",record.get("coll_guid"));
			resObject.put("member", record.get("member"));
			resObject.put("collection",getDifferentCollectionInfoBySql(type, record.getStr("coll_guid")));
			result.add(resObject);
			}
		}
		return result;
	}
	
	/**
	 * 取消用户收藏
	 * @param String member
	 * @param String collGuid
	 * @param Integer type
	 * @return boolean
	 * @throws Exception 
	 * */
	public boolean cancelCollection(String member,String collGuid) throws Exception {
	    Record collection = Db.findFirst("select * from shop_member_coll where member=? and coll_guid=?",member,collGuid);
		if(null==collection) {
			throw new Exception(MessageFormat.format("用户guid:{0},收藏品guid:{1}的收藏信息不存在，无法完成当前操作",member,collGuid));
		}
		switch (collection.getInt("type")) {
		case 1:
			return AppBbsService.service.cancelBbsCollection(collGuid, member);
		case 2:
			return Db.update("delete from shop_member_coll where coll_guid = ? and member=?",collGuid,member)==1?true:false;
		case 3 :
			return AppMemberPublishService.service.cancelPostCollection(collGuid, member);
		default:
			return false;
		}
		
		
		
	}
	
	/**
	 * 根据type 返回不同收藏品的信息
	 * @param Integer type
	 * @return Object
	 * 
	 * */
	private Object getDifferentCollectionInfoBySql(Integer type,String collGuid) {
		
		switch (type) {
		// 私人定制
		case 1:
			return  Bbs.dao.findFirst("select * from shop_bbs where guid=?",collGuid);
		// 货品	
		case 2 :
			return Goods.dao.findFirst("select * from shop_goods where guid=?",collGuid);
		// 会员帖子	
		case 3:
			return MemberPublish.dao.findFirst("select * from shop_member_publish where guid=?",collGuid);
		}
		return null ;
	}
	
	
	/**
	 * 添加收藏
	 * @param Integer type
	 * @param String collGuid
	 * @param String memberGuid
	 * @return boolean 
	 * @throws Exception 
	 * 
	 * */
	public boolean addCollection(Integer type,String collGuid,String memberGuid) throws Exception {
		if(type==null) {
			throw new Exception("收藏类型不能为空");
		}
		if(StrKit.isBlank(collGuid)) {
			throw new Exception("收藏物品guid 不能为空");
		}
		if(StrKit.isBlank(memberGuid)) {
			throw new Exception("当前会员的guid 不能为空");
		}
		// 检查收藏的内容是否存在
		switch (type) {
		// 私人定制
		case 1:
			Bbs isExistBbs = Bbs.dao.findFirst("select * from shop_bbs where guid = ?",collGuid);
			if(isExistBbs==null) {
				throw new Exception("当前收藏内容不存在，请检查 收藏类型 或者 唯一标识 是否正确");
			}
			break;
			// 货品
		case 2:
			Goods isExistGoods = Goods.dao.findFirst("select * from shop_goods where guid = ?",collGuid);
			if(isExistGoods==null) {
				throw new Exception("当前收藏内容不存在，请检查 收藏类型 或者 唯一标识 是否正确");
			}
			break;
			// 会员帖子
		case 3:
			MemberPublish isExistPublish = MemberPublish.dao.findFirst("select * from shop_bbs where guid = ?",collGuid);
			if(isExistPublish==null) {
				throw new Exception("当前收藏内容不存在，请检查 收藏类型 或者 唯一标识 是否正确");
			}
			break;

		default:
			throw new Exception("不支持的收藏类型");
		}
		
		//查看是否收藏过
		Record isExist = Db.findFirst("select * from shop_member_coll where type = ? and coll_guid=? and member = ?",type,collGuid,memberGuid);
		if(isExist !=null) {
			throw new Exception("收藏列表中已存在，请勿重复操作！");
		}
		Record record = new Record();
		record.set("type", type);
		record.set("coll_guid", collGuid);
		record.set("member", memberGuid);
		record.set("create_time", new Date());
		return Db.save("shop_member_coll", record);
	}
	


}
