package com.mooc.house.common.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

public class BeanHelper {
 
  private static final String updateTimeKey  = "updateTime";
  
  private static final String createTimeKey  = "createTime";
 
  /**
   * 给Class设置属性值
   * @param target
   * @param clazz
   */
  public static <T> void setDefaultProp(T target,Class<T> clazz) {
	//获得clazz参数对应的property数组
    PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(clazz);
    //遍历所有的属性
    for (PropertyDescriptor propertyDescriptor : descriptors) {
      //每一个属性的名称
      String fieldName = propertyDescriptor.getName();
      Object value;
      try {
    	//从target中获得相应属性的值
        value = PropertyUtils.getProperty(target,fieldName );
      } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
        throw new RuntimeException("can not set property  when get for "+ target +" and clazz "+clazz +" field "+ fieldName);
      }
      //判断是否是一个string 或者值是否为空，
      if (String.class.isAssignableFrom(propertyDescriptor.getPropertyType()) && value == null) {
        try {
        	//给target中fieldname对应的属性，如果为字符串，设置默认值为空
          PropertyUtils.setProperty(target, fieldName, "");
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
          throw new RuntimeException("can not set property when set for "+ target +" and clazz "+clazz + " field "+ fieldName);
        }
      }else if (Number.class.isAssignableFrom(propertyDescriptor.getPropertyType()) && value == null) {
          try {
        	  //如果是数字，设置默认值为0
            BeanUtils.setProperty(target, fieldName, "0");
          } catch (Exception e) {
            throw new RuntimeException("can not set property when set for "+ target +" and clazz "+clazz + " field "+ fieldName);
          }
      }
    }
  }
  
  public static <T> void onUpdate(T target){
    try {
      PropertyUtils.setProperty(target, updateTimeKey, System.currentTimeMillis());
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      return;
    }
  }
  
  private static <T> void innerDefault(T target, Class<?> clazz, PropertyDescriptor[] descriptors) {
	  //遍历属性数组里的所有属性
	    for (PropertyDescriptor propertyDescriptor : descriptors) {
	    	//获得一个属性的属性名
	      String fieldName = propertyDescriptor.getName();
	      Object value;
	      try {
	    	  //得到响应target中相应属性名的属性值
	        value = PropertyUtils.getProperty(target,fieldName );
	      } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
	        throw new RuntimeException("can not set property  when get for "+ target +" and clazz "+clazz +" field "+ fieldName);
	      }
	      //对属性名对应属性值的类型进行判断，并且赋默认值
	      if (String.class.isAssignableFrom(propertyDescriptor.getPropertyType()) && value == null) {
	        try {
	        	//string 附默认值“”
	          PropertyUtils.setProperty(target, fieldName, "");
	        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
	          throw new RuntimeException("can not set property when set for "+ target +" and clazz "+clazz + " field "+ fieldName);
	        }
	      }else if (Number.class.isAssignableFrom(propertyDescriptor.getPropertyType()) && value == null) {
	          try {
	        	  //数字附默认值0
	            BeanUtils.setProperty(target, fieldName, "0");
	          } catch (Exception e) {
	            throw new RuntimeException("can not set property when set for "+ target +" and clazz "+clazz + " field "+ fieldName);
	          }
	      }else if (Date.class.isAssignableFrom(propertyDescriptor.getPropertyType()) && value == null) {
	          try {
	        	  //日期附默认值date(0)
	             BeanUtils.setProperty(target, fieldName, new Date(0));
	          } catch (Exception e) {
	             throw new RuntimeException("can not set property when set for "+ target +" and clazz "+clazz + " field "+ fieldName);
	          }
	      }
	    }
	  }
  
  public static <T> void onInsert(T target){
	  //获得target的class映射文件
	Class<?> clazz = target.getClass();
	//属性值数组
	PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(clazz);
	//设置属性默认值
	innerDefault(target, clazz, descriptors);
    long time = System.currentTimeMillis();
    Date date = new Date(time);
    try {
    	//设置updatetime属性
      PropertyUtils.setProperty(target, updateTimeKey, date);
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      
    }
    try {
    	//设置createtime属性
      PropertyUtils.setProperty(target, createTimeKey, date);
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      
    }
  }
  

}
