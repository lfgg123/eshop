package com.eshop.util;

import java.lang.reflect.Method;

public class BeanUtils {

    public static <T> T transferObject(Object obj, Class clz) {
        T result = null;
        if (obj != null && !obj.equals("")) {
            Method[] methods = obj.getClass().getMethods();
            try {
                result = (T) clz.newInstance();
            } catch (Exception e1) {
                return null;
            }
            Method m;
            for (int i = 0; i < methods.length; i++) {
                m = methods[i];
                try {
                    if (m.getName().startsWith("set")) {
                        String fieldName = m.getName().replaceFirst("set", "");
                        Method method = result.getClass().getMethod(m.getName(), m.getParameterTypes());
                        Method getMethod = obj.getClass().getMethod("get" + fieldName, new Class[]{});
                        method.invoke(result, getMethod.invoke(obj, new Object[]{}));
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }
        return result;
    }
}