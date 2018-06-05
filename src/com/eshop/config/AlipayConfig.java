package com.eshop.config;

/**
 * 支付宝支付参数
 * Created by CTL on 2017/11/28.
 */
public class AlipayConfig {
    // 商户appid
    public static String APPID = "2017120500385528";
    // 私钥 pkcs8格式的
    public static String RSA_PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDlZ3yVEF22agGTJkR3o2U0+N6ZIH5y4peTbGvAkepUFI8/FjSo4bFO36b0PzeZAzvcErIEdonBR981ST3/16kspOJ9L1xpb74Vde8wGG2Vzh9z3YKrO3yZewFov7X2awLpzUVLIAwrbK6NRR4SqP6/lHDpBW2UGeuKHtdE9ga6uiddydXruh0ANBE1ENN34SW63rreCbIdpcbxfqWMNtglfwHebq/Dfe6YQPufQihsBOyRN7CjQD8bDXoUTMRIBCBN8pZd28xSCoUtaCAesr968BiQKfugy9U5mvkv2mcoJHMPSFX4EDkw9bNRei0DT9IqQiprSX4fFDunBqDLu0vHAgMBAAECggEBALDAZpX3sSy+So5VGsXPaEhDREdZaPQdsEoPbIpIIvM7kA88jcBdfJ7X5awlFTJY8oMjCHQ13PfF564Hy7RXNurF9BgeKKa4SQ4B+xPkjnKxCTzdvDMwt1UOGEzcAVhmpSz9UtmKTLYcm/UO/xfDi9ySg9iWrRJCUl/uWVCx+wdctyQbL4Rb/ESQ/mESYhJpRdDLIMeuLK2IjGoeP/OgiseX1znYSbVh6VBqh40Cb4v+ZQcTBWW/2EIaUKEeeiq9eAjJbG/9G/e6eo1ZCYJy0xV85uLX8nMOaXcV+oLSRDim6yAqmj3h2GmRy6HDeIp8bXoBjTCo11dU+0iNYEWBJtECgYEA+ZujOxntA6AepGwSHF9cq9yYsXy6ohNCAVGoUq8DbnH1HdEMUrV2sp6BSEY/KWUKrBjyAk1c9M6nmIvoJTgjN4MxoLFjWICxoXgQ5sc/5jnJlTSsZtFMKCghjKKwLrd7G0uc1MiuSGgxswbUirdhOqOmCpWfk/YnAp8wb6ayLikCgYEA60dmIFXTmwcfV33Z+okQxEMl2cAPCMMGpRkhBudbtKcWz+lZug/mpFsw/FHeUDU6zjwIUEqtYgaAr7mLV9SCA4ixGvCV8ErjeKZsH1nuXzEzbdj0Np0Mvad2XSkWiMBXzRgwqol84+JxcsujPAIkHn9ExULsSy9bzWVXRSvICG8CgYAVCQO5kFbEDnNmoeDo2TSDG0UW+A72Z68RU2NfDHa6kQn6KErkkXxeUzNL9veNsoEyvatvfDuQOfYvyeKXeGKnY/wDsc9Qfw/5LpgmCrac3VPaqiS5SXwkijVBl9hHJ5lNVJLz77GYUoeVscappgigm3f7vl5DZuQkuWuojSDI+QKBgQCuEp1UvytYP+TIbqyf2Fk3XBbdDGQTL3fzHqx41yPafOU73oIsTydt4qpThJ7e6QMhIvljrlypcfY0DSszSF8JDlE6hVXp+v5hfX/xki/y7gwG8cV9wqRyjTvdoVCyzNwpOgSHqEFmuVsGe/PBqjPe/Hp9GA6LcfaR+LCg1MuisQKBgQCEojuhqBQNN9WaPv3sbh+weBbSdDnVL9amAYlaEIhPYjYg2aJaq2G3PLio4OZmUXbv8tzy1OMUoThG6EMjQ6R0X5kFsnNkKVrVVGSzP+Le3swDIJ4lfuD5veRxKKPHTs31r9tWrxTpd+kft3nToUSnHxnz1I+uwCmFngJ7JpjPTA==";
    // 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://47.93.18.21/eshop/pay/notifyUrl";
//    public static String notify_url = "https://www.919sc.com/eshop/pay/notifyUrl";
    // 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
    public static String return_url = "http://47.93.18.21/eshop/pay/returnUrl";
    // 请求网关地址
    public static String URL_DEV = "https://openapi.alipaydev.com/gateway.do";
    public static String URL = "https://openapi.alipay.com/gateway.do";
    // 编码
    public static String CHARSET = "UTF-8";
    // 返回格式
    public static String FORMAT = "json";
    // 支付宝公钥
    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgNb5g9t6YIyYkDc5hjP8D131NatQOIaoYGeanm97USW03mWtBZc1G42kOTjk+nqJC+JCSvE6r+/HqEKklVygFmJ42JP+eBbfI97f/V2m4w6FKFbwTzi660y/pXxshkZioGdImI5jyvBRUGL9lQ064ZXn5Phk79UefWlw6DmBV4LsIAo0SJRqq6VuZsaoHox8sa4+NJ7C7S64bu1CPK8zvBSj2uFU2ylHJRrnEDD4yw3QZ28jWo2L/LQIG7mQRO2UNU8PCLMbqcUEMIbPCnc9gpw3sqaVXSRlXDx0Kpm2wgzi9ITHfT1MAoEJJJ97oPoxctZvJWsQoYwtzdAU1+MyVQIDAQAB";
    // 日志记录目录
    public static String log_path = "/log";
    // RSA2
    public static String SIGNTYPE = "RSA2";

    public static String seller_id = "2088621493853430";

    public static String timeout_express = "30m";
}
