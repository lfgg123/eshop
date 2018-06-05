package com.eshop.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseUser<M extends BaseUser<M>> extends Model<M> implements IBean {

	public M setId(java.lang.Integer id) {
		set("id", id);
		return (M)this;
	}

	public java.lang.Integer getId() {
		return getInt("id");
	}

	public M setAccount(java.lang.String account) {
		set("account", account);
		return (M)this;
	}

	public java.lang.String getAccount() {
		return getStr("account");
	}

	public M setPassword(java.lang.String password) {
		set("password", password);
		return (M)this;
	}

	public java.lang.String getPassword() {
		return getStr("password");
	}

	public M setBz(java.lang.String bz) {
		set("bz", bz);
		return (M)this;
	}

	public java.lang.String getBz() {
		return getStr("bz");
	}

	public M setRole(java.lang.Integer role) {
		set("role", role);
		return (M)this;
	}

	public java.lang.Integer getRole() {
		return getInt("role");
	}

	public M setCreateTime(java.util.Date createTime) {
		set("create_time", createTime);
		return (M)this;
	}

	public java.util.Date getCreateTime() {
		return get("create_time");
	}

	public M setModifyTime(java.util.Date modifyTime) {
		set("modify_time", modifyTime);
		return (M)this;
	}

	public java.util.Date getModifyTime() {
		return get("modify_time");
	}

	public M setState(java.lang.Integer state) {
		set("state", state);
		return (M)this;
	}

	public java.lang.Integer getState() {
		return getInt("state");
	}

}
