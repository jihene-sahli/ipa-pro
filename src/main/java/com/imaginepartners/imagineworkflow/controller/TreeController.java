package com.imaginepartners.imagineworkflow.controller;

import com.imaginepartners.imagineworkflow.models.IwTree;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import org.apache.log4j.LogManager;
import org.primefaces.event.RowEditEvent;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.activiti.engine.identity.Group;
import org.apache.commons.collections.ListUtils;

@Component("treeController")
@Scope("view")
public class TreeController implements InitializingBean, Serializable{

	@Autowired
	private BusinessService businessService;

	@Autowired
	private ActivitiService activitiService;

	private List<IwTree> leafs;

	private List<IwTree> allTrees;

	private List<IwTree> notLeafs;

	private ArrayList<String> allGroupeIds;

	private IwTree newOne;

	/**
	 * acts as PostContruct in java server faces tech.
	 * @throws Exception
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		leafs= new ArrayList<IwTree>();
		allGroupeIds= (ArrayList<String>) extractIds(activitiService.getAllGroupList());
		for(IwTree tree: businessService.getAllParent())
			walkTree(tree);
		allTrees = new ArrayList<IwTree>();
		for (Object obj : businessService.getEntiteList(IwTree.class.getName()))
			allTrees.add((IwTree) obj);
		notLeafs = ListUtils.subtract(leafs, allTrees);
	}

	/**
	 * in order to find all leafs we must iterate over the tree.
	 * @param tree to recursevelly find and get sub trees.
	 */
	private void walkTree(IwTree tree){
		List<IwTree> children= businessService.getChilds(tree);
		for(Iterator<IwTree> iterator= children.iterator(); iterator.hasNext(); ){
			IwTree childTree= iterator.next();
			List<IwTree> childs= businessService.getChilds(childTree);
			if(childs.isEmpty())
				leafs.add(childTree);
			walkTree(childTree);
		}
	}

	public void onRowEdit(RowEditEvent event, IwTree tree){
		businessService.saveEntite(tree);
	}

	public void onRowEditCanceled(AjaxBehaviorEvent event){

	}

	public void onChange(AjaxBehaviorEvent event){

	}

	private List<String> extractIds(List<Group> groupes){
		List<String> ids= new ArrayList<String>();
		for(Group group:groupes)
			ids.add(group.getId());
		return ids;
	}

	public void onParentSelected(AjaxBehaviorEvent event) {

	}

	public void createInstance(ActionEvent event) {
		newOne = new IwTree();
	}

	public void onAddChild(ActionEvent event) {
		businessService.saveEntite(newOne);
		createInstance(event);
	}

	public List<IwTree> getLeafs() {
		return leafs;
	}

	public void setLeafs(List<IwTree> leafs) {
		this.leafs = leafs;
	}

	public ArrayList<String> getAllGroupeIds() {
		return allGroupeIds;
	}

	public void setAllGroupeIds(ArrayList<String> allGroupeIds) {
		this.allGroupeIds = allGroupeIds;
	}

	public List<IwTree> getNotLeafs() {
		return notLeafs;
	}

	public void setNotLeafs(List<IwTree> notLeafs) {
		this.notLeafs = notLeafs;
	}

	public List<IwTree> getAllTrees() {
		return allTrees;
	}

	public void setAllTrees(List<IwTree> allTrees) {
		this.allTrees = allTrees;
	}

	public IwTree getNewOne() {
		return newOne;
	}

	public void setNewOne(IwTree newOne) {
		this.newOne = newOne;
	}
}
