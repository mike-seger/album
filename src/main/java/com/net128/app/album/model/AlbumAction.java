package com.net128.app.album.model;

public class AlbumAction {
	public enum Action { standard, requestHitory };
	public Action action=Action.standard;
	public String response;
	public String groupchatJid;
	public String getResponse() {
		return response;
	}
	
	public AlbumAction(String response, Action action, String groupchatJid) {
		this.response = response;
		this.action = action;
		this.groupchatJid = groupchatJid;
	}
	
	public AlbumAction(String response) {
		this.response = response;
	}
}
