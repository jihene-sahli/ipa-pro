package com.imaginepartners.imagineworkflow.validator;

import com.imaginepartners.imagineworkflow.util.AppConstants;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.RegexValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@FacesValidator
@Component
public class ExtendedRegexValidator extends RegexValidator {

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) {
		String pattern = (String) component.getAttributes().get(AppConstants.REGEX_VALIDATOR_PATTERN);
		if (StringUtils.isNotEmpty(pattern)) {
			setPattern(pattern);
			super.validate(context, component, value);
		}
	}
}
