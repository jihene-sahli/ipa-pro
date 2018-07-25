package com.imaginepartners.imagineworkflow.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imaginepartners.imagineworkflow.services.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1", produces = "application/json;charset=UTF-8")
public class EntityController {
	@Autowired
	private BusinessService businessService;

	/**
	 *
	 * @return Greetings text
     */
	@RequestMapping(value = "/", method = RequestMethod.GET )
	@ResponseBody
	public String home() {
		return "Welcome";
	}

	/**
	 * Get the list of all available entities
	 */
	@RequestMapping(value = "/entities", method = RequestMethod.GET )
	@ResponseBody
	public List list() {
		List items = businessService.getMultiLineEntityList();
		return items;
	}

	/**
	 * Get the schema of an entity
	 * @param entity
     */
	@RequestMapping(value = "/{entity}/schema", method = RequestMethod.GET )
	@ResponseBody
	public Object schema(@PathVariable("entity") String entity) {
		Object schema = businessService.getEntitySchema(entity);
		if(schema != null) {
			return new ResponseEntity<>(schema, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Get the list of all items of given entity
	 * @param entity
	 * @return
     */
	@RequestMapping(value = "/{entity}", method = RequestMethod.GET )
	@ResponseBody
	public List findAll(@PathVariable("entity") String entity) {
		List items = businessService.getEntityItems(entity);
		return items;
	}

	/**
	 * Find entity item by given id
	 * @param entity
	 * @param id
     * @return
     */
	@RequestMapping(value = "/{entity}/{id}", method = RequestMethod.GET )
	@ResponseBody
	public Object findOne(@PathVariable(value = "entity") String entity, @PathVariable(value = "id") String id) {
		Object item = businessService.getEntityItem(entity, id);
		if(item != null) {
			return new ResponseEntity<>(item, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Create a new entity item
	 * @param entity
	 * @param body
     * @return
     */
	@RequestMapping(value = "/{entity}", method = RequestMethod.POST )
	@ResponseBody
	public Object create(@PathVariable("entity") String entity, @RequestBody(required = false) Object body) {
		return businessService.saveEntity(entity, body);
	}

	/**
	 * Update an existing entity
	 * @param entity
	 * @param body
     * @return
     */
	@RequestMapping(value = "/{entity}/{id}", method = RequestMethod.POST )
	@ResponseBody
	public Object update(@PathVariable("entity") String entity, @PathVariable("id") String id, @RequestBody(required = false) Object body) {
		return businessService.saveEntity(entity, body);
	}

	/**
	 *
	 * @param entity
	 * @param id
     * @return
     */
	@RequestMapping(value = "/{entity}/{id}", method = RequestMethod.DELETE )
	@ResponseBody
	public Boolean delete(@PathVariable("entity") String entity, @PathVariable(value = "id") String id) {
		return businessService.deleteEntity(entity, id);
	}
}
