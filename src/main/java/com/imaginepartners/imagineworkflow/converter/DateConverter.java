package com.imaginepartners.imagineworkflow.converter;

import com.imaginepartners.imagineworkflow.util.AppConstants;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.apache.log4j.LogManager;
import org.springframework.stereotype.Component;

@FacesConverter("dateConverter")
@Component
public class DateConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		String format = (String) component.getAttributes().get(AppConstants.DATE_CONVERTER_FORMAT_ATTRIBUTE);
		if (value == null) {
			return null;
		}
		try {
			return new SimpleDateFormat(format).parse(value);
		} catch (ParseException ex) {
			LogManager.getLogger(DateConverter.class.getName()).error(null, ex);
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		String format = (String) component.getAttributes().get(AppConstants.DATE_CONVERTER_FORMAT_ATTRIBUTE);
		String timezone = (String) component.getAttributes().get(AppConstants.DATE_CONVERTER_TIMEZONE_ATTRIBUTE);
		if (value == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if(timezone == null) {
			timezone = AppConstants.DEFAULT_TIMEZONE;
		}
		sdf.setTimeZone(TimeZone.getTimeZone(timezone));
		return sdf.format((Date) value);
	}
}
