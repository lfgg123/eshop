package com.eshop.service;

import com.eshop.model.Cart;
import com.eshop.model.CartItem;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class AppCartService {

    private static Cart dao = Cart.dao;

    private static CartItem idao = CartItem.dao;

    public int getCartItemAmount(String member){
        Cart cart = checkCart(member);
        return Db.find("select * from shop_cart_item where cart_guid = ?", cart.getGuid()).size();
    }

    /**
     * 删除购物车货品
     * @param member
     * @param goodGuid
     * @return
     */
    public boolean deleteCartItem(String member,String goodGuid){
        Cart cart = checkCart(member);
        CartItem cartItem = idao.findFirst("select * from shop_cart_item where cart_guid = ? and good_guid = ?",cart.getGuid(),goodGuid);
        if(cartItem!=null){
        	return cartItem.delete();
        }else{
        	return false;
        }
    }

    /**
     * 删除购物车货品
     * @param member
     * @param goodGuids
     * @return
     */
    public boolean deleteCartItems(String member,String goodGuids){
        Cart cart = checkCart(member);
        String[] goodGuidArr = goodGuids.split(",");
        for(int i=0;i<goodGuidArr.length;i++){
            CartItem cartItem = idao.findFirst("select * from shop_cart_item where cart_guid = ? and good_guid = ?",cart.getGuid(),goodGuidArr[i]);
            if(null != cartItem){
                cartItem.delete();
            }
        }
        return true;
    }

    /**
     * 添加货品到购物车
     * @param goodGuid
     * @param member
     * @param quantity
     * @return 返回购物车里当前商品的条目数
     */
    public int goodToCart(String goodGuid,String member,Integer quantity,Integer prdType){
        Cart cart = checkCart(member);//确保创建了购物车主记录
        CartItem cartItem = idao.findFirst("select * from shop_cart_item where cart_guid = ? and good_guid = ?",cart.getGuid(),goodGuid);
        if(null != cartItem){//如果购物车里已经有该商品了， 那么就更新数量
            cartItem.setQuantity(quantity+cartItem.getQuantity());
            Record updateRecord = new Record().setColumns(cartItem);
            Db.update("shop_cart_item","id",updateRecord);
        }else{
        	//添加商品明细
        	Record newRecord = new Record().set("cart_guid",cart.getGuid()).set("quantity",quantity).set("good_guid",goodGuid);
        	if(null !=prdType && 1 == prdType){
        		newRecord.set("prd_type",prdType);
        	}
        	Db.save("shop_cart_item",newRecord);
        }
        return Db.find("select * from shop_cart_item where cart_guid = ?", cart.getGuid()).size();
    }

    /**
     * 购物车货品列表
     * @param member
     * @return
     */
    public List<CartItem> getGoodsFromCart(String member){
        Cart cart = checkCart(member);
        List<CartItem> cartItems = idao.find("select sci.id,sci.good_guid ,sci.quantity,sci.prd_type,sg.image,sg.name good_name,sg.price,sg.pro_guid from shop_cart_item sci left join shop_goods sg on sg.guid = sci.good_guid where sci.cart_guid = ?",cart.getGuid());
        for(CartItem cartItem : cartItems){
            cartItem.setPrice(cartItem.getDouble("price"));
            cartItem.setProductGuid(cartItem.getStr("pro_guid"));
            cartItem.setGoodSpecList(cartItem.findGoodSpec());
        }
        return cartItems;
    }

    private Cart checkCart(String member){
        Cart cart = dao.findFirst("select * from shop_cart where member = ?",member);
        if(null == cart){
            cart = new Cart();
            Record cartR = new Record();
            cart.setGuid(UUID.randomUUID().toString());
            cart.setMember(member);
            cart.setCreateTime(new Date());
            cartR.setColumns(cart);
            boolean result = Db.save("shop_cart",cartR);
        }
        return cart;
    }
    /**
     * 待确认商品列表
     * @param ids
     * @return ArrayList
     */
    public List<CartItem> confirmOrderGoods(Long[] ids){
        ArrayList list = new ArrayList();
        for(int i=0;i<ids.length;i++){
            
        	if(ids[i]!=null){
        		 CartItem cartItem = CartItem.dao.findFirst("select sci.good_guid,sci.quantity,sci.id,sci.prd_type,sg.price,sg.image,sg.name good_name from shop_cart_item sci left join shop_goods sg on sci.good_guid = sg.guid where sci.id = ?",ids[i]);
        		 if(cartItem!=null){
                 cartItem.setPrice(cartItem.getDouble("price"));
                 cartItem.setGoodSpecList(cartItem.findGoodSpec());
                 list.add(cartItem);
        		 }
        	}
        }
        return list;
    }
}
