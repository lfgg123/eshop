package com.eshop.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseOrderItem<M extends BaseOrderItem<M>> extends Model<M> implements IBean {

	public M setId(java.lang.Integer id) {
		set("id", id);
		return (M)this;
	}

	public java.lang.Integer getId() {
		return getInt("id");
	}

	public M setOrder(java.lang.Integer order) {
		set("order", order);
		return (M)this;
	}

	public java.lang.Integer getOrder() {
		return getInt("order");
	}

	public M setGoodGuid(java.lang.String goodGuid) {
		set("good_guid", goodGuid);
		return (M)this;
	}

	public java.lang.String getGoodGuid() {
		return getStr("good_guid");
	}

	public M setName(java.lang.String name) {
		set("name", name);
		return (M)this;
	}

	public java.lang.String getName() {
		return getStr("name");
	}

	public M setQuantity(java.lang.Integer quantity) {
		set("quantity", quantity);
		return (M)this;
	}

	public java.lang.Integer getQuantity() {
		return getInt("quantity");
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

	public M setSn(java.lang.String sn) {
		set("sn", sn);
		return (M)this;
	}

	public java.lang.String getSn() {
		return getStr("sn");
	}

	public M setIsBack(java.lang.Integer isBack) {
		set("is_back", isBack);
		return (M)this;
	}

	public java.lang.Integer getIsBack() {
		return getInt("is_back");
	}

	public M setEvaStatus(java.lang.Integer evaStatus) {
		set("eva_status", evaStatus);
		return (M)this;
	}

	public java.lang.Integer getEvaStatus() {
		return getInt("eva_status");
	}

}
