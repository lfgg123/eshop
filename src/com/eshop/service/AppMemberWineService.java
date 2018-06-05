package com.eshop.service;

import com.eshop.model.MemberWine;
import com.jfinal.kit.StrKit;

import java.util.List;

public class AppMemberWineService {
    private MemberWine dao = MemberWine.dao;

    /**
     * 获取用户目前拥有酒币
     * @param member
     * @return int
     */
    public int getWine(String member){
        List<MemberWine> memberWines = dao.find("select *	 from shop_member_wine where member = ?",member);
        int totalWine = 0;
        for(MemberWine mw:memberWines){
            if(mw.getIsuse()==0){
                totalWine += mw.getWine();
            }else{
                totalWine -= mw.getWine();
            }
        }
        return totalWine;
    }
    
    /**
     * 
     * 获取用户酒币收支明细
     * @param String member
     * @return List<MemberWine>	
     * @throws Exception 
     * **/
    public List<MemberWine> getMemberWineCoinInfoList(String member) throws Exception{
    		if(StrKit.isBlank(member)) {
    			throw new Exception("当前用户信息的 guid 不能为空");
    		}
    		return dao.find("select * from shop_member_wine where member = ?",member);
    }
}
