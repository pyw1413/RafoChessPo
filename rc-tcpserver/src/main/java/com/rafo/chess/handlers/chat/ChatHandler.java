package com.rafo.chess.handlers.chat;

import com.kodgames.battle.entity.chat.WBChatREQ;
import com.kodgames.battle.service.server.KodgamesExtension;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ChatHandler extends BaseClientRequestHandler {
	final static Logger logger = LoggerFactory.getLogger(ChatHandler.class);

	@Override
	public void handleClientRequest(User user, ISFSObject params) {
		KodgamesExtension gameExt = (KodgamesExtension) getParentExtension();
		Room room = gameExt.getParentRoom();

		WBChatREQ message = new WBChatREQ();
/*		Map<String, BWChatRES> result = gameExt.getChatService().broadChatMsg(message);
		Set<Map.Entry<String,BWChatRES>> sets = result.entrySet();
		for(Map.Entry<String,BWChatRES> s:sets){
			gameExt.send(CmdsUtils.SFS_EVENT_CHAT_SYN,s.getValue().toSFSObject(),gameExt.getApi().getUserByName(s.getKey()));
		}*/
	}

	private void assembleMessage(User user, ISFSObject params,WBChatREQ message,Room room){
		message.setContent(params.getUtfString("content"));
		message.setSendTime(params.getLong("sendTime "));
		message.setRoomID(Integer.parseInt(room.getName()));
		message.setSenderAccountID(user.getName());
	}
}
