package com.eshop.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseGoods<M extends BaseGoods<M>> extends Model<M> implements IBean {

	public M setId(java.lang.Integer id) {
		set("id", id);
		return (M)this;
	}

	public java.lang.Integer getId() {
		return getInt("id");
	}

	public M setProGuid(java.lang.String proGuid) {
		set("pro_guid", proGuid);
		return (M)this;
	}

	public java.lang.String getProGuid() {
		return getStr("pro_guid");
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

	public M setRemark(java.lang.String remark) {
		set("remark", remark);
		return (M)this;
	}

	public java.lang.String getRemark() {
		return getStr("remark");
	}

	public M setPrice(java.lang.Double price) {
		set("price", price);
		return (M)this;
	}

	public java.lang.Double getPrice() {
		return getDouble("price");
	}

	public M setImage(java.lang.String image) {
		set("image", image);
		return (M)this;
	}

	public java.lang.String getImage() {
		return getStr("image");
	}

	public M setStock(java.lang.Integer stock) {
		set("stock", stock);
		return (M)this;
	}

	public java.lang.Integer getStock() {
		return getInt("stock");
	}

	public M setSalenum(java.lang.Integer salenum) {
		set("salenum", salenum);
		return (M)this;
	}

	public java.lang.Integer getSalenum() {
		return getInt("salenum");
	}

	public M setIsHot(java.lang.Integer isHot) {
		set("is_hot", isHot);
		return (M)this;
	}

	public java.lang.Integer getIsHot() {
		return getInt("is_hot");
	}

	public M setIsGround(java.lang.Integer isGround) {
		set("is_ground", isGround);
		return (M)this;
	}

	public java.lang.Integer getIsGround() {
		return getInt("is_ground");
	}

	public M setSeoDescription(java.lang.String seoDescription) {
		set("seo_description", seoDescription);
		return (M)this;
	}

	public java.lang.String getSeoDescription() {
		return getStr("seo_description");
	}

	public M setSeoKeywords(java.lang.String seoKeywords) {
		set("seo_keywords", seoKeywords);
		return (M)this;
	}

	public java.lang.String getSeoKeywords() {
		return getStr("seo_keywords");
	}

	public M setSeoTitle(java.lang.String seoTitle) {
		set("seo_title", seoTitle);
		return (M)this;
	}

	public java.lang.String getSeoTitle() {
		return getStr("seo_title");
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
