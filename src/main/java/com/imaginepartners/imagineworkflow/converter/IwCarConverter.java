package com.imaginepartners.imagineworkflow.converter;

import com.imaginepartners.imagineworkflow.models.IwCar;
import com.imaginepartners.imagineworkflow.util.AppConstants;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.springframework.stereotype.Component;

@FacesConverter("iwCarConverter")
@Component
public class IwCarConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		List<IwCar> carsList = (List<IwCar>) component.getAttributes().get(AppConstants.CONVERTER_ITEMS_LIST);
		for (IwCar car : carsList) {
			if (value.equals(String.valueOf(car.getIwCarId()))) {
				return car;
			}
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value == null) {
			return null;
		}
		return String.valueOf(((IwCar) value).getIwCarId());
	}
}
