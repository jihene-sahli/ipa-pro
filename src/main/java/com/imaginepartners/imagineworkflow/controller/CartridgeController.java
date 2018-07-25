package com.imaginepartners.imagineworkflow.controller;

import com.imaginepartners.imagineworkflow.models.IwColumnTask;
import com.imaginepartners.imagineworkflow.models.IwForm;
import com.imaginepartners.imagineworkflow.models.IwInput;
import com.imaginepartners.imagineworkflow.models.IwVariableProcess;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.util.AppConstants;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.event.AjaxBehaviorEvent;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("view")
public class CartridgeController implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(CartridgeController.class);

	private List<ProcessDefinition> lastProcDefList;

	private ProcessDefinition slectedProcDef;

	private DualListModel<IwForm> forms;

	private DualListModel<IwInput> inputs;

	private String type;

	@Autowired
	private ActivitiService activitiService;

	@Autowired
	private BusinessService businessService;

	private boolean displayFormList;

	private boolean displayInputList;

	private List<IwForm> formList;

	private List<IwInput> inputList;

	private List<IwInput> listInputs;

	private boolean cartouche;

	private boolean colonne;

	private boolean showSaveButton;

	private List<IwVariableProcess> varProcessList;

	private DualListModel<IwVariableProcess> iwVariableProcessList;

	private List<IwVariableProcess> variableProcess;

	private Long countId;

	@PostConstruct
	public void init() {
		countId = -1l;
		type = FacesUtil.getUrlParam("type");
		if (type.toLowerCase().equals(AppConstants.CARTOUCHE.toLowerCase())) {
			this.cartouche = true;
		}
		if (type.toLowerCase().equals(AppConstants.COLONNE.toLowerCase())) {
			this.colonne = true;
		}
		lastProcDefList = activitiService.getLastProcDefList();
		listInputs = new ArrayList<IwInput>();
		if (variableProcess == null) {
			variableProcess = new ArrayList<IwVariableProcess>();
		}
	}

	public void displayFormlistMenu(AjaxBehaviorEvent event) {
		displayFormList = true;
		displayInputList = true;
		Set<String> formKies = activitiService.getProcDefFormKeyList(slectedProcDef.getId());
		formList = businessService.getIwFormListByFormKey(formKies);
		inputList = new ArrayList();
		varProcessList = businessService.getVariableProcessList(slectedProcDef.getKey(),colonne,cartouche);
		List<IwForm> varFormList = new ArrayList<IwForm>();
		List<IwInput> varInputList = new ArrayList<IwInput>();
		for (IwVariableProcess varProc : varProcessList) {
			if (varProc.getIwInput() != null && ((varProc.getIwShowTaskList() != null && varProc.getIwShowTaskList() && colonne) || (varProc.getIwShowTaskForm() != null && varProc.getIwShowTaskForm() && cartouche))) {
				if (!varInputList.contains(varProc.getIwInput())) {
					varInputList.add(varProc.getIwInput());
				}
				if (varProc.getIwInput().getIwForm() != null && !varFormList.contains(varProc.getIwInput().getIwForm())) {
					varFormList.add(varProc.getIwInput().getIwForm());
				}
			}
		}
		for (IwForm frm : formList) {
			if (varFormList.contains(frm)) {
				inputList.addAll(frm.getIwInputList());
			}
		}
		forms = new DualListModel<IwForm>((List) CollectionUtils.disjunction(formList, varFormList), varFormList);
		updateIwVariableProcessList();
	}

	public void updateIwVariableProcessList() {
		List<IwForm> varFormList = forms.getTarget();
		List<IwVariableProcess> varProcessUnselectedList = new ArrayList<IwVariableProcess>();
		List<IwVariableProcess> varProcessSelectedList = new ArrayList<IwVariableProcess>();
		if (varFormList != null && !varFormList.isEmpty()) {
			for (IwForm frm : varFormList) {
				if (frm.getIwInputList() != null && !frm.getIwInputList().isEmpty()) {
					for (IwInput input : frm.getIwInputList()) {
						if (notSelectedIwInput(input)) {
							varProcessUnselectedList.add(getIwVariableProcess(input));
						}
					}
				}
			}
		}
		if (colonne) {
			List<IwColumnTask> columnTaskList = businessService.getColumnTaskList();
			if (columnTaskList != null && !columnTaskList.isEmpty()) {
				for (IwColumnTask colTask : columnTaskList) {
					if (notSelectedColumnTask(colTask)) {
						varProcessUnselectedList.add(getIwVariableProcess(colTask));
					}
				}
			}
		}
		if (varProcessList != null && !varProcessList.isEmpty()) {
			for (IwVariableProcess varProcess : varProcessList) {
				if ((colonne && varProcess.getIwShowTaskList() != null && varProcess.getIwShowTaskList()) || (cartouche && varProcess.getIwColumnTask() == null && varProcess.getIwShowTaskForm() != null && varProcess.getIwShowTaskForm()) ) {
					if (varProcess.getIwInput() == null || varProcess.getIwInput().getIwForm() != null && varFormList.contains(varProcess.getIwInput().getIwForm())) {
						varProcessSelectedList.add(varProcess);
						variableProcess.add(varProcess);
					}
				}
			}
		}
		iwVariableProcessList = new DualListModel<IwVariableProcess>(varProcessUnselectedList, varProcessSelectedList);
	}

	public void onTransfer(AjaxBehaviorEvent event) {
		updateIwVariableProcessList();
	}

	public void saveVariableProcessList() {
		List<IwVariableProcess> saveList = new ArrayList<IwVariableProcess>();
		for (IwVariableProcess p : iwVariableProcessList.getTarget()) {
			if(colonne) {
				p.setIwShowTaskList(true);
				p.setIwIndexColonne(iwVariableProcessList.getTarget().indexOf(p));
				saveList.add(p);
			}else if(cartouche && p.getIwColumnTask() == null){
				p.setIwShowTaskForm(true);
				p.setIwIndex(iwVariableProcessList.getTarget().indexOf(p));
				saveList.add(p);
			}
			if (p.getIwVariableProcessId() < 0) {
				p.setIwVariableProcessId(null);
			}
		}

		for (IwVariableProcess p : varProcessList) {
			if (iwVariableProcessList.getTarget() == null || iwVariableProcessList.getTarget().isEmpty() || !iwVariableProcessList.getTarget().contains(p)) {
				if(colonne) {
					p.setIwShowTaskList(false);
					p.setIwIndexColonne(null);
					saveList.add(p);
				}else if(cartouche && p.getIwColumnTask() == null){
					p.setIwShowTaskForm(false);
					p.setIwIndex(null);
					saveList.add(p);
				}

				if (p.getIwVariableProcessId() < 0) {
					p.setIwVariableProcessId(null);
				}
			}
		}
		businessService.saveEntityList(saveList);
	}

	public boolean isDisplayInputList() {
		return displayInputList;
	}

	public void setDisplayInputList(boolean displayInputList) {
		this.displayInputList = displayInputList;
	}

	public boolean isDisplayFormList() {
		return displayFormList;
	}

	public void setDisplayFormList(boolean displayFormList) {
		this.displayFormList = displayFormList;
	}

	public List<IwInput> getListInputs() {
		return listInputs;
	}

	public void setListInputs(List<IwInput> listInputs) {
		this.listInputs = listInputs;
	}

	public List<IwForm> getFormList() {
		return formList;
	}

	public void setFormList(List<IwForm> formList) {
		this.formList = formList;
	}

	public ProcessDefinition getSlectedProcDef() {
		return slectedProcDef;
	}

	public void setSlectedProcDef(ProcessDefinition slectedProcDef) {
		this.slectedProcDef = slectedProcDef;
	}

	public List<ProcessDefinition> getLastProcDefList() {
		return lastProcDefList;
	}

	public void setLastProcDefList(List<ProcessDefinition> lastProcDefList) {
		this.lastProcDefList = lastProcDefList;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isCartouche() {
		return cartouche;
	}

	public void setCartouche(boolean cartouche) {
		this.cartouche = cartouche;
	}

	public boolean isColonne() {
		return colonne;
	}

	public void setColonne(boolean colonne) {
		this.colonne = colonne;
	}

	public DualListModel<IwInput> getInputs() {
		return inputs;
	}

	public void setInputs(DualListModel<IwInput> inputs) {
		this.inputs = inputs;
	}

	public DualListModel<IwForm> getForms() {
		return forms;
	}

	public void setForms(DualListModel<IwForm> forms) {
		this.forms = forms;
	}

	public boolean isShowSaveButton() {
		return showSaveButton;
	}

	public void setShowSaveButton(boolean showSaveButton) {
		this.showSaveButton = showSaveButton;
	}

	public boolean notSelectedIwInput(Object input) {
		if (varProcessList != null && !varProcessList.isEmpty()) {
			for (IwVariableProcess varProcess : varProcessList) {
				if (input != null && input.equals(varProcess.getIwInput()) &&( (colonne && varProcess.getIwShowTaskList() != null && varProcess.getIwShowTaskList() ) || (cartouche && varProcess.getIwShowTaskForm() != null && varProcess.getIwShowTaskForm() ))) {
					return false;
				}
			}
		}
		return true;
	}

	public IwVariableProcess getIwVariableProcess(IwInput input) {
		if (varProcessList != null && !varProcessList.isEmpty()) {
			for (IwVariableProcess varProcess : varProcessList) {
				if (input != null && input.equals(varProcess.getIwInput())) {
					variableProcess.add(varProcess);
					return varProcess;
				}
			}
		}
		IwVariableProcess p = new IwVariableProcess();
		p.setIwVariableProcessId(--countId);
		p.setIwInput(input);
		p.setIwProcessKey(slectedProcDef.getKey());
		variableProcess.add(p);
		return p;
	}

	public IwVariableProcess getIwVariableProcess(IwColumnTask colTask) {
		if (varProcessList != null && !varProcessList.isEmpty()) {
			for (IwVariableProcess varProcess : varProcessList) {
				if (colTask != null && colTask.equals(varProcess.getIwColumnTask())) {
					variableProcess.add(varProcess);
					return varProcess;
				}
			}
		}
		IwVariableProcess p = new IwVariableProcess();
		p.setIwVariableProcessId(--countId);
		p.setIwColumnTask(colTask);
		p.setIwProcessKey(slectedProcDef.getKey());
		variableProcess.add(p);
		return p;
	}

	public boolean notSelectedColumnTask(IwColumnTask colTask) {
		if (varProcessList != null && !varProcessList.isEmpty()) {
			for (IwVariableProcess varProcess : varProcessList) {
				if (colTask != null && colTask.equals(varProcess.getIwColumnTask()) && varProcess.getIwShowTaskList()!=null && varProcess.getIwShowTaskList()) {
					return false;
				}
			}
		}
		return true;
	}

	public DualListModel<IwVariableProcess> getIwVariableProcessList() {
		return iwVariableProcessList;
	}

	public void setIwVariableProcessList(DualListModel<IwVariableProcess> iwVariableProcessList) {
		this.iwVariableProcessList = iwVariableProcessList;
	}

	public String getLabel(IwVariableProcess varProcess) {

		if (varProcess.getIwInput() != null) {
			return varProcess.getIwInput().getLabel() + " [ " + varProcess.getIwInput().getIwinputrealid() + " ] | " + varProcess.getIwInput().getIwForm().getIwName() + " [ " + varProcess.getIwInput().getIwForm().getIwFormId() + " ]";
		} else if (varProcess.getIwColumnTask() != null) {
			return varProcess.getIwColumnTask().getIwName() + " [ " + varProcess.getIwColumnTask().getIwColumnTaskId() + " ]";
		}
		return "";
	}

	public List<IwVariableProcess> getVariableProcess() {
		return variableProcess;
	}

	public void setVariableProcess(List<IwVariableProcess> variableProcess) {
		this.variableProcess = variableProcess;
	}
}
