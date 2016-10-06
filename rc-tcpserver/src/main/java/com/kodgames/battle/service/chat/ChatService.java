package com.kodgames.battle.service.chat;

import java.util.HashMap;
import java.util.Map;

import com.kodgames.battle.common.constants.GlobalConstants;
import com.kodgames.battle.entity.chat.BWChatRES;
import com.kodgames.battle.entity.chat.WBChatREQ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.battle.common.Player;
import com.kodgames.battle.common.Room;


public class ChatService
{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6155256623581616940L;
	private static Logger logger = LoggerFactory.getLogger(ChatService.class);
	private Room room;

	public ChatService(){
	}

	public void init(Room room){
		this.room = room;
	}
	
	public Map<String, BWChatRES> broadChatMsg(WBChatREQ message)
	{

		if(room == null)
		{
			logger.error("player is not belongs to any room, account ID: " + message.getSenderAccountID());
//			builder.setResult(GlobalConstants.BW_CHAT_SEND_FAILED);
			return null;
		}
		
		int senderChair = -1;
		// 获得同房间里除自己以外所有人
		Map<String, Player> players = room.getPlayers();
		for (Map.Entry<String, Player> player : players.entrySet())
		{
			if(player.getKey().equals(message.getSenderAccountID()))
			{
				senderChair = player.getValue().getChair();
			}
		}

		Map<String, BWChatRES> results = new HashMap<>();
		for (Map.Entry<String, Player> player : players.entrySet())
		{
			if(player.getKey().equals(message.getSenderAccountID()))
			{
				continue;
			}
			BWChatRES builder = new BWChatRES();
			builder.setSenderChair(senderChair);
			builder.setContent(message.getContent());
			builder.setSendTime(message.getSendTime());
			builder.setResult(GlobalConstants.BW_CHAT_SEND_SUCCESS);
			builder.setRoomID(room.getRoomID());
			builder.setAccountID(player.getKey());
			results.put(player.getKey(), builder);
		}
		return results;
	}
}
