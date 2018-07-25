package com.imaginepartners.imagineworkflow.jsf;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import javax.faces.FacesException;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.application.ViewResource;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletContext;
import org.apache.log4j.LogManager;

public class TemplateResourceHandler extends ResourceHandlerWrapper {

	private ResourceHandler wrapped;

	public TemplateResourceHandler(ResourceHandler wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public ViewResource createViewResource(FacesContext context, String resourceName) {
		if (resourceName.equals("/blank.xhtml")) {
			FaceletContext faceletContext = (FaceletContext) context.getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY);
			String template = (String) faceletContext.getAttribute("template");
			try {
				File file = File.createTempFile("dynamic-", ".xhtml");
				Writer writer = new FileWriter(file, true);
				writer.append("<ui:composition")
				.append(" xmlns:ui='http://java.sun.com/jsf/facelets'")
				.append(" xmlns:h='http://java.sun.com/jsf/html'")
				.append(">")
				.append("<p>Hello from a dynamic include!</p>")
				.append("<p>The below should render as a real input field:</p>")
				.append("<p><h:inputText /></p>")
				.append("</ui:composition>");
				writer.flush();
				final URL url = file.toURI().toURL();
				return new ViewResource() {
					@Override
					public URL getURL() {
						return url;
					}
				};
			} catch (IOException e) {
				throw new FacesException(e);
			}
		}
		return super.createViewResource(context, resourceName);
	}

	@Override
	public ResourceHandler getWrapped() {
		return wrapped;
	}
}
