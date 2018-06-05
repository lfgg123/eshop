package com.eshop.util;

import java.util.regex.Pattern;

public class RegularExpressionValidatorUtils {
	
	/***
	 * 校验手机号是否正确
	 * @param String mobile
	 * @return boolean
	 * */
	public static boolean isMobile(String mobile) {
		String mobileRegularExpression = "^1[0-9]{10}$";
		return Pattern.matches(mobileRegularExpression,mobile);
	}

}
