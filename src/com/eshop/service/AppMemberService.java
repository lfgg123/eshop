package com.eshop.service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.eshop.model.Cart;
import com.eshop.model.Member;
import com.eshop.model.MemberGrade;
import com.eshop.model.Order;
import com.eshop.util.ConfigUtils;
import com.eshop.util.LogConst;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class AppMemberService {

	// 指定输出到pay.log
	private Logger logger = Logger.getLogger(LogConst.SERVER_LOG);

	public final static AppMemberService service = new AppMemberService();

	private final static AppCartService appCartService = new AppCartService();

	private static Member dao = Member.dao;

	/**
	 * 根据guid 获取会员信息
	 * 
	 * @param String
	 *            guid
	 * @return Member member
	 * @throws Exception
	 */
	public Record getMemberInfoByGuid(String guid) throws Exception {
		if (StrKit.isBlank(guid)) {
			throw new Exception("会员guid 不能为空");
		}
		Record member = Db.findFirst("select * from shop_member where guid = ?", guid);
		if (null == member) {
			// 配送员信息
			member = Db.findFirst("select * from shop_agent where guid = ?", guid);
			if (null == member)
				throw new Exception(MessageFormat.format("guid为{0}的会员信息不存在!", guid));
			return member;
		} else {
			// 等级信息
			Integer lGrade;
			if ((lGrade = member.getInt("grade")) != null && lGrade > 0) {
				member.set("gradeInfo",
						MemberGrade.dao.findFirst("select * from shop_member_grade where grade = ?", lGrade));
			}

			// 查找该用户的购物车里有多少商品条目
			Cart cart = Cart.dao.findFirst("select * from shop_cart where member = ?", guid);
			if (cart != null) {
				int count = Db.find("select * from shop_cart_item where cart_guid = ?", cart.getGuid()).size();
				member.set("cartItemCount", count);
			}

			return member;
		}
	}

	/**
	 * 修改头像
	 * 
	 * @param String
	 *            guid
	 * @param String
	 *            imgUrl
	 * @return boolean
	 * @throws Exception
	 */
	public boolean changeHeadPortrait(String guid, String imgUrl) throws Exception {
		Record member = Db.findFirst("select * from shop_member where guid = ?", guid);
		if (null == member) {
			throw new Exception(MessageFormat.format("guid:{0} 的用户信息不存在", guid));
		}
		member.set("img", imgUrl);

		return Db.update("shop_member", member);
	}

	/**
	 * 修改昵称
	 * 
	 * @param String
	 *            guid
	 * @param String
	 *            nick
	 * @return boolean
	 * @throws Exception
	 */
	public boolean modifyNick(String guid, String nick) throws Exception {
		Record member = Db.findFirst("select * from shop_member where guid = ?", guid);
		if (null == member) {
			throw new Exception(MessageFormat.format("guid:{0} 的用户信息不存在", guid));
		}

		member.set("nick", nick);
		return Db.update("shop_member", member);
	}

	/**
	 * 修改性别
	 * 
	 * @param String
	 *            guid
	 * @param String
	 *            gender
	 * @return boolean
	 * @throws Exception
	 **/
	public boolean modifyGender(String guid, Integer gender) throws Exception {
		Record member = Db.findFirst("select * from shop_member where guid = ?", guid);
		if (null == member) {
			throw new Exception(MessageFormat.format("guid:{0} 的用户信息不存在", guid));
		}
		member.set("sex", gender);
		return Db.update("shop_member", member);
	}

	/**
	 * 修改出生日期
	 * 
	 * @param String
	 *            guid
	 * @param String
	 *            date
	 * @return boolean
	 * @throws Exception
	 */
	public boolean modifyBirthDate(String guid, Date birthDate) throws Exception {
		Record member = Db.findFirst("select * from shop_member where guid = ?", guid);
		if (null == member) {
			throw new Exception(MessageFormat.format("guid:{0} 的用户信息不存在", guid));
		}
		member.set("birth", birthDate);
		return Db.update("shop_member", member);
	}

	/**
	 * 获取受邀请的用户列表
	 * 
	 * @param member
	 * @return
	 */
	public List<Member> getInviteMemberList(String member) {
		return dao.find("select * from shop_member sm where up_code = (select my_code from shop_member where guid = ?)",
				member);
	}

	/**
	 * 根据手机号获取会员信息
	 * 
	 * @param String
	 *            mobile
	 * @return Member
	 * 
	 */
	public Member getMemberByMobile(String mobile) {
		Member member = dao.findFirst("select * from shop_member where phone = ?", mobile);
		if (member != null) {
			member.setGradeInfoBySql();
		}
		return member;
	}

	/**
	 * 更新用户的积分
	 * 
	 * @param order
	 * @throws Exception
	 */
	public void updateScore(Order order) {
		String guid = order.getMember();
		Record member = Db.findFirst("select * from shop_member where guid = ?", guid);
		if (null != member) {
			logger.info("updateScore:::::member.getInt(\"evalue\")=" + member.getInt("evalue") + "  order.getFee()="
					+ order.getFee());
			double score = member.getInt("evalue") + order.getFee();
			logger.info("积分测试模式..." + ConfigUtils.getProperty("testFlag"));
			if ("1".equals(ConfigUtils.getProperty("testFlag"))) {
				// 测试模式
				score = member.getInt("evalue") + 100d;
			}
			member.set("evalue", score);
			Db.update("shop_member", member);
		} else {
			logger.info("更新用户积分失败，用户[" + guid + "]不存在.");
		}
	}

}
