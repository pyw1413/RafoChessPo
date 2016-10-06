package com.kodgames.battle.action.battle;

import com.kodgames.battle.common.Player;
import com.kodgames.battle.entity.battle.BWBattleStartRES;
import com.kodgames.battle.entity.battle.BWBattleStepRES;
import com.kodgames.battle.entity.battle.WBBattleStartREQ;
import com.kodgames.battle.service.server.KodgamesExtension;
import com.rafo.chess.utils.CmdsUtils;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

public class WBBattleStartREQAction extends BaseClientRequestHandler
{
	final static Logger logger = LoggerFactory.getLogger(WBBattleStartREQAction.class);

	@Override
	public void handleClientRequest(User user, ISFSObject isfsObject) {
		KodgamesExtension gameExt = (KodgamesExtension) getParentExtension();
		//send();
		WBBattleStartREQ msg = new WBBattleStartREQ();
		msg.setRoomId(Integer.parseInt(user.getLastJoinedRoom().getName()));
		msg.setAccountId(user.getName());
		trace(user.getName() + "========== WBBattleStartREQAction ============");

		Map<String, BWBattleStepRES> battleStepRES = gameExt.getBattleService().battleStart(msg);

		//其他玩家的状态
		BWBattleStartRES status = gameExt.getBattleService().getBattleStatus(msg.getAccountId());
		//TODO: SEND

//		gameExt.send(CmdsUtils.CMD_BATTLE_READY,status.toSFSObject(),gameExt.getParentRoom().getUserList());

		String xx = "";
		for(Player player : gameExt.getRoomService().getRoom().getPlayers().values()){
			xx+= player.getAccountID()+","+player.getPlayState() + ";";
		}

		trace(user.getName() +  "+++++battle_ready++++++++" + gameExt.getParentRoom().getName() + " " + gameExt.getParentRoom().getUserList().size()  + "  " + xx) ;
		Set<Map.Entry<String, BWBattleStepRES>> entry = battleStepRES.entrySet();

		//TODO: SEND
		for(Map.Entry<String, BWBattleStepRES> one:entry){
			gameExt.send(CmdsUtils.CMD_BATTLE_STEP,one.getValue().toSFSObject(),getApi().getUserByName(one.getKey()));
		}


	}

}
