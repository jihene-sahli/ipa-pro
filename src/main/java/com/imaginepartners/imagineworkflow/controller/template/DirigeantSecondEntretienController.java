package com.imaginepartners.imagineworkflow.controller.template;


import com.imaginepartners.imagineworkflow.form.FormTemplate;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.List;



public class DirigeantSecondEntretienController extends FormTemplate implements Serializable {

	@Autowired
	ActivitiService activitiService;

	private List<User> users;

		private static final long serialVersionUID = 1L;
   private String	dirigeantEntretien2 ;
      @PostConstruct
		@Override
		public void init() {
  if(varValues.get("dirigeantEntretien2") == null){
			  varValues.put("dirigeantEntretien2","");
			  dirigeantEntretien2 = (String) varValues.get("dirigeantEntretien2");
		  }


			  dirigeantEntretien2 =(String) varValues.get("dirigeantEntretien2");
          users = activitiService.getUserList();


		}



		@Override
		public void putVars() {

		}


	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}


	public String getDirigeantEntretien2() {
		return dirigeantEntretien2;
	}

	public void setDirigeantEntretien2(String dirigeantEntretien) {
		this.dirigeantEntretien2 = dirigeantEntretien;
		varValues.replace("dirigeantEntretien2",dirigeantEntretien2 ) ;
	}
}
