package com.imaginepartners.imagineworkflow.util;

import com.imaginepartners.imagineworkflow.context.IwContext;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

public final class FacesUtil {

	private static final String IW_SESSION_MESSAGE = "IW_SESSION_MESSAGE";

	private static final String IW_SESSION_MESSAGE_SEVERITY = "IW_SESSION_MESSAGE_SEVERITY";

	private static final String ELEMENT_TO_UPDATE = "element_to_update";

	private static String MULTILINE_ENTITY_CLIENT_EXP;

	public static HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance().
		getExternalContext()
		.getRequest();
	}

	public static void redirect(String url) {
		try {
			FacesContext.getCurrentInstance().
			getExternalContext()
			.redirect(url);
		} catch (IOException ex) {
			LogManager.getLogger(FacesUtil.class).error("redirect", ex);
		}
	}

	public static String getUrlParam(String param) {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		return request.getParameter(param);
	}

	public static String getViewId() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		return facesContext.getViewRoot().getViewId();
	}

	public static Locale getClientLocale() {
		return FacesContext.getCurrentInstance().getViewRoot().getLocale();
	}

	public static void setSessionErrorMessage(String exMessage) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(IW_SESSION_MESSAGE, exMessage);
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(IW_SESSION_MESSAGE_SEVERITY, FacesMessage.SEVERITY_ERROR);
	}

	public static void setSessionInfoMessage(String exMessage) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(IW_SESSION_MESSAGE, exMessage);
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(IW_SESSION_MESSAGE_SEVERITY, FacesMessage.SEVERITY_INFO);
	}

	public static void setSessionWarnMessage(String exMessage) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(IW_SESSION_MESSAGE, exMessage);
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(IW_SESSION_MESSAGE_SEVERITY, FacesMessage.SEVERITY_WARN);
	}

	public static void reinitSessionMessage() {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(IW_SESSION_MESSAGE, null);
	}

	public static void loadSessionMessages(boolean update) throws Exception {
		String exMessage = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(IW_SESSION_MESSAGE);
		Severity severity = (Severity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(IW_SESSION_MESSAGE_SEVERITY);
		if (exMessage != null) {
			FacesContext.getCurrentInstance().addMessage(null,
			new FacesMessage(severity, exMessage, null));
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(IW_SESSION_MESSAGE, null);
		}
	}

	public static void setAjaxInfoMessage(String exMessage) {
		FacesContext.getCurrentInstance().addMessage(null,
		new FacesMessage(FacesMessage.SEVERITY_INFO, exMessage, null));

	}

	public static void setAjaxErrorMessage(String elementId, String exMessage) {
		FacesContext.getCurrentInstance().addMessage(elementId,
		new FacesMessage(FacesMessage.SEVERITY_ERROR, exMessage, null));
	}

	public static void setAjaxInfoMessage(String elementId, String exMessage) {
		FacesContext.getCurrentInstance().addMessage(elementId,
		new FacesMessage(FacesMessage.SEVERITY_INFO, exMessage, null));
	}

	public static void setAjaxErrorMessage(String exMessage) {
		FacesContext.getCurrentInstance().addMessage(null,
		new FacesMessage(FacesMessage.SEVERITY_ERROR, exMessage, null));
	}

	public static void setAjaxWarnMessage(String exMessage) {
		FacesContext.getCurrentInstance().addMessage(null,
		new FacesMessage(FacesMessage.SEVERITY_WARN, exMessage, null));
	}

	public static void setAjaxWarnMessage(String elementId, String exMessage) {
		FacesContext.getCurrentInstance().addMessage(elementId,
		new FacesMessage(FacesMessage.SEVERITY_WARN, exMessage, null));
	}

	public static String getMessage(String key, String... param) {
		try {
			ResourceBundle msg = FacesContext.getCurrentInstance().getApplication().getResourceBundle(FacesContext.getCurrentInstance(), "msg");
			String text = msg.getString(key);
			return MessageFormat.format(text, (Object[]) param);
		} catch (Exception ex) {
			LogManager.getLogger(FacesUtil.class.getName()).error("", ex);
			return "??" + key + "??";
		}
	}

	public static String getWebAppDirectory() {
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		String realPath = servletContext.getRealPath(File.separator);
		realPath = realPath.substring(0, realPath.length() - servletContext.getContextPath().length() - 1);
		realPath = realPath.substring(0, realPath.lastIndexOf(File.separator) + 1);
		return realPath;
	}

	public static String getContextPath() {
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		return servletContext.getContextPath();
	}

	public static String getRealPath(String path) {
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		return servletContext.getRealPath(path);
	}

	public static TimeZone getTimeZone() {
		return RequestContextUtils.getTimeZone((HttpServletRequest) FacesContext.getCurrentInstance().
		getExternalContext()
		.getRequest());
	}

	public static void updateElement(String id) {
		RequestContext context = RequestContext.getCurrentInstance();
		context.update(id);
	}

	public static void setSessionUpdateElement(String elementId) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(ELEMENT_TO_UPDATE, elementId);
	}

	public static void updateSessionElements() {
		String elementId = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(ELEMENT_TO_UPDATE);
		if (elementId != null) {
			updateElement(elementId);
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(ELEMENT_TO_UPDATE, null);
		}
		updateElement(elementId);
	}

	public static String evalAsString(String expression) {
		FacesContext context = FacesContext.getCurrentInstance();
		ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
		ELContext elContext = context.getELContext();
		ValueExpression vex = expressionFactory.createValueExpression(elContext, expression, String.class);
		String result = "";
		try {
			result = (String) vex.getValue(elContext);
			Logger.getLogger(FacesUtil.class).error("elContext"+elContext);
		} catch (NumberFormatException numberFormatException) {
			LogManager.getLogger(FacesUtil.class).error(numberFormatException);
		}
		return result;
	}

	public static List<String> evalAsString(ArrayList<String> entityFilter) {
		FacesContext context = FacesContext.getCurrentInstance();
		ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
		ELContext elContext = context.getELContext();
		List result = new ArrayList<String>();
		for (String expression : entityFilter) {
			ValueExpression vex = expressionFactory.createValueExpression(elContext, expression, String.class);
			try {
				result.add(vex.getValue(elContext).toString());
			} catch (NumberFormatException numberFormatException) {
				Logger.getLogger(FacesUtil.class).error("cant convert given id's values to Numbers");
			}
		}
		return result;
	}

	/**
	 * This is a custom EL implementation.
	 * @param listValues list of values to be evaluated.
	 * @return the evaluated list after parsing them using spring expression language.
	 */
	public static ArrayList<String> spElEval(ArrayList<String> listValues){
		StandardEvaluationContext standardEvaluationContext= IwContext.getStandardEvaluationContext(FacesUtil.getUrlParam("task"));
		ExpressionParser parser= new SpelExpressionParser();
		ArrayList<String> evaluatedValues= new ArrayList<String>();
		for(String entry: listValues){
			Expression endUserExpression;
			try{
				endUserExpression= parser.parseExpression(entry);
				evaluatedValues.add(endUserExpression.getValue(standardEvaluationContext, String.class));
			}catch(NullPointerException nullPointerExceprion){
				LogManager.getLogger(FacesUtil.class).error("null pointer exception", nullPointerExceprion);
				evaluatedValues.add(entry);
			}catch(ParseException parseException){
				LogManager.getLogger(FacesUtil.class).error("can't parse given text: "+entry, parseException);
				evaluatedValues.add(entry);
			}
		}
		return evaluatedValues;
	}

	/**
	 * Calculate mathematical expression and put the result in the appropriate place.
	 * @param instance multiline entity object ref.
	 * @param expression mathematical expression
	 * @return updated multiline entity instance
	 */
	public static Object EVAL_MULTILINE_ENTITY_COLUMN(Object instance, String expression){
		try {
			Field field= Util.LEFT_FIELD_BY_GIVEN_EXP(instance, expression);
			field.setAccessible(true);
			String resultPropertyName= field.getName();
			// Save a ref for the full client expression.
			MULTILINE_ENTITY_CLIENT_EXP= new String(expression.replaceFirst(resultPropertyName, StringUtils.SPACE));
			// Holding the first part of the exp
			// First part is the name of the property that holds the final result.
			MULTILINE_ENTITY_CLIENT_EXP= resultPropertyName  +
			PARSE_EXPRESSION(instance, MULTILINE_ENTITY_CLIENT_EXP);
			String parsedValue= "";
			if(FacesContext.getCurrentInstance() != null) {
				parsedValue= evalAsString(Util.RIGHT_RESULT_BY_GIVEN_EXP(instance, MULTILINE_ENTITY_CLIENT_EXP));
			} else {
				ScriptEngineManager scriptManager= new ScriptEngineManager();
				ScriptEngine engine= scriptManager.getEngineByName("JavaScript");
				try {
					parsedValue= engine.eval(Util.RIGHT_RESULT_BY_GIVEN_EXP(instance, MULTILINE_ENTITY_CLIENT_EXP) ).toString();
					if(parsedValue.toLowerCase().contains("infinity")) {
						LogManager.getLogger(FacesUtil.class.getSimpleName())
						.error("can't evaluate expression exp="+parsedValue, new IllegalArgumentException().getCause());
						parsedValue= "0";
					}
				} catch (ScriptException ex) {
					LogManager.getLogger(FacesUtil.class.getSimpleName())
					.warn(ex.getClass().getSimpleName()+", "+ex.getMessage());
				}
			}
			Class<?> clazz= field.getType();
			if(clazz.isAssignableFrom(String.class))
				field.set(instance, parsedValue);
			if(clazz.isAssignableFrom(Integer.class))
				field.set(instance, Double.valueOf(parsedValue).intValue() );
			if(clazz.isAssignableFrom(Float.class))
				field.set(instance, Float.valueOf(parsedValue));
			if(clazz.isAssignableFrom(Long.class))
				field.set(instance, Double.valueOf(parsedValue).longValue());
			if(clazz.isAssignableFrom(Double.class))
				field.set(instance, Double.valueOf(parsedValue));
			if(clazz.isAssignableFrom(Boolean.class))
				field.set(instance, Boolean.valueOf(parsedValue));
			return instance;
		} catch (NoSuchFieldException ex) {
			LogManager.getLogger(FacesUtil.class.getSimpleName())
			.warn(ex.getClass()+", "+ex.getMessage());
		} catch (IllegalArgumentException ex) {
			LogManager.getLogger(FacesUtil.class.getSimpleName())
			.warn(ex.getClass()+", "+ex.getMessage());
		} catch (IllegalAccessException ex) {
			LogManager.getLogger(FacesUtil.class.getSimpleName())
			.warn(ex.getClass()+", "+ex.getMessage());
		}
		return instance;
	}

	/**
	 * This method stands for parsing the client expression toward an executable expression.
	 * like for example: property_name= 5+ 15= 20.
	 * the value will be saved within the static variable: @see MULTILINE_ENTITY_CLIENT_EXP
	 * @param multilineEntityInstance Multiline entity reference.
	 * @param expression in order to create a recursive method,
	 * we must provide one extra argument to hold the newest values of client expression
	 */
	public static String PARSE_EXPRESSION(Object multilineEntityInstance, String expression){
		char firstCharacter= Util.FIRST_OPERATOR(expression);
		String firstPart= "", secondPart= "";
		try{
			firstPart= expression.split("\\"+firstCharacter)[0].trim();
			try {
				if(firstCharacter != Util.ARITH_OPERANDS[2])
					MULTILINE_ENTITY_CLIENT_EXP = MULTILINE_ENTITY_CLIENT_EXP
					.replaceFirst(Util.REMOVE_ARITH_OPERANDS(firstPart), Util.VALUE_BY_FIELD(multilineEntityInstance,
					Util.FIELD_BY_PROPERTY_NAME(multilineEntityInstance, Util.REMOVE_ARITH_OPERANDS(firstPart))   ).toString()
					);
			} catch (IllegalArgumentException ex) {
				LogManager.getLogger(FacesUtil.class.getSimpleName()).warn(ex.getClass().getSimpleName()+", "+ex.getMessage());
			} catch (IllegalAccessException ex) {
				LogManager.getLogger(FacesUtil.class.getSimpleName()).warn(ex.getClass().getSimpleName()+", "+ex.getMessage());
			} catch (NoSuchFieldException ex) {
				LogManager.getLogger(FacesUtil.class.getSimpleName()).warn(ex.getClass().getSimpleName()+", "+ex.getMessage());
			}
			secondPart= expression.split("\\"+firstCharacter+"")[1].trim();
			secondPart= expression.substring(expression.indexOf(secondPart));
			if(secondPart.trim().length() >1)
				PARSE_EXPRESSION(multilineEntityInstance, secondPart);
		} catch(ArrayIndexOutOfBoundsException arrayIndexOutOfBoundException) {
			LogManager.getLogger(FacesUtil.class.getSimpleName()).warn(arrayIndexOutOfBoundException.getClass().getSimpleName()
			+", "+arrayIndexOutOfBoundException.getMessage());
		}
		return MULTILINE_ENTITY_CLIENT_EXP;
	}

	public static void registerELResolver(ELResolver elResolver) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.getApplication().addELResolver(elResolver);
	}
}
