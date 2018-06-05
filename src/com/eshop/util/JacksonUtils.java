package com.eshop.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfinal.log.Log;

public class JacksonUtils {
	private final static Log logger = Log.getLog(JacksonUtils.class);

	/**
	 * 对象序列化
	 * 
	 * @param Object
	 *            obj
	 * @return String json
	 * @throw Exception *
	 */
	public static String obj2json(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(obj);
		} catch (Exception e) {
			logger.error("JacksonUtil序列化 error:" + e.getMessage());
		}
		return null;
	}

	/**
	 * 对象反序列化
	 * @param String json
	 * @param Class<T> clazz
	 * @return T Object
	 * 
	 */
	public static <T> T json2Obj(String json, Class<T> clazz) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json, clazz);
		} catch (Exception e) {
			logger.error("JacksonUtil反序列化error:" + e.getMessage());
		}
		return null;
	}

	/**
	 *复杂对象反序列化
	 *@param String json
	 * @param TypeReference<T> type
	 * @return T Object 
	 */
	public static <T> T json2Obj(String json, TypeReference<T> type) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json, type);
		} catch (Exception e) {
			logger.error("JacksonUtil反序列化error:" + e.getMessage());
		}
		return null;
	}

}
