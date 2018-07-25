package com.imaginepartners.imagineworkflow.controller;

import com.imaginepartners.imagineworkflow.models.IwEvent;
import com.imaginepartners.imagineworkflow.models.rh.RhConge;
import com.imaginepartners.imagineworkflow.services.ActivitiService;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.services.impl.UserService;
import com.imaginepartners.imagineworkflow.util.ConfigConstants;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.event.AjaxBehaviorEvent;
import com.imaginepartners.imagineworkflow.models.rh.RhCollaborateur;
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

@Controller
@Scope("view")
public class AgendaController implements Serializable {

	private Date navigationDate;

	private List<String> eventSelected;

	private ScheduleModel eventModel;

	private List<DefaultScheduleEvent> listEvents;

	private String searchFilter;

	private DefaultScheduleEvent event;

	private String idGroup;

	@Autowired
	UserService userService;

	@Autowired
	BusinessService businessService;

	@Autowired
	private ActivitiService activitiService;

	public String getSearchFilter() {
		return searchFilter;
	}

	public void setSearchFilter(String searchFilter) {
		this.searchFilter = searchFilter;
	}

	@PostConstruct
	public void init() {
		eventModel = getScheduleModel(userService.getLoggedInUser().getId());
		listEvents = new ArrayList<DefaultScheduleEvent>();
		eventSelected = new ArrayList<String>();
		navigationDate = new Date();
		searchFilter = "";
		remplirCalendar();
	}

	public ScheduleModel getScheduleModel(String userId) {
		ScheduleModel model = new DefaultScheduleModel();
		DefaultScheduleEvent event;
		for (IwEvent iwEvent : businessService.getIwEventForUser(userId)) {
			event = new DefaultScheduleEvent(iwEvent.getIwTitre(), iwEvent.getIwStartDate(), iwEvent.getIwEndDate(), iwEvent.getIwAllDay().booleanValue());
			event.setId(String.valueOf(iwEvent.getIwEventId()));
			event.setData(iwEvent);
			if (iwEvent.getIwColor() != null) {
				event.setStyleClass("background" + iwEvent.getIwColor());
			}
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

	public void addEvent() {
		if (event.getTitle() == null || event.getTitle().isEmpty()) {
			FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.agenda.titrevide"));
		} else if (event.getEndDate().compareTo(event.getStartDate()) < 0) {
			FacesUtil.setAjaxErrorMessage(FacesUtil.getMessage("iw.agneda.datefinsuperieurdatedebut"));
		} else {
			IwEvent iwEvent = (IwEvent) event.getData();
			iwEvent.setIwTitre(event.getTitle());
			iwEvent.setIwStartDate(event.getStartDate());
			iwEvent.setIwEndDate(event.getEndDate());
			iwEvent.setIwUser(userService.getLoggedInUser().getId());
			event.setAllDay(iwEvent.getIwAllDay());
			if (event.getId() == null) {
				eventModel.addEvent(event);
			} else {
				eventModel.updateEvent(event);
			}
			businessService.saveIwEvent(iwEvent);
		}
	}

	public void removeEvent() {
		if (event.getId() != null) {
			eventModel.deleteEvent(event);
		}
		IwEvent iwEvent = (IwEvent) event.getData();
		businessService.deleteIwEvent(iwEvent);
	}

	public void onEventSelect(SelectEvent selectEvent) {
		event = (DefaultScheduleEvent) selectEvent.getObject();
	}

	public void onDateSelect(SelectEvent selectEvent) {
		IwEvent ag = new IwEvent();
		ag.setIwStartDate((Date) selectEvent.getObject());
		ag.setIwEndDate((Date) selectEvent.getObject());
		event = new DefaultScheduleEvent("", ag.getIwStartDate(), ag.getIwEndDate(), ag);
	}

	public void createEvent() {
		IwEvent ag = new IwEvent();
		ag.setIwTitre("");
		ag.setIwAllDay(false);
		ag.setIwDescription("");
		ag.setIwStartDate(new Date());
		ag.setIwEndDate(new Date());
		event = new DefaultScheduleEvent("", ag.getIwStartDate(), ag.getIwEndDate(), ag);
	}

	public void onEventMove(ScheduleEntryMoveEvent eventSchedule) {
		DefaultScheduleEvent event = (DefaultScheduleEvent) eventSchedule.getScheduleEvent();
		IwEvent iwEvent = (IwEvent) event.getData();
		iwEvent.setIwStartDate(event.getStartDate());
		iwEvent.setIwEndDate(event.getEndDate());
		if (eventSchedule.getMinuteDelta() == 0 && eventSchedule.getDayDelta() == 0) {
			iwEvent.setIwAllDay(true);
			event.setAllDay(true);
		} else {
			iwEvent.setIwAllDay(false);
			event.setAllDay(false);
		}
		event.setData(iwEvent);
		eventModel.updateEvent(event);
		businessService.saveIwEvent(iwEvent);
	}

	public void onEventResize(ScheduleEntryResizeEvent eventSchedule) {
		ScheduleEvent event = eventSchedule.getScheduleEvent();
		IwEvent iwEvent = (IwEvent) event.getData();
		iwEvent.setIwEndDate(event.getEndDate());
		businessService.saveIwEvent(iwEvent);
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

	private void remplirCalendar(){

		List listConge=getAuthorizedConge();
		int color=0;
		for(Object obj:listConge) {
			RhConge rhConge = (RhConge)obj;
			if(rhConge.getCollaborateur() != null) {
				RhCollaborateur rhCollaborateur = rhConge.getCollaborateur();
				Calendar dateFin = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
				dateFin.setTime(((RhConge) obj).getDateFin());
				dateFin.add(Calendar.DAY_OF_YEAR,1);

				Calendar dateDebut = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
				dateDebut.setTime(((RhConge) obj).getDateDebut());
				dateDebut.set(Calendar.HOUR_OF_DAY,8);

				DefaultScheduleEvent scheduleEvent = new DefaultScheduleEvent(rhCollaborateur.getPrenom()+ " " + rhCollaborateur.getNom(),dateDebut.getTime() ,dateFin.getTime());
				if(color % 3 == 2)
					scheduleEvent.setStyleClass("backgroundBleu1");
				if(color % 3 == 0)
					scheduleEvent.setStyleClass("backgroundBleu2");

				eventModel.addEvent(scheduleEvent);

			}


			color++;
		}




	}
	private List<Object> getAuthorizedConge(){
		if(activitiService.isMemberOfGroup(userService.getLoggedInUser().getId(),"ROLE_DRH")){
			return businessService.getEntiteList(RhConge.class.getName());
		}
		RhCollaborateur collaborateur=businessService.getRhCollaborateurByActIdUser(userService.getLoggedInUser().getId());
		if(collaborateur == null)
			return new ArrayList<>();
		List<Object> ListCong = businessService.getObjectBySQL("SELECT * FROM RH_CONGE r where r.COLLABORATEUR = '"+collaborateur.getId()+"'",RhConge.class);
		if(ListCong == null)
			return new ArrayList<>();

		return ListCong;
	}

}
