package com.eshop.service;

import java.util.ArrayList;
import java.util.List;

import com.eshop.model.Clazz;

public class AppClazzService {
	private static Clazz dao = Clazz.dao;
	
	/**
	 * 根据级别获取类的列表
	 * @param int grade 级别
	 * @return List<Clazz>
	 * */
	public List<Clazz> getGoodClazzByClassGrade(int grade){
		return dao.find("select * from shop_class where grade =?",grade);
	}
	
	/**
	 * 根据parentId获取类的列表
	 * @param String parent
	 * @return List<Clazz>
	 * */
	public List<Clazz> getGoodClazzByParent(String parent){
		List<Clazz> secClazzs= dao.find("select * from shop_class where parent = ?",parent);
		if(secClazzs!=null&&!secClazzs.isEmpty()) {
			for(Clazz sec:secClazzs) {
				sec.setSubClassWithSql();
				}
			}
		return secClazzs;
	}
	

}
