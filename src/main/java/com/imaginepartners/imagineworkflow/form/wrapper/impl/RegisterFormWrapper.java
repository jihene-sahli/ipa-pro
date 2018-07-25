package com.imaginepartners.imagineworkflow.form.wrapper.impl;

import com.imaginepartners.imagineworkflow.form.FormWrapper;
import com.imaginepartners.imagineworkflow.models.IwInput;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.util.FormContants;
import java.util.Date;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.LogManager;

public class RegisterFormWrapper extends FormWrapper {

	public RegisterFormWrapper(IwInput iwInput, Object value, boolean isReadable, boolean isWritable, BusinessService businessService, ActivitiService activitiService, String taskId, String procInstId) {
		super(iwInput, value, isReadable, isWritable, businessService, activitiService, taskId, procInstId);
		if (this.value == null) {
			this.value = this.iwInput.getValue();
			if (this.iwInput.getOptions() != null && !this.iwInput.getOptions().isEmpty()) {
				if (FormContants.REGISTER_FORM_TYPE_TIMESTAMP_NAME.equals(this.iwInput.getOptions().get(0))) {
					this.value += String.valueOf(new Date().getTime());
				} else if (FormContants.REGISTER_FORM_TYPE_RANDOM_STRING_NAME.equals(this.iwInput.getOptions().get(0))) {
					this.value += RandomStringUtils.randomAlphabetic(6);
				} else if (FormContants.REGISTER_FORM_TYPE_RANDOM_NUM_NAME.equals(this.iwInput.getOptions().get(0))) {
					this.value += RandomStringUtils.randomNumeric(6);
				} else if (FormContants.REGISTER_FORM_TYPE_NUM_INST_NAME.equals(this.iwInput.getOptions().get(0))) {
					this.value += procInstId;
				} else if (FormContants.REGISTER_FORM_TYPE_NUM_TASK_NAME.equals(this.iwInput.getOptions().get(0))) {
					this.value += taskId;
				} else if (FormContants.REGISTER_FORM_TYPE_NUM_AUTO_INC_NAME.equals(this.iwInput.getOptions().get(0))) {
					if (this.iwInput.getOptions2() != null && !this.iwInput.getOptions2().isEmpty()) {
						this.value += String.valueOf(businessService.getRegistreNextVal(this.iwInput.getOptions2().get(0)));
					}
				}
			}
			if (this.iwInput.getOptions3() != null && !this.iwInput.getOptions3().isEmpty()) {
				this.value += this.iwInput.getOptions3().get(0);
			}
		}
	}

	public Object getValue() {
		return this.value;
	}

	@Override
	public void persiste(BusinessService businessService, ActivitiService activitiService) {

	}
}
