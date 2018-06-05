package com.eshop.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jdom2.JDOMException;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.eshop.config.AlipayConfig;
import com.eshop.model.Order;
import com.eshop.service.AppMemberService;
import com.eshop.service.AppOrderService;
import com.eshop.util.LogConst;
import com.eshop.util.wxConfig.ConstantUtil;
import com.eshop.util.wxConfig.MD5Util;
import com.eshop.util.wxConfig.PrepayIdRequestHandler;
import com.eshop.util.wxConfig.WXUtil;
import com.eshop.util.wxConfig.XMLUtil;
import com.eshop.vo.ResultJson;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.tx.Tx;

/**
 * 支付
 * Created by CTL on 2017/11/28.
 */
public class AppPayController extends Controller {
	//指定输出到pay.log
    private Logger logger = Logger.getLogger(LogConst.PAY_LOG);

    private AppOrderService appOrderService = new AppOrderService();

    private AppMemberService appMemberService = new AppMemberService();
    
    public void alipay(){
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(getPara("body"));
        model.setSubject(getPara("subject"));
        model.setOutTradeNo(getPara("sn"));
        model.setTimeoutExpress(AlipayConfig.timeout_express);
        model.setTotalAmount(getPara("total_amount"));
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(AlipayConfig.notify_url);
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            renderJson(ResultJson.success(response.getBody()));//就是orderString 可以直接给客户端请求，无需再做处理。
        } catch (AlipayApiException e) {
            logger.error("",e);
        }
    }

    /**
     *  支付宝支付回调
     * @throws AlipayApiException
     * @throws UnsupportedEncodingException
     */
    @Before({Tx.class,POST.class})
    public void notifyUrl() throws AlipayApiException, UnsupportedEncodingException {
    	logger.info("notifyUrl--------------");
        //获取支付宝GET过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = getRequest().getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //不要去掉下面注释，否则会导致验证不通过
//            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            logger.info( ":valueStr=" + valueStr);
            params.put(name, valueStr);
        }
        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
        //商户订单号
        String sn = new String(getRequest().getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
        //支付宝交易号
        String trade_no = new String(getRequest().getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
        double total_amount = Double.valueOf(getRequest().getParameter("total_amount"));
        //卖家id
        String seller_id = new String(getRequest().getParameter("seller_id").getBytes("ISO-8859-1"),"UTF-8");
        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
        //计算得出通知验证结果
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
        logger.info("params == "+params);
        boolean verify_result = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, AlipayConfig.SIGNTYPE);
        logger.info("verify_result==" + verify_result);
        if(verify_result){//验证成功
            Order order = appOrderService.findOrder(sn);
            order.setPayStatus(1);
            order.setPayTime(new Date());
            order.setPaySn(trade_no);
            order.setPayType(1);

            logger.info("seller_id=" + seller_id);
            if(seller_id.equals(AlipayConfig.seller_id) && total_amount == order.getFee()) {
                if (appOrderService.updateOrder(order)) {
                	
                	logger.info("更新用户积分");
                	appMemberService.updateScore(order);
                	
                    logger.info("订单更新成功");
                    renderJson("success");
                } else {
                    logger.info("订单状态更新失败,请联系客服");
                    renderJson("订单状态更新失败,请联系客服");
                }
            }else{
                logger.info("支付参数异常,请联系客服");
                renderJson("支付参数异常,请联系客服");
            }
        }else{//验证失败
            logger.info("验证失败");
            renderJson("验证失败");
        }
    }

    @Before(POST.class)
    public void wxPrepay() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        // 获取生成预支付订单的请求类
        PrepayIdRequestHandler prepayReqHandler = new PrepayIdRequestHandler(getRequest(), getResponse());
        String totalFee = getRequest().getParameter("total_fee");
        String out_trade_no = getRequest().getParameter("sn");
        int total_fee=(int) (Float.valueOf(totalFee)*100);
        logger.info("total:"+totalFee);
        logger.info("total_fee:" + total_fee);
        prepayReqHandler.setParameter("appid", ConstantUtil.APP_ID);
        prepayReqHandler.setParameter("body", ConstantUtil.BODY);
        prepayReqHandler.setParameter("mch_id", ConstantUtil.MCH_ID);
        String nonce_str = WXUtil.getNonceStr();
        prepayReqHandler.setParameter("nonce_str", nonce_str);
        prepayReqHandler.setParameter("notify_url", ConstantUtil.NOTIFY_URL);
        prepayReqHandler.setParameter("out_trade_no", out_trade_no);
        prepayReqHandler.setParameter("spbill_create_ip", getRequest().getRemoteAddr());
        String timestamp = WXUtil.getTimeStamp();
        prepayReqHandler.setParameter("time_start", timestamp);
        logger.info(String.valueOf(total_fee));
        prepayReqHandler.setParameter("total_fee", String.valueOf(total_fee));
        prepayReqHandler.setParameter("trade_type", "APP");
        /**
         * 注意签名（sign）的生成方式，具体见官方文档（传参都要参与生成签名，且参数名按照字典序排序，最后接上APP_KEY,转化成大写）
         */
        prepayReqHandler.setParameter("sign", prepayReqHandler.createMD5Sign());
        prepayReqHandler.setGateUrl(ConstantUtil.GATEURL);
        String prepayid = prepayReqHandler.sendPrepay();
        // 若获取prepayid成功，将相关信息返回客户端
        if (prepayid != null && !prepayid.equals("")) {
            String signs = "appid=" + ConstantUtil.APP_ID + "&noncestr=" + nonce_str + "&package=Sign=WXPay&partnerid="
                    + ConstantUtil.PARTNER_ID + "&prepayid=" + prepayid + "&timestamp=" + timestamp + "&key="
                    + ConstantUtil.APP_KEY;
            map.put("prepayid", prepayid);
            /**
             * 签名方式与上面类似
             */
            map.put("sign", MD5Util.MD5Encode(signs, "utf8").toUpperCase());
            map.put("appid", ConstantUtil.APP_ID);
            map.put("timestamp", timestamp);  //等于请求prepayId时的time_start
            map.put("noncestr", nonce_str);   //与请求prepayId时值一致
            map.put("package", "Sign=WXPay");  //固定常量
            map.put("partnerid", ConstantUtil.PARTNER_ID);
            renderJson(ResultJson.success(map));
        } else {
            renderJson(ResultJson.fail());
        }
    }

    /**
     * 微信支付回调
     * @param request
     * @param response
     * @throws IOException
     */
    @Before(Tx.class)
    public void getnotify(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        logger.info("微信支付回调");
        PrintWriter writer = response.getWriter();
        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        String result = new String(outSteam.toByteArray(), "utf-8");
        logger.info("微信支付通知结果：" + result);
        Map<String, String> map = null;
        try {
            /**
             * 解析微信通知返回的信息
             */
            map = XMLUtil.doXMLParse(result);
        } catch (JDOMException e) {
            // TODO Auto-generated catch block
            logger.error("",e);
        }
        logger.info("=========:"+result);
        // 若支付成功，则告知微信服务器收到通知
        if (map.get("return_code").equals("SUCCESS")) {
            if (map.get("result_code").equals("SUCCESS")) {
                logger.info("支付成功！");
                Order order = appOrderService.findOrder(map.get("out_trade_no"));
                logger.info("订单号："+Long.valueOf(map.get("out_trade_no")));
                logger.info("payRecord.getPayTime():"+order.getPayTime()==null+","+order.getPayTime());
                //判断通知是否已处理，若已处理，则不予处理
                if(order.getPayTime()==null){
                    logger.info("通知微信后台");
                    order.setPayStatus(1);
                    order.setPayTime(new Date());
                    order.setPaySn(map.get("transaction_id"));
                    order.setPayType(2);
                    appOrderService.updateOrder(order);
                    String notifyStr = XMLUtil.setXML("SUCCESS", "");
                    writer.write(notifyStr);
                    writer.flush();
                }
            }
        }
    }

    /**
     * 货到付款
     */
    public void cod(){
        logger.info("进入货到付款");
        String sn = getRequest().getParameter("sn");
        Order order = appOrderService.findOrder(sn);
        order.setPayType(3);
        appOrderService.updateOrder(order);
        renderJson(ResultJson.success());
    }
}
