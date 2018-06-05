package com.eshop.service;

import com.eshop.model.MemberPublish;
import com.eshop.model.MemberReturn;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.sun.istack.internal.FinalArrayList;
import com.sun.org.apache.bcel.internal.generic.NEW;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class AppMemberPublishService {
	public final static AppMemberPublishService service = new AppMemberPublishService();
	private final static MemberPublish dao = MemberPublish.dao;

	/**
	 * 保存帖子
	 * 
	 * @param MemberPublish
	 *            publish
	 * @return Integer
	 */
	public boolean savePublish(Record rd) {
		MemberPublish post = new MemberPublish();
		post.setGuid(UUID.randomUUID().toString());
		rd.set("guid", UUID.randomUUID().toString());
		return Db.save("shop_member_publish", rd);
	}

	/**
	 * 获取帖子列表
	 * 
	 * @return List<MemberPublish>
	 */
	public List<Record> getPostList() {
		return Db.find(
				"select a.*,b.nick userName,b.img userImg from shop_member_publish a left join shop_member b on a.member = b.guid order by a.create_time desc");
	}

	/**
	 * 根据Id 获取帖子
	 * 
	 * @param Integer
	 *            id
	 * @return MemberPublish
	 */
	public MemberPublish getPostById(Integer id) {
		MemberPublish publish = dao.findById(id);
		if (null != publish) {
			// 获取发帖人信息
			publish.setMemberInfoBySql();
			List<MemberReturn> returns = MemberReturn.dao
					.find("select * from shop_member_return where publish_guid = ?", publish.getGuid());
			if (null != returns && !returns.isEmpty()) {
				for (MemberReturn lReturn : returns) {
					lReturn.setMemberInfoBySql();
				}
				publish.setReturns(returns);
			}
		}
		return publish;
	}

	/**
	 * 点赞帖子
	 * 
	 * @param Integer
	 *            id
	 * @return Boolean
	 * @throws Exception
	 */
	public boolean upvotePost(Integer id) throws Exception {
		if (null == id) {
			throw new Exception("帖子Id 不能为空");
		}
		MemberPublish memberPublish = dao.findById(id);
		if (null == memberPublish) {
			throw new Exception(MessageFormat.format("id:{0} 的帖子不存在", id));
		}
		memberPublish.setGNum(memberPublish.getGNum() == null ? 1 : memberPublish.getGNum() + 1);
		return memberPublish.update();
	}

	/**
	 * 取消帖子点赞
	 * 
	 * @param Integer
	 *            id
	 * @return Boolean
	 * @throws Exception
	 */
	public boolean downvotePost(Integer id) throws Exception {
		if (null == id) {
			throw new Exception("帖子Id 不能为空");
		}
		MemberPublish memberPublish = dao.findById(id);
		if (null == memberPublish) {
			throw new Exception(MessageFormat.format("id:{0} 的帖子不存在", id));
		}

		if (memberPublish.getGNum() == null || memberPublish.getGNum() <= 0) {
			throw new Exception("操作无效");
		}
		return memberPublish.setGNum(memberPublish.getGNum() - 1).update();
	}

	/**
	 * 收藏帖子
	 * 
	 * @param Integer
	 *            postId
	 * @param String
	 *            memberGuid
	 * @return boolean
	 * @throws Exception
	 */
	public boolean collectPost(final Integer postId, final String memberGuid) throws Exception {
		if (null == postId) {
			throw new Exception("帖子Id不能为空");
		}
		if (StrKit.isBlank(memberGuid)) {
			throw new Exception("会员guid不能为空");
		}

		return Db.tx(new IAtom() {

			@Override
			public boolean run() throws SQLException {
				MemberPublish memberPublish = dao.findById(postId);
				Record collect = new Record();
				collect.set("type", 3);
				collect.set("coll_guid", memberPublish.getGuid());
				collect.set("member", memberGuid);
				collect.set("create_time", new Date());
				boolean res1 = Db.save("shop_member_coll", collect);
				// 修改 member publish 中的收藏数量
				memberPublish.setCNum(memberPublish.getCNum() == null ? 1 : memberPublish.getCNum() + 1);
				boolean res2 = memberPublish.update();
				return res1 && res2;
			}

		});

	}

	/**
	 * 评论帖子
	 * 
	 * @param Integer
	 *            postId
	 * @param String
	 *            memberGuid
	 * @param String
	 *            content
	 * @return boolean
	 * @throws Exception
	 */
	public boolean commentPost(final Integer postId, final String memberGuid, final String content) throws Exception {
		if (null == postId) {
			throw new Exception("帖子Id 不能为空");
		}
		if (StrKit.isBlank(memberGuid)) {
			throw new Exception("会员guid 不能为空");
		}

		if (StrKit.isBlank(content)) {
			throw new Exception("评论内容不能为空");
		}

		return Db.tx(new IAtom() {

			@Override
			public boolean run() throws SQLException {
				MemberPublish memberPublish = dao.findById(postId);

				// 插入评论表
				MemberReturn comment = new MemberReturn();
				comment.setPublishGuid(memberPublish.getGuid());
				comment.setMember(memberGuid);
				comment.setContent(content);
				comment.setIsG(0);
				comment.setIsC(0);
				comment.setCreateTime(new Date());
				boolean res1 = comment.save();
				// 更新member_publish 评论信息
				memberPublish.setRNum(memberPublish.getRNum() == null ? 1 : memberPublish.getRNum() + 1);
				boolean res2 = memberPublish.update();
				return res1 && res2;
			}
		});
	}

	/**
	 * 取消帖子收藏
	 * 
	 * @param String
	 *            postGuid
	 * @param String
	 *            memberGuid
	 * @return boolean
	 * @throws Exception
	 */
	public boolean cancelPostCollection(final String postGuid, final String memberGuid) throws Exception {
		if (StrKit.isBlank(postGuid)) {
			throw new Exception("帖子guid不能为空");
		}
		if (StrKit.isBlank(memberGuid)) {
			throw new Exception("会员guid不能为空");
		}

		return Db.tx(new IAtom() {

			@Override
			public boolean run() throws SQLException {
				MemberPublish memberPublish = dao.findFirst("select * from shop_member_publish where guid=?", postGuid);

				boolean res1 = Db.update("delete from shop_member_coll where coll_guid=? and member=?", postGuid,
						memberGuid) > 0 ? true : false;
				// 修改 member publish 中的收藏数量
				memberPublish.setCNum(memberPublish.getCNum() - 1);
				boolean res2 = memberPublish.update();
				return res1 && res2;
			}

		});

	}

	/**
	 * 获取用户帖子列表
	 * 
	 * @param String
	 *            member
	 * @return List<MemberPublish>
	 */
	public List<MemberPublish> getMemberPublishList(String member) {
		return dao.find("select * from shop_member_publish where member = ?", member);
	}

	/**
	 * 删除帖子
	 * 
	 * @param Integer
	 *            id
	 * @throws Exception
	 */
	public boolean deleteMemberPublistById(Integer id) throws Exception {
		if (id == null) {
			throw new Exception("帖子id不能为空");
		}
		final MemberPublish publish = dao.findById(id);
		if (publish == null) {
			return true;
		}
		return Db.tx(new IAtom() {

			@Override
			public boolean run() throws SQLException {
				// 删除收藏
				boolean res1 = true;
				if (publish.getCNum() > 0)
					res1 = Db.update("delete from shop_member_coll where 	coll_guid = ?", publish.getGuid()) >= 0
							? true
							: false;
				// 删除回复
				boolean res2 = true;
				if (publish.getRNum() > 0) {
					res2 = Db.update("delete from shop_member_return where publish_guid=?", publish.getGuid()) > 0
							? true
							: false;
				}
				// 删除帖子
				boolean res3 = publish.delete();
				return res1 && res2 && res3;
			}

		});
	}

}
