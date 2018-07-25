package com.imaginepartners.imagineworkflow.controller.template;
import com.imaginepartners.imagineworkflow.form.FormTemplate;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.*;




public class DirigeantEntretienController  extends FormTemplate implements Serializable {

	@Autowired
	ActivitiService activitiService;
    private String dirigeantEntretien ;
	private List<User> users;


		private static final long serialVersionUID = 1L;

      @PostConstruct
		@Override
		public void init() {

			if (varValues.get("dirigeantEntretien1") == null) {
				varValues.put("dirigeantEntretien1", "");
				dirigeantEntretien = (String) varValues.get("dirigeantEntretien1");
			}

		  dirigeantEntretien =(String) varValues.get("dirigeantEntretien1");
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


	public String getDirigeantEntretien() {
		return dirigeantEntretien;
	}

	public void setDirigeantEntretien(String dirigeantEntretien) {
		this.dirigeantEntretien = dirigeantEntretien;
		varValues.replace("dirigeantEntretien1",dirigeantEntretien ) ;
      }


}
