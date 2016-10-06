package com.rafo.chess.handlers;

import com.rafo.chess.core.GameExtension;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
import com.smartfoxserver.v2.extensions.ISFSExtension;

public class RoomJoinHandler extends BaseServerEventHandler
{
	@Override
	public void handleServerEvent(ISFSEvent event) throws SFSException
	{
//		SFSExtension xx = getParentExtension();
		User theUser = (User) event.getParameter(SFSEventParam.USER);

		Room room = (Room)event.getParameter(SFSEventParam.ROOM);
		Object aa  = room.getProperty("aa");
		Object bb  = room.getProperty("bb");
		System.out.println("=============111========================" + aa);

	}
}
