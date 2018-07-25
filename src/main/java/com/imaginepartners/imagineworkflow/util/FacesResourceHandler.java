package com.imaginepartners.imagineworkflow.util;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;

public class FacesResourceHandler extends ResourceHandlerWrapper {

	private final ResourceHandler wrapped;

	public FacesResourceHandler(final ResourceHandler resourceHandler) {
		super();
		wrapped = resourceHandler;
	}

	@Override
	public ResourceHandler getWrapped() {
		return wrapped;
	}

	@Override
	public Resource createResource(final String resourceName, final String libraryName) {
		Resource resource = super.createResource(resourceName, libraryName);
		if (resource != null && resource.getRequestPath() != null) {
			String requestPath = resource.getRequestPath();
			if ((requestPath.contains("faces") && (requestPath.contains(".js.xhtml") || requestPath.contains(".css.xhtml"))) || (!requestPath.contains("v="))) {
				resource = new FacesResource(resource);
			}
		}
		return resource;
	}
}
