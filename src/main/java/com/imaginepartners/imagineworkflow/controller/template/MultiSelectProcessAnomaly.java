package com.imaginepartners.imagineworkflow.controller.template;

import com.imaginepartners.imagineworkflow.controller.TaskController;
import com.imaginepartners.imagineworkflow.form.FormTemplate;
import com.imaginepartners.imagineworkflow.models.IwTree;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import org.activiti.engine.identity.User;
import org.apache.log4j.LogManager;
import org.primefaces.component.treetable.TreeTable;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("processAnomaly")
@Scope("view")
public class MultiSelectProcessAnomaly extends FormTemplate implements Serializable{

	@Autowired
	private TaskController taskController;

	private TreeNode root;

	private TreeNode selectedIwTree;

	private List<User> listUsers = new ArrayList<User>();

	@Override
	@PostConstruct
	public void init() {
		putVars();
		if(varValues.get("root") != null) {
			root= (TreeNode) varValues.get("root");
			List<String> listIds= (List<String>) varValues.get("listUsers");
			listUsers= activitiService.getUserListIn(listIds);
		}else{
			root= new DefaultTreeNode(defaultIwTreeObject(), null);
			for(IwTree tree: businessService.getAllParent())
				walkTree(tree, new DefaultTreeNode(tree, root) );
		}
	}

	@Override
	public void putVars() {
		LogManager.getLogger(MultiSelectProcessAnomaly.class).info("inject bean properties.");
		taskController.registerTemplateByBeanInstance("processAnomaly", new String[]{"root","listUsers"}, this );
	}

	/**
	 * Set the values of the template into the varValues Object.
	 * varValues object will be stored in Act_RU_VAR
	 */
	private void persisteValues(){
		varValues.put("root", root);
		varValues.put("listUsers", extractIds( listUsers ) );
	}

	/**
	 * @param tree acts as a parent.
	 * @param parentElement acts as a parent
	 */
	private void walkTree(IwTree tree, TreeNode parentElement){
		List<IwTree> children= businessService.getChilds(tree);
		for(Iterator<IwTree> iterator= children.iterator(); iterator.hasNext(); ){
			IwTree childTree= iterator.next();
			TreeNode childElement= new DefaultTreeNode("no", childTree, parentElement);
			walkTree(childTree, childElement);
		}
	}

	private IwTree defaultIwTreeObject(){
		IwTree tree= new IwTree(0);
		tree.setDescription("empty node");
		tree.setTitle("sélectionner");
		tree.setParent(null);
		tree.setIwTreeList(null);
		return tree;
	}

	public boolean stringToBoolean(String str){
		return str.toLowerCase().contains("yes");
	}

	public void onToggleValue(TreeNode toggledNode){
		if(! Boolean.parseBoolean( toggledNode.getType().toLowerCase() ) )
			removeFromListUsers(((IwTree)toggledNode.getData() ).getDescription() );
		else
			addToListUsers( ((IwTree)toggledNode.getData() ).getDescription() );
		persisteValues();
	}

	private void addToListUsers(String groupeId){
		List<User> users= activitiService.getMemerbs(groupeId);
		for(User user: users){
			if(!listUsers.contains(user))
				listUsers.add(user);
		}
	}

	private void removeFromListUsers(String groupeId){
		List<User> users= activitiService.getMemerbs(groupeId);
		List<String> extractedIds= extractIds(users);
		for(Iterator<User> iterator= listUsers.iterator(); iterator.hasNext(); ){
			User user= iterator.next();
			if(extractedIds.contains(user.getId()))
				iterator.remove();
		}
	}

	private List<String> extractIds(List<User> listUsers){
		List<String> ids= new ArrayList<String>();
		for(User user: listUsers)
			ids.add(user.getId());
		return ids;
	}

	public void onSelectEvent(SelectEvent event){
		persisteValues();
	}

	public void onUnselectEvent(UnselectEvent event){
		persisteValues();
	}

	public void onNodeSelectEvent(NodeSelectEvent event) {
		selectedIwTree.setExpanded(!selectedIwTree.isExpanded());
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public List<User> getListUsers() {
		return listUsers;
	}

	public void setListUsers(List<User> listUsers) {
		this.listUsers = listUsers;
	}

	public TreeNode getSelectedIwTree() {
		return selectedIwTree;
	}

	public void setSelectedIwTree(TreeNode selectedIwTree) {
		this.selectedIwTree = selectedIwTree;
	}
}
