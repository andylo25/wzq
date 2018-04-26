package com.andy.gomoku.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.andy.gomoku.dao.NameValue;
import com.andy.gomoku.entity.BaseEntity;
import com.andy.gomoku.entity.UsrUser;
import com.andy.gomoku.exception.GoSeviceException;
import com.andy.gomoku.utils.EntityMetadata.MetadataEntry;
import com.google.common.collect.Maps;

public class EntityUtils {
	
	private static Logger logger = LoggerFactory.getLogger(EntityUtils.class);
	
	private static ThreadLocal<Map<Class<?>,EntityMetadata>> mcache = new ThreadLocal<Map<Class<?>,EntityMetadata>>() {  
        public Map<Class<?>,EntityMetadata> initialValue() {  
            return new HashMap<>();  
        }  
    };  
	
	public static List<NameValue> getNameValues(BaseEntity baseEntity,boolean containEmpty,boolean containId) {
		List<NameValue> nameValueList = new ArrayList<NameValue>();
		Map<Class<?>, EntityMetadata> map = mcache.get();
		Class<? extends BaseEntity> clasz = baseEntity.getClass();
		EntityMetadata metadata = map.get(clasz );
		if(metadata == null){
			metadata = new EntityMetadata(clasz);
			map.put(clasz, metadata);
		}
		
		Map<String, MetadataEntry> fcs = metadata.getFieldCols();
		for(Entry<String, MetadataEntry> fc : fcs.entrySet()) {
			if(!containId && "id".equals(fc.getKey()))continue;
			Object value = ReflectUtil.getFieldValue(baseEntity, fc.getKey());
			if(containEmpty || value != null) {
				String column = fc.getValue().getColName();
				nameValueList.add(new NameValue(column, value));
			}
		}
		return nameValueList;
	}
	
	/**
	 * Entity 转换(浅转换)为 map
	 * @param baseEntity
	 * @return
	 */
	public static Map<String,Object> toMap(BaseEntity baseEntity){
		Map<String,Object> result = Maps.newHashMap();
		if(baseEntity == null)return result;
		PropertyDescriptor[] fields = BeanUtils.getPropertyDescriptors(baseEntity.getClass());
		for(PropertyDescriptor targetPd : fields){
			String fieldName = targetPd.getName();
			if(filterField(fieldName)) continue;
			try {
				result.put(fieldName, targetPd.getReadMethod().invoke(baseEntity));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new GoSeviceException(e);
			}
		}
		return result;
	}
	
	/**
	 * map 转换(浅转换)为 Entity
	 * @param map
	 * @param claz
	 * @return
	 */
	public static <T> T toEntity(Map<String,Object> map,Class<T> claz){
		if(map == null)return null;
		T target = BeanUtils.instantiate(claz);
		PropertyDescriptor[] fields = BeanUtils.getPropertyDescriptors(claz);
		for(PropertyDescriptor targetPd : fields){
			String fieldName = targetPd.getName();
			if(filterField(fieldName)) continue;
			Method writeMethod = targetPd.getWriteMethod();
			if (writeMethod != null) {
				if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
					writeMethod.setAccessible(true);
				}
				Object value = map.get(fieldName);
				if(value != null){
					try {
						writeMethod.invoke(target, value);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						throw new GoSeviceException(e);
					}
				}
			}
		}
		return target;
	}
	
	/**
	 * 判断目标实体值是否修改过，排除null属性
	 * @param source
	 * @param target
	 * @return
	 */
	public static boolean isChanged(BaseEntity source,BaseEntity target){
		if(target == null || source == null)return false;
		PropertyDescriptor[] fields = BeanUtils.getPropertyDescriptors(target.getClass());
		for(PropertyDescriptor targetPd : fields){
			String fieldName = targetPd.getName();
			if(filterField(fieldName)) continue;
			Method readMethod = targetPd.getReadMethod();
			if (readMethod != null) {
				if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
					readMethod.setAccessible(true);
				}
				try {
					Object value = readMethod.invoke(target);
					if(value != null){
						PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
						if(sourcePd != null){
							Method readMethods = sourcePd.getReadMethod();
							if(readMethods != null){
								Object svalue = readMethods.invoke(source);
								if(!value.equals(svalue)){
									return true;
								}
							}
						}
					}
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new GoSeviceException(e);
				}
			}
		}
		return false;
	}

	private static boolean filterField(String fieldName) {
		return "class".equals(fieldName) || "serialVersionUID".equals(fieldName);
	}
	
	public static void main(String[] args) {
		
		Map<String,Object> map = Maps.newHashMap();
		map.put("value", "3");
		map.put("label", "ee");
		UsrUser fd = toEntity(map , UsrUser.class);
		System.out.println(fd.getId());
		map.put("label", "ee");
		UsrUser fd1 = toEntity(map , UsrUser.class);
		System.out.println(isChanged(fd, fd1));
	}

	
}
