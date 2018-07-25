package com.imaginepartners.imagineworkflow.controller;

import com.imaginepartners.imagineworkflow.models.IwList;
import com.imaginepartners.imagineworkflow.models.IwListOptions;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.DisposableBean;

@Controller
@Scope("view")
public class ListController implements DisposableBean, Serializable {

	@Autowired
	private transient BusinessService businessService;

	private List<IwList> mainListNames;

	private List<IwList> singleListNames;

	private List<IwList> allLists;

	private TreeNode root;

	private TreeNode[] selectedNodes;

	private IwListOptions selectedOption;

	private IwList selectedList;

	private Long listId;

	private String newValue;

	private Boolean registerListe;

	public ListController() {

	}

	/**
	 * Default initialize method
	 */
	@PostConstruct
	public void init() {
		if (registerListe != null && registerListe == false) {
			registerListe = true;
		} else {
			registerListe = false;
		}
		mainListNames = new ArrayList<IwList>();
		allLists = businessService.getIwListList();
		HttpServletRequest servletReq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String res = servletReq.getParameter("listId");
		if (res != null)
			this.listId = Long.valueOf(servletReq.getParameter("listId"));
		if (listId != null) {
			selectedList = businessService.getIwListById(listId);
			if (selectedList != null) {
				singleListNames = new ArrayList<IwList>();
				singleListNames.add(selectedList);
			}
		}
	}

	public void initList() {
		if (listId != null) {
			selectedList = businessService.getIwListById(listId);
			if (selectedList != null) {
				singleListNames = new ArrayList<IwList>();
				singleListNames.add(selectedList);
			}
		}
	}

	/**
	 * Handling FacesMessage Object
	 */
	public void displayMessages() {
		if (selectedNodes != null && selectedNodes.length > 0) {
			StringBuilder str = new StringBuilder();
			for (TreeNode node : selectedNodes) {
				if (node.getData() instanceof IwList)
					str.append("liste: " + ((IwList) node.getData()).getIwName());
				if (node.getData() instanceof IwListOptions) {
					str.append("sous Liste:" + ((IwListOptions) node.getData()).getIwName());
				}
				str.append("<br />");
			}
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "liste(s) sélectionnée(s)", str.toString());
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	/**
	 * @param list initialize the listOption belongs to the passed list as an argument
	 * @return the listOption object belongs to the passing list parameter
	 */
	public List<IwListOptions> listOption(IwList list) {
		list.setIwListOptionsList(businessService.getIwListOptionsListByIwList(list));
		return list.getIwListOptionsList();
	}

	public void editNode() {
		if (selectedList != null) {
			selectedList.setIwName(newValue);
			businessService.saveEntite(selectedList);
		}
		if (selectedOption != null) {
			selectedOption.setIwName(newValue);
			businessService.saveEntite(selectedOption);
		}
		newValue = "";
	}

	/**
	 * @param selectedNodeObject : set the current selected Object passing as Object argument
	 */
	public void refreshSelection(Object selectedNodeObject) {
		if (selectedNodeObject instanceof IwList) {
			this.selectedList = (IwList) selectedNodeObject;
			this.selectedOption = null;
		}
		if (selectedNodeObject instanceof IwListOptions) {
			this.selectedOption = (IwListOptions) selectedNodeObject;
			this.selectedList = null;
		}
	}

	/**
	 * Add new IwList entity to the current ArrayList<IwList>
	 */
	public void add() {
		IwList list = new IwList();
		list.setIwListOptionsList(new ArrayList<IwListOptions>());
		mainListNames.add(list);
		this.registerListe = true;
		list.getIwListOptionsList().add(new IwListOptions());
	}

	/**
	 * @param list: remove the list argument from the global ArrayList<IwList>
	 */
	public void remove(IwList list, int rowwIndex) {
		if (mainListNames != null) {
			mainListNames.remove(rowwIndex);
			this.registerListe = false;
		}
	}

	/**
	 * @param list: add new empty listOption object to list parameter
	 */
	public void addOption(IwList list) {
		IwListOptions test2 = new IwListOptions();
		list.getIwListOptionsList().add(new IwListOptions());
	}

	public void removeOption(IwList list, IwListOptions option) {
		Iterator i = list.getIwListOptionsList().iterator();
		while (i.hasNext()) {
			IwListOptions opt = (IwListOptions) i.next();
			if (opt.getIwName().equals(option.getIwName())) {
				businessService.removeEntite(opt);
				i.remove();
				break;
			}
		}
	}

	public void saveObject(Object obj) {
		if (obj instanceof IwList)
			businessService.saveEntite(obj);
		init();
	}

	public String save(IwList currentList) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		currentList.setIwPostedDate(date);
		if (currentList.getIwDescirption() != null && !currentList.getIwListOptionsList().isEmpty() && currentList.getIwName() != null) {
			businessService.saveEntite(currentList);
			Iterator i = currentList.getIwListOptionsList().iterator();
			while (i.hasNext()) {
				IwListOptions opt = (IwListOptions) i.next();
				opt.setIwListId(currentList);
				businessService.saveEntite(opt);
			}
		}
		allLists = businessService.getIwListList();
		return "liste?faces-redirect=true";
	}

	public String saveSelectedList() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		selectedList.setIwPostedDate(date);
		if (selectedList.getIwDescirption() != null && !selectedList.getIwListOptionsList().isEmpty() && selectedList.getIwName() != null) {
			businessService.saveEntite(selectedList);
			Iterator i = selectedList.getIwListOptionsList().iterator();
			while (i.hasNext()) {
				IwListOptions opt = (IwListOptions) i.next();
				opt.setIwListId(selectedList);
				businessService.saveEntite(opt);
			}
		}
		allLists = businessService.getIwListList();
		return "liste?faces-redirect=true";
	}

	public void saveIwList(IwList currentList) {
		businessService.saveEntite(currentList);
	}

	public void delete(IwList list) {
		businessService.removeList(list);
		allLists = businessService.getIwListList();
	}

	public boolean filled() {
		return !allLists.isEmpty();
	}

	public List<IwList> getMainListNames() {
		return mainListNames;
	}

	public void setMainListNames(List<IwList> mainListNames) {
		this.mainListNames = mainListNames;
	}

	public List<IwList> getAllLists() {
		return allLists;
	}

	public void setAllLists(List<IwList> allLists) {
		this.allLists = allLists;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public TreeNode[] getSelectedNodes() {
		return selectedNodes;
	}

	public void setSelectedNodes(TreeNode[] selectedNodes) {
		this.selectedNodes = selectedNodes;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public IwListOptions getSelectedOption() {
		return selectedOption;
	}

	public void setSelectedOption(IwListOptions selectedOption) {
		this.selectedOption = selectedOption;
	}

	public IwList getSelectedList() {
		return selectedList;
	}

	public void setSelectedList(IwList selectedList) {
		this.selectedList = selectedList;
	}

	public List<IwList> getSingleListNames() {
		return singleListNames;
	}

	public void setSingleListNames(List<IwList> singleListNames) {
		this.singleListNames = singleListNames;
	}

	public Long getListId() {
		return listId;
	}

	public void setListId(Long listId) {
		this.listId = listId;
	}

	public Boolean getRegisterListe() {
		return registerListe;
	}

	public void setRegisterListe(Boolean registerListe) {
		this.registerListe = registerListe;
	}

	@Override
	public void destroy() throws Exception {

	}
}
