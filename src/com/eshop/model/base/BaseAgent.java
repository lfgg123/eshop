package com.eshop.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseAgent<M extends BaseAgent<M>> extends Model<M> implements IBean {

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

	public M setName(java.lang.String name) {
		set("name", name);
		return (M)this;
	}

	public java.lang.String getName() {
		return getStr("name");
	}

	public M setCode(java.lang.String code) {
		set("code", code);
		return (M)this;
	}

	public java.lang.String getCode() {
		return getStr("code");
	}

	public M setStore(java.lang.String store) {
		set("store", store);
		return (M)this;
	}

	public java.lang.String getStore() {
		return getStr("store");
	}

	public M setTel(java.lang.String tel) {
		set("tel", tel);
		return (M)this;
	}

	public java.lang.String getTel() {
		return getStr("tel");
	}

	public M setYcode(java.lang.String ycode) {
		set("ycode", ycode);
		return (M)this;
	}

	public java.lang.String getYcode() {
		return getStr("ycode");
	}

	public M setImg(java.lang.String img) {
		set("img", img);
		return (M)this;
	}

	public java.lang.String getImg() {
		return getStr("img");
	}

	public M setCreateTime(java.util.Date createTime) {
		set("create_time", createTime);
		return (M)this;
	}

	public java.util.Date getCreateTime() {
		return get("create_time");
	}

}
