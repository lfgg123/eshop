package com.eshop.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseModule<M extends BaseModule<M>> extends Model<M> implements IBean {

	public M setId(java.lang.Integer id) {
		set("id", id);
		return (M)this;
	}

	public java.lang.Integer getId() {
		return getInt("id");
	}

	public M setName(java.lang.String name) {
		set("name", name);
		return (M)this;
	}

	public java.lang.String getName() {
		return getStr("name");
	}

	public M setUrl(java.lang.String url) {
		set("url", url);
		return (M)this;
	}

	public java.lang.String getUrl() {
		return getStr("url");
	}

	public M setParent(java.lang.Integer parent) {
		set("parent", parent);
		return (M)this;
	}

	public java.lang.Integer getParent() {
		return getInt("parent");
	}

	public M setMindex(java.lang.String mindex) {
		set("mindex", mindex);
		return (M)this;
	}

	public java.lang.String getMindex() {
		return getStr("mindex");
	}

	public M setIcon(java.lang.String icon) {
		set("icon", icon);
		return (M)this;
	}

	public java.lang.String getIcon() {
		return getStr("icon");
	}

	public M setIsuse(java.lang.Integer isuse) {
		set("isuse", isuse);
		return (M)this;
	}

	public java.lang.Integer getIsuse() {
		return getInt("isuse");
	}

	public M setOper(java.lang.String oper) {
		set("oper", oper);
		return (M)this;
	}

	public java.lang.String getOper() {
		return getStr("oper");
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

	public M setOrderno(java.lang.Integer orderno) {
		set("orderno", orderno);
		return (M)this;
	}

	public java.lang.Integer getOrderno() {
		return getInt("orderno");
	}

}
