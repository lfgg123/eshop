package com.eshop.util.wxConfig;

/**
 * Created by CTL on 2017/11/30.
 */
public class ConstantUtil {
    /**
     * 微信开发平台应用ID
     */
    public static final String APP_ID="";
    /**
     * 应用对应的凭证
     */
    public static final String APP_SECRET="";
    /**
     * 应用对应的密钥
     */
    public static final String APP_KEY="";
    /**
     * 微信支付商户号
     */
    public static final String MCH_ID="";
    /**
     * 商品描述
     */
    public static final String BODY="919支付";
    /**
     * 商户号对应的密钥
     */
    public static final String PARTNER_key="";

    /**
     * 商户id
     */
    public static final String PARTNER_ID="";
    /**
     * 常量固定值
     */
    public static final String GRANT_TYPE="client_credential";
    /**
     * 获取预支付id的接口url
     */
    public static String GATEURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    /**
     * 微信服务器回调通知url
     */
    public static String NOTIFY_URL="/app/notify";
}
