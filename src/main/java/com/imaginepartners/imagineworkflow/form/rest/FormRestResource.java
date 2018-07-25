package com.imaginepartners.imagineworkflow.form.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.imaginepartners.imagineworkflow.context.IwContext;
import com.imaginepartners.imagineworkflow.models.IwForm;
import com.imaginepartners.imagineworkflow.models.IwFormTemplate;
import com.imaginepartners.imagineworkflow.models.IwInput;
import com.imaginepartners.imagineworkflow.models.IwList;
import com.imaginepartners.imagineworkflow.models.IwListOptions;
import com.imaginepartners.imagineworkflow.models.IwMultilineEntity;
import com.imaginepartners.imagineworkflow.models.IwRegistre;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.util.ConfigConstants;
import com.imaginepartners.imagineworkflow.util.FormContants;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FormRestResource implements ModelDataJsonConstants {

	@Autowired
	private BusinessService businessService;

	@Autowired
	private ActivitiService activitiService;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private IwContext context;

	@RequestMapping(value = "/form/new", method = RequestMethod.PUT)
	public @ResponseBody ObjectNode newForm(@RequestBody MultiValueMap<String, String> values) {
		IwForm form = businessService.newIwForm();
		ObjectNode formNode = objectMapper.createObjectNode();
		try {
			form = businessService.newIwForm();
			form.setIwName(values.getFirst(FormContants.FORM_BUILDER_NAME_PARAM_NAME));
			form.setIwDescription(values.getFirst(FormContants.FORM_BUILDER_DESCRIPTION_PARAM_NAME));
			form.setIwFormSourceJson(values.getFirst(FormContants.FORM_BUILDER_JSON_PARAM_NAME));
			Date date = new Date();
			form.setIwCreateTime(date);
			form.setIwLastUpdateTime(date);
			businessService.saveIwForm(form);
			formNode.put(FormContants.FORM_BUILDER_RESPONSE_STATUS_NAME, FormContants.FORM_BUILDER_RESPONSE_SUCCESS_NAME);
			formNode.put(FormContants.FORM_BUILDER_FORM_ID_PARAM_NAME, form.getIwFormId());
			return formNode;
		} catch (Exception exp) {
			LogManager.getLogger(FormRestResource.class).error(exp);
			formNode.put(FormContants.FORM_BUILDER_RESPONSE_STATUS_NAME, FormContants.FORM_BUILDER_RESPONSE_ERROR_NAME);
			formNode.put(FormContants.FORM_BUILDER_FORM_ID_PARAM_NAME, form.getIwFormId());
			return formNode;
		}
	}

	@RequestMapping(value = "/form/{formId}/save", method = RequestMethod.PUT)
	public @ResponseBody ObjectNode saveForm(@PathVariable Long formId, @RequestBody MultiValueMap<String, String> values) {
		IwForm form;
		ObjectNode formNode = objectMapper.createObjectNode();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		try {
			form = businessService.getIwForm(formId);
			form.setIwName(values.getFirst(FormContants.FORM_BUILDER_NAME_PARAM_NAME));
			form.setIwDescription(values.getFirst(FormContants.FORM_BUILDER_DESCRIPTION_PARAM_NAME));
			form.setIwFormSourceJson(values.getFirst(FormContants.FORM_BUILDER_JSON_PARAM_NAME));
			form.setIwLastUpdateTime(new Date());
			// Create or update IwInput from json form builder
			List<IwInput> editorIwInputList = objectMapper.readValue(values.getFirst(FormContants.FORM_BUILDER_JSON_PARAM_NAME), new TypeReference<List<IwInput>>() {});
			Iterator<IwInput> iwInputsIterator = form.getIwInputList().iterator();
			while (iwInputsIterator.hasNext()) {
				IwInput iwInput = iwInputsIterator.next();
				int indexOfIwInput = iwInputListIndex(editorIwInputList, iwInput);
				if (indexOfIwInput < 0) {
					iwInputsIterator.remove();
				}
			}
			for (IwInput iwInput : editorIwInputList) {
				iwInput.setIwForm(form);
				int indexOfIwInput = iwInputListIndex(form.getIwInputList(), iwInput);
				if (indexOfIwInput >= 0) {
					form.getIwInputList().set(indexOfIwInput, iwInput);
				} else {
					form.getIwInputList().add(iwInput);
				}
			}
			businessService.saveIwForm(form);
			formNode.put(FormContants.FORM_BUILDER_RESPONSE_STATUS_NAME, FormContants.FORM_BUILDER_RESPONSE_SUCCESS_NAME);
			formNode.put(FormContants.FORM_BUILDER_FORM_ID_PARAM_NAME, formId);
			return formNode;

		} catch (Exception exp) {
			LogManager.getLogger(FormRestResource.class).error(exp);
			formNode.put(FormContants.FORM_BUILDER_RESPONSE_STATUS_NAME, FormContants.FORM_BUILDER_RESPONSE_ERROR_NAME);
			formNode.put(FormContants.FORM_BUILDER_FORM_ID_PARAM_NAME, formId);
			return formNode;
		}
	}

	public int iwInputListIndex(List<IwInput> iwInputList, IwInput iwInput) {
		for (IwInput item : iwInputList) {
			if (iwInput.getIwinputrealid()!= null && iwInput.getIwinputrealid().equals(item.getIwinputrealid())) {
				return iwInputList.indexOf(item);
			}
		}
		return -2;
	}

	@RequestMapping(value = "/form/{formId}/json", method = RequestMethod.GET, produces = "application/json")
	public ObjectNode getFormJson(@PathVariable Long formId) {
		IwForm form;
		ObjectNode formNode = objectMapper.createObjectNode();
		try {
			form = businessService.getIwForm(formId);
			if (form != null) {
				formNode.put(FormContants.FORM_BUILDER_RESPONSE_STATUS_NAME, FormContants.FORM_BUILDER_RESPONSE_SUCCESS_NAME);
				formNode.put(FormContants.FORM_BUILDER_FORM_ID_PARAM_NAME, formId);
				formNode.put(FormContants.FORM_BUILDER_NAME_PARAM_NAME, form.getIwName());
				formNode.put(FormContants.FORM_BUILDER_DESCRIPTION_PARAM_NAME, form.getIwDescription());
				formNode.put(FormContants.FORM_BUILDER_JSON_PARAM_NAME, objectMapper.readTree(form.getIwFormSourceJson()));
			} else {
				formNode.put(FormContants.FORM_BUILDER_RESPONSE_STATUS_NAME, FormContants.FORM_BUILDER_RESPONSE_ERROR_NAME);
				formNode.put(FormContants.FORM_BUILDER_FORM_ID_PARAM_NAME, formId);
				formNode.put(FormContants.FORM_BUILDER_JSON_PARAM_NAME, FormContants.FORM_BUILDER_FORM_DOESNT_EXIST_MESSAGE);
			}
		} catch (Exception exp) {
			LogManager.getLogger(FormRestResource.class).error(exp);
			formNode.put(FormContants.FORM_BUILDER_RESPONSE_STATUS_NAME, FormContants.FORM_BUILDER_RESPONSE_ERROR_NAME);
			formNode.put(FormContants.FORM_BUILDER_FORM_ID_PARAM_NAME, formId);
			formNode.put(FormContants.FORM_BUILDER_JSON_PARAM_NAME, exp.getLocalizedMessage());
		}
		return formNode;
	}

	@RequestMapping(value = "/group/json", method = RequestMethod.GET, produces = "application/json")
	public ArrayNode getGroupsJson() {
		List<Group> groups = activitiService.getGroupList(businessService.getConfigValue(ConfigConstants.GROUP_ID_DEFAULT_PREFIX));
		ArrayNode array = objectMapper.createArrayNode();
		for (Group group : groups) {
			ObjectNode node = array.addObject();
			node.put("id", group.getId());
			node.put("name", group.getName());
		}
		return array;
	}

	@RequestMapping(value = "/user/json", method = RequestMethod.GET, produces = "application/json")
	public ArrayNode getUsersJson() {
		List<User> users = activitiService.getUserList();
		ArrayNode array = objectMapper.createArrayNode();
		for (User user : users) {
			ObjectNode node = array.addObject();
			node.put("id", user.getId());
			node.put("firstName", user.getFirstName());
			node.put("lastName", user.getLastName());
		}
		return array;
	}

	@RequestMapping(value = "/resp/json", method = RequestMethod.GET, produces = "application/json")
	public ObjectNode getResponsableByLoggedUser(){
		return objectMapper.createObjectNode().put("id", context.getResponsable())
		.put("firstName", activitiService.getUser(context.getResponsable()).getFirstName())
		.put("lastName", activitiService.getUser(context.getResponsable()).getLastName());
	}

	@RequestMapping(value = "/entity/json", method = RequestMethod.GET, produces = "application/json")
	public ObjectNode getMultilineEntitiesJson() {
		List<IwMultilineEntity> entities = businessService.getMultiLineEntityList();
		ObjectNode array = objectMapper.createObjectNode();
		for (IwMultilineEntity entity : entities) {
			ObjectNode node = array.putObject(entity.getIwClass());
			node.put("name", entity.getIwName());
			ObjectNode fields = node.putObject("fields");
			Class<?> cls = null;
			try {
				cls = Class.forName(entity.getIwClass());
			} catch (ClassNotFoundException ex) {
				LogManager.getLogger(FormRestResource.class.getName()).error("reflectionError---->", ex);
			}
			if (cls != null) {
				for (Field fld : cls.getDeclaredFields()) {
					if (!"serialVersionUID".equals(fld.getName())) {
						try {
							fields.put(fld.getName(), this.businessService.getDescriptionFiledEntity(entity.getIwClass(), fld.getName()));
						} catch (Exception exp) {
							LogManager.getLogger(FormRestResource.class.getName()).error("reflectionError---->", exp);
							fields.put(fld.getName(), "");
						}

					}
				}
			}
		}
		return array;
	}

	@RequestMapping(value = "/form/json", method = RequestMethod.GET, produces = "application/json")
	public ObjectNode getFormList() {
		ObjectNode array = objectMapper.createObjectNode();
		List<IwForm> forms = businessService.getIwFormList();
		for (IwForm frm : forms) {
			array.put(String.valueOf(frm.getIwFormId()), frm.getIwName());
		}
		return array;
	}

	@RequestMapping(value = "/list/json", method = RequestMethod.GET, produces = "application/json")
	public ObjectNode getListOfLists() {
		List<IwList> myList = businessService.getIwListList();
		ObjectNode array = objectMapper.createObjectNode();
		for (IwList list : myList) {
			ObjectNode name = array.putObject(list.getIwListId().toString());
			name.put("name", list.getIwName());
			ObjectNode fields = name.putObject("fields");
			list.setIwListOptionsList(businessService.getIwListOptionsListByIwList(list));
			for (IwListOptions opt : list.getIwListOptionsList()) {
				fields.put(opt.getIwListOptionsId().toString(), opt.getIwName());
			}
		}
		return array;
	}

	@RequestMapping(value = "/template/json", method = RequestMethod.GET, produces = "application/json")
	public ObjectNode getListOfFormTemplates() {
		ObjectNode array = objectMapper.createObjectNode();
		List<IwFormTemplate> forms = businessService.getIwFormTemplateList();
		for (IwFormTemplate frm : forms) {
			ObjectNode fields = array.putObject(String.valueOf(frm.getIwValue()));
			fields.put("name", frm.getIwName());
		}
		return array;
	}

	@RequestMapping(value = "/tache/json", method = RequestMethod.GET, produces = "application/json")
	public ObjectNode getListOfTasksByProcess() {
		ObjectNode array = objectMapper.createObjectNode();
		for (ProcessDefinition procDef : activitiService.getLastProcDefList()) {
			if (!procDef.isSuspended()) {
				ObjectNode process = array.putObject(procDef.getKey());
				String procName = StringUtils.isNotBlank(procDef.getName()) ? procDef.getName() : procDef.getKey();
				if (StringUtils.isBlank(procName)) {
					procName = procDef.getId();
				}
				process.put("name", procName);
				ObjectNode fields = process.putObject("fields");
				for (Map.Entry<String, String> task : activitiService.getTaskModelList(procDef.getId()).entrySet()) {

					fields.put(task.getKey(), task.getValue());
				}
			}
		}
		return array;
	}

	@RequestMapping(value = "/registre/json", method = RequestMethod.GET, produces = "application/json")
	public ObjectNode getRegistresJson() {
		List<IwRegistre> registres = businessService.getRegistreList();
		ObjectNode array = objectMapper.createObjectNode();
		for (IwRegistre registre : registres) {
			array.put(String.valueOf(registre.getIwRegistreId()), registre.getIwName());
		}
		return array;
	}
}
