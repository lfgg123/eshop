package com.eshop.controller;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eshop.interceptor.AppLoginInterceptor;
import com.eshop.model.Clazz;
import com.eshop.model.EnoughCut;
import com.eshop.model.GoodEva;
import com.eshop.model.Goods;
import com.eshop.model.Run;
import com.eshop.model.TimeBuy;
import com.eshop.service.AppClazzService;
import com.eshop.service.AppEnoughCutService;
import com.eshop.service.AppGoodEvaService;
import com.eshop.service.AppGoodsService;
import com.eshop.service.AppProductService;
import com.eshop.service.AppRunService;
import com.eshop.util.IoFileUtil;
import com.eshop.util.JacksonUtils;
import com.eshop.vo.ResultJson;
import com.google.zxing.Result;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

public class AppGoodsController extends Controller {

	private AppGoodsService goodsService = new AppGoodsService();
	private AppClazzService clazzService = new AppClazzService();
	private AppEnoughCutService enoughCutService = new AppEnoughCutService();
	private AppRunService appRunService = new AppRunService();
	private AppGoodEvaService goodEvaService = new AppGoodEvaService();
	private AppProductService productService = new AppProductService();
	

	/**
	 * 获取限时商品列表
	 * 
	 * @throws Exception
	 */
	@Clear(AppLoginInterceptor.class)
	public void getTimeBuyGoodsList() throws Exception {
		List<TimeBuy> goodList = goodsService.getTimeBuyGoodsList();
		if (null == goodList) {
			goodList = new ArrayList<>();
		}
		renderText(JacksonUtils.obj2json(ResultJson.success(goodList)));
	}

	/**
	 * 根据一级分类获取限时购买商品列表
	 * 
	 * @throws Exception
	 **/
	@Clear(AppLoginInterceptor.class)
	public void getTimeBuyGoodsListByFirstClass() throws Exception {
		String guid = getPara("guid");
		List<TimeBuy> timeBuyList = goodsService.getTimeBuyGoodsListByFirstClass(guid);
		if (null == timeBuyList) {
			timeBuyList = new ArrayList<>();
		}
		renderText(JacksonUtils.obj2json(ResultJson.success(timeBuyList)));
	}

	/**
	 * 根据商品一级分类获取919畅销货品列表	
	 * 
	 * @throws Exception
	 * 
	 */
	@Clear(AppLoginInterceptor.class)
	public void get919HotGoodsListByFirstClass() throws Exception {
		String guid = getPara("guid");
		List<Goods> hotGoodsList = goodsService.get919HotGoodsListByFirstClass(guid);
		if (null == hotGoodsList) {
			hotGoodsList = new ArrayList<>();
		}
		renderText(JacksonUtils.obj2json(ResultJson.success(hotGoodsList)));

	}
	

	/**
	 * 搜索商品
	 */
	@Clear(AppLoginInterceptor.class)
	public void findProductListByName() {
		Integer main = getParaToInt("main",0);
		Integer salesNum = getParaToInt("salesnum",0);
		Integer price= getParaToInt("price",0);
		List<Record> productList = goodsService.getProductListByName(getPara("productName"),main,salesNum,price);
		if (null == productList) {
			productList = new ArrayList<>();
		}
		renderJson(ResultJson.success(productList));
	}

	/**
	 * 	商品一级分类
	 * 
	 */
	@Clear(AppLoginInterceptor.class)
	public void getFirstGradeGoodsClass() {
		List<Clazz> goodClassList = clazzService.getGoodClazzByClassGrade(1);
		if (null == goodClassList) {
			goodClassList = new ArrayList<>();
		}
		renderText(JacksonUtils.obj2json(ResultJson.success(goodClassList)));
	}

	/**
	 * 根据父分类获取商品子分类和商品列表
	 * 
	 */
	@Clear(AppLoginInterceptor.class)
	public void getProductClassByParent() {
		String parent = getPara("parent").trim();
		List<Clazz> goodClassList = clazzService.getGoodClazzByParent(parent);
		if (null == goodClassList) {
			goodClassList = new ArrayList<>();
		} 
		renderText(JacksonUtils.obj2json(ResultJson.success(goodClassList)));
	}

	/**
	 * 获取919 产品列表
	 */
	@Clear(AppLoginInterceptor.class)
	public void get919ProductList() {
		List<Goods> products = goodsService.get919GoodsList();
		if (null == products) {
			products = new ArrayList<>();
		}
		renderText(JacksonUtils.obj2json(ResultJson.success(products)));
	}

	/**
	 * 获取919精选产品列表
	 * 
	 * @throws Exception
	 */
	@Clear(AppLoginInterceptor.class)
	public void get919ChoiceGoodsList() throws Exception {
		List<Map> goods = goodsService.getChoiceGoodsList();
		if (null == goods) {
			goods = new ArrayList<>();
		}

		renderText(JacksonUtils.obj2json(ResultJson.success(goods)));
	}

	/**
	 * 获取轮播图列表
	 */
	@Clear(AppLoginInterceptor.class)
	public void getSlidShowList() {
		List<Record> result = enoughCutService.getEnoughCutList();
		if (null == result) {
			result = new ArrayList<>();
		}
		renderJson(ResultJson.success(result));
	}

	/**
	 * 根据guid获取货品
	 */
	public void getGoodsByGuid() {
		String member = getPara("TOKEN");
		String guid = getPara("guid");
		Goods goods = goodsService.getGoodsByGuid(guid,member);
		if (null == goods) {
			renderText(JacksonUtils.obj2json(ResultJson.fail(MessageFormat.format("guid:{0}的货品信息不存在", guid))));
		} else {
			renderText(JacksonUtils.obj2json(ResultJson.success(goods)));
		}
	}

	/**
	 * 根据分类Id获取货品列表
	 * @throws Exception 
	 */
	@Clear(AppLoginInterceptor.class)
	public void getGoodsByProClass() throws Exception {
		String proClass = getPara("proClass");
		List<Record> productList = productService.getProductsByProClass(proClass);
		if (null == productList) {
			productList = new ArrayList<>();
		}
		renderJson(ResultJson.success(productList));
	}

	/**
	 * 获取跑腿商品列表
	 */
	@Clear(AppLoginInterceptor.class)
	public void getRunProductList() {
		List<Goods> runProductList = appRunService.getRunList(1);
		if (null == runProductList) {
			runProductList = new ArrayList<>();
		}
		renderText(JacksonUtils.obj2json(ResultJson.success(runProductList)));
	}

	/**
	 * 获取919畅销产品列表
	 */
	@Clear(AppLoginInterceptor.class)
	public void get919HotGoodsList() {
		List<Goods> goodsList = goodsService.get919HotGoods();
		if (null == goodsList) {
			goodsList = new ArrayList<>();
		}
		renderText(JacksonUtils.obj2json(ResultJson.success(goodsList)));
	}

	/**
	 * 获取用户评价列表
	 * 
	 * @throws Exception
	 * 
	 */
	public void getMemberGoodsComment() throws Exception {
		String member = getPara("TOKEN");
		List<GoodEva> goodEvas = goodEvaService.getMemberGoodsEvaList(member);
		if (null == goodEvas) {
			goodEvas = new ArrayList<>();
		}
		renderText(JacksonUtils.obj2json(ResultJson.success(goodEvas)));
	}

	/**
	 * 获取用户评价详情
	 * 
	 */
	public void getMemberCommentDetail() {
		Long id = getParaToLong("commentId");
		GoodEva goodEva = goodEvaService.getGoodsEvaById(id);
		renderText(JacksonUtils.obj2json(ResultJson.success(goodEva)));
	}
	
	/**
	 * 删除评价
	 * 
	 * */
	public void deleteMemberComment() {
		Long id = getParaToLong("commentId");
		boolean result = goodEvaService.deleteGoodsEvaById(id);
		if(result) {
			renderText(JacksonUtils.obj2json(ResultJson.success("删除成功")));
		}else {
			renderText(JacksonUtils.obj2json(ResultJson.fail("操作失败，请稍后再试")));
			
		}
		
		
	}

	/**
	 * 追加评价
	 * 
	 * @throws Exception
	 */
	public void addSecondComment() throws Exception {
		UploadFile img1 = getFile("img1");
		UploadFile img2 = getFile("img2");
		UploadFile img3 = getFile("img3");
		List<UploadFile> list=new ArrayList<UploadFile>();
		if(img1!=null){
			list.add(img1);
		}
		if(img2!=null){
			list.add(img2);	
		}
		if(img3!=null){
			list.add(img3);
		}
		IoFileUtil.saveImages(list);
		Long id = getParaToLong("commentId");
		String content = getPara("content");
		String fileName = "";
		if (img1 != null)
			fileName += img1.getFileName();
		if (img2 != null) {
			fileName += "," + img2.getFileName();
		}
		if (img3 != null) {
			fileName += "," + img3.getFileName();
		}

		if (id == null) {
			throw new Exception("评论id 不能为空");
		}

		if (StrKit.isBlank(content)) {
			throw new Exception("追平内容不能为空");
		}
		boolean result = goodEvaService.addSecondComment(id, content, fileName);
		if (result) {
			renderText(JacksonUtils.obj2json(ResultJson.success("追加评论成功!")));
		} else {
			renderText(JacksonUtils.obj2json(ResultJson.fail("操作失败，请稍后再试!")));
		}

	}

	/**
	 * 提交用户评价
	 * 
	 * @throws Exception
	 * 
	 */
	@Before(POST.class)
	public void addMemberComment() throws Exception {
		UploadFile img1 = getFile("img1");
		UploadFile img2 = getFile("img2");
		UploadFile img3 = getFile("img3");
		String member = getPara("TOKEN");
		List<UploadFile> list=new ArrayList<UploadFile>();
		if(img1!=null){
			list.add(img1);
		}
		if(img2!=null){
			list.add(img2);	
		}
		if(img3!=null){
			list.add(img3);
		}
//		IoFileUtil.saveImages(list);
		String fContent = getPara("content");
		String goodGuid = getPara("goodGuid");
		String sn = getPara("sn");
		String fileName = "";
		if (img1 != null)
			fileName += img1.getFileName();
		if (img2 != null) {
			fileName += "," + img2.getFileName();
		}
		if (img3 != null) {
			fileName += "," + img3.getFileName();
		}
		if (StrKit.isBlank(fContent)) {
			throw new Exception("评论内容不能为空");
		}

		if (StrKit.isBlank(goodGuid)) {
			throw new Exception("产品guid不能为空");
		}
		if (StrKit.isBlank(sn)) {
			throw new Exception("订单编号不能为空");
		}
		boolean result = goodEvaService.addMemberComment(goodGuid,member, fContent, fileName, sn);
		if (result) {
			renderText(JacksonUtils.obj2json(ResultJson.success("添加评论成功!")));
		} else {
			renderText(JacksonUtils.obj2json(ResultJson.fail("操作失败，请稍后再试!")));
		}

	}

	/**
	 * 获取品牌筛选条件列表
	 * 
	 */
	@Clear(AppLoginInterceptor.class)
	public void getBrandFilterList() {
		Record attrRecord = Db.findFirst("select * from shop_attr where name like '%品牌%'");
		if (attrRecord == null || StrKit.isBlank(attrRecord.getStr("guid"))) {
			renderText(JacksonUtils.obj2json(ResultJson.fail("系统数据初始化错误，请稍后再试!")));
		}
		List<Map<String, Object>> reMaps = new ArrayList<>();
		List<Record> attrValueRecordList = Db
				.find("select * from shop_attr_value where attr_guid like '%" + attrRecord.getStr("guid") + "%'");
		if (attrValueRecordList != null && !attrValueRecordList.isEmpty()) {
			for (Record record : attrValueRecordList) {
				Map<String, Object> localMap = new HashMap<>();
				localMap.put("id", record.getInt("id"));
				localMap.put("value", record.getStr("value"));
				reMaps.add(localMap);
			}
		}
		renderText(JacksonUtils.obj2json(ResultJson.success(reMaps)));
	}

	/**
	 * 获取国家筛选条件列表
	 */
	@Clear(AppLoginInterceptor.class)
	public void getCountryFilterList() {
		Record attrRecord = Db.findFirst("select * from shop_attr where name like '%国家%'");
		if (attrRecord == null || StrKit.isBlank(attrRecord.getStr("guid"))) {
			renderText(JacksonUtils.obj2json(ResultJson.fail("系统数据初始化错误，请稍后再试!")));
		}
		List<Map<String, Object>> reMaps = new ArrayList<>();
		List<Record> attrValueRecordList = Db
				.find("select * from shop_attr_value where attr_guid like '%" + attrRecord.getStr("guid") + "%'");
		if (attrValueRecordList != null && !attrValueRecordList.isEmpty()) {
			for (Record record : attrValueRecordList) {
				Map<String, Object> localMap = new HashMap<>();
				localMap.put("id", record.getInt("id"));
				localMap.put("value", record.getStr("value"));
				reMaps.add(localMap);
			}
		}
		renderText(JacksonUtils.obj2json(ResultJson.success(reMaps)));
	}

	/**
	 * 根据分类筛选商品
	 * 
	 * @throws Exception
	 * 
	 */
	@Clear(AppLoginInterceptor.class)
	public void getFilteredGoodsList() throws Exception {
		String proClass = getPara("proClass");
		String commodity = getPara("commodity");
		Integer brand = getParaToInt("brand");
		Integer country = getParaToInt("country");
		Integer main = getParaToInt("main",0);
		Integer salesNum = getParaToInt("salesnum",0);
		Integer price= getParaToInt("price",0);
		Double priceFrom = null, priceTo = null;
		if (!StrKit.isBlank(getPara("priceFrom"))) {
			priceFrom = Double.parseDouble(getPara("priceFrom"));
		}

		if (!StrKit.isBlank(getPara("priceTo"))) {
			priceTo = Double.parseDouble(getPara("priceTo"));
		}
		List<Record> goods = goodsService.filterGoodsByClass(proClass, commodity, brand, country, priceFrom, priceTo,main,salesNum,price);
		if (goods == null) {
			goods = new ArrayList<>();
		}
		renderJson(ResultJson.success(goods));

	}
	
}
