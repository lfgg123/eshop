package com.eshop.vo;

import com.eshop.util.Status;

/**
 * 返回对象
 * 
 * @author
 *
 */
public class ResultJson {

	public int code;
	public String message;
	public Object result;
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}

	public ResultJson() {
	}

	public ResultJson(int code, String message, Object obj) {
		this.code = code;
		this.message = message;
		this.result = obj;

	}

	public ResultJson(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public static ResultJson success(Object obj) {
		return new ResultJson(Status.code_success, Status.message_success, obj);
	}

	public static ResultJson success() {
		return new ResultJson(Status.code_success, Status.message_success);
	}

	public static ResultJson fail(Object obj) {
		return new ResultJson(Status.code_fail, Status.message_fail, obj);
	}

	public static ResultJson fail() {
		return new ResultJson(Status.code_fail, Status.message_fail);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}
