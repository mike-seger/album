package com.net128.app.album;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.net128.app.album.model.AlbumAction;
import com.swisscom.io.extwc.sdk.exception.BotException;
import com.swisscom.io.extwc.sdk.helper.WebConnectorBot;
import com.swisscom.io.extwc.sdk.helper.WebConnectorBotParameters;
import com.swisscom.io.extwc.sdk.types.HistoryRequest;
import com.swisscom.io.extwc.sdk.types.HistoryResult;
import com.swisscom.io.extwc.sdk.types.Message;

public class AlbumBot extends WebConnectorBot {
	private final static Logger logger = LoggerFactory.getLogger(AlbumBot.class);
	private WebConnectorBotParameters wcbp;
	private final AlbumManager albumManager;
	private final static String groupChatSenderPrefix="groupchat:";

	private String adminJid;

	public AlbumBot(AlbumManager albumManager, WebConnectorBotParameters wcbp, Object lock, String webSocketUrl, String jwt, String adminJid) {
		super(lock, webSocketUrl, jwt);
		this.wcbp=wcbp;
		this.adminJid = adminJid;
		this.albumManager=albumManager;
	}

	@Override
	public void onConnected() {
		if (adminJid != null) {
			sendMessage(wcbp.welcomeMessage, adminJid);
		}
	}

	@Override
	public void onTextMessage(Message message) {
		logger.info("Received: " + message);
		try {
			AlbumAction albumAction = albumManager.handleMessage(message);
			if (albumAction!=null) {
				if(albumAction.getResponse() != null) {
					sendMessage(albumAction.getResponse(), message.data.from);
				}
				if(AlbumAction.Action.requestHitory.equals(albumAction.action)) {
					send(new HistoryRequest(HistoryRequest.Mode.messages, 100, 
						groupChatSenderPrefix+message.data.from));
				}
			}
		} catch(BotException e) {
			logger.debug("User error:" + message, e);
			sendMessage(e.getMessage(), message.data.from);
		} catch(Exception e) {
			logger.error("Error handling:" + message, e);
			sendMessage(e.getMessage(), message.data.from);
		}
	}
	
	@Override 
	public void onHistoryResult(HistoryResult historyResult) {
		String qid=historyResult.data.conversations.queryId;
		String from=getFromFromQueryId(qid);
		if(from==null) {
			if(historyResult.data.conversations.conversation.size()>0) {
				from=getFromFromQueryId(historyResult.data.conversations.conversation.get(0).id);
			}
		}
		AlbumAction albumAction = albumManager.handleHistoryResult(historyResult, from);
		if (albumAction!=null && from!=null) {
			sendMessage(albumAction.getResponse(), from);
		}
	}
	
	private String getFromFromQueryId(String queryId) {
		return queryId!=null&&queryId.startsWith(groupChatSenderPrefix)?
			queryId.substring(groupChatSenderPrefix.length()):null;
	}
}
