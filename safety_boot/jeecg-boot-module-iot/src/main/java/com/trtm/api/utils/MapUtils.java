package com.trtm.api.utils;

import com.trtm.api.utils.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public class MapUtils {

	/**
	 * map转对象
	 * @param beanClass
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public static <T> T toObject(Map<String, Object> map, Class<T> beanClass) throws Exception{
		T bean = null;
		try {
			//对象实例化
			if (beanClass.isEnum()){
				T[] enumConstants = beanClass.getEnumConstants();
				if (enumConstants == null || enumConstants.length == 0) {
					return null;
				}
				return getEnumBean(map, enumConstants);
			}
			bean = beanClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(map == null || map.isEmpty()) {
			return bean;
		}
		//循环map集合
		for(String methodName : map.keySet()) {
			Object value = map.get(methodName);
			if(value == null) {
				continue;
			}
			Field beanField = null;
			try {
				beanField = beanClass.getDeclaredField(methodName);
			} catch (NoSuchFieldException e) {
				try {
					beanField = beanClass.getSuperclass().getDeclaredField(methodName);
				} catch (Exception e1) {
					continue;
				}
			}
			if(beanField == null) {
				continue;
			}
			beanField.setAccessible(true);
			// 获取类属性的类型，将value类型转换
			String methodType = beanField.getType().getName();
			if(beanField.getType().isAssignableFrom(String.class)) {
				if (value instanceof Map<?, ?>) {
					value = JSONObject.valueToString(value);
				}else {
					value = value.toString();
				}
			} else if(beanField.getType().isAssignableFrom(Long.class) || beanField.getType().getName().equals("long")) {
				value = Long.parseLong(value.toString());
	        } else if(beanField.getType().isAssignableFrom(Integer.class) || beanField.getType().getName().equals("int")) {
	        	value = Integer.parseInt(value.toString());
	        } else if(beanField.getType().isAssignableFrom(Float.class) || beanField.getType().getName().equals("float")) {
	        	value = Float.parseFloat(value.toString());
	        } else if(beanField.getType().isAssignableFrom(Double.class) || beanField.getType().getName().equals("double")) {
	        	value = Double.parseDouble(value.toString());
	        } else if(beanField.getType().isAssignableFrom(Boolean.class) || beanField.getType().getName().equals("boolean")) {
	        	value = Boolean.parseBoolean(value.toString());
	        } else if(beanField.getType().isAssignableFrom(Date.class)){
	        	value = TimeUtils.parse(value.toString());
	        } else if(beanField.getType().isAssignableFrom(Set.class)){
	        	value = StringUtils.StringToSet(value.toString());
	        } else if(beanField.getType().isEnum()) {
	        	// 枚举类型进行转换
	        	value = convertToEnum(methodType, value);
	        } else if(beanField.getType().isAssignableFrom(ArrayList.class)) {
	        	// 获取数组中对象
				ParameterizedType pt = (ParameterizedType) beanField.getGenericType();
				Class<?> listClass = (Class) pt.getActualTypeArguments()[0];
				if (!guessType(listClass)) {
					List<Object> objects = new ArrayList<Object>();
					// 数组类型进行循环递归
					List<Map<String, Object>> maps = (List<Map<String, Object>>) value;
					for(Map<String, Object> tempMap : maps) {
						Object object = toObject(tempMap, listClass);
						objects.add(object);
					}
					value = objects;
				}
	        } else {
	        	// 定义的类
        		Class<?> tempClass = beanField.getType();
        		Map<String, Object> tempMap = (Map<String, Object>) value;
        		value = toObject(tempMap, tempClass);
	        }
			try {
				String methodName1 = "set" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
				Method method = beanClass.getMethod(methodName1, beanField.getType());
				method.invoke(bean, value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return bean;
	}

	private static <T> T getEnumBean(Map<String, Object> map, T[] enumConstants) {
		String key = (String) map.get("key");
		for (T enumConstant : enumConstants) {
			if (enumConstant.toString().equals(key)){
				return enumConstant;
			}
		}
		return null;
	}

	/**
	 * 判断类型是否是基本类型、包装类型、String类型
	 */
	private static boolean guessType(Class clas) {
		String[] types = {
				"java.lang.Integer",
		        "java.lang.Double",
		        "java.lang.Float",
		        "java.lang.Long",
		        "java.lang.Short",
		        "java.lang.Byte",
		        "java.lang.Boolean",
		        "java.lang.Character",
		        "java.lang.String",
		        "int","double","long","short","byte","boolean","char","float"};
		for(String str : types) {
			if(str.equals(clas.getName())) {
				return true;
			}
		}
		//// 枚举也是基本类型
		//if (Enum.class.isAssignableFrom(clas)){
		//	return true;
		//}
		return false;
	}

	private static Enum convertToEnum(String enumName, Object value) throws Exception {
		Class<Enum> clazz = null;
		try {
			clazz = (Class<Enum>)Class.forName(enumName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (clazz.isAssignableFrom(value.getClass())) {
            Enum myE = (Enum) value;
            return myE;
        }
		try {
			return Enum.valueOf(clazz, value.toString());
		} catch (Exception e) {

		}
		return toObject((Map<String, Object>) value,clazz);
	}

	/**
	 * map集合转对象集合
	 * @param <T>
	 * @param beanClass
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> toObjectList(List<Map<String, Object>> maps, Class<T> beanClass) throws Exception{
		if(maps == null || maps.size() < 0) {
			return null;
		}
		List<T> beanList = new ArrayList<T>();
		for(Map<String, Object> tempMap : maps) {
			T bean = toObject(tempMap, beanClass);
			beanList.add(bean);
		}
		return beanList;
	}

	/**
	 * 实体类转Map
	 * 后续版本要求使用json格式  请使用 entityToMap
	 * @param object
	 * @return
	 */
	public static Map<String, Object> entityToMap(Object object) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Field> fieldList = new ArrayList<Field>() ;
		Class tempClass = object.getClass();
		while (tempClass !=null && !"java.lang.object".equalsIgnoreCase(tempClass.getName()) ) {//当父类为null的时候说明到达了最上层的父类(Object类).
			fieldList.addAll(Arrays.asList(tempClass .getDeclaredFields()));
			tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己

		}
		for (Field field : fieldList) {
			try {
				boolean flag = field.isAccessible();
				field.setAccessible(true);
				Object o = field.get(object);
				if (o == null){
					continue;
				}
				map.put(field.getName(), o);
				//if (guessType(o.getClass())){
				//	map.put(field.getName(), o);
				//} else {
				//	map.put(field.getName(), entityToMap(o));
				//}
				field.setAccessible(flag);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}

}
