package com.eshop.interceptor;

import org.apache.log4j.Logger;

import com.eshop.util.JacksonUtils;
import com.eshop.vo.ActionLog;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;

public class EshopAppExceptionHandler implements Interceptor {
	private final static Logger logger = Logger.getLogger(EshopAppExceptionHandler.class);

	@Override
	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		boolean isSuccess = false;
		String errorMessage = "";
		try {
			inv.invoke();
			isSuccess = true;
		} catch (Exception e) {
			isSuccess = false;
			Throwable throwable = null;
			if (e.getCause() != null) {
				throwable = e.getCause().getCause();
			} else {
				throwable = (Throwable) e;
			}
			// 返回异常信息
			if (StrKit.isBlank(throwable.getMessage())) {
				errorMessage = "系统异常，请稍后再试";
			} else {
				errorMessage = throwable.getMessage();
			}
			String replyJson = "{\"code\":0" + ",\"message\":\"" + errorMessage + "\"}";
			controller.renderJson(replyJson);
		} finally {
			// 插入日志
			if (isSuccess) {
				ActionLog actionLog = null;
				if (isSuccess) {
					actionLog = new ActionLog(inv.getViewPath(), controller.getClass(), inv.getActionKey(), true, null);
					logger.info(JacksonUtils.obj2json(actionLog));
				} else {
					actionLog = new ActionLog(inv.getViewPath(), controller.getClass(), inv.getActionKey(), false,
							errorMessage);
					logger.error(JacksonUtils.obj2json(actionLog));
				}

			}

		}

	}

}
