package com.eshop.service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.eshop.model.GoodEva;
import com.eshop.model.Member;
import com.jfinal.kit.StrKit;

public class AppGoodEvaService {
	private static GoodEva dao = GoodEva.dao;

	/**
	 * 获取用户评价列表
	 * 
	 * @param String
	 *            member
	 * @return
	 * @throws Exception
	 */
	public List<GoodEva> getMemberGoodsEvaList(String memberGuid) throws Exception {
		if (StrKit.isBlank(memberGuid)) {
			throw new Exception("当前用户信息的guid 不能为空");
		}
		List<GoodEva> goodEvaList = dao.find("select id,good_guid,member,isuse from shop_good_eva where member = ?",
				memberGuid);
		if (null != goodEvaList) {
			for (GoodEva goodEva : goodEvaList) {
				goodEva.setGoodsBySql();
			}
		}
		return goodEvaList;
	}

	/**
	 * 根据Id获取评价详情
	 * 
	 * @param Long
	 *            id
	 * @return GoodsEva
	 */
	public GoodEva getGoodsEvaById(Long id) {
		return dao.findById(id);
	}

	/**
	 * 删除用户评价
	 * 
	 * @param Long
	 *            id
	 * @return boolean
	 */
	public boolean deleteGoodsEvaById(Long id) {
		GoodEva goodEva = dao.findById(id);
		if (goodEva == null) {
			return true;
		}
		return dao.deleteById(id);
	}

	/**
	 * 追加评论
	 * 
	 * @param Long
	 *            id
	 * @param String
	 *            sContent
	 * @param String
	 *            sImg
	 * @return boolean
	 * @throws Exception
	 */
	public boolean addSecondComment(Long id, String sContent, String sImg) throws Exception {
		GoodEva goodEva = dao.findById(id);
		if (null == goodEva) {
			throw new Exception(MessageFormat.format("id:{0} 的用户评价信息不存在", id));
		}
		if (1 == goodEva.getIsuse()) {
			throw new Exception("当前订单中的商品已完成追评，请勿重复操作！");
		}
		goodEva.setSecondContent(sContent);
		goodEva.setSecondImg(sImg);
		goodEva.setSecondTime(new Date());
		goodEva.setIsuse(1);
		return goodEva.update();
	}

	/**
	 * 提交评价
	 * 
	 * @param String
	 *            goodGuid
	 * @param String
	 *            memberGuid
	 * @param String
	 *            fContent
	 * @param String
	 *            fImg
	 * @throws Exception
	 */
	public boolean addMemberComment(String goodGuid, String memberGuid, String fContent, String fImg, String sn)
			throws Exception {
		GoodEva isExistEva = dao.findFirst(
				"select eva.* from shop_good_eva eva" + " left join shop_order_item i on i.good_guid = eva.good_guid"
						+ " where eva.good_guid =? and eva.member = ?",
				goodGuid, memberGuid);
		if (isExistEva != null) {
//			throw new Exception("当前订单中的商品已完成评价，请勿重复操作！");
			return false;
		}
		GoodEva goodEva = new GoodEva();
		goodEva.setGuid(UUID.randomUUID().toString());
		goodEva.setGoodGuid(goodGuid);
		goodEva.setSn(sn);
		goodEva.setMember(memberGuid);
		goodEva.setFirstContent(fContent);
		goodEva.setFirstImg(fImg);
		goodEva.setFirstTime(new Date());
		return goodEva.save();
	}

}
