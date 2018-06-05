package com.eshop.service;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import com.eshop.model.Bbs;
import com.eshop.model.BbsReturn;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.sun.org.apache.bcel.internal.generic.DDIV;
import com.sun.org.apache.bcel.internal.generic.NEW;

public class AppBbsService {

	private final static Bbs dao = Bbs.dao;
	public static final AppBbsService service = new AppBbsService();

	/**
	 * 获取私人定制列表
	 */
	public List<Bbs> getBbsList() {
		return dao.find("select * from shop_bbs");
	}

	/**
	 * 根据Id 获取私人定制详情
	 * 
	 * @param Long
	 *            bbsId
	 * @return Bbs
	 */
	public Bbs getBbsById(Long bbsId) {
		Bbs result = dao.findById(bbsId);
		result.getBbsReturns();
		;
		result.getRecommendGoods();
		return result;
	}

	/**
	 * 私人定制点赞
	 * 
	 * @param Long
	 *            bbsId
	 * @return boolean
	 * @throws Exception
	 */
	public boolean upvoteBbs(Long bbsId) throws Exception {
		if (null == bbsId) {
			throw new Exception("私人定制id不能为空");
		}
		Bbs bbs = dao.findById(bbsId);
		if (null == bbs) {
			throw new Exception(MessageFormat.format("id:{0} 的私人定制不存在", bbsId));
		}
		bbs.setGNum(bbs.getGNum() == null ? 1 : bbs.getGNum() + 1);
		return bbs.update();
	}

	/**
	 * 取消点赞
	 * 
	 * @param Long
	 *            bbsId
	 * @return boolean
	 * @throws Exception
	 */
	public boolean downvoteBbs(Long bbsId) throws Exception {
		if (null == bbsId) {
			throw new Exception("私人定制id不能为空");
		}
		Bbs bbs = dao.findById(bbsId);
		if (null == bbs) {
			throw new Exception(MessageFormat.format("id:{0} 的私人定制不存在", bbsId));
		}

		if (bbs.getGNum() == null || bbs.getGNum() <= 0) {
			throw new Exception("操作无效");
		}
		bbs.setGNum(bbs.getGNum() - 1);
		return bbs.update();
	}

	/**
	 * 私人定制收藏
	 * 
	 * @param bbsId
	 * @param stirng
	 *            member
	 * @throws Exception
	 */
	public boolean collectBbs(final Long bbsId, final String member) throws Exception {
		if (null == bbsId) {
			throw new Exception("私人定制id不能为空");
		}
		if (StrKit.isBlank(member)) {
			throw new Exception("在线会员信息的guid 不能为空");
		}
		// 判断是否收藏当前用户是否收藏过
		Record isExist = Db.findFirst("select * from shop_member_coll coll"
				+ " left join shop_bbs bbs on bbs.guid = coll.coll_guid " + "where member=? and bbs.id=?", member,
				bbsId);
		if (null != isExist) {
			throw new Exception("当前文章已经在收藏列表中");
		}

		return Db.tx(new IAtom() {

			@Override
			public boolean run() throws SQLException {
				Bbs bbs = dao.findById(bbsId);

				// 插入会员收藏表
				Record collect = new Record();
				collect.set("type", 1);
				collect.set("coll_guid", bbs.getGuid());
				collect.set("member", member);
				collect.set("create_time", new Date());
				boolean res1 = Db.save("shop_member_coll", collect);
				// 私人定制收藏字段自增
				bbs.setCNum(bbs.getCNum() == null ? 1 : bbs.getCNum() + 1);
				boolean res2 = bbs.update();
				return res1 && res2;
			}
		});

	}

	/**
	 * 私人定制回复
	 * 
	 * @param Long
	 *            bbsId
	 * @param String
	 *            member
	 * @param String
	 *            content
	 * @return boolean
	 * @throws Exception
	 */
	public boolean replyBbs(final Long bbsId, final String member, final String content) throws Exception {
		if (null == bbsId) {
			throw new Exception("私人定制id不能为空");
		}
		if (StrKit.isBlank(member)) {
			throw new Exception("在线会员信息的guid 不能为空");
		}
		return Db.tx(new IAtom() {

			@Override
			public boolean run() throws SQLException {
				Bbs bbs = dao.findById(bbsId);

				// 插入回复
				BbsReturn bbsReturn = new BbsReturn();
				bbsReturn.setBbsGuid(bbs.getGuid());
				bbsReturn.setMember(member);
				bbsReturn.setContent(content);
				bbsReturn.setIsG(0);
				bbsReturn.setCreateTime(new Date());
				boolean res1 = bbsReturn.save();
				// 更新私人定制表
				bbs.setRNum(bbs.getRNum() == null ? 1 : bbs.getRNum() + 1);
				boolean res2 = bbs.update();
				return res1 && res2;
			}
		});

	}

	/**
	 * 私人定制取消收藏
	 * 
	 * @param bbsGuid
	 * @param stirng
	 *            member
	 * @throws Exception
	 */
	public boolean cancelBbsCollection(final String bbsGuid, final String member) throws Exception {
		if (StrKit.isBlank(bbsGuid)) {
			throw new Exception("私人定制guid不能为空");
		}
		if (StrKit.isBlank(member)) {
			throw new Exception("在线会员信息的guid 不能为空");
		}

		return Db.tx(new IAtom() {

			@Override
			public boolean run() throws SQLException {
				Bbs bbs = dao.findFirst("select * from shop_bbs where guid = ?", bbsGuid);
				// 删除会员收藏表
				boolean res1 = Db.update("delete from shop_member_coll where coll_guid=? and member=?", bbsGuid,
						member) > 0 ? true : false;
				// 私人定制收藏字段自减
				bbs.setCNum(bbs.getCNum() - 1);
				boolean res2 = bbs.update();
				return res1 && res2;
			}
		});

	}

}
