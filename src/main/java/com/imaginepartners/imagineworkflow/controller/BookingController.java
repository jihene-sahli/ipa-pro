package com.imaginepartners.imagineworkflow.controller;

import com.imaginepartners.imagineworkflow.models.IwBooking;
import com.imaginepartners.imagineworkflow.models.IwCar;
import com.imaginepartners.imagineworkflow.models.IwResource;
import com.imaginepartners.imagineworkflow.models.IwResourceType;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.services.impl.UserService;
import com.imaginepartners.imagineworkflow.util.ConfigConstants;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import com.imaginepartners.imagineworkflow.util.Util;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.event.AjaxBehaviorEvent;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.context.RequestContext;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller("bookingController")
@Scope("view")
public class BookingController implements Serializable {

	private Date navigationDate;

	private List<String> eventSelected;

	private ScheduleModel eventModel;

	private List<DefaultScheduleEvent> listEvents;

	private String searchFilter;

	private DefaultScheduleEvent event;

	private String idGroup;

	private String resourceId;

	@Autowired
	private UserService userService;

	@Autowired
	private BusinessService businessService;

	@Autowired
	private ActivitiService activitiService;

	private IwResource currentResource;

	private List<IwResourceType> resourceAttribute;

	private IwResourceType resourceTypeSelected;

	private Object objectSelect;

	private List listResource= new ArrayList();

	private boolean disableResourceEntity= true;

	private String idAttributeName="iwCarId";

	@PostConstruct
	public void init() {
		resourceId = FacesUtil.getUrlParam("resource");
		eventModel = getScheduleModel();
		listEvents = new ArrayList<DefaultScheduleEvent>();
		eventSelected = new ArrayList<String>();
		navigationDate = new Date();
		searchFilter = "";
		resourceAttribute= businessService.getAllResourceType();
		createEvent();
	}

	public void createEvent() {
		IwBooking currentBooking = new IwBooking();
		currentBooking.setIwAllDay(true);
		currentBooking.setIwStartTime(new Date());
		currentBooking.setIwEndTime(new Date());
		currentResource = new IwResource();
		resourceTypeSelected= new IwResourceType();
		currentResource.setIwEntityName(resourceTypeSelected);
		currentBooking.setIwResourceId(currentResource);
		event = new DefaultScheduleEvent("", currentBooking.getIwStartTime(),
		currentBooking.getIwEndTime(), currentBooking);
	}

	public void onResourceSelect(AjaxBehaviorEvent ajaxEvent) {
		resourceTypeSelected= ((IwBooking)event.getData()).getIwResourceId().getIwEntityName();
		Logger.getLogger(BookingController.class).info("ajax request behavior "+resourceTypeSelected.getIwEntityName());
		SelectOneMenu selectCompoenent = (SelectOneMenu) ajaxEvent.getComponent();
		try{
			String className= resourceTypeSelected.getIwEntityName();
			Class clazz = Class.forName("com.imaginepartners.imagineworkflow.models." + className);
			listResource = new ArrayList();
			for (Object obj : businessService.getListResourceByType(clazz)) {
				listResource.add(obj);
			}
			currentResource.setIwEntityName(resourceTypeSelected);
			idAttributeName= Util.getEntityIdField(clazz).getName();
			disableResourceEntity= false;
		}catch (ClassNotFoundException ex){
			idAttributeName= "";
			disableResourceEntity= true;
			Logger.getLogger(BookingController.class).error("class not found ", ex);
		}
	}

	public void onSelectOneMenu(AjaxBehaviorEvent event) throws IllegalArgumentException, IllegalAccessException{
		SelectOneMenu som= (SelectOneMenu) event.getComponent();
		Logger.getLogger(BookingController.class).info("select one menu for resource "+som.getValue().toString());
		currentResource.setIwEntityId(Integer.valueOf(som.getValue().toString()));
	}

	private Field getSelectedField(String resourceSelect) {
		Field[] fields = IwResource.class.getDeclaredFields();
		List<Field> listFields = Arrays.asList(fields);
		for (Field field : listFields) {
			if (field.getName().toLowerCase().contains(resourceSelect.toLowerCase())) {
				return field;
			}
		}
		return null;
	}

	public ScheduleModel getScheduleModel() {
		LogManager.getLogger("--getScheduleModel--").debug("getScheduleModel called");
		ScheduleModel model = new DefaultScheduleModel();
		DefaultScheduleEvent event;
		List<IwBooking> loanList;
		if (StringUtils.isNotBlank(resourceId)) {
			IwResource resourceFromUrlReq= businessService.getResourceById(Integer.valueOf(resourceId));
			loanList = businessService.getIwBookingForGivenResource(resourceFromUrlReq);
		} else {
			loanList = businessService.getIwBookingList();
		}
		for (IwBooking iwLoan : loanList) {
			LogManager.getLogger("--getScheduleModel--").debug(iwLoan.getIwResourceId().getIwEntityName().getIwDescription());
			event = new DefaultScheduleEvent(iwLoan.getIwResourceId().getIwEntityName().getIwDescription(),
			iwLoan.getIwStartTime(),
			iwLoan.getIwEndTime(),
			iwLoan.getIwAllDay());

			event.setId(String.valueOf(iwLoan.getIwBookingId()));
			event.setData(iwLoan);
			event.setTitle(getEventTitle(iwLoan));
			setStyleClass(iwLoan, event);
			model.addEvent(event);
		}
		return model;
	}

	public List<DefaultScheduleEvent> getListEvents() {
		if (!listEvents.isEmpty()) {
			listEvents.clear();
		}
		for (ScheduleEvent e : eventModel.getEvents()) {
			if (searchFilter.isEmpty() || (e.getTitle().toLowerCase().contains(searchFilter.toLowerCase()))) {
				listEvents.add((DefaultScheduleEvent) e);
			}
		}
		return listEvents;
	}

	public void setListEvents(List<DefaultScheduleEvent> listEvents) {
		this.listEvents = listEvents;
	}

	public List<String> getEventSelected() {
		return eventSelected;
	}

	public void setEventSelected(List<String> eventSelected) {
		this.eventSelected = eventSelected;
	}

	public Date getNavigationDate() {
		return navigationDate;
	}

	public void calendarSelect() {
		RequestContext context = RequestContext.getCurrentInstance();
		context.addCallbackParam("dateCellSelected", new SimpleDateFormat(businessService.getConfigValue(ConfigConstants.SHORT_DATE_PATTERN)).format(navigationDate));
	}

	public void changeSelectList(AjaxBehaviorEvent e) {
		ScheduleEvent eventSchedule = eventModel.getEvent(eventSelected.get(0));
		RequestContext context = RequestContext.getCurrentInstance();
		navigationDate = eventSchedule.getStartDate();
		context.addCallbackParam("dateCellSelected", new SimpleDateFormat("yyyy-MM-dd").format(eventSchedule.getStartDate()));
	}

	public void setNavigationDate(Date navigationDate) {
		this.navigationDate = navigationDate;
	}

	public Date getInitialDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), Calendar.FEBRUARY, calendar.get(Calendar.DATE), 0, 0, 0);
		return calendar.getTime();
	}

	public ScheduleModel getEventModel() {
		return eventModel;
	}

	public ScheduleEvent getEvent() {
		return event;
	}

	public void setEvent(DefaultScheduleEvent event) {
		this.event = event;
	}

	public Object findObject(Integer id){
		if(id != null)
			return businessService.getTupleFromClassById(resourceTypeSelected.getIwEntityName().getClass(), id);
		return null;
	}

	public void addEvent() {
		currentResource.setIwEntityName(resourceTypeSelected);
		List<Field> myList= Arrays.asList(objectSelect.getClass().getDeclaredFields());
		for(Field field: myList){
			field.setAccessible(true);
			if(field.getName().toLowerCase().contains("id") && !field.getName().toLowerCase().contains("serial")){
				try {
					currentResource.setIwEntityId(Integer.valueOf(field.get(objectSelect).toString() ));
				} catch (IllegalArgumentException ex) {
					Logger.getLogger(BookingController.class).error("cant find argument exception", ex);
				} catch (IllegalAccessException ex) {
					Logger.getLogger(BookingController.class).error("cant access attribute", ex);
				}
			}
		}

		businessService.saveIwResource(currentResource);
		((IwBooking)event.getData()).setIwResourceId(currentResource);
		if (event.getEndDate().compareTo(event.getStartDate()) < 0) {
			FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.agneda.datefinsuperieurdatedebut"));
		} else {
			IwBooking iwLoan = (IwBooking) event.getData();
			List<IwBooking> datedLoans = businessService
			.getBookingListForResourceByDate(iwLoan.getIwResourceId().getIdResource(), event.getStartDate(), event.getEndDate());
			if (!datedLoans.isEmpty()) {
				if ("reserved".equals(iwLoan.getIwStatus())) {
					FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.location.voituredejareserveecedate"));
				} else {
					FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.location.voituredejaloueecedate"));
				}
			} else {
				iwLoan.setIwStartTime(event.getStartDate());
				iwLoan.setIwEndTime(event.getEndDate());
				iwLoan.setIwTenant(userService.getLoggedInUser().getId());
				setStyleClass(iwLoan, event);
				if (event.getId() == null) {
					eventModel.addEvent(event);
				} else {
					eventModel.updateEvent(event);
				}
				event.setTitle(getEventTitle(iwLoan));
				businessService.saveIwBooking(iwLoan);
				runLoanProcess(iwLoan);
			}
		}
	}

	public void removeEvent() {
		if (event.getId() != null) {
			eventModel.deleteEvent(event);
		}
		IwBooking iwLoan = (IwBooking) event.getData();
	}

	public void onEventSelect(SelectEvent selectEvent) {
		try {
			event = (DefaultScheduleEvent) selectEvent.getObject();
			String str= ((IwBooking) event.getData()).getIwResourceId().getIwEntityName().getIwEntityName();
			Class clazz = Class.forName("com.imaginepartners.imagineworkflow.models." + str);
			for (Object obj : businessService.getListResourceByType(clazz)) {
				listResource.add(obj);
			}
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(BookingController.class).error("cant find class", ex);
		}
	}

	public void onDateSelect(SelectEvent selectEvent) {
		IwBooking ag = new IwBooking();
		ag.setIwStartTime((Date) selectEvent.getObject());
		ag.setIwEndTime((Date) selectEvent.getObject());
		ag.setIwResourceId(currentResource);
		event = new DefaultScheduleEvent("", ag.getIwStartTime(), ag.getIwEndTime(), ag);
	}

	public void onEventMove(ScheduleEntryMoveEvent eventSchedule) {
		DefaultScheduleEvent event = (DefaultScheduleEvent) eventSchedule.getScheduleEvent();
		IwBooking iwLoan = (IwBooking) event.getData();
		iwLoan.setIwStartTime(event.getStartDate());
		iwLoan.setIwEndTime(event.getEndDate());
		if (eventSchedule.getMinuteDelta() == 0 && eventSchedule.getDayDelta() == 0) {
			iwLoan.setIwAllDay(true);
			event.setAllDay(true);
		} else {
			iwLoan.setIwAllDay(false);
			event.setAllDay(false);
		}
		event.setData(iwLoan);
		eventModel.updateEvent(event);
		try {
			String str= ((IwBooking) event.getData()).getIwResourceId().getIwEntityName().getIwEntityName();
			Class clazz = Class.forName("com.imaginepartners.imagineworkflow.models." + str);

			for (Object obj : businessService.getListResourceByType(clazz)) {
				listResource.add(obj);
			}
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(BookingController.class).error("cant find class", ex);
		}
	}

	public void onEventResize(ScheduleEntryResizeEvent eventSchedule) {
		ScheduleEvent event = eventSchedule.getScheduleEvent();
		IwBooking iwLoan = (IwBooking) event.getData();
		iwLoan.setIwEndTime(event.getEndDate());
	}

	public String getHourFormat(Date date) {
		return new SimpleDateFormat(businessService.getConfigValue(ConfigConstants.SHORT_DATE_PATTERN), Locale.FRENCH).format(date);
	}

	public void onViewChange(SelectEvent selectEvent) {
		String viewName = selectEvent.getObject().toString();
	}

	public String getIdGroup() {
		return idGroup;
	}

	public void setIdGroup(String idGroup) {
		this.idGroup = idGroup;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public BusinessService getBusinessService() {
		return businessService;
	}

	public void setBusinessService(BusinessService businessService) {
		this.businessService = businessService;
	}

	public List<IwCar> getCarsList() {
		return businessService.getCarsList();
	}

	private String getEventTitle(IwBooking iwLoan) {
		return iwLoan.getIwResourceId().getIwEntityName().getIwEntityName()
		+ " "
		+ FacesUtil.getMessage("iw.par")
		+ " "
		+ (iwLoan.getIwTenant() != null ? iwLoan.getIwTenant().toUpperCase() : "");
	}

	private void setStyleClass(IwBooking iwLoan, DefaultScheduleEvent sechduleEvent) {
		if ("reserved".equals(iwLoan.getIwStatus())) {
			sechduleEvent.setStyleClass("TurquoiseBack");
		} else {
			sechduleEvent.setStyleClass("OrangeBack");
		}
	}

	private void runLoanProcess(IwBooking iwLoan) {
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("loan", iwLoan.getIwBookingId());
		vars.put("user", userService.getLoggedInUser().getId());
		vars.put("form_1442703534825", userService.getLoggedInUser().getFirstName());
		vars.put("form_1442703537596", userService.getLoggedInUser().getLastName());
		vars.put("form_1442703693826", iwLoan.getIwStartTime());
		vars.put("form_1442703759703", iwLoan.getIwStartTime());
		vars.put("form_1442703786930", iwLoan.getIwEndTime());
		vars.put("form_1442703846974", iwLoan.getIwResourceId());
		ProcessInstance processInstance = activitiService.startProcessInstanceByKey("IDPoolResa", vars);
		if (processInstance != null) {
			try {
				String title = getEventTitle(iwLoan);
				activitiService.setProcessInstanceName(processInstance.getId(), title);

				FacesUtil.setSessionInfoMessage(FacesUtil.getMessage("iw.message.processcreeavecsucces", title));
			} catch (Exception exp) {
				FacesUtil.setSessionInfoMessage(exp.getLocalizedMessage());
			}
		}
	}

	public String getSearchFilter() {
		return searchFilter;
	}

	public void setSearchFilter(String searchFilter) {
		this.searchFilter = searchFilter;
	}

	public IwResource getCurrentResource() {
		return currentResource;
	}

	public void setCurrentResource(IwResource currentResource) {
		this.currentResource = currentResource;
	}

	public Object getObjectSelect() {
		return objectSelect;
	}

	public void setObjectSelect(Object objectSelect) {
		this.objectSelect = objectSelect;
	}

	public List getListResource() {
		return listResource;
	}

	public void setListResource(List listResource) {
		this.listResource = listResource;
	}

	public List<IwResourceType> getResourceAttribute() {
		return resourceAttribute;
	}

	public void setResourceAttribute(List<IwResourceType> resourceAttribute) {
		this.resourceAttribute = resourceAttribute;
	}

	public IwResourceType getResourceTypeSelected() {
		return resourceTypeSelected;
	}

	public void setResourceTypeSelected(IwResourceType resourceTypeSelected) {
		this.resourceTypeSelected = resourceTypeSelected;
	}

	public boolean isDisableResourceEntity() {
		return disableResourceEntity;
	}

	public void setDisableResourceEntity(boolean disableResourceEntity) {
		this.disableResourceEntity = disableResourceEntity;
	}

	public String getIdAttributeName() {
		return idAttributeName;
	}

	public void setIdAttributeName(String idAttributeName) {
		this.idAttributeName = idAttributeName;
	}
}
