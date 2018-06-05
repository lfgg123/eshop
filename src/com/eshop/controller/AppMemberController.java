package com.eshop.controller;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cedarsoftware.util.DateUtilities;
import com.eshop.interceptor.AppLoginInterceptor;
import com.eshop.model.Content;
import com.eshop.model.Member;
import com.eshop.model.MemberAddress;
import com.eshop.model.MemberPublish;
import com.eshop.model.MemberReturn;
import com.eshop.model.MemberWine;
import com.eshop.service.AppContentService;
import com.eshop.service.AppMemberAddressService;
import com.eshop.service.AppMemberCollectionService;
import com.eshop.service.AppMemberHistoryService;
import com.eshop.service.AppMemberPublishService;
import com.eshop.service.AppMemberReturnService;
import com.eshop.service.AppMemberService;
import com.eshop.service.AppMemberWineService;
import com.eshop.util.IoFileUtil;
import com.eshop.util.JacksonUtils;
import com.eshop.util.PictureCompress;
import com.eshop.vo.ResultJson;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

public class AppMemberController extends Controller {
	private static AppMemberAddressService addressService = new AppMemberAddressService();
	private static AppContentService contentService = new AppContentService();
	private static AppMemberPublishService memberPublishService = new AppMemberPublishService();
	private static AppMemberWineService wineService = new AppMemberWineService();
	private static AppMemberCollectionService collectionService = AppMemberCollectionService.service;
	private static AppMemberHistoryService historyService = AppMemberHistoryService.service;
	private static AppMemberService memberService = AppMemberService.service;
	private static AppMemberReturnService returnService = AppMemberReturnService.service;

	/**
	 * 获取当前用户地址列表
	 */
	public void getAddressList() {
		String member = getPara("TOKEN");
		List<MemberAddress> addressList = addressService.getMemberAddressList(member);
		if (null == addressList) {
			addressList = new ArrayList<>();
		}
		renderText(JacksonUtils.obj2json(ResultJson.success(addressList)));
	}

	/**
	 * 添加当前用户地址
	 */
	public void addAddress() {
		MemberAddress address = getBean(MemberAddress.class, "");
		String member = getPara("TOKEN");
		address.setMember(member);
		Integer result = addressService.addMemberAddress(address);
		if (null != result) {
			Map map = new HashMap<>();
			map.put("id", result);
			renderText(JacksonUtils.obj2json(ResultJson.success(map)));
		} else {
			renderText(JacksonUtils.obj2json(ResultJson.fail("操作失败")));
		}
	}

	/**
	 * 修改用户地址
	 * 
	 * @throws Exception
	 * 
	 */
	public void modifyAddress() throws Exception {
		MemberAddress address = getBean(MemberAddress.class, "");
		Integer result = addressService.modifyMemberAddress(address);
		if (null != result) {
			Map map = new HashMap<>();
			map.put("id", result);
			renderText(JacksonUtils.obj2json(ResultJson.success(map)));
		} else {
			renderText(JacksonUtils.obj2json(ResultJson.fail("")));
		}
	}

	/**
	 * 删除用户地址
	 * 
	 * @throws Exception
	 */
	public void deleteAddress() throws Exception {
		Integer id = getParaToInt("id");
		boolean result = addressService.deleteAddress(id);
		if (result) {
			renderText(JacksonUtils.obj2json(ResultJson.success("删除成功")));
		} else {
			renderText(JacksonUtils.obj2json(ResultJson.fail("操作失败")));
		}

	}

	/**
	 * 获取广播消息列表
	 * 
	 */
	public void getBroadCastList() {
		List<Content> contents = contentService.getBroadCastContent();
		if (null == contents) {
			contents = new ArrayList<>();
		}
		renderText(JacksonUtils.obj2json(ResultJson.success(contents)));
	}

	/**
	 * 发表帖子
	 */
	public void publishPosts() {

		UploadFile img1 = getFile("img1");
		UploadFile img2 = getFile("img2");
		UploadFile img3 = getFile("img3");

		List<UploadFile> list = new ArrayList<UploadFile>();
		if (img1 != null) {
			list.add(img1);
		}
		if (img2 != null) {
			list.add(img2);
		}
		if (img3 != null) {
			list.add(img3);
		}
		// IoFileUtil.saveImages(list);
		String targetImgName = IoFileUtil.savePostImages(list);
		String member = getPara("TOKEN");
		String title = getPara("title");
		String content = getPara("content");

		String imgUrl = "";
		if (null != img1) {
			imgUrl += img1.getFileName() + ";";
		}

		if (null != img2) {
			imgUrl += img2.getFileName() + ";";
		}
		if (null != img3) {
			imgUrl += img3.getFileName() + ";";
		}

		Record rd = new Record();
		rd.set("title", title);
		rd.set("content", content);
		rd.set("imgs", targetImgName);
		rd.set("member", member);
		rd.set("create_time", Calendar.getInstance().getTime());
		boolean result = memberPublishService.savePublish(rd);
		if (result) {
			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("id", rd.getInt("id"));
			renderText(JacksonUtils.obj2json(ResultJson.success(resultMap)));
		} else {
			renderText(JacksonUtils.obj2json(ResultJson.fail("保存失败!")));
		}
	}

	/**
	 * 获取帖子列表
	 */
	@Clear(AppLoginInterceptor.class)
	public void getPostList() {
		List<Record> memberPublishList = memberPublishService.getPostList();
		if (null == memberPublishList) {
			memberPublishList = new ArrayList<>();
		}
		renderJson(ResultJson.success(memberPublishList));
	}

	/**
	 * 根据id 获取帖子
	 */
	@Clear(AppLoginInterceptor.class)
	public void getPostById() {
		Integer postId = getParaToInt("postId");
		MemberPublish memberPublish = memberPublishService.getPostById(postId);
		if (null == memberPublish) {
			renderText(JacksonUtils.obj2json(ResultJson.fail(MessageFormat.format("id:{0} 的帖子不存在", postId))));
		} else {
			renderText(JacksonUtils.obj2json(ResultJson.success(memberPublish)));
		}

	}

	/***
	 * 点赞帖子
	 * 
	 * @throws Exception
	 */
	public void upvotePost() throws Exception {
		Integer postId = getParaToInt("postId");
		boolean result = memberPublishService.upvotePost(postId);
		if (result) {
			renderText(JacksonUtils.obj2json(ResultJson.success("点赞成功")));
		} else {
			renderText(JacksonUtils.obj2json(ResultJson.fail("操作失败")));
		}
	}

	/**
	 * 取消点赞帖子
	 * 
	 * @throws Exception
	 */
	public void downvotePost() throws Exception {
		Integer postId = getParaToInt("postId");
		boolean result = memberPublishService.downvotePost(postId);
		if (result) {
			renderText(JacksonUtils.obj2json(ResultJson.success("取消点赞成功")));
		} else {
			renderText(JacksonUtils.obj2json(ResultJson.fail("操作失败")));
		}
	}

	/**
	 * 收藏帖子
	 * 
	 * @throws Exception
	 */
	public void collectPost() throws Exception {
		Integer postId = getParaToInt("postId");
		String member = getPara("TOKEN");
		boolean result = memberPublishService.collectPost(postId, member);
		if (result) {
			renderText(JacksonUtils.obj2json(ResultJson.success("收藏成功")));
		} else {
			renderText(JacksonUtils.obj2json(ResultJson.fail("操作失败")));
		}
	}

	/**
	 * 评论帖子
	 * 
	 * @throws Exception
	 */
	public void commentPost() throws Exception {
		Integer postId = getParaToInt("postId");
		String content = getPara("content");
		String member = getPara("TOKEN");
		boolean result = memberPublishService.commentPost(postId, member, content);
		if (result) {
			renderText(JacksonUtils.obj2json(ResultJson.success("评论成功")));
		} else {
			renderText(JacksonUtils.obj2json(ResultJson.fail("操作失败")));
		}
	}

	/**
	 * 删除帖子
	 * 
	 * @throws Exception
	 */
	public void deleteMemberPost() throws Exception {
		Integer id = getParaToInt("id");
		boolean result = memberPublishService.deleteMemberPublistById(id);
		if (result) {
			renderText(JacksonUtils.obj2json(ResultJson.success("删除成功")));
		} else {
			renderText(JacksonUtils.obj2json(ResultJson.fail("操作失败")));
		}

	}

	/**
	 * 删除回复
	 * 
	 * @throws Exception
	 */
	public void deleteMemberReturn() throws Exception {
		Integer id = getParaToInt("id");
		boolean result = returnService.deleteMemberReturnById(id);
		if (result) {
			renderText(JacksonUtils.obj2json(ResultJson.success("删除成功")));
		} else {
			renderText(JacksonUtils.obj2json(ResultJson.fail("操作失败")));
		}
	}

	/***
	 * 获取用户酒币收支明细
	 * 
	 * @throws Exception
	 */
	public void getMemberWineIncomeAndExpenditureDetail() throws Exception {
		List<MemberWine> wineList = wineService.getMemberWineCoinInfoList(getPara("TOKEN"));
		if (null == wineList) {
			wineList = new ArrayList<>();
		}
		renderText(JacksonUtils.obj2json(ResultJson.success(wineList)));
	}

	/**
	 * 获取用户收藏列表
	 * 
	 */
	public void getMemberCollectionListByType() {
		Integer type = getParaToInt("type");
		List result = collectionService.getMemberCollectionListByType(getPara("TOKEN"), type);
		renderText(JacksonUtils.obj2json(ResultJson.success(result)));
	}

	/**
	 * 取消用户收藏
	 * 
	 * @throws Exception
	 * 
	 */
	public void cancelMemberCollection() throws Exception {
		String collGuid = getPara("collGuid");
		boolean result = collectionService.cancelCollection(getPara("TOKEN"), collGuid);
		if (result) {
			renderText(JacksonUtils.obj2json(ResultJson.success("取消收藏成功")));
		} else {
			renderText(JacksonUtils.obj2json(ResultJson.fail("操作失败")));
		}
	}

	/**
	 * 获取用户浏览历史
	 * 
	 */
	public void getMemberBrowsingHistory() {
		List result = historyService.getMemberBrowsingHistory(getPara("TOKEN"));
		if (null == result) {
			result = new ArrayList<>();
		}
		renderText(JacksonUtils.obj2json(ResultJson.success(result)));
	}

	/**
	 * 清除浏览数据
	 */
	public void clearBrowsingHistory() {
		boolean result = historyService.clearMemberHistory(getPara("TOKEN"));
		if (result) {
			renderJson(JacksonUtils.obj2json(ResultJson.success("删除成功")));
		} else {
			renderJson(JacksonUtils.obj2json(ResultJson.fail("操作失败，请稍后再试")));
		}
	}

	/**
	 * 获取用户帖子列表
	 */
	public void getMemberBbsList() {
		List<MemberPublish> memberPublishs = memberPublishService.getMemberPublishList(getPara("TOKEN"));
		if (null == memberPublishs) {
			memberPublishs = new ArrayList<>();
		}
		renderJson(JacksonUtils.obj2json(ResultJson.success(memberPublishs)));
	}

	/**
	 * 获取有用户回复列表
	 */
	public void getMemberReturnList() {
		List<MemberReturn> memberReturns = returnService.getMemberReturnList(getPara("TOKEN"));
		if (null == memberReturns) {
			memberReturns = new ArrayList<>();
		}
		renderJson(JacksonUtils.obj2json(ResultJson.success(memberReturns)));
	}

	/***
	 * 获取对用户的回复列表
	 * 
	 */
	public void getReturnForMemberList() {
		List<MemberReturn> memberReturns = returnService.getReturnForMemberList(getPara("TOKEN"));
		if (null == memberReturns) {
			memberReturns = new ArrayList<>();
		}
		renderJson(JacksonUtils.obj2json(ResultJson.success(memberReturns)));
	}

	/**
	 * 获取个人信息
	 * 
	 * @throws Exception
	 */
	public void getMemberInfo() throws Exception {
		Record record = memberService.getMemberInfoByGuid(getPara("TOKEN"));
		renderJson(JacksonUtils.obj2json(ResultJson.success(record.getColumns())));
	}

	/**
	 * 根据guid获取个人信息
	 * 
	 * @throws Exception
	 * 
	 */
	public void getMemberInfoByGuid() throws Exception {
		String guid = getPara("guid");
		Record record = memberService.getMemberInfoByGuid(guid);
		renderJson(JacksonUtils.obj2json(ResultJson.success(record.getColumns())));
	}

	/**
	 * 修改头像
	 * 
	 * @throws Exception
	 */
	public void changeHeadPortrait() throws Exception {
		// UploadFile img = getFile("img");
		UploadFile img = getFile("img", PathKit.getWebRootPath()); // 最大上传20M的图片
		String contextPath = PathKit.getWebRootPath() + "\\nmw\\";
		String basePath = PathKit.getWebRootPath().replace("WebRoot", "");
		// 重命名
		String nex = img.getFileName().split("\\.")[1];
		String newName = Calendar.getInstance().getTimeInMillis() + "." + nex;

		String newFileName = img.getUploadPath() + "\\thumb\\" + "\\" + Calendar.getInstance().getTimeInMillis() + "."
				+ nex;
		File newFile = new File(newFileName);
		// 或者直接写入对应的项目盘符地址
		// File newFile = new
		// File("地址"+Calendar.getInstance().getTimeInMillis()+"."+nex);
		// img.getFile().renameTo(newFile);

		// 压缩图片
		PictureCompress.createThumbnail(img.getUploadPath() + "/" + img.getFileName(), newFileName, 400, 300);

		// IoFileUtil.cutFile(basePath+"\\"+newFile.getName(),
		// contextPath+newFile.getName());
		// IoFileUtil.cutFile(img.getUploadPath()+"\\"+newFile.getName(),
		// contextPath+newFile.getName());
		// List<UploadFile> list = new ArrayList<UploadFile>();
		// list.add(img);
		// IoFileUtil.saveImages(list);
		boolean result = memberService.changeHeadPortrait(getPara("TOKEN"), newFile.getName());

		if (result) {
			renderJson(ResultJson.success("修改成功"));
		} else {
			renderJson(ResultJson.fail("操作失败，请稍后再试!"));
		}
	}

	/**
	 * 修改昵称
	 * 
	 * @throws Exception
	 * 
	 */
	public void modifyNickName() throws Exception {
		String nickName = getPara("nickname");
		boolean result = memberService.modifyNick(getPara("TOKEN"), nickName);

		if (result) {
			renderJson(ResultJson.success("修改成功"));
		} else {
			renderJson(ResultJson.fail("操作失败，请稍后再试!"));
		}
	}

	/**
	 * 修改性别
	 * 
	 * @throws Exception
	 * 
	 */
	public void modifyGender() throws Exception {
		Integer gender = getParaToInt("gender");
		boolean result = memberService.modifyGender(getPara("TOKEN"), gender);
		if (result) {
			renderJson(ResultJson.success("修改成功"));
		} else {
			renderJson(ResultJson.fail("操作失败，请稍后再试!"));
		}
	}

	/**
	 * 修改出生日期
	 * 
	 * @throws Exception
	 * 
	 */
	public void modifyBirth() throws Exception {
		Date birth = getParaToDate("birth");
		boolean result = memberService.modifyBirthDate(getPara("TOKEN"), birth);
		if (result) {
			renderJson(ResultJson.success("修改成功"));
		} else {
			renderJson(ResultJson.fail("操作失败，请稍后再试!"));
		}
	}

	/**
	 * 添加收藏
	 * 
	 * @throws Exception
	 * 
	 */
	public void addCollection() throws Exception {
		Integer type = getParaToInt("type");
		String collGuid = getPara("collGuid");
		boolean result = collectionService.addCollection(type, collGuid, getPara("TOKEN"));
		if (result) {
			renderText(JacksonUtils.obj2json(ResultJson.success("收藏成功")));
		} else {
			renderText(JacksonUtils.obj2json(ResultJson.fail("操作失败，请稍后再试")));

		}
	}

}
