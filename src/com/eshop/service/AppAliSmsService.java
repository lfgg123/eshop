package com.eshop.service;

import java.util.HashMap;
import java.util.Map;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.beust.jcommander.Strings;
import com.eshop.util.ConfigUtils;
import com.eshop.util.JacksonUtils;

public class AppAliSmsService {

	/**
	 * 发送注册验证码
	 * 
	 * @throws Exception
	 * 
	 */
	public static void sendRegisterMessageCode(String mobile, String code) throws Exception {
		String smsCode = ConfigUtils.getProperty("sms.code.register");
		Map<String, String> map = new HashMap<String, String>();
		map.put("code", code);
		sendMessage(mobile, smsCode, JacksonUtils.obj2json(map));
	}

	/**
	 * 发送登录验证码
	 * 
	 * @throws Exception
	 */
	public static void sendLoginMessageCode(String mobile, String code) throws Exception {
		String smsCode = ConfigUtils.getProperty("sms.code.login");
		Map<String, String> map = new HashMap<>();
		map.put("code", code);
		sendMessage(mobile, smsCode, JacksonUtils.obj2json(map));
	}

	/**
	 * 发送配送短信
	 * 
	 * @throws Exception
	 */
	public static void sendDeliveryCode(String phoneNumber, String courier, String courierPhoneNumber)
			throws Exception {
		String smsCode = ConfigUtils.getProperty("sms.code.delivery");
		Map<String, String> map = new HashMap<>();
		map.put("phoneNumber", phoneNumber);
		map.put("courier", courier);
		map.put("courierPhoneNumber", courierPhoneNumber);
		sendMessage(phoneNumber, smsCode, JacksonUtils.obj2json(map));
	}

	/**
	 * 发送订单完成短信
	 * 
	 * @throws Exception
	 */
	public static void sendOrderFinishedMessage(String mobile, String servicePhone) throws Exception {
		String smsCode = ConfigUtils.getProperty("sms.code.finish");
		Map<String, String> map = new HashMap<>();
		map.put("servicePhone", servicePhone);
		sendMessage(mobile, smsCode, JacksonUtils.obj2json(map));
	}

	/**
	 * 发送短信
	 * 
	 * @throws Exception
	 */
	private static SendSmsResponse sendMessage(String mobile, String smsCode, String templateParam) throws Exception {
		// 设置超时时间-可自行调整
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");
		// 初始化ascClient需要的几个参数
		final String product = ConfigUtils.getProperty("aliyun.sms.product");// 短信API产品名称（短信产品名固定，无需修改）
		final String domain = ConfigUtils.getProperty("aliyun.sms.domain");// 短信API产品域名（接口地址固定，无需修改）
		// 替换成你的AK
		final String accessKeyId = ConfigUtils.getProperty("aliyun.sms.accessKeyId");// 你的accessKeyId,参考本文档步骤2
		final String accessKeySecret = ConfigUtils.getProperty("aliyun.sms.accessKeySecret");// 你的accessKeySecret，参考本文档步骤2
		// 初始化ascClient,暂时不支持多region（请勿修改）
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		IAcsClient acsClient = new DefaultAcsClient(profile);
		// 组装请求对象
		SendSmsRequest request = new SendSmsRequest();
		// 使用post提交
		request.setMethod(MethodType.POST);
		// 必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
		request.setPhoneNumbers(mobile);
		// 必填:短信签名-可在短信控制台中找到
		request.setSignName(ConfigUtils.getProperty("aliyun.sms.signName"));
		// 必填:短信模板-可在短信控制台中找到
		request.setTemplateCode(smsCode);
		// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		// 友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
		request.setTemplateParam(templateParam);
		// 可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
		// request.setSmsUpExtendCode("90997");
		// 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
		// request.setOutId("yourOutId");
		// 请求失败这里会抛ClientException异常
		SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
		if (sendSmsResponse.getCode() == null || !sendSmsResponse.getCode().equals("OK")) {
			String messageString = getExceptionMessageByCode(sendSmsResponse);
			throw new Exception(messageString);	
		}
		return sendSmsResponse;
	}

	private static String getExceptionMessageByCode(SendSmsResponse sendSmsResponse) {
		String messageString = "发送失败，请稍后再试";
		switch (sendSmsResponse.getCode()) {
		case "isv.OUT_OF_SERVICE":
			// 业务停机
		case "isp.SYSTEM_ERROR":
			// 系统错误
			messageString = "短信系统异常，请稍后再试";
			break;
		case "isv.BLACK_KEY_CONTROL_LIMIT":
			// 黑名单管控
			messageString = "短信平台暂时无法为当前手机号提供服务";
			break;
		case "isv.MOBILE_NUMBER_ILLEGAL":
			// 非法手机号
			messageString = "当前手机号无效，请输入正确手机号";
			break;
		case "isv.BUSINESS_LIMIT_CONTROL":
			// 业务限流
			messageString = "操作过于频繁，请稍后再试";
			break;
		}
		return messageString;

	}

	public static void main(String args[]) throws Exception {
		// 发送注册验证码
		// sendRegisterMessageCode("18317118847","123456");
		// 发送登录验证码
		// sendLoginMessageCode("18317118847","123456");
		// 发送配送短信
		// sendDeliveryCode("18317118847","test","123456");

	}

}
