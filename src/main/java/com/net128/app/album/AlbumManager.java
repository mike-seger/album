package com.net128.app.album;

import static com.swisscom.io.extwc.sdk.helper.UnicodeHelper.lineHorizontalLight;
import static com.swisscom.io.extwc.sdk.helper.UnicodeHelper.repeat;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.net128.app.album.helper.AudioInfo;
import com.net128.app.album.helper.AudioInfoHelper;
import com.net128.app.album.model.AlbumAction;
import com.net128.app.album.model.MediaItem;
import com.net128.app.album.persistence.MediaRepository;
import com.swisscom.io.extwc.sdk.exception.BotException;
import com.swisscom.io.extwc.sdk.helper.WebConnectorBotParameters;
import com.swisscom.io.extwc.sdk.types.HistoryResult;
import com.swisscom.io.extwc.sdk.types.HistoryResult.Conversation;
import com.swisscom.io.extwc.sdk.types.Media;
import com.swisscom.io.extwc.sdk.types.Message;
import com.swisscom.io.extwc.sdk.types.MessageType;

public class AlbumManager {
	private final static Logger logger = LoggerFactory.getLogger(AlbumManager.class);
	private final static String commandPrefix="/album";
	private final static String commandCollect=commandPrefix+"-collect";
	private final static String commandClear=commandPrefix+"-clear";
	private final static Set<String> helpCommands=new HashSet<>(
		Arrays.asList(new String []{commandPrefix, commandPrefix+"-help", "help", "/", "?"})); 
	private MediaRepository mediaRepository=new MediaRepository();
	private WebConnectorBotParameters wcbp;
	
	public AlbumManager(WebConnectorBotParameters wcbp) {
		this.wcbp=wcbp;
	}
	
	public Collection<MediaItem> getMediaItems(String groupChatJid, String mediaBaseType) {
		return mediaRepository.findByGroupChatJidAndMediaType(groupChatJid, mediaBaseType);
	}
		
	public AlbumAction handleMessage(Message message) {
		Message.Data d=message.data;
		if(MessageType.groupchat.equals(d.type) 
				&& d.groupchat.initiator!=null) {
			if(d.text!=null && helpCommands.contains(d.text.trim().toLowerCase())) {
				return new AlbumAction(usage(d.from));
			}
			return handle(message.id, d.text, d.media, d.from, 
				d.groupchat.initiator.ucid, d.groupchat.initiator.display);
		}
		return null;
	}

	public AlbumAction handleHistoryResult(HistoryResult historyResult, String from) {
		if(historyResult.data.conversations!=null) {
			int i=0;
			for(Conversation conversation : historyResult.data.conversations.conversation) {
				for(Message message : conversation.entry) {
					Message.Data d=message.data;
					if(d.media!=null) {
						try {
							handleMessage(message);
							i++;
						} catch(Exception e) {
							logger.warn(e.getMessage());
						}
					}
				}
			}
			return new AlbumAction(String.format(
				getMessage("message.collected.history"), i), null, null);
		}
		return null;
	}

	public AlbumAction handle(String id, String text, Media media, String groupChatJid, 
			String initiatorUcid, String initiatorDisplay) {
		if(media!=null && media.url!=null && "file".equals(media.type)) {
			return handleMedia(id, text, media, groupChatJid, initiatorUcid, initiatorDisplay);
		} else if(text!=null && text.startsWith(commandPrefix)) {
			return handleCommand(text, groupChatJid);
		}
		return null;
	}
	
	private AlbumAction handleCommand(String command, String groupChatJid) {
		command=command.trim();
		if(commandCollect.equals(command)){
			return new AlbumAction(
				wcbp.appProperties.getProperty("message.collect.history"), 
				AlbumAction.Action.requestHitory,
				groupChatJid
			);
		} else if(commandClear.equals(command)){
			List<MediaItem> items=mediaRepository.removeByGroupChatJid(groupChatJid);
			return new AlbumAction(String.format(
				getMessage("message.clear.history"), items.size()), null, groupChatJid);
		}
		throw new BotException("Unknown command: '"+command+"' on "+wcbp.url);
	}
	
	private String getMessage(String key) {
		return wcbp.appProperties.getProperty(key);
	}
	
	private String mediaLink(String groupChatJid, String title, String uri) {
		StringBuilder sb=new StringBuilder();
		sb.append(String.format(getMessage("message.open.link"), title));
		String url=wcbp.url+uri+"#"+groupChatJid;
		sb.append(url);
		sb.append("\n");
		return sb.toString();
	}
	
	private String usage(String groupChatJid) {
		StringBuilder sb=new StringBuilder();
		sb.append("ALBUM\n");
		sb.append(repeat(lineHorizontalLight,11));
		sb.append(getMessage("message.usage"));
		sb.append("\n\n");
		sb.append(mediaLink(groupChatJid, "Image", Server.imagePath));
		sb.append("\n");
		sb.append(mediaLink(groupChatJid, "Sound", Server.soundPath));
		return sb.toString().trim();
	}
	
	private AlbumAction handleMedia(String id, String text, Media media, String groupChatJid, 
			String initiatorUcid, String initiatorDisplay) {
		String jid=initiatorUcid;
		if(initiatorUcid.indexOf('@')<0) {
			jid+="@my-io.ch";
		}
		updateMedia(media);
		MediaItem mediaItem=new MediaItem(id, media, groupChatJid, media.url, 
			media.contentType, new Date(), jid, initiatorDisplay);
		mediaRepository.saveOrUpdate(mediaItem);
		return new AlbumAction(getMessage("message.media.registered")+" "+media.name);
	}
	
	private void updateMedia(Media media) {
		String contentType=null;
		try {
			if(media.duration==null) {
				if(media.contentType!=null && media.contentType.startsWith("audio/")) {
					contentType=media.contentType;
					AudioInfo audioInfo=AudioInfoHelper.audioInfo(media.url, media.contentType);
					if(audioInfo.durationMs!=null) {
						media.duration=(long)audioInfo.durationMs;
					}
					if(audioInfo.name!=null) {
						media.name=audioInfo.name;
					} else {
						if(media.name!=null) {
							media.name=media.name.replaceAll("[.][A-Za-z]+$", "");
						}
					}
				}
			}
		} catch(Exception e) {
			logger.error("Error getting audio duration for: "+contentType, e);
			throw new RuntimeException(e);
		}
	}
}
