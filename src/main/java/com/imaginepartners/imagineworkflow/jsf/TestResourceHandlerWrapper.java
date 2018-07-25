package com.imaginepartners.imagineworkflow.jsf;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLStreamHandler;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.faces.FacesException;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.application.ViewResource;
import javax.faces.context.FacesContext;

public class TestResourceHandlerWrapper extends ResourceHandlerWrapper {

	private ResourceHandler wrapped;

	public TestResourceHandlerWrapper(ResourceHandler wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public ViewResource createViewResource(FacesContext context, String resourceName) {
		if (resourceName.startsWith("/dynamic.xhtml?")) {
			try {
				String query = resourceName.substring("/dynamic.xhtml?".length());
				Map<String, String> params = splitQuery(query);
				String content = "<ui:composition"
				+ " xmlns='http://www.w3.org/1999/xhtml' xmlns:ui='http://java.sun.com/jsf/facelets'"
				+ " xmlns:h='http://java.sun.com/jsf/html'> MY CONTENT"
				+ "</ui:composition>";
				final URL url = new URL(null, "string://helloworld", new MyCustomHandler(content));
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

	public static Map<String, String> splitQuery(String query) throws UnsupportedEncodingException {
		Map<String, String> params = new LinkedHashMap<String, String>();
		String[] pairs = query.split("&");
		for (String pair : pairs) {
			int idx = pair.indexOf("=");
			params.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
		}
		return params;
	}

	@Override
	public ResourceHandler getWrapped() {
		return wrapped;
	}

	static class MyCustomHandler extends URLStreamHandler {

		private String content;

		public MyCustomHandler(String content) {
			this.content = content;
		}

		@Override
		protected URLConnection openConnection(URL u) throws IOException {
			return new UserURLConnection(u, content);
		}

		private static class UserURLConnection extends URLConnection {

			private String content;

			public UserURLConnection(URL url, String content) {
				super(url);
				this.content = content;
			}

			@Override
			public void connect() throws IOException {
			}

			@Override
			public InputStream getInputStream() throws IOException {
				return new ByteArrayInputStream(content.getBytes("UTF-8"));
			}
		}
	}
}
