package com.net128.app.album.nanohttpd;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;

import fi.iki.elonen.router.RouterNanoHTTPD;
import fi.iki.elonen.util.ServerRunner;

public class AppNanoletsPico extends RouterNanoHTTPD {

	private static final int PORT = 8080;

	static class StaticPageTestHandler extends StaticPageHandler {

		@Override
		protected BufferedInputStream fileToInputStream(File fileOrdirectory) throws IOException {
			if ("exception.html".equals(fileOrdirectory.getName())) {
				throw new IOException("trigger something wrong");
			}
			return super.fileToInputStream(fileOrdirectory);
		}
	}

	public AppNanoletsPico() throws IOException {
		super(PORT);
		addMappings();
		System.out.println("\nRunning! Point your browers to http://localhost:" + PORT + "/ \n");
	}

	@Override
	public void addMappings() {
		super.addMappings();
		addRoute("/general/:param1/:param2", GeneralHandler.class);
		addRoute("/browse/(.)+", StaticPageTestHandler.class, new File("src/test/resources").getAbsoluteFile());
	}

	public static void main(String[] args) {
		ServerRunner.run(AppNanoletsPico.class);
	}
}