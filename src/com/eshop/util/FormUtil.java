package com.eshop.util;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.plugin.activerecord.Record;

/**
 * 表单内容转Record
 * @author fengpei
 */
public class FormUtil {
	
	public static Record formToRecord(HttpServletRequest request,
			Enumeration<String> e) {
		Record map = new Record();
		while (e.hasMoreElements()) {
			String keyString = String.valueOf(e.nextElement());
			Object valueObject = null;
			if (!keyString.equals("timeStamp")) {
				valueObject = request.getParameter(keyString);
				if(valueObject!=null) {
					map.set(keyString, valueObject);
				}
			}
		}
		return map;
	}
}
