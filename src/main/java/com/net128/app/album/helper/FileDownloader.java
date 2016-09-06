package com.net128.app.album.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class FileDownloader {
	public static File downloadFromUrl(String urlString, String ext) throws IOException {
		URL url=new URL(urlString);
		File temp = File.createTempFile("download", ext);
		URLConnection urlConn = url.openConnection();
		try (InputStream is = urlConn.getInputStream();) {
			try (FileOutputStream fos = new FileOutputStream(temp);) {
				byte[] buffer = new byte[4096];
				int length;
				while ((length = is.read(buffer)) > 0) {
					fos.write(buffer, 0, length);
				}
			}
		}
		return temp;
	}
}
