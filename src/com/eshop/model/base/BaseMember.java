package com.eshop.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseMember<M extends BaseMember<M>> extends Model<M> implements IBean {

	public M setId(java.lang.Integer id) {
		set("id", id);
		return (M)this;
	}

	public java.lang.Integer getId() {
		return getInt("id");
	}

	public M setGuid(java.lang.String guid) {
		set("guid", guid);
		return (M)this;
	}

	public java.lang.String getGuid() {
		return getStr("guid");
	}

	public M setNick(java.lang.String nick) {
		set("nick", nick);
		return (M)this;
	}

	public java.lang.String getNick() {
		return getStr("nick");
	}

	public M setPhone(java.lang.String phone) {
		set("phone", phone);
		return (M)this;
	}

	public java.lang.String getPhone() {
		return getStr("phone");
	}

	public M setImg(java.lang.String img) {
		set("img", img);
		return (M)this;
	}

	public java.lang.String getImg() {
		return getStr("img");
	}

	public M setSex(java.lang.Integer sex) {
		set("sex", sex);
		return (M)this;
	}

	public java.lang.Integer getSex() {
		return getInt("sex");
	}

	public M setBirth(java.util.Date birth) {
		set("birth", birth);
		return (M)this;
	}

	public java.util.Date getBirth() {
		return get("birth");
	}

	public M setWine(java.lang.Integer wine) {
		set("wine", wine);
		return (M)this;
	}

	public java.lang.Integer getWine() {
		return getInt("wine");
	}

	public M setGrade(java.lang.Integer grade) {
		set("grade", grade);
		return (M)this;
	}

	public java.lang.Integer getGrade() {
		return getInt("grade");
	}

	public M setEvalue(java.lang.Integer evalue) {
		set("evalue", evalue);
		return (M)this;
	}

	public java.lang.Integer getEvalue() {
		return getInt("evalue");
	}

	public M setRegTime(java.util.Date regTime) {
		set("reg_time", regTime);
		return (M)this;
	}

	public java.util.Date getRegTime() {
		return get("reg_time");
	}

	public M setLoginTime(java.util.Date loginTime) {
		set("login_time", loginTime);
		return (M)this;
	}

	public java.util.Date getLoginTime() {
		return get("login_time");
	}

	public M setModifyTime(java.util.Date modifyTime) {
		set("modify_time", modifyTime);
		return (M)this;
	}

	public java.util.Date getModifyTime() {
		return get("modify_time");
	}

	public M setMyCode(java.lang.String myCode) {
		set("my_code", myCode);
		return (M)this;
	}

	public java.lang.String getMyCode() {
		return getStr("my_code");
	}

	public M setUpCode(java.lang.String upCode) {
		set("up_code", upCode);
		return (M)this;
	}

	public java.lang.String getUpCode() {
		return getStr("up_code");
	}

	public M setIsuse(java.lang.Integer isuse) {
		set("isuse", isuse);
		return (M)this;
	}

	public java.lang.Integer getIsuse() {
		return getInt("isuse");
	}

}
