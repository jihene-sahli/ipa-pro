package com.imaginepartners.imagineworkflow.controller;

import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.apache.commons.collections.CollectionUtils;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafik on 14/09/16.
 */

@Controller
@Scope("session")
public class PickListController implements Serializable {

	@Autowired
	private ActivitiService activitiService;


	public void handlePickListTransfer(TransferEvent event) {

		org.primefaces.component.picklist.PickList pickList = (org.primefaces.component.picklist.PickList) event.getSource();
		DualListModel dualList = (DualListModel)pickList.getValue();

		List source = dualList.getSource();
		List target = dualList.getTarget();
		List items = (List)event.getItems();
		FacesContext context = FacesContext.getCurrentInstance();
		String maxSelect = context.getApplication().evaluateExpressionGet(context, "#{formWrapper.value.iwInput.maxSelect}", String.class);
		String component = context.getApplication().evaluateExpressionGet(context, "#{formWrapper.value.iwInput.component}", String.class);
		String value = context.getApplication().evaluateExpressionGet(context, "#{formWrapper.value.iwInput.value}", String.class);

		int maxInputSize;
		try {
			maxInputSize = Integer.parseInt(maxSelect);
		} catch (Exception e) {
			maxInputSize = 0;
		}

		if("picklist".equals(component)){
			if(maxInputSize != 0 && target.size() > maxInputSize && event.isAdd()){
				FacesUtil.setAjaxInfoMessage("Vous ne pouveaz pas selectionner plus de "+maxInputSize+" element(s)");
				DualListModel newDualList = new DualListModel((List) CollectionUtils.union(source, items), (List) CollectionUtils.subtract(target, items));
				pickList.setValue(newDualList);
			}
		}else {
			List newSource = new ArrayList();
			List newTarget = new ArrayList();
			Boolean ecceded = false;
			if("group".equals(value)) {
				List groupIds = new ArrayList<String>();
				for(Group group : (List<Group>)items){
					groupIds.add(group.getId());
				}
				for (Group group : (List<Group>)target) {
					if (maxInputSize != 0 && target.size() > maxInputSize && groupIds.contains(group.getId()) && event.isAdd()) {
						newSource.add(group);
						ecceded = true;
					} else
						newTarget.add(group);
				}
			}
			else{
				List userIds = new ArrayList<String>();
				for(User user : (List<User>)items){
					userIds.add(user.getId());
				}
				for (User user : (List<User>)target) {
					if (maxInputSize != 0 && target.size() > maxInputSize && userIds.contains(user.getId()) && event.isAdd()) {
						newSource.add(user);
						ecceded = true;
					} else
						newTarget.add(user);
				}
			}

			if(ecceded) {
				FacesUtil.setAjaxInfoMessage("Vous ne pouveaz pas selectionner plus de " + maxInputSize + " element(s)");
				DualListModel newDualList = new DualListModel(newSource, newTarget);
				pickList.setValue(newDualList);
			}
		}
	}

}
