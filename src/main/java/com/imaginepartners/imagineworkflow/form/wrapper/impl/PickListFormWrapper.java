/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginepartners.imagineworkflow.form.wrapper.impl;

import com.imaginepartners.imagineworkflow.form.*;
import com.imaginepartners.imagineworkflow.models.IwInput;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import java.util.ArrayList;
import java.util.List;
import org.activiti.engine.form.FormProperty;
import org.apache.commons.collections.CollectionUtils;
import org.primefaces.model.DualListModel;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;

/**
 *
 * @author ibrahimhammani
 */
public class PickListFormWrapper extends FormWrapper {

    private DualListModel<String> dualList;

    public PickListFormWrapper(FormProperty form, Object value) {
        super(form, value);
    }

    public PickListFormWrapper(IwInput iwInput, Object value, boolean isReadable, boolean isWritable, BusinessService businessService, ActivitiService activitiService, String taskId, String procInstId) {

        super(iwInput, value, isReadable, isWritable, businessService, activitiService, taskId, procInstId);

        if (this.value == null) {
            this.value = new ArrayList<String>();
        }
		if("picklist".equals(iwInput.getComponent()))
			dualList = new DualListModel((List) CollectionUtils.disjunction(iwInput.getOptions(), (List<String>) this.value), (List<String>) this.value);
		else {
			List values;
			List<String> options = iwInput.getOptions();
			List listStringValues = new ArrayList<String>();

			if ("group".equals(iwInput.getValue())) {
				values = new ArrayList<Group>();
				for (Group group : (List<Group>) this.value) {
					values.add(group);
					listStringValues.add(group.getId());
				}
			} else {
				values = new ArrayList<User>();
				for (User user : (List<User>) this.value) {
					values.add(user);
					listStringValues.add(user.getId());
				}
			}

			if ("userGroup".equals(iwInput.getValue())) {
				List users = new ArrayList<User>();
				if (!options.isEmpty()) {
					String group = options.get(0);
					List<User> usersGroup = activitiService.getMemerbs(group);
					for (User user : usersGroup) {
						if (!listStringValues.contains(user.getId())) {
							users.add(user);
						}
					}
				}
				dualList = new DualListModel(users, (List<User>) values);
			} else if ("user".equals(iwInput.getValue())) {
				List users = new ArrayList<User>();
				for (String userId : options) {
					if (!listStringValues.contains(userId)) {
						User user = activitiService.getUser(userId);
						users.add(user);
					}
				}
				dualList = new DualListModel(users, (List<User>) values);
			} else {
				List groupes = new ArrayList<Group>();
				for (String groupId : options) {
					if (!listStringValues.contains(groupId)) {
						Group group = activitiService.getGroup(groupId);
						groupes.add(group);
					}
				}
				dualList = new DualListModel(groupes, (List<Group>) values);
			}
		}
	}

    @Override
    public Object getValue() {
        this.value = dualList.getTarget();
        return this.value;
    }

    @Override
    public void persiste(BusinessService businessService, ActivitiService activitiService) {
    }

    public DualListModel<String> getDualList() {
        return dualList;
    }

    public void setDualList(DualListModel<String> dualList) {
        this.dualList = dualList;
    }
}
