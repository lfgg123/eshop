package com.eshop.service;

import java.util.List;

import com.eshop.model.MemberPublish;
import com.eshop.model.MemberReturn;

public class AppMemberReturnService {
	public final static AppMemberReturnService service = new AppMemberReturnService();
	private MemberReturn dao = MemberReturn.dao;

	/**
	 * 获取用户回复列表
	 * 
	 * @param String
	 *            member
	 * @return List<MemberReturn>
	 */
	public List<MemberReturn> getMemberReturnList(String member) {
		List<MemberReturn> returns = dao.find("select re.*,m.nick,m.img from shop_member_return re left join shop_member m on m.guid = re.member where member = ?", member);
		if (null != returns) {
			for (MemberReturn ret : returns) {
				/*MemberPublish publish = MemberPublish.dao.findFirst("select * from shop_member_publish where guid=?",
						ret.getPublishGuid());*/
				MemberPublish publish = MemberPublish.dao.findFirst("select sp.*,shop_member.nick,shop_member.img from shop_member_publish sp left join shop_member on shop_member.guid=sp.member where sp.guid=?",
						ret.getPublishGuid());
				ret.setPublish(publish);
			}
		}
		return returns;
	}

	/**
	 * 获取用户的回复列表
	 * 
	 * @param String
	 *            member
	 * @return List<MemberReturn>
	 */
	public List<MemberReturn> getReturnForMemberList(String member) {
		//List<MemberReturn> returns = dao.find("select r.* from shop_member_return r"
				//+ " left join shop_member_publish p on p.guid = r.publish_guid" + " where p.member = ?", member);
		
		List<MemberReturn> returns = dao.find("select r.*,shop_member.img,shop_member.nick from shop_member_return r"
				+ " left join shop_member_publish p on p.guid = r.publish_guid left join shop_member on shop_member.guid=r.member " + " where p.member = ?", member);
		if (null != returns) {
			for (MemberReturn ret : returns) {
				MemberPublish publish = MemberPublish.dao.findFirst("select * from shop_member_publish where guid=?",
						ret.getPublishGuid());
				ret.setPublish(publish);
			}
		}
		return returns;
	}
	
	
	/**
	 * 删除用户回复
	 * @param Integer id
	 * @throws Exception 
	 * */
	public boolean deleteMemberReturnById(Integer id) throws Exception {
		if(id==null) {
			throw new Exception("用户回复id不能为空");
		}
		MemberReturn memberReturn = dao.findById(id);
		if(memberReturn==null) {
			return true;
		}
		return memberReturn.delete();
	}
	

}
