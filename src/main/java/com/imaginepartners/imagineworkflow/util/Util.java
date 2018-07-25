package com.imaginepartners.imagineworkflow.util;

import com.imaginepartners.imagineworkflow.converter.EntityConverter;
import java.lang.reflect.Field;
import java.util.*;
import javax.persistence.Id;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;

public class Util {

	public static char[] OPERATORS;

	public static char[] ARITH_OPERANDS;

	static {
		OPERATORS= new char[]{'+', '-', '*', '/', '='};
		ARITH_OPERANDS = new char[]{'(', ')', '='};
	}

	public static Object getEntityIdValue(String className, Object entity) {
		Field fld = Util.getEntityIdField(className);
		if (fld != null) {
			try {
				fld.setAccessible(true);
				return fld.get(entity);
			} catch (Exception ex) {
				LogManager.getLogger(EntityConverter.class.getName()).error(null, ex);
			}
		}
		return null;
	}

	public static void setEntityFieldValue(String className, Object entity, String fieldName, Object value) {
		try {
			Class entityClass = Class.forName(className);
			Field fld = entityClass.getDeclaredField(fieldName);
			if (fld != null) {
				try {
					fld.setAccessible(true);
					fld.set(entity, value);
				} catch (Exception ex) {
					LogManager.getLogger(EntityConverter.class.getName()).error(null, ex);
				}
			}
		} catch (Exception ex) {
			LogManager.getLogger(EntityConverter.class.getName()).error(null, ex);
		}
	}

	public static Object getEntityFieldValue(String className, Object entity, String fieldName) {
		try {
			Class entityClass = Class.forName(className);
			Field fld = entityClass.getDeclaredField(fieldName);
			if (fld != null) {
				try {
					fld.setAccessible(true);
					return fld.get(entity);
				} catch (Exception ex) {
					LogManager.getLogger(EntityConverter.class.getName()).error(null, ex);
				}
			}
		} catch (Exception ex) {
			LogManager.getLogger(EntityConverter.class.getName()).error(null, ex);
		}
		return null;
	}

	public static Field getEntityIdField(String className) {
		try {
			Class entityClass = Class.forName(className);
			return Util.getEntityIdField(entityClass);
		} catch (ClassNotFoundException ex) {
			LogManager.getLogger(EntityConverter.class.getName()).error(null, ex);
		}
		return null;
	}

	public static Field getEntityIdField(Class entityClass) {
		for (Field fld : entityClass.getDeclaredFields()) {
			if (fld.getAnnotation(Id.class) != null) {
				return fld;
			}
		}
		return null;
	}

	public static String convertToAlphaNumeric(String text) {
		if (StringUtils.isEmpty(text)) {
			return "";
		}
		// Start by removing accents
		String textNoAccents = Util.replaceAccents(text);
		// Now remove all non alphanumeric chars
		StringBuffer result = new StringBuffer(textNoAccents.length());
		char[] testChars = textNoAccents.toCharArray();
		for (char testChar : testChars) {
			if (Character.isLetterOrDigit(testChar) && testChar < 128) {
				result.append(testChar);
			}
		}
		return result.toString();
	}

	public static String replaceAccents(String text) {
		if (StringUtils.isEmpty(text)) {
			return text;
		}
		String temp = text;
		temp = temp.replaceAll("\u00c0", "A");
		temp = temp.replaceAll("\u00c1", "A");
		temp = temp.replaceAll("\u00c2", "A");
		temp = temp.replaceAll("\u00c3", "A");
		temp = temp.replaceAll("\u00c4", "A");
		temp = temp.replaceAll("\u00c5", "A");
		temp = temp.replaceAll("\u0100", "A");
		temp = temp.replaceAll("\u0102", "A");
		temp = temp.replaceAll("\u0104", "A");
		temp = temp.replaceAll("\u01cd", "A");
		temp = temp.replaceAll("\u01de", "A");
		temp = temp.replaceAll("\u01e0", "A");
		temp = temp.replaceAll("\u01fa", "A");
		temp = temp.replaceAll("\u0200", "A");
		temp = temp.replaceAll("\u0202", "A");
		temp = temp.replaceAll("\u0226", "A");
		temp = temp.replaceAll("\u00e0", "a");
		temp = temp.replaceAll("\u00e1", "a");
		temp = temp.replaceAll("\u00e2", "a");
		temp = temp.replaceAll("\u00e3", "a");
		temp = temp.replaceAll("\u00e4", "a");
		temp = temp.replaceAll("\u00e5", "a");
		temp = temp.replaceAll("\u0101", "a");
		temp = temp.replaceAll("\u0103", "a");
		temp = temp.replaceAll("\u0105", "a");
		temp = temp.replaceAll("\u01ce", "a");
		temp = temp.replaceAll("\u01df", "a");
		temp = temp.replaceAll("\u01e1", "a");
		temp = temp.replaceAll("\u01fb", "a");
		temp = temp.replaceAll("\u0201", "a");
		temp = temp.replaceAll("\u0203", "a");
		temp = temp.replaceAll("\u0227", "a");
		temp = temp.replaceAll("\u00c6", "AE");
		temp = temp.replaceAll("\u01e2", "AE");
		temp = temp.replaceAll("\u01fc", "AE");
		temp = temp.replaceAll("\u00e6", "ae");
		temp = temp.replaceAll("\u01e3", "ae");
		temp = temp.replaceAll("\u01fd", "ae");
		temp = temp.replaceAll("\u008c", "OE");
		temp = temp.replaceAll("\u0152", "OE");
		temp = temp.replaceAll("\u009c", "oe");
		temp = temp.replaceAll("\u0153", "oe");
		temp = temp.replaceAll("\u00c7", "C");
		temp = temp.replaceAll("\u0106", "C");
		temp = temp.replaceAll("\u0108", "C");
		temp = temp.replaceAll("\u010a", "C");
		temp = temp.replaceAll("\u010c", "C");
		temp = temp.replaceAll("\u00e7", "c");
		temp = temp.replaceAll("\u0107", "c");
		temp = temp.replaceAll("\u0109", "c");
		temp = temp.replaceAll("\u010b", "c");
		temp = temp.replaceAll("\u010d", "c");
		temp = temp.replaceAll("\u00d0", "D");
		temp = temp.replaceAll("\u010e", "D");
		temp = temp.replaceAll("\u0110", "D");
		temp = temp.replaceAll("\u00f0", "d");
		temp = temp.replaceAll("\u010f", "d");
		temp = temp.replaceAll("\u0111", "d");
		temp = temp.replaceAll("\u00c8", "E");
		temp = temp.replaceAll("\u00c9", "E");
		temp = temp.replaceAll("\u00ca", "E");
		temp = temp.replaceAll("\u00cb", "E");
		temp = temp.replaceAll("\u0112", "E");
		temp = temp.replaceAll("\u0114", "E");
		temp = temp.replaceAll("\u0116", "E");
		temp = temp.replaceAll("\u0118", "E");
		temp = temp.replaceAll("\u011a", "E");
		temp = temp.replaceAll("\u0204", "E");
		temp = temp.replaceAll("\u0206", "E");
		temp = temp.replaceAll("\u0228", "E");
		temp = temp.replaceAll("\u00e8", "e");
		temp = temp.replaceAll("\u00e9", "e");
		temp = temp.replaceAll("\u00ea", "e");
		temp = temp.replaceAll("\u00eb", "e");
		temp = temp.replaceAll("\u0113", "e");
		temp = temp.replaceAll("\u0115", "e");
		temp = temp.replaceAll("\u0117", "e");
		temp = temp.replaceAll("\u0119", "e");
		temp = temp.replaceAll("\u011b", "e");
		temp = temp.replaceAll("\u01dd", "e");
		temp = temp.replaceAll("\u0205", "e");
		temp = temp.replaceAll("\u0207", "e");
		temp = temp.replaceAll("\u0229", "e");
		temp = temp.replaceAll("\u011c", "G");
		temp = temp.replaceAll("\u011e", "G");
		temp = temp.replaceAll("\u0120", "G");
		temp = temp.replaceAll("\u0122", "G");
		temp = temp.replaceAll("\u01e4", "G");
		temp = temp.replaceAll("\u01e6", "G");
		temp = temp.replaceAll("\u01f4", "G");
		temp = temp.replaceAll("\u011d", "g");
		temp = temp.replaceAll("\u011f", "g");
		temp = temp.replaceAll("\u0121", "g");
		temp = temp.replaceAll("\u0123", "g");
		temp = temp.replaceAll("\u01e5", "g");
		temp = temp.replaceAll("\u01e7", "g");
		temp = temp.replaceAll("\u01f5", "g");
		temp = temp.replaceAll("\u0124", "H");
		temp = temp.replaceAll("\u0126", "H");
		temp = temp.replaceAll("\u021e", "H");
		temp = temp.replaceAll("\u0125", "h");
		temp = temp.replaceAll("\u0127", "h");
		temp = temp.replaceAll("\u021f", "h");
		temp = temp.replaceAll("\u00cc", "I");
		temp = temp.replaceAll("\u00cd", "I");
		temp = temp.replaceAll("\u00ce", "I");
		temp = temp.replaceAll("\u00cf", "I");
		temp = temp.replaceAll("\u0128", "I");
		temp = temp.replaceAll("\u012a", "I");
		temp = temp.replaceAll("\u012c", "I");
		temp = temp.replaceAll("\u012e", "I");
		temp = temp.replaceAll("\u0130", "I");
		temp = temp.replaceAll("\u01cf", "I");
		temp = temp.replaceAll("\u0208", "I");
		temp = temp.replaceAll("\u020a", "I");
		temp = temp.replaceAll("\u00ec", "i");
		temp = temp.replaceAll("\u00ed", "i");
		temp = temp.replaceAll("\u00ee", "i");
		temp = temp.replaceAll("\u00ef", "i");
		temp = temp.replaceAll("\u0129", "i");
		temp = temp.replaceAll("\u012b", "i");
		temp = temp.replaceAll("\u012d", "i");
		temp = temp.replaceAll("\u012f", "i");
		temp = temp.replaceAll("\u0131", "i");
		temp = temp.replaceAll("\u01d0", "i");
		temp = temp.replaceAll("\u0209", "i");
		temp = temp.replaceAll("\u020b", "i");
		temp = temp.replaceAll("\u0132", "IJ");
		temp = temp.replaceAll("\u0133", "ij");
		temp = temp.replaceAll("\u0134", "J");
		temp = temp.replaceAll("\u0135", "j");
		temp = temp.replaceAll("\u0136", "K");
		temp = temp.replaceAll("\u01e8", "K");
		temp = temp.replaceAll("\u0137", "k");
		temp = temp.replaceAll("\u0138", "k");
		temp = temp.replaceAll("\u01e9", "k");
		temp = temp.replaceAll("\u0139", "L");
		temp = temp.replaceAll("\u013b", "L");
		temp = temp.replaceAll("\u013d", "L");
		temp = temp.replaceAll("\u013f", "L");
		temp = temp.replaceAll("\u0141", "L");
		temp = temp.replaceAll("\u013a", "l");
		temp = temp.replaceAll("\u013c", "l");
		temp = temp.replaceAll("\u013e", "l");
		temp = temp.replaceAll("\u0140", "l");
		temp = temp.replaceAll("\u0142", "l");
		temp = temp.replaceAll("\u0234", "l");
		temp = temp.replaceAll("\u00d1", "N");
		temp = temp.replaceAll("\u0143", "N");
		temp = temp.replaceAll("\u0145", "N");
		temp = temp.replaceAll("\u0147", "N");
		temp = temp.replaceAll("\u014a", "N");
		temp = temp.replaceAll("\u01f8", "N");
		temp = temp.replaceAll("\u00f1", "n");
		temp = temp.replaceAll("\u0144", "n");
		temp = temp.replaceAll("\u0146", "n");
		temp = temp.replaceAll("\u0148", "n");
		temp = temp.replaceAll("\u0149", "n");
		temp = temp.replaceAll("\u014b", "n");
		temp = temp.replaceAll("\u01f9", "n");
		temp = temp.replaceAll("\u0235", "n");
		temp = temp.replaceAll("\u00d2", "O");
		temp = temp.replaceAll("\u00d3", "O");
		temp = temp.replaceAll("\u00d4", "O");
		temp = temp.replaceAll("\u00d5", "O");
		temp = temp.replaceAll("\u00d6", "O");
		temp = temp.replaceAll("\u00d8", "O");
		temp = temp.replaceAll("\u014c", "O");
		temp = temp.replaceAll("\u014e", "O");
		temp = temp.replaceAll("\u0150", "O");
		temp = temp.replaceAll("\u01d1", "O");
		temp = temp.replaceAll("\u01ea", "O");
		temp = temp.replaceAll("\u01ec", "O");
		temp = temp.replaceAll("\u01fe", "O");
		temp = temp.replaceAll("\u020c", "O");
		temp = temp.replaceAll("\u020e", "O");
		temp = temp.replaceAll("\u022a", "O");
		temp = temp.replaceAll("\u022c", "O");
		temp = temp.replaceAll("\u022e", "O");
		temp = temp.replaceAll("\u0230", "O");
		temp = temp.replaceAll("\u00f2", "o");
		temp = temp.replaceAll("\u00f3", "o");
		temp = temp.replaceAll("\u00f4", "o");
		temp = temp.replaceAll("\u00f5", "o");
		temp = temp.replaceAll("\u00f6", "o");
		temp = temp.replaceAll("\u00f8", "o");
		temp = temp.replaceAll("\u014d", "o");
		temp = temp.replaceAll("\u014f", "o");
		temp = temp.replaceAll("\u0151", "o");
		temp = temp.replaceAll("\u01d2", "o");
		temp = temp.replaceAll("\u01eb", "o");
		temp = temp.replaceAll("\u01ed", "o");
		temp = temp.replaceAll("\u01ff", "o");
		temp = temp.replaceAll("\u020d", "o");
		temp = temp.replaceAll("\u020f", "o");
		temp = temp.replaceAll("\u022b", "o");
		temp = temp.replaceAll("\u022d", "o");
		temp = temp.replaceAll("\u022f", "o");
		temp = temp.replaceAll("\u0231", "o");
		temp = temp.replaceAll("\u0156", "R");
		temp = temp.replaceAll("\u0158", "R");
		temp = temp.replaceAll("\u0210", "R");
		temp = temp.replaceAll("\u0212", "R");
		temp = temp.replaceAll("\u0157", "r");
		temp = temp.replaceAll("\u0159", "r");
		temp = temp.replaceAll("\u0211", "r");
		temp = temp.replaceAll("\u0213", "r");
		temp = temp.replaceAll("\u015a", "S");
		temp = temp.replaceAll("\u015c", "S");
		temp = temp.replaceAll("\u015e", "S");
		temp = temp.replaceAll("\u0160", "S");
		temp = temp.replaceAll("\u0218", "S");
		temp = temp.replaceAll("\u015b", "s");
		temp = temp.replaceAll("\u015d", "s");
		temp = temp.replaceAll("\u015f", "s");
		temp = temp.replaceAll("\u0161", "s");
		temp = temp.replaceAll("\u0219", "s");
		temp = temp.replaceAll("\u00de", "T");
		temp = temp.replaceAll("\u0162", "T");
		temp = temp.replaceAll("\u0164", "T");
		temp = temp.replaceAll("\u0166", "T");
		temp = temp.replaceAll("\u021a", "T");
		temp = temp.replaceAll("\u00fe", "t");
		temp = temp.replaceAll("\u0163", "t");
		temp = temp.replaceAll("\u0165", "t");
		temp = temp.replaceAll("\u0167", "t");
		temp = temp.replaceAll("\u021b", "t");
		temp = temp.replaceAll("\u0236", "t");
		temp = temp.replaceAll("\u00d9", "U");
		temp = temp.replaceAll("\u00da", "U");
		temp = temp.replaceAll("\u00db", "U");
		temp = temp.replaceAll("\u00dc", "U");
		temp = temp.replaceAll("\u0168", "U");
		temp = temp.replaceAll("\u016a", "U");
		temp = temp.replaceAll("\u016c", "U");
		temp = temp.replaceAll("\u016e", "U");
		temp = temp.replaceAll("\u0170", "U");
		temp = temp.replaceAll("\u0172", "U");
		temp = temp.replaceAll("\u01d3", "U");
		temp = temp.replaceAll("\u01d5", "U");
		temp = temp.replaceAll("\u01d7", "U");
		temp = temp.replaceAll("\u01d9", "U");
		temp = temp.replaceAll("\u01db", "U");
		temp = temp.replaceAll("\u0214", "U");
		temp = temp.replaceAll("\u0216", "U");
		temp = temp.replaceAll("\u00f9", "u");
		temp = temp.replaceAll("\u00fa", "u");
		temp = temp.replaceAll("\u00fb", "u");
		temp = temp.replaceAll("\u00fc", "u");
		temp = temp.replaceAll("\u0169", "u");
		temp = temp.replaceAll("\u016b", "u");
		temp = temp.replaceAll("\u016d", "u");
		temp = temp.replaceAll("\u016f", "u");
		temp = temp.replaceAll("\u0171", "u");
		temp = temp.replaceAll("\u0173", "u");
		temp = temp.replaceAll("\u01d4", "u");
		temp = temp.replaceAll("\u01d6", "u");
		temp = temp.replaceAll("\u01d8", "u");
		temp = temp.replaceAll("\u01da", "u");
		temp = temp.replaceAll("\u01dc", "u");
		temp = temp.replaceAll("\u0215", "u");
		temp = temp.replaceAll("\u0217", "u");
		temp = temp.replaceAll("\u0174", "W");
		temp = temp.replaceAll("\u0175", "w");
		temp = temp.replaceAll("\u00dd", "Y");
		temp = temp.replaceAll("\u0176", "Y");
		temp = temp.replaceAll("\u0178", "Y");
		temp = temp.replaceAll("\u0232", "Y");
		temp = temp.replaceAll("\u00fd", "y");
		temp = temp.replaceAll("\u00ff", "y");
		temp = temp.replaceAll("\u0177", "y");
		temp = temp.replaceAll("\u0233", "y");
		temp = temp.replaceAll("\u0179", "Z");
		temp = temp.replaceAll("\u017b", "Z");
		temp = temp.replaceAll("\u017d", "Z");
		temp = temp.replaceAll("\u017a", "z");
		temp = temp.replaceAll("\u017c", "z");
		temp = temp.replaceAll("\u017e", "z");
		temp = temp.replaceAll("\u00df", "ss");
		return temp;
	}

	/**
	 * Sort a map by values
	 * @param map list of key, value entries
	 * @return map sorted by values
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			// a comparator using generic type
			@Override
			public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
				// Handle null values
				if (e1.getValue() == null) {
					return (e2.getValue() == null) ? 0 : -1;
				}
				if (e2.getValue() == null) {
					return 1;
				}
				return (e1.getValue()).compareTo(e2.getValue());
			}
		});
		Map<K, V> result = new LinkedHashMap<>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	/**
	 * return the first operator of given text.
	 * see operators class variable to know all available operators.
	 * @param text string value
	 * @return the first operator or n character means no result was found.
	 */
	public static char FIRST_OPERATOR(String text){
		int co=0;
		while(co<text.length()){
			for(char character: OPERATORS){
				if(text.charAt(co) == character)
					return character;
			}
			co++;
		}
		return 'n';
	}


	/**
	 * Static method for removing the special characters declared in
	 * @param part text on witch we would like to remove operands.
	 * @return the new part without the operands.
	 */
	public static String REMOVE_ARITH_OPERANDS(String part){
		for(char character: ARITH_OPERANDS){
			if(part.contains(character+""))
				part= part.replaceAll("\\"+character, "");
		}
		return part;
	}

	/**
	 * Get the value of given property  for multiline entity instance.
	 * @param instance multiline entity object reference.
	 * @param field field object see Reflection API
	 * @return the property full name if the requested value equals to null.
	 * if the requested property value !equals to null then the result will be an empty string
	 * witch means the property result is already calculated.
	 * @throws IllegalArgumentException if the requested value equals to null
	 * @throws IllegalAccessException for security purpose. @see generics API
	 */
	public static Object VALUE_BY_FIELD(Object instance, Field field) throws IllegalArgumentException, IllegalAccessException{
		field.setAccessible(true);
		return field.get(instance) != null ? field.get(instance) : new String("0");
	}

	/**
	 *
	 * @param instance multiline entity object ref or another object with JPA annotations
	 * @param fieldName declared field name uses
	 * @return field generic value
	 * @throws IllegalArgumentException if java reflection API can't find and get the field by it's simple name
	 * @throws IllegalAccessException right for accessing given field
	 * @throws NoSuchFieldException if the field doesn't exist.
	 */
	public static Object VALUE_BY_FIELD(Object instance, String fieldName) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException{
		try {
			Field field= instance.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(instance);
		}catch (NoSuchFieldException ex) {
			LogManager.getLogger(Util.class.getSimpleName()).warn(ex.getClass()+", "+ex.getMessage());
		} catch (SecurityException ex) {
			LogManager.getLogger(Util.class.getSimpleName()).warn(ex.getClass()+", "+ex.getMessage());
		} catch (IllegalArgumentException ex) {
			LogManager.getLogger(Util.class.getSimpleName()).warn(ex.getClass()+", "+ex.getMessage());
		} catch (IllegalAccessException ex) {
			LogManager.getLogger(Util.class.getSimpleName()).warn(ex.getClass()+", "+ex.getMessage());
		}
		return null;
	}

	/**
	 *
	 * @param instance multiline entity reference
	 * @param propertyName full property name declared in the .java file
	 * @return field object @see reflection API
	 * @throws NoSuchFieldException if the requested field don't match at least one instance variable.
	 */
	public static Field FIELD_BY_PROPERTY_NAME(Object instance, String propertyName) throws NoSuchFieldException{
		return instance.getClass().getDeclaredField(propertyName);
	}

	/**
	 *
	 * @param instance multiline entity reference
	 * @param expression the parsed expression indicating the first property name,
	 * on witch we would like to put the final result
	 * @return util.Field first property field object
	 * @throws NoSuchFieldException if the field doesn't exist.
	 */
	public static Field LEFT_FIELD_BY_GIVEN_EXP(Object instance, String expression) throws NoSuchFieldException{
		return instance.getClass().getDeclaredField(expression.split("\\"+ARITH_OPERANDS[2] )[0].trim() );
	}

	/**
	 *
	 * @param instance multiline entity reference
	 * @param expression the parsed expression indicating the first property name,
	 * on witch we would like to put the final result
	 * @return util.Field first property field object
	 * @throws NoSuchFieldException if the field doesn't exist.
	 */
	public static String RIGHT_RESULT_BY_GIVEN_EXP(Object instance, String expression) throws NoSuchFieldException{
		return expression.split("\\"+ARITH_OPERANDS[2] )[1] ;
	}
}
