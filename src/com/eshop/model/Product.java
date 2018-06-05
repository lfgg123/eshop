package com.eshop.model;

import com.eshop.model.base.BaseProduct;

import java.util.List;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Product extends BaseProduct<Product> {
	public static final Product dao = new Product().dao();

	private List<Goods> goods;

	public List<Goods> getGoods() {
		return goods;
	}

	public void setGoods(List<Goods> goods) {
		this.goods = goods;
	}
}