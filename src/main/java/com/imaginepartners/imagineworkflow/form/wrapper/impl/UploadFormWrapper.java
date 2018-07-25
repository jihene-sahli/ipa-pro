package com.imaginepartners.imagineworkflow.form.wrapper.impl;

import com.imaginepartners.imagineworkflow.form.FormWrapper;
import com.imaginepartners.imagineworkflow.models.IwFile;
import com.imaginepartners.imagineworkflow.models.IwInput;
import com.imaginepartners.imagineworkflow.models.IwUpload;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import java.util.ArrayList;

public class UploadFormWrapper extends FormWrapper {

	private IwUpload iwUpload;

	public UploadFormWrapper(IwInput iwInput, Object value, boolean isReadable, boolean isWritable, BusinessService businessService, ActivitiService activitiService, String taskId, String procInstId) {
		super(iwInput, value, isReadable, isWritable, businessService, activitiService, taskId, procInstId);
		if (value != null) {
			iwUpload = businessService.getIwUpload((Long) value);
		} else {
			iwUpload = new IwUpload();
			iwUpload.setIwFileList(new ArrayList<IwFile>());
		}
	}

	@Override
	public Object getValue() {
		if (value == null && iwUpload != null) {
			value = iwUpload.getIwUploadId();
		}
		return value;
	}

	public IwUpload getIwUpload() {
		return iwUpload;
	}

	public void setIwUpload(IwUpload iwUpload) {
		this.iwUpload = iwUpload;
	}

	@Override
	public void persiste(BusinessService businessService, ActivitiService activitiService) {
		if (iwUpload != null) {
			businessService.saveIwUpload(iwUpload);
		}
	}
}
