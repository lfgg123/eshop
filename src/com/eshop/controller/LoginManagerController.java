package com.eshop.controller;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;
import com.eshop.interceptor.AppLoginInterceptor;
import com.eshop.model.Member;
import com.eshop.service.AppAliSmsService;
import com.eshop.service.AppMemberService;
import com.eshop.util.Constants;
import com.eshop.util.JacksonUtils;
import com.eshop.util.RegularExpressionValidatorUtils;
import com.eshop.util.VerifyCodeUtils;
import com.eshop.vo.ResultJson;
import com.fengpei.ioc.AutoInstance;
import com.fengpei.ioc.Controller;
import com.jfinal.aop.Clear;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.sun.org.apache.bcel.internal.generic.NEW;

@AutoInstance
public class LoginManagerController extends Controller {
	private static AppMemberService memberService = AppMemberService.service;

	/**
	 * 会员注册
	 * 
	 * @param String
	 *            mobile
	 * @param String
	 *            verifyCode
	 * @param String
	 *            inviteCode
	 * @return boolean
	 * @throws Exception
	 */
	@Clear(AppLoginInterceptor.class)
	public void register() throws Exception {
		final String mobile = getPara("mobile");
		if (StrKit.isBlank(mobile)) {
			throw new Exception("请填写手机号");
		}
		if (!RegularExpressionValidatorUtils.isMobile(mobile)) {
			throw new Exception("请输入正确的手机号");
		}
		String verifyCode = getPara("verifyCode");
		if (StrKit.isBlank(verifyCode)) {
			throw new Exception("请填写验证码");
		}
		String inviteCode = getPara("inviteCode");
		// 判断手机号是否存在
		Member isExistMember = memberService.getMemberByMobile(mobile);
		if (null != isExistMember) {
			// 删除验证码
			Db.update("delete from shop_sms where phone=?", mobile);
			throw new Exception("当前手机号已经完成注册，请直接登录");
		}
		// 判断验证码是否正确
		Record record = Db.findFirst("select * from shop_sms where phone = ? order by id desc", mobile);
		if (record == null) {
			throw new Exception("请重新获取验证码");
		}
		if (!verifyCode.equals(record.get("code").toString().trim())) {
			throw new Exception("验证码错误，请重新输入");
		}
		final Record member = new Record();
		member.set("phone", mobile);
		member.set("nick", "a" + mobile);
		member.set("guid", UUID.randomUUID().toString());
		member.set("sex", 0);
		member.set("birth", new Date());
		member.set("wine", 0);
		member.set("grade", 0);
		member.set("evalue", 0);
		member.set("up_code", inviteCode);
		member.set("reg_time", new Date());
		member.set("isuse", 0);
		boolean dbResult = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				// 插入member表
				boolean res1 = Db.save("shop_member", member);
				// 删除验证码
				boolean res2 = Db.update("delete from shop_sms where phone=?", mobile) > 0 ? true : false;
				return res1 && res2;
			}
		});
		if (!dbResult) {
			throw new Exception("数据库异常，请稍后再试!");
		}
		Map<String, Object> res = new HashMap<>();
		res.put("token", member.getStr("guid"));
		renderText(JacksonUtils.obj2json(ResultJson.success(res)));
	}

	/**
	 * 会员登录
	 * 
	 * @param mobile
	 * @param verifyCode
	 * @return boolean
	 * @throws Exception
	 */
	@Clear(AppLoginInterceptor.class)
	public void login() throws Exception {
		final String mobile = getPara("mobile");
		if (StrKit.isBlank(mobile)) {
			throw new Exception("请填写手机号");
		}
		if (!RegularExpressionValidatorUtils.isMobile(mobile)) {
			throw new Exception("请输入正确的手机号");
		}
		String verifyCode = getPara("verifyCode");
		if (StrKit.isBlank(verifyCode)) {
			throw new Exception("请填写验证码");
		}
		// 判断是否为 配送员
		Record agent = Db.findFirst("select * from shop_agent where tel=?", mobile);
		if (agent != null) {
			// 判断验证码是否正确
			Record record = Db.findFirst("select * from shop_sms where phone = ? order by id desc", mobile);
			if (record == null) {
				throw new Exception("请重新获取验证码");
			}
			if (!verifyCode.equals(record.get("code").toString().trim())) {
				throw new Exception("验证码错误，请重新输入");
			}
			boolean res1 = Db.update("delete from shop_sms where phone=?", mobile) > 0 ? true : false;
			if (!res1) {
				throw new Exception("系统繁忙，请稍后再试");
			}
			Map<String, Object> res = new HashMap<>();
			res.put("token", agent.getStr("guid"));
			res.put("agent", "Y");
			renderText(JacksonUtils.obj2json(ResultJson.success(res)));

		} else {
			// 判断手机号是否存在
			final Member member;
			final Member memberFromDb = memberService.getMemberByMobile(mobile);
			if (memberFromDb == null) {
				String inviteCode = getPara("inviteCode");
				member = new Member();
				member.setPhone(mobile);
				member.setNick("a" + mobile);
				member.setGuid(UUID.randomUUID().toString());
				member.setSex(0);
				member.setBirth(new Date());
				member.setWine(0);
				member.setEvalue(0);
				member.setGrade(1);//默认为1
				member.setLoginTime(new Date());
				member.setModifyTime(new Date());
				member.setRegTime(new Date());
				member.setIsuse(0);
				member.setUpCode(inviteCode);
				member.setMyCode(mobile.substring(mobile.length()-6,mobile.length()));
			} else {
				member = memberFromDb;
			}
			// 判断账号是否有效
			if (member.getIsuse() == -1) {
				Db.update("delete from shop_sms where phone=?", mobile);
				throw new Exception("当前手机号绑定的账号已经被封停，请尝试其他账号");
			}

			// 判断验证码是否正确
			Record record = Db.findFirst("select * from shop_sms where phone = ? order by id desc", mobile);
			if (record == null) {
				throw new Exception("请重新获取验证码");
			}
			if (!verifyCode.equals(record.get("code").toString().trim())) {
				throw new Exception("验证码错误，请重新输入");
			}

			boolean dbResult = Db.tx(new IAtom() {
				@Override
				public boolean run() throws SQLException {

					// 删除验证码
					boolean res1 = Db.update("delete from shop_sms where phone=?", mobile) > 0 ? true : false;
					boolean res2;
					if (memberFromDb == null) {
						// 插入member表
						res2 = member.save();
					} else {
						res2 = member.setLoginTime(new Date()).update();
					}
					return res1 && res2;
				}
			});
			if (!dbResult) {
				throw new Exception("系统繁忙，请稍后再试");
			}
			Map<String, Object> res = new HashMap<>();
			res.put("token", member.getStr("guid"));
			res.put("agent", "N");
			renderText(JacksonUtils.obj2json(ResultJson.success(res)));
		}

	}

	/**
	 * 会员退出登录
	 * 
	 */
	public void logout() {
		HttpSession session = getSession(false);
		if (session != null) {
			session.invalidate();
		}
		renderText(JacksonUtils.obj2json(ResultJson.success("退出完成")));
	}

	/**
	 * 发送手机验证码
	 * 
	 * @param mobile
	 * @return
	 * @throws Exception
	 */
	@Clear(AppLoginInterceptor.class)
	public void sendVerifyCode() throws Exception {
		String mobile = getPara("mobile");
		boolean isLogin = getParaToBoolean("isLogin");
		// 正则判断手机号是否正确
		if (!RegularExpressionValidatorUtils.isMobile(mobile)) {
			throw new Exception("请输入有效的手机号！");
		}
		// 生成验证码
		String verifyCode = VerifyCodeUtils.generateVerifyCode(6, "1234567890");
		// // 请求参数
		// String url = ConfigUtils.getProperty("sms.url");
		// String account = ConfigUtils.getProperty("sms.account");
		// String password = ConfigUtils.getProperty("sms.password");
		// StringBuffer mobileBuffer = new StringBuffer(mobile);
		// StringBuffer contextString = new StringBuffer(verifyCode);
		// StringBuffer extno = new StringBuffer();
		// // 发送手机短信
		// String response = AppFengchuangSmsService.doPost(account.trim(),
		// password.trim(), mobileBuffer, contextString, "919APP", "", extno);
		// if (StrKit.isBlank(response)) {
		// throw new Exception("短信接口异常，请稍后再试!");
		// }
		// // 0:状态,1:发送编号,2:无效号码数,3:成功提交数,4:黑名单数 5:消息
		// List<String> resultList = Arrays.asList(response.split(","));
		// if (StrKit.isBlank(resultList.get(0))) {
		// throw new Exception("短信接口异常，请稍后再试!");
		// }
		//
		// if (!"0".equals(resultList.get(0))) {
		// throw new Exception(resultList.get(5));
		// }
		if (isLogin)
			AppAliSmsService.sendLoginMessageCode(mobile, verifyCode);
		else
			AppAliSmsService.sendRegisterMessageCode(mobile, verifyCode);
		// 放入表中
		Record record = new Record();
		record.set("phone", mobile);
		record.set("code", verifyCode);
		Db.save("shop_sms", record);
		renderText(JacksonUtils.obj2json(ResultJson.success("验证码发送成功，请注意查收!")));
	}

	/**
	 * 删除验证码
	 * 
	 * @param String
	 *            mobile
	 * @return
	 * @throws Exception
	 */
	@Clear
	public void delVerifyCode() throws Exception {
		String mobile = getPara("mobile");
		if (StrKit.isBlank(mobile)) {
			throw new Exception("手机号不能为空");
		}

		List<Record> records = Db.find("select * from shop_sms where phone=?", mobile);
		if (records != null && !records.isEmpty()) {
			int result = Db.update("delete from shop_sms where phone=?", mobile);
			if (result > 0) {
				renderText(JacksonUtils.obj2json(ResultJson.success("删除成功")));
			} else {
				renderText(JacksonUtils.obj2json(ResultJson.success("操作失败")));

			}
		}
		renderText(JacksonUtils.obj2json(ResultJson.success("删除成功")));
	}

	public static String getVerifyCodeKey(String mobile) {
		return Constants.APP + "_" + mobile;
	}
	

	private final static Integer VERIFY_CODE_VALId_TIME = 60 * 1000;

}
