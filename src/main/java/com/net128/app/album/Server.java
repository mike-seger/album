package com.net128.app.album;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.net128.app.album.model.MediaItem;
import com.net128.app.album.model.audioplayer.AudioItem;
import com.net128.app.album.model.nanogallery.ImageItem;
import com.net128.app.album.persistence.MongoDatastoreFactory;
import com.swisscom.io.extwc.sdk.helper.ResourceLoader;
import com.swisscom.io.extwc.sdk.helper.WebConnectorBot;
import com.swisscom.io.extwc.sdk.helper.WebConnectorBotParameters;
import com.swisscom.io.extwc.sdk.helper.WebConnectorBotServer;
import com.swisscom.io.extwc.sdk.httpd.RestHandler;

import fi.iki.elonen.NanoHTTPD;

public class Server extends WebConnectorBotServer {
	private final static ObjectMapper om=new ObjectMapper();
	private final static WebConnectorBotParameters wcbp = 
		new WebConnectorBotParameters("/application.properties", "/credentials.json");
	private static AlbumManager albumManager;
	private final static String imgListRootUrl="/rest/images/";
	private final static String soundListRootUrl="/rest/sounds/";
	public final static String imagePath="/album/index.html";
	public final static String soundPath="/audioplayer/index.html";

	public static void main(String[] args) throws IOException, InterruptedException {
		MongoDatastoreFactory.init(wcbp.appProperties);
		new Server().start();
	}

	public Server() throws IOException, InterruptedException {
		super(wcbp);
		albumManager = new AlbumManager(wcbp);
		addMappings();
	}

	@Override
	public WebConnectorBot createBot(Object lock, String webSocketUrl, String jwt) {
		return new AlbumBot(albumManager, wcbp, lock, webSocketUrl, jwt, wcbp.appProperties.getProperty("jid.admin"));
	}

	@Override
	public void addMappings() {
		super.addMappings();
//		addRoute("/general/:param1/:param2", GeneralHandler.class);
		addClasspathRoute("/", StaticPageHandler.class, "/webapp//html/index.html");
		addClasspathRoute("/album/.+", StaticPageHandler.class, "/webapp//html/album/index.html");
		addClasspathRoute("/audioplayer/.+", StaticPageHandler.class, "/webapp/html/audioplayer/index.html");
		addRoute(imgListRootUrl+".+", ImageListRestHandler.class);
		addRoute(soundListRootUrl+".+", SoundListRestHandler.class);
	}

	public void addClasspathRoute(String uri, Class<?> handler, String location) {
		removeRoute(uri);
		addRoute(uri, handler, ResourceLoader.getClasspathFile(getClass(), location).getParentFile());
	}

	public static class ImageListRestHandler extends MediaItemListHandler {
		public Response get(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
			List<ImageItem> items=new ArrayList<>();
			for(MediaItem m : getMediaItems(session, "image")) {
				items.add(new ImageItem(m.media.name, m.media.url, m.media.thumbnail));
			}
			return createResponse(session, items);
		}
	}

	public static class SoundListRestHandler extends MediaItemListHandler {
		public Response get(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
			List<AudioItem> audioItems=new ArrayList<>();
			for(MediaItem m : getMediaItems(session, "audio")) {
				audioItems.add(new AudioItem(m.url, m.media.name, m.media.duration));
			}
			return createResponse(session, audioItems);
		}
	}
	
	private static class MediaItemListHandler extends RestHandler {
		public Collection<MediaItem> getMediaItems(IHTTPSession session, String mediaBaseType) {
			String groupChatJid=session.getUri().substring(soundListRootUrl.length());
			Collection<MediaItem> mediaItems=albumManager.getMediaItems(groupChatJid, mediaBaseType);
			return mediaItems;
		}
		public Response createResponse(IHTTPSession session, Object o) {
			try {
				return NanoHTTPD.newFixedLengthResponse(getStatus(), getMimeType(), om.writeValueAsString(o));
			} catch (JsonProcessingException e) {
				throw new RuntimeException("Error occurred processing: "+session.getUri(), e);
			}			
		}
	}
}
