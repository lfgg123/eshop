package com.eshop.vo;

import java.io.Serializable;

import com.jfinal.core.Controller;

public class ActionLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String url;
	private Class controller;
	private String actionName;
	private boolean successStatus;
	private String errorMessage;

	public ActionLog(String url, Class controller, String actionName, boolean isSuccess, String errorMessage) {
		this.url = url;
		this.controller = controller;
		this.actionName = actionName;
		this.successStatus = isSuccess;
		this.errorMessage = errorMessage;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Class getController() {
		return controller;
	}

	public void setController(Class controller) {
		this.controller = controller;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public boolean isSuccessStatus() {
		return successStatus;
	}

	public void setSuccessStatus(boolean successStatus) {
		this.successStatus = successStatus;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
