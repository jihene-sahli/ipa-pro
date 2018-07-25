package com.imaginepartners.imagineworkflow.converter;

import com.imaginepartners.imagineworkflow.models.IwVariableProcess;
import com.imaginepartners.imagineworkflow.util.AppConstants;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.springframework.stereotype.Component;

@FacesConverter("variableProcessConverter")
@Component
public class VariableProcessConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        List<IwVariableProcess> list = (List<IwVariableProcess>) component.getAttributes().get(AppConstants.CONVERTER_ITEMS_LIST);
        if (list != null && !list.isEmpty()) {
            for (IwVariableProcess varPocess : list) {
                if (varPocess.getIwVariableProcessId() != null && varPocess.getIwVariableProcessId().compareTo(new Long(value)) == 0) {
                    return varPocess;
                }
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            if (value instanceof IwVariableProcess) {
                return String.valueOf(((IwVariableProcess) value).getIwVariableProcessId());
            }
            if (value instanceof Long) {
                return String.valueOf(((Long) value));
            }
        }
        return null;
    }
}
