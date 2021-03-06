package com.eshop.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseProduct<M extends BaseProduct<M>> extends Model<M> implements IBean {

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

	public M setPriceArea(java.lang.String priceArea) {
		set("price_area", priceArea);
		return (M)this;
	}

	public java.lang.String getPriceArea() {
		return getStr("price_area");
	}

	public M setImage(java.lang.String image) {
		set("image", image);
		return (M)this;
	}

	public java.lang.String getImage() {
		return getStr("image");
	}

	public M setImg1(java.lang.String img1) {
		set("img1", img1);
		return (M)this;
	}

	public java.lang.String getImg1() {
		return getStr("img1");
	}

	public M setImg2(java.lang.String img2) {
		set("img2", img2);
		return (M)this;
	}

	public java.lang.String getImg2() {
		return getStr("img2");
	}

	public M setImg3(java.lang.String img3) {
		set("img3", img3);
		return (M)this;
	}

	public java.lang.String getImg3() {
		return getStr("img3");
	}

	public M setImg4(java.lang.String img4) {
		set("img4", img4);
		return (M)this;
	}

	public java.lang.String getImg4() {
		return getStr("img4");
	}

	public M setIntroduction(java.lang.String introduction) {
		set("introduction", introduction);
		return (M)this;
	}

	public java.lang.String getIntroduction() {
		return getStr("introduction");
	}

	public M setSalenum(java.lang.Integer salenum) {
		set("salenum", salenum);
		return (M)this;
	}

	public java.lang.Integer getSalenum() {
		return getInt("salenum");
	}

	public M setProClass(java.lang.String proClass) {
		set("pro_class", proClass);
		return (M)this;
	}

	public java.lang.String getProClass() {
		return getStr("pro_class");
	}
	
	public M setAttr(java.lang.String attr) {
		set("attr", attr);
		return (M)this;
	}

	public java.lang.String getAttr() {
		return getStr("attr");
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

}
