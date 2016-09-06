package com.net128.app.album.model;

import java.util.Date;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

import com.swisscom.io.extwc.sdk.types.JsonObject;
import com.swisscom.io.extwc.sdk.types.Media;

@Entity
public class MediaItem extends JsonObject {
    @Id
	public String id;
	@Embedded
 	public Media media;
	@Indexed
    public String groupChatJid;
	@Indexed
	public String url;
	@Indexed
	public String mimetype;
	@Indexed
	public Date created;
	@Indexed
	public String fromJid;
	public String fromDisplayName;
	public MediaItem(){}
	public MediaItem(String id, Media media, String groupChatJid, String url, String mimetype, Date created, String fromJid,
			String fromDisplayName) {
		super();
		this.id = id;
		this.media = media;
		this.groupChatJid = groupChatJid;
		this.url = url;
		this.mimetype = mimetype;
		this.created = created;
		this.fromJid = fromJid;
		this.fromDisplayName = fromDisplayName;
	}
}
