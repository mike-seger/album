package com.net128.app.album.persistence;

import java.util.List;

import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.net128.app.album.model.MediaItem;

public class MediaRepository extends Repository<MediaItem> {
	private final static Logger logger = LoggerFactory.getLogger(MediaRepository.class);
	public MediaItem saveOrUpdate(MediaItem item) {
		List<MediaItem> items=removeByGroupChatJidAndUrl(item.groupChatJid, item.url);
		if(items.size()>0) {
			logger.info("While saving, removed {} items with the same content", items.size());
		}
		return super.saveOrUpdate(MediaItem.class, item, item.id);
	}
	
	public List<MediaItem> findByGroupChatJid(String groupChatJid) {
		return findByGroupChatJidAndMediaType(groupChatJid, null);
	}
	
	public List<MediaItem> findByGroupChatJidAndUrl(String groupChatJid, String url) {
		Query<MediaItem> query=ds.find(MediaItem.class);
		query.and(
			query.criteria("groupChatJid").equal(groupChatJid),
			query.criteria("url").equal(url)
		);
		return query.asList();
	}
	
	public List<MediaItem> findByGroupChatJidAndMediaType(String groupChatJid, String mimeBaseType) {
		Query<MediaItem> query=ds.find(MediaItem.class);
		query.and(query.criteria("groupChatJid").equal(groupChatJid));
		if(mimeBaseType!=null) {
			query.and(query.criteria("mimetype").startsWith(mimeBaseType+"/"));
		}
		return query.order("-created").asList();
	}
	
	public List<MediaItem> removeByGroupChatJidAndUrl(String groupChatJid, String url) {
		List<MediaItem> items=findByGroupChatJidAndUrl(groupChatJid, url);
		remove(items);
		return items;
	}
	
	public List<MediaItem> removeByGroupChatJid(String groupChatJid) {
		List<MediaItem> items=findByGroupChatJid(groupChatJid);
		remove(items);
		return items;
	}
}
