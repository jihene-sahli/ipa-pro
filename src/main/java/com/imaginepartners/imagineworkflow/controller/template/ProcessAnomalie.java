package com.imaginepartners.imagineworkflow.controller.template;

import com.imaginepartners.imagineworkflow.controller.TaskController;
import com.imaginepartners.imagineworkflow.form.FormTemplate;
import com.imaginepartners.imagineworkflow.models.IwTree;
import java.io.Serializable;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.event.AjaxBehaviorEvent;
import org.activiti.engine.identity.User;

import org.apache.log4j.LogManager;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.event.ReorderEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("processAnomalie")
@Scope("view")
public class ProcessAnomalie extends FormTemplate implements Serializable{

	@Autowired
	private TaskController taskController;

	private Map<IwTree, ArrayList<IwTree>> slot= new LinkedHashMap<IwTree, ArrayList<IwTree>>();

	private List<IwTree> selectedEntries=new ArrayList<IwTree>();

	private AjaxBehaviorEvent event;

	private IwTree emptyTree;

	private boolean showPickList;
	private DualListModel<IwTree> elements= new DualListModel();

	private List<IwTree> pickListElements= new ArrayList<IwTree>();

	private List<IwTree> treeSource= new ArrayList<IwTree>();

	private List<IwTree> treeTarget= new ArrayList<IwTree>();

	private List<User> sourceUsers= new ArrayList<User>();

	private List<User> targetUsers= new ArrayList<User>();

	private List<User> pickListUsers= new ArrayList<User>();

	private DualListModel<User> listUsers= new DualListModel<User>();

	private String[] persisteValuesName= new String[]{"showPickList","slot","treeSource","treeTarget"};

	/**
	 * acts as PostContruct.
	 * since we have been using persisted bytes values, we can use custom bean creation from the taskController register bean method.
	 */
	@Override
	@PostConstruct
	public void init() {
		putVars();
		if(varValues.get("slot") != null) {
			slot= (Map<IwTree, ArrayList<IwTree>>) varValues.get("slot");
			treeSource= (List<IwTree>) varValues.get("treeSource");
			treeTarget= (List<IwTree>) varValues.get("treeTarget");
			showPickList= (boolean) varValues.get("showPickList");
			initSelectedEntries();
			pickListElements.addAll(treeSource);
			pickListElements.addAll(treeTarget);
			elements= new DualListModel<IwTree>(treeSource, treeTarget);
			initUsers();
		} else {
			emptyTree= defaultIwTreeObject();
			List<IwTree> firstLevel= businessService.getAllParent();
			firstLevel.add(0, emptyTree);
			selectedEntries.add(emptyTree);
			slot.put(emptyTree, (ArrayList<IwTree>) firstLevel);
		}
	}

	/**
	 * Initialize persisted bean properties.
	 * varValues is the necessary object that holds the persisted values.
	 * initialize varValues object using taskId, or taskHistId. according to the current task.
	 */
	@Override
	public void putVars() {
		taskController.registerTemplateByBeanInstance("processAnomalie", persisteValuesName, this );
	}

	private void initUsers() {
		pickListUsers.clear();
		targetUsers.clear();
		sourceUsers.clear();
		for(IwTree tree: elements.getTarget()) {
			targetUsers.addAll( activitiService.getMemerbs( tree.getDescription() ) );
		}
		for(IwTree tree: elements.getSource()) {
			sourceUsers.addAll( activitiService.getMemerbs( tree.getDescription() ) );
		}
		pickListUsers.addAll(sourceUsers);
		pickListUsers.addAll(targetUsers);
		listUsers= new DualListModel<User>(sourceUsers, targetUsers);
	}

	/**
	 * Set the values of the template into the varValues Object.
	 * varValues object will be stored in Act_RU_VAR
	 */
	private void persisteValues() {
		initUsers();
		varValues.put("slot", slot);
		varValues.put("treeSource", elements.getSource());
		varValues.put("treeTarget", elements.getTarget());
		varValues.put("showPickList", showPickList);
		varValues.put("listUsers", extractIds());
	}

	private List<String> extractIds() {
		List<String> ids= new ArrayList<String>();
		for(User user: listUsers.getTarget())
			ids.add(user.getId());
		return ids;
	}

	/**
	 * Trigger onTrensfer method when the user transfer an element in the pickList component
	 * @param event
	 */
	public void onTransfer(TransferEvent event) {
		persisteValues();
	}

	public void onSelect(SelectEvent event) {
		persisteValues();
	}

	public void onUnselect(UnselectEvent event) {
		persisteValues();
	}

	public void onReorder(ReorderEvent event) {
		persisteValues();
	}

	/**
	 *
	 * @param event ajaxBehaviorEvent is the heights level of all ajax event in JSF tech
	 */
	public void onChange(AjaxBehaviorEvent event) {
		this.event= event;
		Object selectedValue= ((SelectOneMenu) event.getComponent()).getValue();
		int level = getLevel((IwTree) selectedValue);
		List<IwTree> oldList= businessService.getParent((IwTree) selectedValue);
		if(slot.containsKey(emptyTree)) {
			if( slot.size() == level) {
				slot.remove(emptyTree);
				slot.put((IwTree)selectedValue, (ArrayList<IwTree>) oldList);
				List<IwTree> newList= (ArrayList<IwTree>) businessService.getChilds((IwTree) businessService.getEntitytById(IwTree.class.getName(), ((IwTree)selectedValue ).getChild().toString()));
				showPickList= businessService.getChilds(newList.get(0)).isEmpty() ;
				if(showPickList) {
					pickListElements= newList;
					treeSource= newList;
					elements= new DualListModel<IwTree>(treeSource, treeTarget);
				}
				if(newList != null && !newList.isEmpty() && !showPickList) {
					newList.add(0, emptyTree);
					selectedEntries.add(emptyTree);
					slot.put(emptyTree, (ArrayList<IwTree>) newList);
				}
			}
		} else {
			updateElement(level);
		}
		persisteValues();
	}

	private int getLevel(IwTree node) {
		int co=-1;
		for(Map.Entry<IwTree, ArrayList<IwTree>> entry: slot.entrySet()) {
			co++;
			for(IwTree tree: entry.getValue()) {
				if(tree.equals(node))
					return ++co;
			}
		}
		return 0;
	}

	private void updateElement(int index) {
		Iterator it= slot.entrySet().iterator();
		List<IwTree> storedList= new ArrayList<IwTree>();
		int level=0;
		while(it.hasNext()) {
			Map.Entry entry= (Map.Entry) it.next();
			level++;
			if(level >= index) {
				selectedEntries.remove(entry.getKey());
				it.remove();
				if(level == index) {
					storedList= (List<IwTree>) entry.getValue();
				}
			}
		}
		slot.put(emptyTree, (ArrayList<IwTree>) storedList);
		onChange(event);
	}

	private boolean isContainsNode(IwTree node) {
		for(Map.Entry<IwTree, ArrayList<IwTree>> entry: slot.entrySet()) {
			for(IwTree tree: entry.getValue())
				if(tree.equals(node))
					return true;
		}
		return false;
	}

	private IwTree defaultIwTreeObject() {
		IwTree tree= new IwTree(0);
		tree.setDescription("empty node");
		tree.setTitle("selectionner");
		tree.setParent(null);
		tree.setIwTreeList(null);
		return tree;
	}

	private void initSelectedEntries() {
		for(Map.Entry<IwTree, ArrayList<IwTree>> entry: slot.entrySet()) {
			selectedEntries.add(entry.getKey());
		}
	}

	public Map<IwTree, ArrayList<IwTree>> getSlot() {
		return slot;
	}

	public void setSlot(Map<IwTree, ArrayList<IwTree>> slot) {
		this.slot = slot;
	}

	public List<IwTree> getSelectedEntries() {
		return selectedEntries;
	}

	public void setSelectedEntries(List<IwTree> selectedEntries) {
		this.selectedEntries = selectedEntries;
	}

	public boolean isShowPickList() {
		return showPickList;
	}

	public void setShowPickList(boolean showPickList) {
		this.showPickList = showPickList;
	}

	public DualListModel<IwTree> getElements() {
		return elements;
	}

	public void setElements(DualListModel<IwTree> elements) {
		this.elements = elements;
	}

	public List<IwTree> getPickListElements() {
		return pickListElements;
	}

	public void setPickListElements(List<IwTree> pickListElements) {
		this.pickListElements = pickListElements;
	}

	public DualListModel<User> getListUsers() {
		return listUsers;
	}

	public void setListUsers(DualListModel<User> listUsers) {
		this.listUsers = listUsers;
	}

	public List<User> getPickListUsers() {
		return pickListUsers;
	}

	public void setPickListUsers(List<User> pickListUsers) {
		this.pickListUsers = pickListUsers;
	}

	/**
	 * Try to build a tree for given parent object.
	 * @param parent IwTree parent object.
	 */
	private String buildTree(IwTree parent) {
		return null;
	}
}
