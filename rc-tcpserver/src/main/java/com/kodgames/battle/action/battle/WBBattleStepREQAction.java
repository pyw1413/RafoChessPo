package com.kodgames.battle.action.battle;

import com.kodgames.battle.entity.battle.BWBattleStepRES;
import com.kodgames.battle.entity.battle.WBBattleStepREQ;
import com.kodgames.battle.service.room.RoomHelper;
import com.kodgames.battle.service.server.KodgamesExtension;
import com.rafo.chess.core.ROCHExtension;
import com.rafo.chess.utils.CmdsUtils;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

public class WBBattleStepREQAction extends BaseClientRequestHandler
{
    final static Logger logger = LoggerFactory.getLogger(WBBattleStepREQAction.class);

    @Override
    public void handleClientRequest(User user, ISFSObject isfsObject)
    {
		KodgamesExtension roomExt = (KodgamesExtension) getParentExtension();
        WBBattleStepREQ message = new WBBattleStepREQ();
        assembleMessage(user, isfsObject, message);
		Map<String, BWBattleStepRES> results = roomExt.getBattleService().battleStep(message);
		Set<Map.Entry<String, BWBattleStepRES>>  entry = results.entrySet();

		for(Map.Entry<String, BWBattleStepRES> one:entry){
			roomExt.send(CmdsUtils.CMD_BATTLE_STEP,one.getValue().toSFSObject(),getApi().getUserByName(one.getKey()));
		}

        //TODO: 判断是否是第一局结束，如果是第一局结束，扣除房卡 (battleStep已判断处理,未主动发送client通知)
    }


    private void assembleMessage(User user, ISFSObject isfsObject, WBBattleStepREQ message)
    {
        message.setAccountId(user.getName());
        message.setRoomId(isfsObject.getInt("roomId"));
        message.setCard(isfsObject.getInt("card"));
        message.setPlayType(isfsObject.getInt("playType"));
    }

}
