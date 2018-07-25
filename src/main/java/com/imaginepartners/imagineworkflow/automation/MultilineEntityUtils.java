package com.imaginepartners.imagineworkflow.automation;

import com.imaginepartners.imagineworkflow.util.Util;
import java.lang.reflect.Field;
import javax.persistence.Column;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;

public final class MultilineEntityUtils extends AutomationUtils{

	public MultilineEntityUtils(){
		LogManager.getLogger(MultilineEntityUtils.class.getSimpleName())
		.debug("constructor method call");
	}

	/**
	 * Helper class method for initializing fields.
	 * @param clazz class object.
	 * @return new instance for given class.
	 */
	public static Object INIT_CLAZZ(Class<?> clazz){
		try {
			Object newInstance= clazz.newInstance();
			for(Field field: clazz.getDeclaredFields()){
				try {
					field.setAccessible(true);
					if( field.getType().isAssignableFrom(String.class) )
						field.set(newInstance, BUILD_OBJECT(String.class, 10));
					if(field.getType().isAssignableFrom(Integer.class) && !field.getName().equals("idPrime"))
						field.set(newInstance, BUILD_OBJECT(Integer.class, 4));
				} catch (IllegalArgumentException ex) {
					LogManager.getLogger(Util.class.getSimpleName()).warn(ex.getClass()+", "+ex.getMessage());
				} catch (IllegalAccessException ex) {
					LogManager.getLogger(Util.class.getSimpleName()).warn(ex.getClass()+", "+ex.getMessage());
				}
			}
			return newInstance;
		} catch (InstantiationException ex) {
			LogManager.getLogger(Util.class.getSimpleName()).warn(ex.getClass()+", "+ex.getMessage());
		} catch (IllegalAccessException ex) {
			LogManager.getLogger(Util.class.getSimpleName()).warn(ex.getClass()+", "+ex.getMessage());
		}
		return null;
	}

	/**
	 * Try to convert JPA object to string.
	 * @param multilineEntityInstance multiLine entity object ref
	 * @return converted list of property name value pairs of the given multiline entity instance.
	 */
	public static String MULTILINE_ENITY_TO_STRING(Object multilineEntityInstance){
		StringBuilder strBldr= new StringBuilder();
		for(Field field: multilineEntityInstance.getClass().getDeclaredFields()){
			field.setAccessible(true);
			if(field.getAnnotation(Column.class) != null){
				strBldr.append(field.getName());
				strBldr.append(": ");
				try {
					strBldr.append(field.get(multilineEntityInstance).toString());
				} catch (IllegalArgumentException ex) {
					LogManager.getLogger(Util.class.getSimpleName()).warn(ex.getClass().getSimpleName()+", "+ex.getMessage());
					strBldr.append("");
				} catch (IllegalAccessException ex) {
					LogManager.getLogger(Util.class.getSimpleName()).warn(ex.getClass().getSimpleName()+", "+ex.getMessage());
					strBldr.append("");
				}catch (NullPointerException npe){
					strBldr.append("null");
				}
				strBldr.append(String.valueOf(";  ")) ;
			}
		}
		return strBldr.toString();
	}

	/**
	 * Build dynamic math expression
	 * @param instances multiline entity object ref, or other class using java persistante API
	 * @param resultFielsdNames property name holding result
	 * @return a string of array containinig math expression ready to be calculated and evaluated
	 */
	public static String [] BUILD_EXPRESSIONS(Object [] instances, String[] resultFielsdNames){
		if(instances.length != resultFielsdNames.length)
			return null;
		int co=0;
		Object obj= null;
		String dynamicExp[]= new String [instances.length];
		while(co < instances.length) {
			obj= instances[co];
			StringBuilder exp= new StringBuilder(resultFielsdNames[co]+" = ");
			for(Field field: obj.getClass().getDeclaredFields()){
				field.setAccessible(true);
				try {
					/**
					 * We can add some filters here like field.getValue != null
					 * try to create expression dynamically.
					 */
					if(field.getAnnotation(Column.class) != null && field.getType().isAssignableFrom(Integer.class)) {
						exp.append(field.getName());
						exp.append(StringUtils.SPACE);
						exp.append(Util.OPERATORS[GENERATE_RANDOM_NUMBER(0, 3)]);
						exp.append(StringUtils.SPACE);
					}
				} catch (IllegalArgumentException ex) {
					LogManager.getLogger(Util.class.getSimpleName()).warn(ex.getClass()+", "+ex.getMessage());
				}
			}
			/**
			 * Normilize expression text.
			 * the final expression must respect the given pattern:
			 * fieldOne * fieldThree / fieldXXXX as an example.
			 */
			exp.delete(exp.length()-3, exp.length()-1);
			dynamicExp[co]= exp.toString();
			co++;
		}
		return dynamicExp;
	}
}
