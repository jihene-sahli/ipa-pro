package com.imaginepartners.imagineworkflow.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imaginepartners.imagineworkflow.models.IwForm;
import com.imaginepartners.imagineworkflow.models.IwInput;
import com.imaginepartners.imagineworkflow.models.IwVariableTemplate;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.services.GenerateDocService;
import com.imaginepartners.imagineworkflow.util.AppConstants;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import org.activiti.engine.repository.ProcessDefinition;
import org.docx4j.samples.AbstractSample;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.faces.event.AjaxBehaviorEvent;
import java.io.*;
import java.util.*;

/**
 * Created by lenovo on 31/01/2017.
 */
@Controller("documentController")
@Scope("view")
public class DocumentController extends AbstractSample implements Serializable {

	@Autowired
	private GenerateDocService generateDocService;
	@Autowired
	private BusinessService businessService;
	@Autowired
	private ActivitiService activitiService;
    private String inputfilepath;
    private UploadedFile doc;
	private String path;
	private  boolean save = true;
    private IwForm selectedForm;
    private ObjectMapper mapper;
    private List<IwInput> inputList = new ArrayList<IwInput>();
    private IwInput selectedInput;
	private String toFind;
	private String replacer;
	private ProcessDefinition selectedProcDef;
    private Map.Entry<String,Object> selectedEntry;
    private Map<String, Object> map;
	private List<String> varList;
    private String selectedVar;
	private String selectedDoc;
	private File repertoire;
	private List <String> docList;
	private IwVariableTemplate selectedVariableTemplate;

	public DocumentController() {
	}

	@PostConstruct
	public void init() {

		path= AppConstants.DEFAULT_REPORTS_PATH;
		 mapper = new ObjectMapper();
		repertoire = new File(path);
		if (!repertoire.exists()) {
			repertoire.mkdirs();
			doGetDocList();
		}else {
			doGetDocList();
		}

	}


	public void onFormRowSelect(SelectEvent event) throws IOException {
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		List<IwInput> editorIwInputList =  mapper.readValue(selectedForm.getIwFormSourceJson(),new TypeReference<List<IwInput>>() {});
		Iterator<IwInput> iwInputsIterator = selectedForm.getIwInputList().iterator();
		while (iwInputsIterator.hasNext()) {
			IwInput iwInput = iwInputsIterator.next();
			int indexOfIwInput = iwInputListIndex(editorIwInputList, iwInput);
			if (indexOfIwInput < 0) {
				iwInputsIterator.remove();
			}
		}
		for (IwInput iwInput : editorIwInputList) {
			iwInput.setIwForm(selectedForm);
			int indexOfIwInput = iwInputListIndex(selectedForm.getIwInputList(), iwInput);
			if (indexOfIwInput >= 0) {
				selectedForm.getIwInputList().set(indexOfIwInput, iwInput);
			} else {
				selectedForm.getIwInputList().add(iwInput);
			}
		}
            inputList=selectedForm.getIwInputList();
	}

	public void onInputRowSelect(SelectEvent event) {
		this.replacer=selectedInput.getId();
		 RequestContext.getCurrentInstance().execute("PF('dialogVar').show();");
	}

	public void onVarTemplateRowSelect(SelectEvent event) {
		this.replacer=selectedVariableTemplate.getId();
		RequestContext.getCurrentInstance().execute("PF('dialogVar').show();");
	}

	public void onDefRowSelect(SelectEvent event) {

		map= new HashMap<String,Object>() ;
		varList= new ArrayList<String>();
		map =activitiService.getVariablesProcess(selectedProcDef.getKey());

		for(Map.Entry<String, Object> entry : map.entrySet()) {
            varList.add(entry.getKey());
		}
		RequestContext.getCurrentInstance().execute("PF('dialogVar').show();");

	}

	public void onVarRowSelect( AjaxBehaviorEvent event) {

		this.replacer=selectedVar;

	}

	public void onDocRadioSelect( AjaxBehaviorEvent event) {

       inputfilepath=path+selectedDoc;

	}

	public int iwInputListIndex(List<IwInput> iwInputList, IwInput iwInput) {
		for (IwInput item : iwInputList) {
			if (iwInput.getIwinputrealid()!= null && iwInput.getIwinputrealid().equals(item.getIwinputrealid())) {
				return iwInputList.indexOf(item);
			}
		}
		return -2;
	}

	public void handleFileUpload(FileUploadEvent e) throws IOException {

		// Get uploaded file from the FileUploadEvent
		this.doc = e.getFile();

		   copyFile(e.getFile().getFileName(), e.getFile().getInputstream());
		   inputfilepath=path+doc.getFileName();
           doGetDocList();
	}

	public void copyFile(String fileName, InputStream in) {
		try {

			// write the inputStream to a FileOutputStream
			OutputStream out = new FileOutputStream(new File(path + fileName));

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = in.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}

			in.close();
			out.flush();
			out.close();

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public void doAddIdsToDocx( ) throws Exception {

       if (inputfilepath != null && toFind != null && replacer != null){

          generateDocService.addIdsToDocx(inputfilepath,toFind,replacer);
		   FacesUtil.setAjaxInfoMessage("Votre document est modifié avec succes");

	   }else {
		   FacesUtil.setAjaxErrorMessage("Veuillez vérifier vos données");
	   }
	}

	public void doGetDocList(){
		docList= new ArrayList<String>();

		for (String doc : repertoire.list()) {
			if (doc.endsWith(".docx")  && !doc.startsWith("~")) {
				docList.add(doc);
			}
		}
	}


	public GenerateDocService getGenerateDocService() {
		return generateDocService;
	}

	public void setGenerateDocService(GenerateDocService generateDocService) {
		this.generateDocService = generateDocService;
	}

	public String getInputfilepath() {
		return inputfilepath;
	}

	public void setInputfilepath(String inputfilepath) {
		this.inputfilepath = inputfilepath;
	}

	public UploadedFile getDoc() {
		return doc;
	}

	public void setDoc(UploadedFile doc) {
		this.doc = doc;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public BusinessService getBusinessService() {
		return businessService;
	}

	public void setBusinessService(BusinessService businessService) {
		this.businessService = businessService;
	}

	public IwForm getSelectedForm() {
		return selectedForm;
	}

	public void setSelectedForm(IwForm selectedForm) {
		this.selectedForm = selectedForm;
	}

	public ObjectMapper getMapper() {
		return mapper;
	}

	public void setMapper(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	public List<IwInput> getInputList() {
		return inputList;
	}

	public void setInputList(List<IwInput> inputList) {
		this.inputList = inputList;
	}

	public IwInput getSelectedInput() {
		return selectedInput;
	}

	public void setSelectedInput(IwInput selectedInput) {
		this.selectedInput = selectedInput;
	}

	public String getToFind() {
		return toFind;
	}

	public void setToFind(String toFind) {
		this.toFind = toFind;
	}

	public String getReplacer() {
		return replacer;
	}

	public void setReplacer(String replacer) {
		this.replacer = replacer;
	}

	public ActivitiService getActivitiService() {
		return activitiService;
	}

	public void setActivitiService(ActivitiService activitiService) {
		this.activitiService = activitiService;
	}

	public ProcessDefinition getSelectedProcDef() {
		return selectedProcDef;
	}

	public void setSelectedProcDef(ProcessDefinition selectedProcDef) {
		this.selectedProcDef = selectedProcDef;
	}

	public List<String> getVarList() {
		return varList;
	}

	public void setVarList(List<String> varList) {
		this.varList = varList;
	}

	public Map.Entry<String, Object> getSelectedEntry() {
		return selectedEntry;
	}

	public void setSelectedEntry(Map.Entry<String, Object> selectedEntry) {
		this.selectedEntry = selectedEntry;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public String getSelectedVar() {
		return selectedVar;
	}

	public void setSelectedVar(String selectedVar) {
		this.selectedVar = selectedVar;
	}

	public String getSelectedDoc() {
		return selectedDoc;
	}

	public void setSelectedDoc(String selectedDoc) {
		this.selectedDoc = selectedDoc;
	}

	public File getRepertoire() {
		return repertoire;
	}

	public void setRepertoire(File repertoire) {
		this.repertoire = repertoire;
	}

	public List<String> getDocList() {
		return docList;
	}

	public void setDocList(List<String> docList) {
		this.docList = docList;
	}

	public IwVariableTemplate getSelectedVariableTemplate() {
		return selectedVariableTemplate;
	}

	public void setSelectedVariableTemplate(IwVariableTemplate selectedVariableTemplate) {
		this.selectedVariableTemplate = selectedVariableTemplate;
	}
}
