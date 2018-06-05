package com.eshop.exception;

public class EshopAppGetParaException extends RuntimeException {
	
	private String errorMessage;
	
	public EshopAppGetParaException(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String getMessage() {
		return errorMessage;
	}
	
	

}
