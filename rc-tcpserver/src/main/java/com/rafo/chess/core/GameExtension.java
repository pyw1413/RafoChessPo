package com.rafo.chess.core;

import com.kodgames.battle.action.battle.WBBattlePlayerOfflineSYNAction;
import com.kodgames.battle.action.battle.WBBattleStartREQAction;
import com.kodgames.battle.action.battle.WBBattleStepREQAction;
import com.kodgames.battle.action.chat.WBChatREQAction;
import com.kodgames.battle.action.room.GBRoomDestoryREQAction;
import com.kodgames.battle.action.room.GBRoomJoinEventListener;
import com.kodgames.battle.action.room.GBRoomQuitREQAction;
import com.kodgames.battle.common.constants.GlobalConstants;
import com.kodgames.battle.entity.room.GBRoomCreateREQ;
import com.rafo.chess.handlers.chat.ChatHandler;
import com.rafo.chess.handlers.game.GamePlayerOfflineHandler;
import com.rafo.chess.handlers.game.GameStartHandler;
import com.rafo.chess.handlers.game.GameStepHandler;
import com.rafo.chess.handlers.room.RoomDestoryHandler;
import com.rafo.chess.handlers.room.RoomJoinEventHandler;
import com.rafo.chess.handlers.room.RoomQuitHandler;
import com.rafo.chess.manager.MySQLManager;
import com.rafo.chess.manager.RedisManager;
import com.rafo.chess.model.*;
import com.rafo.chess.resources.DataContainer;
import com.rafo.chess.service.GameService;
import com.rafo.chess.service.RafoRoomService;
import com.rafo.chess.utils.CmdsUtils;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.SFSExtension;

import java.util.*;

/**
 * Created by Administrator on 2016/9/12.
 */
public class GameExtension extends SFSExtension {

    private GameService gameService;
    private RafoRoomService roomService;

    @Override
    public void init() {
        RedisManager.getInstance().init(this);
        MySQLManager.getInstance().init(this);

        String resourcePath = "D:/config";
//        String dataPath = System.getProperty("user.dir") + "/bin";
        String dataPath =  "D:/work/RafoChessProject/rc-tcpserver/build/classes";
        DataContainer.getInstance().init(dataPath, resourcePath);

        gameService = new GameService(this);
        roomService = new RafoRoomService(this);

        addEventHandler(SFSEventType.USER_JOIN_ROOM, RoomJoinEventHandler.class);

        addEventHandler(SFSEventType.USER_DISCONNECT,GamePlayerOfflineHandler.class);

        addRequestHandler(CmdsUtils.CMD_ROOM_QUIT, RoomQuitHandler.class);
        addRequestHandler(CmdsUtils.CMD_ROOM_DESTROY,RoomDestoryHandler.class);
        addRequestHandler(CmdsUtils.CMD_ROOM_DESTROY_VOTE_REQ,RoomDestoryHandler.class);
        addRequestHandler(CmdsUtils.SFS_EVENT_CHAT_SYN,ChatHandler.class);

        addRequestHandler(CmdsUtils.CMD_BATTLE_START,GameStartHandler.class);
        addRequestHandler(CmdsUtils.CMD_BATTLE_STEP,GameStepHandler.class);

    }

    public boolean initService(GBRoomCreateREQ message, int playerId){
        if(gameService.getRoom() == null) {

            GameRoomSettings gameSetting = new GameRoomSettings();
            gameSetting.setRoomId(message.getRoomID());
            gameSetting.setRoomType(message.getBankerType());
            gameSetting.setGameType(message.getType());
            gameSetting.setPlayType(0);
            gameSetting.setPlayTypeExt1("1,3,4");
            gameSetting.setPlayTypeExt2("2,6,8,9,12");
            gameSetting.setRoomSize(4);
            gameSetting.setTotalRounds(GlobalConstants.COUNT2ROUNDMAP.get(message.getCount()));
            gameSetting.setCurrRounds(1);

            try {
                roomService.createRoom(gameSetting);
                gameService.setRoom(roomService.getRoom());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public GameService getGameService() {
        return gameService;
    }

    public RafoRoomService getRoomService() {
        return roomService;
    }
}
