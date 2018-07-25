package com.imaginepartners.imagineworkflow.controller;

import com.imaginepartners.imagineworkflow.models.IwForm;
import com.imaginepartners.imagineworkflow.models.IwInput;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import com.imaginepartners.imagineworkflow.util.XlsxConverter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.util.CellAddress;
import org.primefaces.component.wizard.Wizard;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.diagram.ConnectEvent;
import org.primefaces.event.diagram.ConnectionChangeEvent;
import org.primefaces.event.diagram.DisconnectEvent;
import org.primefaces.model.diagram.DefaultDiagramModel;
import org.primefaces.model.diagram.Element;
import org.primefaces.model.diagram.connector.StraightConnector;
import org.primefaces.model.diagram.endpoint.DotEndPoint;
import org.primefaces.model.diagram.endpoint.EndPoint;
import org.primefaces.model.diagram.endpoint.EndPointAnchor;
import org.primefaces.model.diagram.endpoint.RectangleEndPoint;
import org.primefaces.model.diagram.overlay.ArrowOverlay;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("handleUploads")
@Scope("view")
public class HandleUploads implements InitializingBean, Serializable{

	@Autowired
	private BusinessService businessService;

	@Autowired
	private ActivitiService activitiService;

	private List<CulumnModel> columns;

	private List<CulumnModel> selectedColumns;

	private int headerRowNumber = -1;

	private static String INITIATOR= "Demandeur";

	private String filePassword;

	private boolean disablePasswordInput= true;

	private boolean fileReady;

	private List<Integer> rows = new ArrayList<Integer>();

	private XlsxConverter xlsxConverter;

	private File uploadedFile;

	private ProcessDefinition selectedProcDef;

	private IwForm selectedForm;

	private String selectedTaskStep;

	private List<ProcessDefinition> lastProcessDefinitions;

	private List<IwForm> forms;

	private List<String> tasks;

	private DefaultDiagramModel diagramModel;

	private boolean checkBox;

	private boolean showDiagram;

	private List<Map<String, Object>> listMap;

	private List<Map<String, Object>> entries;

	/**
	 * Acts as postContruct
	 * @throws Exception
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		columns = new ArrayList<CulumnModel>();
		lastProcessDefinitions = activitiService.getLastProcDefList();
		forms = new ArrayList<IwForm>();
		selectedColumns = new ArrayList<CulumnModel>();
		createDir();
		uploadedFile= new File("UPLOADED_FILES/excel/upload_"+new Date().getTime());
		listMap= new ArrayList<Map<String, Object>>();
		tasks= new ArrayList<String>();
	}

	private void createDir() {
		File file= new File("UPLOADED_FILES/excel/");
		if(!file.exists()) {
			LogManager.getLogger(HandleUploads.class.getSimpleName()).debug("file dont exist");
			if(file.mkdirs()) {
				LogManager.getLogger(HandleUploads.class.getSimpleName()).info("file is created");
			} else {
				LogManager.getLogger(HandleUploads.class.getSimpleName()).warn("can't create given file");
			}
		} else {
			LogManager.getLogger(HandleUploads.class.getSimpleName()).info("file already exist");
		}
	}

	private EndPoint createRectangleEndPoint(EndPointAnchor anchor) {
		RectangleEndPoint recEndPoint = new RectangleEndPoint(anchor);
		recEndPoint.setScope("network");
		recEndPoint.setSource(true);
		recEndPoint.setStyle("{fillStyle:'#98AFC7'}");
		recEndPoint.setHoverStyle("{fillStyle:'#5C738B'}");
		return recEndPoint;
	}

	private EndPoint createDotEndPoint(EndPointAnchor anchor) {
		DotEndPoint endPoint = new DotEndPoint(anchor);
		endPoint.setScope("network");
		endPoint.setTarget(true);
		endPoint.setStyle("{fillStyle:'#98AFC7'}");
		endPoint.setHoverStyle("{fillStyle:'#5C738B'}");
		return endPoint;
	}

	/**
	 * On flow next event listener
	 * @param event flowEvent to control the behavior of the wizard
	 * @return next step name.
	 */
	public String onFlowProcess(FlowEvent event) {
		LogManager.getLogger(HandleUploads.class).info("on flow event");
		if(event.getOldStep().equals("xlsx")) {
			launchInstances();
		}
		return event.getNewStep();
	}

	/**
	 * listener method for file upload event.
	 * once the file uploaded, method executed.
	 * @param event FileUploadEvent.
	 */
	public void handleFileUpload(FileUploadEvent event) {
		try {
			// create a copy of uploaded file in the hard drive.
			IOUtils.copy(event.getFile().getInputstream(), new FileOutputStream(uploadedFile));
			initXlsConverter(null);
		} catch (IOException ex) {
			LogManager.getLogger(HandleUploads.class.getSimpleName()).error("file input/output exception", ex);
		}
	}

	/**
	 * open uploaded file with the given password
	 * @param pass password.
	 */
	private void initXlsConverter(String pass) {
		try {
			// Get reference of workbook using apache lib.
			if(pass == null)
				xlsxConverter = new XlsxConverter(FileUtils.openInputStream(uploadedFile));
			else
				xlsxConverter = new XlsxConverter(FileUtils.openInputStream(uploadedFile), pass);

			/**
			 * Initialize all values for the select one menu component, for the end user.
			 * in order to choose the first row for applying the processing.
			 */
			fileReady= true;
			initRowslist();
			// Confirm that the file is fully loaded.
			FacesUtil.setAjaxInfoMessage("Succesful", " file is uploaded.");
		} catch (IOException ex) {
			FacesUtil.setAjaxErrorMessage("erreur", "error on laoding file.");
			LogManager.getLogger(HandleUploads.class.getSimpleName()).error("input/output exception", ex);
		} catch (InvalidFormatException ex) {
			FacesUtil.setAjaxErrorMessage("erreur", "invalide file");
			LogManager.getLogger(HandleUploads.class.getSimpleName()).error("invalide file", ex);
		} catch (EncryptedDocumentException ex) {
			disablePasswordInput = false;
			fileReady= false;
			FacesUtil.setAjaxErrorMessage("erreur", "file incrypted.");
			LogManager.getLogger(HandleUploads.class.getSimpleName()).error("file incrypted", ex);
		}
	}

	/**
	 * Setting the password from the view
	 */
	public void onSetpassword() {
		LogManager.getLogger(HandleUploads.class).info("on password input chnage");
		initXlsConverter(filePassword);
		if(xlsxConverter != null) {
			LogManager.getLogger(HandleUploads.class).info("uploaded file is ready to use, password:"+filePassword+" is correct");
			xlsxConverter.setPassword(filePassword);
			initRowslist();
		} else {
			LogManager.getLogger(HandleUploads.class).warn("file inreadable, try to verify the passwrod!");
		}
	}

	/**
	 * In order to specify the first line number for witch the processing of the given file
	 * should preceed, the end user must provide the header line number.
	 * this list cover all the declared line numbers.
	 *
	 * initializing the list of maps listMap Object with empty maps in the first place
	 * for avoiding null Pointer exception.
	 */
	private void initRowslist() {
		rows.clear();
		listMap.clear();
		for (int i = 0; i < XlsxConverter.WORKBOOK.getSheetAt(0).getLastRowNum(); i++) {
			rows.add(i);
			listMap.add(i, new HashMap<String, Object>());
		}
	}

	/**
	 *  listener method on witch the processing should consider the start line number.
	 * define the columns of the uploaded file and put them to columns object.
	 * @param event Ajax behavior event, super class for all JSF listeners
	 */
	public void onChange(AjaxBehaviorEvent event) {
		if (headerRowNumber >= 0)
			xlsxConverter.setStarting_header_row(this.headerRowNumber);
		xlsxConverter.readDocument(headerRowNumber);
		entries= xlsxConverter.getEntries();
		for (String fieldName : xlsxConverter.getColumns()) {
			columns.add(new CulumnModel(fieldName));
		}
		changeStepWizard(event.getComponent(), "xlsx");
		showDiagram= true;
	}

	/**
	 * in order to complete a task, end user must provide us with the task object
	 * @param event ajaxbehaviorEvent.
	 */
	public void onTaskChange(AjaxBehaviorEvent event) {
		LogManager.getLogger(HandleUploads.class).info("on task change event, selected task is: " + selectedTaskStep);
	}

	public void launchInstances() {
		LogManager.getLogger(HandleUploads.class.getSimpleName()).info("launch instances preceeed");
		int i=0;
		while(i <listMap.size()) {
			Map<String, Object> map= listMap.get(i);
			try {
				String initiator=null;
				if(entries.get(i).get(INITIATOR) != null)
					initiator= entries.get(i).get(INITIATOR).toString();
				if(StringUtils.isNotBlank(initiator)) {
					map.put("initiator", initiator);
					ProcessInstance procInst= activitiService.startProcessInstanceByKey(selectedProcDef.getKey(), map);
					activitiService.setProcessInstanceName(procInst.getId(), selectedProcDef.getName());
					Task task= activitiService.getTaskByExecIdandAssigne(procInst.getId(), initiator);
					activitiService.completeTask(task.getId(), map);
				} else {
					LogManager.getLogger(HandleUploads.class.getSimpleName()).warn("there is no initiator for this given row");
				}
			} catch (IndexOutOfBoundsException indexOutOfBoundEsception) {
				LogManager.getLogger(HandleUploads.class.getSimpleName()).error("index out of bound exception", indexOutOfBoundEsception);
			} catch (SecurityException ex) {
				LogManager.getLogger(HandleUploads.class.getSimpleName()).error("secutity exception", ex);
			} catch (IllegalArgumentException ex) {
				LogManager.getLogger(HandleUploads.class.getSimpleName()).error("illegal argument exception", ex);
			}
			i++;
		}
		FacesUtil.setSessionInfoMessage("finish the launch of processes");
		FacesUtil.redirect("../tache/liste.xhtml");
	}

	public void onValidate(ActionEvent event) {
		if(StringUtils.isNotBlank(selectedTaskStep)) {
			Task task= activitiService.getTask(selectedTaskStep.split("_")[1]);
			for(Map<String, Object> map: listMap) {
				Iterator<Map.Entry<String, Object>> iterator= map.entrySet().iterator();
				while(iterator.hasNext()) {
					Map.Entry<String, Object> entry= iterator.next();
					String str= entry.getValue().toString().toLowerCase().replaceAll("\\s+", "");
					if(task.getAssignee().toLowerCase().equals(str)) {
						execute(task, map);
						break;
					}
				}
			}
		}
	}

	/**
	 * Complete task with variables
	 * @param task Activiti.Task Interface.
	 * @param valideMap variables
	 */
	private void execute(Task task, Map<String, Object> valideMap) {
		activitiService.completeTask(task.getId(), valideMap);
	}

	/**
	 * Listener event to be triggered when user change password
	 * @param event
	 */
	public void onPasswordChange(AjaxBehaviorEvent event) {
		if(StringUtils.isNotBlank(filePassword)) {
			xlsxConverter.setPassword(filePassword);
			initRowslist();
		} else {
			FacesUtil.setAjaxWarnMessage("The password is empty");
		}
	}

	/**
	 * Selecting the process Definition.
	 * @param event
	 */
	public void onModelChange(AjaxBehaviorEvent event) {
		if(selectedProcDef != null) {
			for (String iwFormId : activitiService.getProcDefFormKeyList(selectedProcDef.getId())) {
				this.forms.add(businessService.getIwForm(Long.valueOf(iwFormId)));
			}
			for(Task task: activitiService.getTasksByProcDef(selectedProcDef) )
				tasks.add(task.getName()+"_"+task.getId());
		}else{
			tasks.clear();
			forms.clear();
		}
	}

	/**
	 * On selected the iwForm.
	 * @param event
	 */
	public void onSelectedForm(AjaxBehaviorEvent event) {
		changeStepWizard(event.getComponent(), "fichier");
		initDiagramModel();
	}

	private void changeStepWizard(UIComponent component, String step) {
		for(int i=0; i<5; i++) {
			component= component.getParent();
			if(component instanceof Wizard) {
				((Wizard)component).setStep(step);
				break;
			}
		}
	}

	public void onCheckBox(CulumnModel columnModel, Object columnIndex) {
		if (columnIndex instanceof Integer) {
			if (checkBox) {
				if (!selectedColumns.contains(columnModel))
					addElement(columnModel);
			} else
				removeElement(columnModel);
		}
	}

	/**
	 * Create the elements of the diagram. for wiring purposes.
	 */
	private void initDiagramModel() {
		diagramModel = new DefaultDiagramModel();


		StraightConnector connector = new StraightConnector();
		connector.setPaintStyle("{strokeStyle:'#98AFC7', lineWidth:3}");
		connector.setHoverPaintStyle("{strokeStyle:'#5C738B'}");
		diagramModel.getDefaultConnectionOverlays().add(new ArrowOverlay(20, 20, 1, 1));
		diagramModel.setMaxConnections(-1);
		diagramModel.setDefaultConnector(connector);
		int co = 1;
		for (IwInput input : businessService.getIwInputByFormId(selectedForm)) {
			co += 3;
			Element element = new Element(
			new ElementData(input.getIwForm().getIwFormId(), input.getId(),	input.getValidation(), input.getLabel()), "5em", co + "em");
			element.setDraggable(false);
			EndPoint endPoint = createRectangleEndPoint(EndPointAnchor.RIGHT);
			element.addEndPoint(endPoint);
			diagramModel.addElement(element);
		}
	}

	public void addElement(CulumnModel columnModel) {
		int co = getRightElementsNumber();
		co *= 5;
		Element element = new Element(new ElementData(columnModel.fieldName), "70em", co + "em");
		element.setDraggable(true);
		EndPoint endPoint = createDotEndPoint(EndPointAnchor.LEFT);
		element.addEndPoint(endPoint);
		diagramModel.addElement(element);
	}

	public void removeElement(CulumnModel columnModel) {
		Iterator<Element> iterator = diagramModel.getElements().iterator();
		while (iterator.hasNext()) {
			ElementData ed = (ElementData) iterator.next().getData();
			if(!ed.isType()) {
				if(ed.getFieldName().equals(columnModel.getFieldName()))
					iterator.remove();
			}
		}
	}

	private int getRightElementsNumber() {
		int co = 0;
		for (Element element : diagramModel.getElements()) {
			if (!((ElementData) element.getData()).isType())
				co++;
		}
		return co;
	}

	private List<ElementData> getRightElements() {
		List<ElementData> rightElement= new ArrayList<ElementData>();
		for(Element element: diagramModel.getElements() ) {
			if( !( (ElementData)element.getData() ).isType() )
				rightElement.add( ( (ElementData)element.getData() ) );
		}
		return rightElement;
	}

	public void onConnect(ConnectEvent connectEvent) {
		String fieldName = ((ElementData) connectEvent.getTargetElement().getData()).getFieldName();
		String inputId =  ((ElementData) connectEvent.getSourceElement().getData()).getInputId();
		String type = ((ElementData) connectEvent.getSourceElement().getData()).getValidation();
		Class clazzValue = getClassFromType(type);
		Object cellContent = getCellContentByAdr(headerRowNumber + 1, getColumnNumber(fieldName));
		if(cellContent != null) {
			if(type.toLowerCase().equals(cellContent.getClass().getSimpleName().toLowerCase())) {
				updateListMaps(fieldName, inputId, "onconnect");
				FacesUtil.setAjaxInfoMessage("type match", "type match");
				connectEvent.getTargetEndPoint().setStyle("{fillStyle:'#00FF00'}");
				connectEvent.getTargetEndPoint().setHoverStyle("{fillStyle:'#00FF00'}");
			}else{
				FacesUtil.setAjaxWarnMessage("Type mismatch", "Type mismatch");
				connectEvent.getTargetEndPoint().setStyle("{fillStyle:'#FF0000'}");
				connectEvent.getTargetEndPoint().setHoverStyle("{fillStyle:'#FF0000'}");
			}
		}
	}

	public void onDisconnect(DisconnectEvent disconectEvent) {
		String fieldName= ((ElementData) disconectEvent.getTargetElement().getData()).getFieldName();
		String inputId=  ((ElementData)disconectEvent.getSourceElement().getData()).getInputId();
		updateListMaps(fieldName, inputId, "ondisconnect");
	}

	public void onConnectionChange(ConnectionChangeEvent connectionChangeEvent) {
		String newFieldName = ((ElementData) connectionChangeEvent.getNewTargetElement().getData()).getFieldName();
		String oldFieldName = ((ElementData) connectionChangeEvent.getOriginalTargetElement().getData()).getFieldName();
		String type = ((ElementData) connectionChangeEvent.getNewSourceElement().getData()).getValidation();
		String inputId = ((ElementData) connectionChangeEvent.getOriginalSourceElement().getData()).getInputId();
		Object cellContent = getCellContentByAdr(headerRowNumber + 1, getColumnNumber(newFieldName));
		if(cellContent != null) {
			Integer id=0;
			if(type.toLowerCase().equals(cellContent.getClass().getSimpleName().toLowerCase())) {
				for(Map<String, Object>  map: entries) {
					try {
						listMap.get(id).remove(oldFieldName);
						int realLineNumber= headerRowNumber + id + 1;
						Object object= getCellContentByAdr(realLineNumber, getColumnNumber(newFieldName));
						if(object instanceof String) {
							if( XlsxConverter.isDate(object.toString() ) ) {
								listMap.get(id).put(inputId,  XlsxConverter.StringToDate(object.toString()));
							}else
								listMap.get(id).put(inputId,  object.toString());
						}if(object instanceof Boolean)
							listMap.get(id).put(inputId,  new Boolean(object.toString()) );
						if(object instanceof Date)
							listMap.get(id).put(inputId,  (Date)object );
						if(object instanceof Long)
							listMap.get(id).put(inputId,  Long.valueOf(object.toString())   );
						id++;
					} catch (SecurityException ex) {
						LogManager.getLogger(HandleUploads.class.getSimpleName()).error("sucurity exception", ex);
					} catch (IllegalArgumentException ex) {
						LogManager.getLogger(HandleUploads.class.getSimpleName()).error("illegal argument exception", ex);
					}
				}
				FacesUtil.setAjaxInfoMessage("type match", "type match");
				connectionChangeEvent.getOriginalTargetEndPoint().setStyle("{fillStyle:'#98AFC7'}");
				connectionChangeEvent.getOriginalTargetEndPoint().setHoverStyle("{fillStyle:'#98AFC7'}");
				connectionChangeEvent.getNewTargetEndPoint().setStyle("{fillStyle:'#00FF00'}");
				connectionChangeEvent.getNewTargetEndPoint().setHoverStyle("{fillStyle:'#00FF00'}");
			} else {
				FacesUtil.setAjaxWarnMessage("Type mismatch", "Type mismatch");
				connectionChangeEvent.getNewTargetEndPoint().setStyle("{fillStyle:'#FF0000'}");
				connectionChangeEvent.getNewTargetEndPoint().setHoverStyle("{fillStyle:'#FF0000'}");
			}
		}
	}

	private void updateListMaps(String fieldName, String inputId, String operation) {
		Integer id=0;
		for(Map<String, Object>  map: entries) {
			try {
				int realLineNumber= headerRowNumber+id+1;
				Object object= getCellContentByAdr(realLineNumber, getColumnNumber(fieldName));
				if(operation.toLowerCase().contains("onconnect")) {
					if(object instanceof String) {
						if( XlsxConverter.isDate(object.toString() ) ) {
							listMap.get(id).put(inputId,  XlsxConverter.StringToDate(object.toString()));
						} else {
							listMap.get(id).put(inputId,  object.toString());
						}
					}
					if(object instanceof Boolean)
						listMap.get(id).put(inputId,  new Boolean(object.toString()) );
					if(object instanceof Date)
						listMap.get(id).put(inputId,  (Date)object );
					if(object instanceof Long)
						listMap.get(id).put(inputId,  Long.valueOf(object.toString())   );
				}
				if(operation.toLowerCase().contains("ondisconnect"))
					listMap.get(id).remove(inputId);
				id++;
			}catch (SecurityException ex) {
				LogManager.getLogger(HandleUploads.class.getSimpleName()).error("sucurity exception", ex);
			} catch (IllegalArgumentException ex) {
				LogManager.getLogger(HandleUploads.class.getSimpleName()).error("illegal argument exception", ex);
			}
		}
	}

	private Integer getColumnNumber(String fieldName) {
		int co= 0;
		for(CulumnModel cm: columns) {
			if(cm.getFieldName().equals(fieldName))
				return co;
			co++;
		}
		return null;
	}

	/**
	 * @deprecated replace double by long
	 * @param row row number
	 * @param column column number
	 * @return cell content type
	 */
	private Object getCellContentByCoo(int row, int column) {
		Object obj= XlsxConverter.getCellContentByCellAddress(new CellAddress(row, column));
		if(obj instanceof String)
			return obj.toString();
		if(obj instanceof Boolean)
			return Boolean.parseBoolean(obj.toString());
		if(obj instanceof Date)
			return new Date(obj.toString());
		if(obj instanceof Double)
			return Double.parseDouble(obj.toString());
		return null;
	}

	private Object getCellContentByAdr(int row, int column) {
		Object obj= XlsxConverter.getCellContentByCellAddress(new CellAddress(row, column));
		if(obj instanceof String)
			return obj.toString();
		if(obj instanceof Boolean)
			return Boolean.parseBoolean(obj.toString());
		if(obj instanceof Date)
			return new Date(obj.toString());
		if(obj instanceof Double)
			return ((Double) obj).longValue();
		return null;
	}

	private Class getClassFromType(String value) {
		if(value.toLowerCase().contains("string"))
			return String.class;
		if(value.toLowerCase().contains("long"))
			return Long.class;
		if(value.toLowerCase().contains("date"))
			return Date.class;
		if(value.toLowerCase().contains("boolean"))
			return Boolean.class;
		return null;
	}

	public List<CulumnModel> getColumns() {
		return columns;
	}

	public void setColumns(List<CulumnModel> columns) {
		this.columns = columns;
	}

	public int getHeaderRowNumber() {
		return headerRowNumber;
	}

	public void setHeaderRowNumber(int headerRowNumber) {
		this.headerRowNumber = headerRowNumber;
	}

	public List<Integer> getRows() {
		return rows;
	}

	public ProcessDefinition getSelectedProcDef() {
		return selectedProcDef;
	}

	public void setSelectedProcDef(ProcessDefinition selectedProcDef) {
		this.selectedProcDef = selectedProcDef;
	}

	public List<ProcessDefinition> getLastProcessDefinitions() {
		return lastProcessDefinitions;
	}

	public void setLastProcessDefinitions(List<ProcessDefinition> lastProcessDefinitions) {
		this.lastProcessDefinitions = lastProcessDefinitions;
	}

	public List<IwForm> getForms() {
		return forms;
	}

	public void setForms(List<IwForm> forms) {
		this.forms = forms;
	}

	public IwForm getSelectedForm() {
		return selectedForm;
	}

	public void setSelectedForm(IwForm selectedForm) {
		this.selectedForm = selectedForm;
	}

	public DefaultDiagramModel getDiagramModel() {
		return diagramModel;
	}

	public void setDiagramModel(DefaultDiagramModel diagramModel) {
		this.diagramModel = diagramModel;
	}

	public boolean isCheckBox() {
		return checkBox;
	}

	public void setCheckBox(boolean checkBox) {
		this.checkBox = checkBox;
	}

	public String getFilePassword() {
		return filePassword;
	}

	public void setFilePassword(String filePassword) {
		this.filePassword = filePassword;
	}

	public boolean isDisablePasswordInput() {
		return disablePasswordInput;
	}

	public void setDisablePasswordInput(boolean disablePasswordInput) {
		this.disablePasswordInput = disablePasswordInput;
	}

	public boolean isFileReady() {
		return fileReady;
	}

	public void setFileReady(boolean fileReady) {
		this.fileReady = fileReady;
	}

	public String getSelectedTaskStep() {
		return selectedTaskStep;
	}

	public void setSelectedTaskStep(String selectedTaskStep) {
		this.selectedTaskStep = selectedTaskStep;
	}

	public List<String> getTasks() {
		return tasks;
	}

	public void setTasks(List<String> tasks) {
		this.tasks = tasks;
	}

	public boolean isShowDiagram() {
		return showDiagram;
	}

	public void setShowDiagram(boolean showDiagram) {
		this.showDiagram = showDiagram;
	}

	public List<Map<String, Object>> getEntries() {
		return entries;
	}

	public void setEntries(List<Map<String, Object>> entries) {
		this.entries = entries;
	}

	/**
	 * Class holding the fieldName as a primary field.
	 * this class meant to be used for as the header of the uploaded file. nothing more
	 */
	public class CulumnModel implements Serializable {
		private String fieldName;

		public CulumnModel(String fieldName) {
			this.fieldName = fieldName;
		}

		public String getFieldName() {
			return fieldName;
		}

		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}
	}

	/**
	 * This class meant to be used to load the diagramModel data field.
	 */
	public class ElementData implements Serializable {
		private String inputId;

		private String inputType;

		private String label;

		private String validation;

		private String value;

		private Long formId;

		private String fieldName;

		private boolean type = true;

		public ElementData() {

		}

		public ElementData(Long formId) {
			this.formId = formId;
		}

		public ElementData(String fieldName) {
			this.fieldName = fieldName;
			type = false;
		}

		public ElementData(Long formId, String inputId) {
			this.formId = formId;
			this.inputId = inputId;
		}

		public ElementData(Long formId, String inputId, String validation) {
			this.formId = formId;
			this.inputId = inputId;
			this.validation = validation;
		}

		public ElementData(Long formId, String inputId, String validation, String label) {
			this.formId = formId;
			this.inputId = inputId;
			this.validation = validation;
			this.label = label;
		}

		public String getInputId() {
			return inputId;
		}

		public void setInputId(String InputId) {
			this.inputId = InputId;
		}

		public String getInputType() {
			return inputType;
		}

		public void setInputType(String InputType) {
			this.inputType = InputType;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String Label) {
			this.label = Label;
		}

		public String getValidation() {
			return validation;
		}

		public void setValidation(String validation) {
			this.validation = validation;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public Long getFormId() {
			return formId;
		}

		public void setFormId(Long formId) {
			this.formId = formId;
		}

		public String getFieldName() {
			return fieldName;
		}

		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}

		public boolean isType() {
			return type;
		}

		public void setType(boolean type) {
			this.type = type;
		}
	}
}
