package com.imaginepartners.imagineworkflow.converter;

import com.imaginepartners.imagineworkflow.models.IwTree;
import com.imaginepartners.imagineworkflow.util.AppConstants;
import java.util.ArrayList;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@FacesConverter("iwTreeConverter")
@Component
public class IwTreeConverter implements Converter {
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		}
		List<IwTree> iwTree = (ArrayList<IwTree>) component.getAttributes().get(AppConstants.CONVERTER_ITEMS_LIST);
		for (IwTree tree : iwTree) {
			if (value.equals(String.valueOf(tree.getChild()) ) ) {
				return tree;
			}
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value == null) {
			return null;
		}
		return String.valueOf(((IwTree) value).getChild());
	}
}
