package com.imaginepartners.imagineworkflow.automation;

import com.imaginepartners.imagineworkflow.util.Util;
import java.io.Serializable;
import org.apache.log4j.LogManager;

public class AutomationUtils extends Object implements Serializable{

	public AutomationUtils(){
		LogManager.getLogger(AutomationUtils.class.getSimpleName())
		.debug("constructor method call");
	}

	/**
	 * Build new object (primitive type) limited by the given size.
	 * @param type full class name
	 * @param lengh number of characters @see size
	 * @return new object as primitive instanciated  and ready to use.
	 */
	public static Object BUILD_OBJECT(Class<?> type, int lengh){
		Object obj = null;
		if(type.isAssignableFrom(String.class))
			obj= new String();
		if(type.isAssignableFrom(Integer.class))
			obj= new Integer(0);
		int d=0;
		for(int i=0; i<lengh; i++){
			if(obj instanceof String){
				d=  GENERATE_RANDOM_NUMBER(65, 95);
				obj = obj + ((char) d + "");
			}
			if(obj instanceof Integer){
				d=  GENERATE_RANDOM_NUMBER(48, 58);
				obj = (Integer)obj + new Integer((char)d+"");
			}
		}
		return obj;
	}

	/**
	 * generate a value between x and y numbers passed as arguments
	 * @param x minimum number
	 * @param y maximum number
	 * @return the generated value between x and y.
	 */
	public static int GENERATE_RANDOM_NUMBER(int x, int y){
		int randomNumber= 0;
		while(true){
			randomNumber= (int) (Math.random()*100);
			if(randomNumber >=x && randomNumber <= y)
				return randomNumber;
		}
	}
}
