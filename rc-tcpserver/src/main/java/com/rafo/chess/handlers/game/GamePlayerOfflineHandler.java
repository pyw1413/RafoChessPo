package com.rafo.chess.handlers.game;

import com.rafo.chess.core.GameExtension;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GamePlayerOfflineHandler extends BaseServerEventHandler
{
	Logger Logger = LoggerFactory.getLogger(GamePlayerOfflineHandler.class);


	@Override
	public void handleServerEvent(ISFSEvent isfsEvent) throws SFSException {
		GameExtension gameExt = (GameExtension) getParentExtension();
		User user = (User) isfsEvent.getParameter(SFSEventParam.USER);

        gameExt.getGameService().playerOffline(Integer.parseInt(user.getName()));

	}
}
