package com.andy.gomoku.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.springframework.beans.BeanUtils;

import com.andy.gomoku.exception.GoSeviceException;

public class ReflectUtil {
    public static Object getFieldValue(Object obj, String fieldName) {
    	PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(obj.getClass(), fieldName);
    	if(descriptor == null)return null;
    	Object result = null;
		try {
			result = descriptor.getReadMethod().invoke(obj);
		} catch (Exception e) {
			throw new GoSeviceException("取属性"+fieldName+"]异常",e);
		}
    	return result;
    }

    public static void setFieldValue(Object obj, String fieldName, Object fieldValue) {
    	PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(obj.getClass(), fieldName);
    	Method writeMethod = descriptor.getWriteMethod();
		if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
			writeMethod.setAccessible(true);
		}
    	try {
			descriptor.getWriteMethod().invoke(obj, fieldValue);
		} catch (Exception e) {
			throw new GoSeviceException(e);
		}
    }
    
}