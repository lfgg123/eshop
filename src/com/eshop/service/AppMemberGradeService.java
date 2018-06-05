package com.eshop.service;

import com.eshop.model.MemberGrade;

public class AppMemberGradeService {
    private static MemberGrade dao = MemberGrade.dao;

    /**
     * 根据会员等级获取配送费
     * @param member
     * @return
     */
    public int getPayForDeliver(String member){
        MemberGrade memberGrade = dao.findFirst("select smg.* from shop_member_grade smg left join shop_member sm on sm.grade = smg.grade and sm.guid = ?",member);
        return 0;
    }
}
