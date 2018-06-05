package com.eshop.util;

import java.util.UUID;

public class UuidUtil {

	public static String getUuid(){
		return UUID.randomUUID().toString().replace("-", "");
	}
	public static void main(String[] arg){
		System.out.println(UuidUtil.getUuid());
	}
}
