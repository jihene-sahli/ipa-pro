package com.imaginepartners.imagineworkflow.form.wrapper.impl;

import com.imaginepartners.imagineworkflow.form.*;
import com.imaginepartners.imagineworkflow.models.IwInput;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import java.util.ArrayList;
import java.util.List;
import org.activiti.engine.form.FormProperty;
import org.apache.commons.collections.CollectionUtils;

public class DoubleCheckListFormWrapper extends FormWrapper {

	private List<String> doublCheckList1;

	private List<String> doublCheckList2;

	public DoubleCheckListFormWrapper(FormProperty form, Object value) {
		super(form, value);
	}

	public DoubleCheckListFormWrapper(IwInput iwInput, Object value, boolean isReadable, boolean isWritable, BusinessService businessService, ActivitiService activitiService, String taskId, String procInstId) {
		super(iwInput, value, isReadable, isWritable, businessService, activitiService, taskId, procInstId);
		if (this.value == null) {
			doublCheckList1 = new ArrayList<String>();
			doublCheckList2 = new ArrayList<String>();
		} else {
			doublCheckList1 = (List<String>) CollectionUtils.intersection((ArrayList<String>) this.value, iwInput.getOptions());
			doublCheckList2 = (List<String>) CollectionUtils.intersection((ArrayList<String>) this.value, iwInput.getOptions2());
		}
	}

	@Override
	public Object getValue() {
		value = (List<String>) CollectionUtils.union(doublCheckList1, doublCheckList2);
		return this.value;
	}

	@Override
	public void persiste(BusinessService businessService, ActivitiService activitiService) {
	}

	public List<String> getDoublCheckList1() {
		return doublCheckList1;
	}

	public void setDoublCheckList1(List<String> doublCheckList1) {
		this.doublCheckList1 = doublCheckList1;
	}

	public List<String> getDoublCheckList2() {
		return doublCheckList2;
	}

	public void setDoublCheckList2(List<String> doublCheckList2) {
		this.doublCheckList2 = doublCheckList2;
	}
}
