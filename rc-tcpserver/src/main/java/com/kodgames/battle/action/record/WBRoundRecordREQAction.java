package com.kodgames.battle.action.record;

import com.kodgames.battle.service.server.KodgamesExtension;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WBRoundRecordREQAction extends BaseClientRequestHandler
{
	final static Logger logger = LoggerFactory.getLogger(WBRoundRecordREQAction.class);

	@Override
	public void handleClientRequest(User user, ISFSObject isfsObject) {
		KodgamesExtension gameExt = (KodgamesExtension) getParentExtension();
		//BWRoomRecordRES res = gameExt.getRecordService().getRoundRecords();
	}

/*	@Override
	public void handleMessage(RemoteNode remoteNode, RecordService service, WBRoundRecordREQ message, int callback) 
	{
		logger.info("WBRoundRecordREQAction, accountID={} playerID={}", message.getAccountID(), message.getPlayerID());
		BWRoundRecordRES res = service.getRoundRecords(
			message.getAccountID(), message.getRoomID(), message.getPlayerID());
		Transmitter.getInstance().write(remoteNode, GlobalConstants.DEFAULT_CALLBACK, res);
	}

	@Override
	public Object getMessageKey(RemoteNode remoteNode, int protocoliD, WBRoundRecordREQ message)
	{
		return message.getAccountID();
	}*/


}