package com.imaginepartners.imagineworkflow.converter;

import com.imaginepartners.imagineworkflow.models.IwPhase;
import com.imaginepartners.imagineworkflow.util.AppConstants;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.springframework.stereotype.Component;

@FacesConverter("phaseConverter")
@Component
public class PhaseConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		List<IwPhase> phaseList = (List<IwPhase>) component.getAttributes().get(AppConstants.CONVERTER_ITEMS_LIST);
		for (IwPhase p : phaseList) {
			if (value.equals(String.valueOf(p.getIwPhaseId()))) {
				return p;
			}
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			return String.valueOf(((IwPhase) value).getIwPhaseId());
		}
		return null;
	}
}
