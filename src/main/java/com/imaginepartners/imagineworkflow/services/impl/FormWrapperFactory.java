package com.imaginepartners.imagineworkflow.services.impl;

import com.imaginepartners.imagineworkflow.form.FormWrapper;
import com.imaginepartners.imagineworkflow.form.wrapper.impl.AddEntityFormWrapper;
import com.imaginepartners.imagineworkflow.form.wrapper.impl.ConditionalSelectFormWrapper;
import com.imaginepartners.imagineworkflow.form.wrapper.impl.DateFormWrapper;
import com.imaginepartners.imagineworkflow.form.wrapper.impl.DefaultFormWrapper;
import com.imaginepartners.imagineworkflow.form.wrapper.impl.DoubleCheckListFormWrapper;
import com.imaginepartners.imagineworkflow.form.wrapper.impl.DynamicTextFormWrapper;
import com.imaginepartners.imagineworkflow.form.wrapper.impl.GroupFormWrapper;
import com.imaginepartners.imagineworkflow.form.wrapper.impl.MultilineEntityConfigFormWrapper;
import com.imaginepartners.imagineworkflow.form.wrapper.impl.MultilineEntityFormWrapper;
import com.imaginepartners.imagineworkflow.form.wrapper.impl.MultilineFormWrapper;
import com.imaginepartners.imagineworkflow.form.wrapper.impl.PickListFormWrapper;
import com.imaginepartners.imagineworkflow.form.wrapper.impl.RegisterFormWrapper;
import com.imaginepartners.imagineworkflow.form.wrapper.impl.StaticTextFormWrapper;
import com.imaginepartners.imagineworkflow.form.wrapper.impl.TreeCheckListFormWrapper;
import com.imaginepartners.imagineworkflow.form.wrapper.impl.UploadFormWrapper;
import com.imaginepartners.imagineworkflow.form.wrapper.impl.UserFormWrapper;
import com.imaginepartners.imagineworkflow.models.IwInput;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.util.FormContants;
import org.activiti.engine.form.FormProperty;
import org.springframework.stereotype.Service;

@Service
public class FormWrapperFactory {

	public FormWrapper createFormWrapper(FormProperty form, Object value) {
		return new DefaultFormWrapper(form, value);
	}

	public FormWrapper createFormWrapper(IwInput iwInput, Object value, boolean isReadable, boolean isWritable,
										 BusinessService businessService, ActivitiService activitiService, String taskId, String procInstId) {
		if (FormContants.MULTI_LINE_ENTITY_CONFIG_FORM_TYPE_NAME.equals(iwInput.getComponent())) {
			return new MultilineEntityConfigFormWrapper(iwInput, value, isReadable, isWritable, businessService, activitiService, taskId, procInstId);
		} else if (FormContants.MULTI_LINE_ENTITY_FORM_TYPE_NAME.equals(iwInput.getComponent())) {
			return new MultilineEntityFormWrapper(iwInput, value, isReadable, isWritable, businessService, activitiService, taskId, procInstId);
		} else if (FormContants.STATIC_TEXT_FORM_TYPE_NAME.equals(iwInput.getComponent())) {
			return new StaticTextFormWrapper(iwInput, value, isReadable, isWritable, businessService, activitiService, taskId, procInstId);
		} else if (FormContants.MULTI_LINE_FORM_TYPE_NAME.equals(iwInput.getComponent())) {
			return new MultilineFormWrapper(iwInput, value, isReadable, isWritable, businessService, activitiService, taskId, procInstId);
		} else if (FormContants.CONDITIONAL_SELECT_FORM_TYPE_NAME.equals(iwInput.getComponent())) {
			return new ConditionalSelectFormWrapper(iwInput, value, isReadable, isWritable, businessService, activitiService, taskId, procInstId);
		} else if (FormContants.ADD_ENTITY_FORM_TYPE_NAME.equals(iwInput.getComponent())) {
			return new AddEntityFormWrapper(iwInput, value, isReadable, isWritable, businessService, activitiService, taskId, procInstId);
		} else if (FormContants.TREE_CHECK_LIST_FORM_TYPE_NAME.equals(iwInput.getComponent())) {
			return new TreeCheckListFormWrapper(iwInput, value, isReadable, isWritable, businessService, activitiService, taskId, procInstId);
		} else if (FormContants.USER_FORM_TYPE_NAME.equals(iwInput.getComponent())) {
			return new UserFormWrapper(iwInput, value, isReadable, isWritable, businessService, activitiService, taskId, procInstId);
		} else if (FormContants.GROUP_FORM_TYPE_NAME.equals(iwInput.getComponent())) {
			return new GroupFormWrapper(iwInput, value, isReadable, isWritable, businessService, activitiService, taskId, procInstId);
		} else if (FormContants.PICKLIST_FORM_TYPE_NAME.equals(iwInput.getComponent())
		|| FormContants.PICKLIST_IDENTITY_FORM_TYPE_NAME.equals(iwInput.getComponent())) {
			return new PickListFormWrapper(iwInput, value, isReadable, isWritable, businessService, activitiService, taskId, procInstId);
		} else if (FormContants.DATE_FORM_TYPE_NAME.equals(iwInput.getComponent())) {
			return new DateFormWrapper(iwInput, value, isReadable, isWritable, businessService, activitiService, taskId, procInstId);
		} else if (FormContants.DOUBLE_CHECK_LIST_FORM_TYPE_NAME.equals(iwInput.getComponent())) {
			return new DoubleCheckListFormWrapper(iwInput, value, isReadable, isWritable, businessService, activitiService, taskId, procInstId);
		} else if (FormContants.DYNAMIC_TEXT_FORM_TYPE_NAME.equals(iwInput.getComponent())) {
			return new DynamicTextFormWrapper(iwInput, value, isReadable, isWritable, businessService, activitiService, taskId, procInstId);
		} else if (FormContants.FILE_INPUT_FORM_TYPE_NAME.equals(iwInput.getComponent())) {
			return new UploadFormWrapper(iwInput, value, isReadable, isWritable, businessService, activitiService, taskId, procInstId);
		} else if (FormContants.REGISTER_FORM_TYPE_NAME.equals(iwInput.getComponent())) {
			return new RegisterFormWrapper(iwInput, value, isReadable, isWritable, businessService, activitiService, taskId, procInstId);
		} else
		if(FormContants.SELECT_ONE_MENU_NAME.toLowerCase().equals(iwInput.getComponent().toLowerCase()) && !iwInput.getOptions().isEmpty() && value == null ) {
			// We know that required value of select compoenent always initialized from the form builder even with false value.
			if(iwInput.getRequired())
				return new DefaultFormWrapper(iwInput, iwInput.getOptions().get(0), isReadable, isWritable, businessService, activitiService, taskId, procInstId);
			else
				return new DefaultFormWrapper(iwInput, null, isReadable, isWritable, businessService, activitiService, taskId, procInstId);
		} else {
			return new DefaultFormWrapper(iwInput, value, isReadable, isWritable, businessService, activitiService, taskId, procInstId);
		}
	}
}
