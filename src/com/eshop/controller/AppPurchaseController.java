package com.eshop.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.StringUtils;
import com.eshop.exception.EshopAppGetParaException;
import com.eshop.interceptor.AppLoginInterceptor;
import com.eshop.model.*;
import com.eshop.service.*;
import com.eshop.util.IdWorker;
import com.eshop.util.LogConst;
import com.eshop.vo.ResultJson;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 购买流程
 */
public class AppPurchaseController extends Controller{

    private Logger logger = Logger.getLogger(LogConst.PAY_LOG);

    private AppProductService appProductService = new AppProductService();

    private AppEnoughCutService appEnoughCutService = new AppEnoughCutService();

    private AppCartService appCartService = new AppCartService();

    private AppMemberWineService appMemberWineService = new AppMemberWineService();

    private AppMemberGradeService appMemberGradeService = new AppMemberGradeService();

    private AppMemberService memberService = new AppMemberService();

    private AppOrderService appOrderService = new AppOrderService();

    private AppGoodsService appGoodsService = new AppGoodsService();

    /**
     * 按类别获取商品列表
     */
    public void getProductListByProClass(){
        try {
            String proClass = getPara("proClass");
            if(StringUtils.isEmpty(proClass))
                throw new EshopAppGetParaException("proClass不能为空");
            renderJson(ResultJson.success(appProductService.getProductListByProClass(proClass)));
        } catch (Exception e) {
            logger.error("按类别获取商品列表失败",e);
            throw new EshopAppGetParaException("按类别获取商品列表失败");
        }
    }

    /**
     * 单个商品
     */
    @Before(Tx.class)
    @Clear(AppLoginInterceptor.class)
    public void getProductInfo(){
        try {
            String productGuid = getPara("productGuid");
            if(StringUtils.isEmpty(productGuid))
                throw new EshopAppGetParaException("productGuid不能为空");
            //足迹
            String member = getPara("TOKEN");
            if(!StringUtils.isEmpty(member)){
                Record newRecord = new Record().set("member",member).set("create_time",new Date()).set("good_guid",productGuid);
                Db.save("shop_member_history",newRecord);
            }
            renderJson(ResultJson.success(appProductService.getProduct(getPara("productGuid"))));
        } catch (Exception e) {
            logger.error("获取单个商品失败",e);
            throw new EshopAppGetParaException("获取单个商品失败");
        }
    }

    /**
     * 根据商品获取货品
     */
    @Clear(AppLoginInterceptor.class)
    public void getGoodsByPro(){
        try {
            String productGuid = getPara("productGuid");
            if(StringUtils.isEmpty(productGuid))
                throw new EshopAppGetParaException("productGuid不能为空");
            Object json = JSONObject.toJSON(appGoodsService.getGoods(getPara("productGuid")));
            renderJson(ResultJson.success(json));
        } catch (Exception e) {
            logger.error("获取货品失败",e);
            throw new EshopAppGetParaException("获取货品失败");
        }
    }

    /**
     * 获取商品评价
     */
    @Clear(AppLoginInterceptor.class)
    public void getGoodEva(){
        try {
            String productGuid = getPara("productGuid");
            if(StringUtils.isEmpty(productGuid))
                throw new EshopAppGetParaException("productGuid不能为空");
            renderJson(ResultJson.success(appProductService.getGoodEva(getPara("productGuid"))));
        } catch (Exception e) {
            logger.error("获取商品评价失败",e);
            throw new EshopAppGetParaException("获取商品评价失败");
        }
    }

    /**
     * 获取满减送货品列表
     */
    public void getEnoughCutGoodList(){
        try {
            EnoughCut enoughCut = appEnoughCutService.getEnoughCut();
            if(null != enoughCut){
                Object json = JSONObject.toJSON(appEnoughCutService.getEnoughCutProductList(enoughCut));
                renderJson(ResultJson.success(json));
            }
        } catch (Exception e) {
            logger.error("获取满减送货品列表失败",e);
            throw new EshopAppGetParaException("获取满减送货品列表失败");
        }

    }

    /**
     * 添加货品至购物车
     */
    @Before(POST.class)
    public void goodToCart(){
    	logger.info("good to cart....");
        String goodGuid = getPara("goodGuid");
        String member = getPara("TOKEN");
        Integer quantity = getParaToInt("quantity");
        Integer prdType = getParaToInt("prdType");
        if(StringUtils.isEmpty(goodGuid) && StringUtils.isEmpty(member) && null == quantity)
            throw new EshopAppGetParaException("必要参数不能为空");
        try {
            int count = appCartService.goodToCart(goodGuid,member,quantity,prdType);
			if(count > 0){
                renderJson(ResultJson.success(count));
            }else{
                renderJson(ResultJson.fail(0));
            }
        } catch (Exception e) {
            logger.error("添加货品至购物车失败",e);
            throw new EshopAppGetParaException("添加货品至购物车失败");
        }
    }

    /**
     * 购物车货品列表
     */
    public void getGoodsFromCart(){
        try {
            Object json = JSONObject.toJSON(appCartService.getGoodsFromCart(getPara("TOKEN")));
            renderJson(ResultJson.success(json));
        } catch (Exception e) {
            logger.error("获取购物车货品列表失败",e);
            throw new EshopAppGetParaException("获取购物车货品列表失败");
        }
    }

    /**
     * 立即购买入口
     */
    @Before(POST.class)
    public void buyNow(){
        if(StringUtils.isEmpty(getPara("goodGuid")) && null == getParaToInt("quantity"))
            throw new EshopAppGetParaException("必要参数不能为空");
        String member = getPara("TOKEN");
        List<CartItem> cartItems = new ArrayList<>();
        try {
            Goods goods = appGoodsService.getGood(getPara("goodGuid"),getParaToInt("quantity"));
            if(goods!=null){
                CartItem cartItem = new CartItem();
                 cartItem.setGoodGuid(goods.getGuid());
                 cartItem.setQuantity(goods.getQuantity());
                 cartItem.setPrice(goods.getPrice());
                 cartItems.add(cartItem);
                 int wine = appMemberWineService.getWine(member);
                 Object orderGoods = JSONObject.toJSON(goods);
                 JSONObject jsonObject = new JSONObject();
                 jsonObject.put("orderGoods",orderGoods);
                 jsonObject.put("wine",wine);
                 jsonObject.put("cut",appEnoughCutService.getEnoughCutAmount(cartItems));
                 jsonObject.put("payForDeliver",appMemberGradeService.getPayForDeliver(member));
                 renderJson(ResultJson.success(jsonObject));
            }else{
            	    renderJson(ResultJson.fail());
            }
        } catch (Exception e) {
            logger.error("确认订单页面获取部分数据失败",e);
            throw new EshopAppGetParaException("确认订单页面获取部分数据失败");
        }
    }

    /**
     * 购物车入口
     * 确认订单页面的商品列表,满减金额,酒币,配送费
     */
    @Before(POST.class)
    public void confirmOrderGoods(){
        if(StringUtils.isEmpty(getPara("cartItems")))
            throw new EshopAppGetParaException("cartItems不能为空");
        JSONArray items = JSONArray.parseArray(getPara("cartItems"));
        if(items.size() <= 0){
            throw new EshopAppGetParaException("订单货品不能为空");
        }
        Long[] cartItemIds = new Long[items.size()];
        String member = getPara("TOKEN");
        try {
            for(int i=0;i<items.size();i++){
                JSONObject cartItem = items.getJSONObject(i);
                if(cartItem.getInteger("selected") == 1)
                    cartItemIds[i] = cartItem.getLong("id");
            }
            List<CartItem> cartItems = appCartService.confirmOrderGoods(cartItemIds);
            int wine = appMemberWineService.getWine(member);
            Object orderGoods = JSONObject.toJSON(cartItems);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("orderGoods",orderGoods);
            jsonObject.put("wine",wine);
            jsonObject.put("cut",appEnoughCutService.getEnoughCutAmount(cartItems));
            jsonObject.put("payForDeliver",appMemberGradeService.getPayForDeliver(member));
            renderJson(ResultJson.success(jsonObject));
        } catch (Exception e) {
            logger.error("确认订单页面获取部分数据失败",e);
            throw new EshopAppGetParaException("确认订单页面获取部分数据失败");
        }
    }

    /**
     * 取消订单
     */
    @Before({Tx.class})
    public void cancelOrder(){
        String sn = getPara("sn");
        if(StringUtils.isEmpty(sn))
            throw new EshopAppGetParaException("sn不能为空");
        if(appOrderService.cancelOrder(sn)){
            renderJson(ResultJson.success());
        }else{
            renderJson(ResultJson.fail());
        }
    }

    /**
     * 删除订单
     */
    public void deleteOrder(){
        String sn = getPara("sn");
        if(StringUtils.isEmpty(sn))
            throw new EshopAppGetParaException("sn不能为空");
        if(appOrderService.deleteOrder(sn)){
            renderJson(ResultJson.success());
        }else{
            renderJson(ResultJson.fail());
        }
    }
    
    /**
     * 确认收货
     */
    public void confirmGetGoods(){
    	 String sn = getPara("sn");
    	if(appOrderService.confirmGetGoods(sn)){
            renderJson(ResultJson.success());
        }else{
            renderJson(ResultJson.fail());
        }
     }

    /**
     * 删除购物车货品
     */
    public void deleteItemFromCart(){
        try {
            String goodGuid = getPara("goodGuid");
            if(StringUtils.isEmpty(goodGuid))
                throw new EshopAppGetParaException("goodGuid不能为空");
            appCartService.deleteCartItem(getPara("TOKEN"),goodGuid);
            int count = appCartService.getCartItemAmount(getPara("TOKEN"));
            if(count >= 0){
                renderJson(ResultJson.success(count));
            }else{
                renderJson(ResultJson.fail(0));
            }
        } catch (Exception e) {
            logger.error("删除购物车货品失败",e);
            throw new EshopAppGetParaException("删除购物车货品失败");
        }
    }

    /**
     * 批量删除购物车货品
     */
    public void deleteFromCart(){
        try {
            String goodGuids = getPara("goodGuids");
            if(StringUtils.isEmpty(goodGuids))
                throw new EshopAppGetParaException("goodGuid不能为空");
            appCartService.deleteCartItems(getPara("TOKEN"),goodGuids);
            int count = appCartService.getCartItemAmount(getPara("TOKEN"));
            if(count >= 0){
                renderJson(ResultJson.success(count));
            }else{
                renderJson(ResultJson.fail(0));
            }
        } catch (Exception e) {
            logger.error("删除购物车货品失败",e);
            throw new EshopAppGetParaException("删除购物车货品失败");
        }
    }

    /**
     * 创建订单,需要如下数据:
     * 购物车货品信息,地址,联系人,送达时间,
     * 配送费,满减优惠,优惠券,留言,酒币抵扣,
     * 是否匿名购买,实付款
     *
     * 创建订单流程:
     * 1.生成订单
     * 2.生成订单详情
     */
    @Before({POST.class,Tx.class})
    public void newOrder(){
        try {
            //1.生成订单 MemberAddress address = getBean(MemberAddress.class,"");
            String orderInfo = getPara("orderInfo");
            if(StringUtils.isEmpty(orderInfo))
                throw new EshopAppGetParaException("orderInfo不能为空");
            Order order = generateOrder(orderInfo);
            //2.生成订单详情
            String json = getPara("goods");
            if(StringUtils.isEmpty(json))
                throw new EshopAppGetParaException("goods不能为空");
            List<OrderItem> orderItems = new ArrayList<>();
            JSONArray jsonarry = JSONArray.parseArray(json);
            if(jsonarry.size() <= 0){
                throw new EshopAppGetParaException("订单货品不能为空");
            }
            for(int i=0;i<jsonarry.size();i++){
                JSONObject good = jsonarry.getJSONObject(i);
                Goods goods = appGoodsService.getGood(good.getString("goodGuid"),null);
                if(goods.getStock()<good.getIntValue("quantity"))
                    throw new EshopAppGetParaException("库存不够");
                OrderItem orderItem = new OrderItem();
                orderItem.setGoodGuid(good.getString("goodGuid"));
                orderItem.setPrice(good.getDouble("price"));
                orderItem.setQuantity(good.getIntValue("quantity"));
                orderItem.setName(good.getString("goodName"));
                orderItem.setImage(good.getString("image"));
                orderItem.setIsBack(1);
                orderItem.setEvaStatus(1);
                orderItem.setOrder(order.getId());
                orderItem.setSn(order.getSn());
                orderItems.add(orderItem);
                appOrderService.createOrderItem(orderItem);
                //删除订单在购物车中的货品
                appCartService.deleteCartItem(getPara("TOKEN"),good.getString("goodGuid"));
                goods.setStock(goods.getStock() - orderItem.getQuantity());
                Record updateRecord = new Record().setColumns(goods);
                Db.update("shop_goods","id",updateRecord);
            }
            order.setOrderType(appOrderService.getOrderType(orderItems,getPara("TOKEN")));
            Record updateRecord = new Record().setColumns(order);
            Db.update("shop_order","id",updateRecord);
            renderJson(ResultJson.success(order.getSn()));
        }catch (EshopAppGetParaException e){
            logger.error("",e);
            throw e;
        } catch (Exception e) {
            logger.error("生成订单失败",e);
            throw new EshopAppGetParaException("生成订单失败");
        }

    }

    //测试,待删
    public void getTest(){
        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItem.setGoodGuid("0d31037f17dc4cf892a20bf9d4abda0e");
        orderItems.add(orderItem);
        Integer type = appOrderService.getOrderType(orderItems,"47bf3d8e-63a9-4b8e-be5d-0ae43772febf");
        renderJson(type);
    }

    /**
     * 生成订单:
     * 订单编号规则(暂定):eshop+时间+自增数字
     */
    private Order generateOrder(String orderInfo){
        JSONObject detail = JSON.parseObject(orderInfo);
        logger.info("用户"+getPara("TOKEN")+"准备生成订单");
        Order order = new Order();
        String sn = "eshop"+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+ leftPad(String.valueOf(IdWorker.incrementAndGet()));
        order.setSn(sn);
        order.setMember(getPara("TOKEN"));
        order.setProvince(detail.getString("province"));
        order.setCity(detail.getString("city"));
        order.setArea(detail.getString("area"));
        order.setAddress(detail.getString("address"));
        order.setConsignee(detail.getString("consignee"));
        order.setFee(detail.getDouble("fee"));
        order.setFreight(detail.getIntValue("freight"));
        order.setDeliveryTime(detail.getString("delivery_time"));
        order.setFullCutDeals(detail.getIntValue("full_cut_deals"));
        order.setWine(detail.getIntValue("wine"));
        order.setIsnee(detail.getIntValue("isnee"));
        order.setPhone(detail.getString("phone"));
        if(order.getFullCutDeals()>0)
            order.setIsEnoughCut(1);
        else
            order.setIsEnoughCut(0);
        order.setOrderStatus(0);
        order.setPayStatus(0);
        order.setDisStatus(0);
        order.setEvaStatus(0);
        order.setCreateTime(new Date());
        order = appOrderService.createOrder(order);
        return order;
    }

    private String leftPad(String source){
        if(source.length() <= 6) {
            String res = String.format("%6s", source);
            res = res.replaceAll("\\s", "0");
            return res;
        }else{
            return source;
        }
    }
    /**
     * 获取本人邀请的用户列表
     */
    public void inviteMemberList(){
        try {
            String member = getPara("TOKEN");
            renderJson(ResultJson.success(memberService.getInviteMemberList(member)));
        } catch (Exception e) {
            logger.error("获取本人邀请的用户列表失败",e);
            throw new EshopAppGetParaException("获取本人邀请的用户列表失败");
        }
    }

    /**
     * 获取新任务
     */
    @Before(POST.class)
    public void getTaskList(){
        try {
            renderJson(ResultJson.success(appOrderService.getTask()));
        } catch (Exception e) {
            logger.error("获取新任务失败",e);
            throw new EshopAppGetParaException("获取新任务失败");
        }
    }

    /**
     * 猜我喜欢
     */
    public void guessLike(){
        try {
            renderJson(ResultJson.success(appProductService.guessike(getPara("TOKEN"))));
        } catch (Exception e) {
            logger.error("获取猜我喜欢商品失败",e);
            throw new EshopAppGetParaException("获取猜我喜欢商品失败");
        }
    }
}
