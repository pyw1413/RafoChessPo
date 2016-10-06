package com.kodgames.battle.action.record;

import com.kodgames.battle.service.server.KodgamesExtension;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WBRoomRecordREQAction extends BaseClientRequestHandler
{
	final static Logger logger = LoggerFactory.getLogger(WBRoomRecordREQAction.class);

/*	@Override
	public void handleMessage(RemoteNode remoteNode, RecordService service, WBRoomRecordREQ message, int callback)
	{
		logger.info("WBRoomRecordREQAction, accountID={}", message.getAccountID());
		BWRoomRecordRES res = service.getRoomRecord(message.getPlayerID(), message.getAccountID());
		Transmitter.getInstance().write(remoteNode, GlobalConstants.DEFAULT_CALLBACK, res);
	}

	@Override
	public Object getMessageKey(RemoteNode remoteNode, int protocoliD, WBRoomRecordREQ message)
	{
		return message.getAccountID();
	}*/


	@Override
	public void handleClientRequest(User user, ISFSObject isfsObject) {
//		logger.info("WBRoomRecordREQAction, accountID={}", message.getAccountID());
		KodgamesExtension gameExt = (KodgamesExtension) getParentExtension();
		//BWRoomRecordRES res = gameExt.getRecordService().getRoomRecord();
	}
}