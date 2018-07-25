package com.imaginepartners.imagineworkflow.converter;

import org.apache.log4j.LogManager;
import org.springframework.stereotype.Component;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@FacesConverter("timeConverter")
@Component
public class TimeConverter implements Converter {

	private DateFormat formatter = new SimpleDateFormat("HH:mm:ss");

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		try {
			return new Time(formatter.parse(value).getTime());
		} catch (ParseException ex) {
			LogManager.getLogger(TimeConverter.class.getName()).error(null, ex);
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value == null) {
			return null;
		}
		return new SimpleDateFormat("HH:mm:ss").format((Time) value);
	}
}
