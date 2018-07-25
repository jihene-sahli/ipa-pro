package com.imaginepartners.imagineworkflow.util;

import javax.faces.application.Resource;
import javax.faces.application.ResourceWrapper;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FacesResource extends ResourceWrapper {

	private Resource wrapped;

	private String version = "";

	public FacesResource(final Resource resource) {
		super();
		wrapped = resource;
		if (resource.getRequestPath().contains("?")) {
			version += "&";
		} else {
			version += "?";
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Date today = new Date();
		version += "v=" + dateFormat.format(today);
	}

	@Override
	public Resource getWrapped() {
		return wrapped;
	}

	@Override
	public String getRequestPath() {
		return super.getRequestPath() + version;
	}

	@Override
	public String getContentType() {
		return getWrapped().getContentType();
	}

	@Override
	public String getLibraryName() {
		return getWrapped().getLibraryName();
	}

	@Override
	public String getResourceName() {
		return getWrapped().getResourceName();
	}

	@Override
	public void setContentType(final String contentType) {
		getWrapped().setContentType(contentType);
	}

	@Override
	public void setLibraryName(final String libraryName) {
		getWrapped().setLibraryName(libraryName);
	}

	@Override
	public void setResourceName(final String resourceName) {
		getWrapped().setResourceName(resourceName);
	}

	@Override
	public String toString() {
		return getWrapped().toString();
	}
}
