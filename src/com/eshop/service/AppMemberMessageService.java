package com.eshop.service;

import java.util.List;

import com.eshop.model.MemberMessage;

public class AppMemberMessageService {
	public final static AppMemberMessageService service = new AppMemberMessageService();
	private static MemberMessage dao = MemberMessage.dao;

	/**
	 * 根据消息类型获取用户消息列表
	 * @param Integer type
	 * @param String member
	 * @return List<MemberMessage>
	 * */
	public List<MemberMessage> getMemberMessageByType(Integer type,String member){
		return dao.find("select * from shop_member_message where 	send_state=1 and sendee = ? and type=?",member,type);
	}
	

}
