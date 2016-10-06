package com.rafo.chess.handlers.game;

import com.kodgames.battle.entity.battle.WBBattleStepREQ;
import com.rafo.chess.core.GameExtension;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameStepHandler extends BaseClientRequestHandler
{
    final static Logger logger = LoggerFactory.getLogger(GameStepHandler.class);

    @Override
    public void handleClientRequest(User user, ISFSObject isfsObject)
    {
        GameExtension roomExt = (GameExtension) getParentExtension();
        WBBattleStepREQ message = new WBBattleStepREQ();
        assembleMessage(user, isfsObject, message);

        roomExt.getGameService().play(Integer.parseInt(user.getName()), message.getPlayType(), message.getCard());
    }


    private void assembleMessage(User user, ISFSObject isfsObject, WBBattleStepREQ message)
    {
        message.setAccountId(user.getName());
        message.setRoomId(isfsObject.getInt("roomId"));
        message.setCard(isfsObject.getInt("card"));
        message.setPlayType(isfsObject.getInt("playType"));
    }

}
