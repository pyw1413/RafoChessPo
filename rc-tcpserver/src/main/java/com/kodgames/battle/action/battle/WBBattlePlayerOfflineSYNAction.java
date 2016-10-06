package com.kodgames.battle.action.battle;

import com.kodgames.battle.common.Player;
import com.kodgames.battle.entity.battle.BWBattleStartRES;
import com.kodgames.battle.service.server.KodgamesExtension;
import com.rafo.chess.utils.CmdsUtils;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WBBattlePlayerOfflineSYNAction extends BaseServerEventHandler
{
	Logger Logger = LoggerFactory.getLogger(WBBattlePlayerOfflineSYNAction.class);


	@Override
	public void handleServerEvent(ISFSEvent isfsEvent) throws SFSException {
		KodgamesExtension gameExt = (KodgamesExtension) getParentExtension();
		User user = (User) isfsEvent.getParameter(SFSEventParam.USER);
		String accountId = user.getName();
		com.kodgames.battle.common.Room battleRoom = gameExt.getRoomService().getRoom();
		Player player = battleRoom.getPlayer(accountId);
		Room gameRoom = gameExt.getParentRoom();
		trace(user.getName() + "===============WBBattlePlayerOfflineSYNAction====================");
		if (player != null && gameRoom != null)
		{
			gameExt.getBattleService().playerOffline(user.getName(), battleRoom.getRoomID());
//			BWBattleStartRES status = gameExt.getBattleService().getBattleStatus(accountId);
//			gameExt.send(CmdsUtils.CMD_BATTLE_READY, status.toSFSObject(), gameRoom.getUserList());
		}

	}
}
