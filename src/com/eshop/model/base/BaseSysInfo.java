package com.eshop.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseSysInfo<M extends BaseSysInfo<M>> extends Model<M> implements IBean {

	public M setGuid(java.lang.String guid) {
		set("guid", guid);
		return (M)this;
	}

	public java.lang.String getGuid() {
		return getStr("guid");
	}

	public M setName(java.lang.String name) {
		set("name", name);
		return (M)this;
	}

	public java.lang.String getName() {
		return getStr("name");
	}

	public M setLogo(java.lang.String logo) {
		set("logo", logo);
		return (M)this;
	}

	public java.lang.String getLogo() {
		return getStr("logo");
	}

	public M setDescription(java.lang.String description) {
		set("description", description);
		return (M)this;
	}

	public java.lang.String getDescription() {
		return getStr("description");
	}

	public M setKeywords(java.lang.String keywords) {
		set("keywords", keywords);
		return (M)this;
	}

	public java.lang.String getKeywords() {
		return getStr("keywords");
	}

	public M setTitle(java.lang.String title) {
		set("title", title);
		return (M)this;
	}

	public java.lang.String getTitle() {
		return getStr("title");
	}

	public M setAddress(java.lang.String address) {
		set("address", address);
		return (M)this;
	}

	public java.lang.String getAddress() {
		return getStr("address");
	}

	public M setZipcode(java.lang.String zipcode) {
		set("zipcode", zipcode);
		return (M)this;
	}

	public java.lang.String getZipcode() {
		return getStr("zipcode");
	}

	public M setPhone(java.lang.String phone) {
		set("phone", phone);
		return (M)this;
	}

	public java.lang.String getPhone() {
		return getStr("phone");
	}

	public M setEmail(java.lang.String email) {
		set("email", email);
		return (M)this;
	}

	public java.lang.String getEmail() {
		return getStr("email");
	}

	public M setCusterm(java.lang.String custerm) {
		set("custerm", custerm);
		return (M)this;
	}

	public java.lang.String getCusterm() {
		return getStr("custerm");
	}

	public M setOper(java.lang.String oper) {
		set("oper", oper);
		return (M)this;
	}

	public java.lang.String getOper() {
		return getStr("oper");
	}

}
