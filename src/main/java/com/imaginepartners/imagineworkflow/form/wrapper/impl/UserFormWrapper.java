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
import org.activiti.engine.identity.User;

/**
 *
 * @author ibrahimhammani
 */
public class UserFormWrapper extends FormWrapper {

    private List<User> users;

    public UserFormWrapper(FormProperty form, Object value) {
        super(form, value);
    }

    public UserFormWrapper(IwInput iwInput, Object value, boolean isReadable, boolean isWritable, BusinessService businessService, ActivitiService activitiService, String taskId, String procInstId) {

        super(iwInput, value, isReadable, isWritable, businessService, activitiService, taskId, procInstId);

        if (this.value != null) {
            this.users = activitiService.getUserListIn((List<String>) value);
        }
    }

    @Override
    public Object getValue() {
        if (users != null && !users.isEmpty()) {
            value = new ArrayList<String>();
            for (User usr : users) {
                ((ArrayList<String>) value).add(usr.getId());
            }
        }
        return this.value;
    }

    @Override
    public void persiste(BusinessService businessService, ActivitiService activitiService) {
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
