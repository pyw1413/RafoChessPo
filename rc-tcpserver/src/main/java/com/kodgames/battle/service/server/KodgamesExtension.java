package com.kodgames.battle.service.server;

import com.kodgames.battle.action.battle.WBBattlePlayerOfflineSYNAction;
import com.kodgames.battle.action.battle.WBBattleStartREQAction;
import com.kodgames.battle.action.battle.WBBattleStepREQAction;
import com.kodgames.battle.action.chat.WBChatREQAction;
import com.kodgames.battle.action.room.*;
import com.kodgames.battle.service.battle.BattleService;
import com.kodgames.battle.service.chat.ChatService;
import com.kodgames.battle.service.record.RecordService;
import com.kodgames.battle.service.room.RoomService;
import com.rafo.chess.manager.MySQLManager;
import com.rafo.chess.manager.RedisManager;
import com.rafo.chess.utils.CmdsUtils;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.extensions.SFSExtension;

/**
 * Created by Administrator on 2016/9/17.
 */
public class KodgamesExtension extends SFSExtension {

    private RoomService roomService;
    private BattleService battleService;
    private ChatService chatService;
    private RecordService recordService;

    @Override
    public void init() {
        RedisManager.getInstance().init(this);
        //MySQLManager.getInstance().init(this);
        roomService = new RoomService();
        recordService = new RecordService();
        addEventHandler(SFSEventType.USER_JOIN_ROOM, GBRoomJoinEventListener.class);

        addEventHandler(SFSEventType.USER_DISCONNECT,WBBattlePlayerOfflineSYNAction.class);

        addRequestHandler(CmdsUtils.CMD_ROOM_QUIT, GBRoomQuitREQAction.class);
        addRequestHandler(CmdsUtils.CMD_ROOM_DESTROY,GBRoomDestoryREQAction.class);
        addRequestHandler(CmdsUtils.CMD_ROOM_DESTROY_VOTE_REQ,GBRoomDestoryREQAction.class);
        addRequestHandler(CmdsUtils.SFS_EVENT_CHAT_SYN,WBChatREQAction.class);

        addRequestHandler(CmdsUtils.CMD_BATTLE_START,WBBattleStartREQAction.class);
        addRequestHandler(CmdsUtils.CMD_BATTLE_STEP,WBBattleStepREQAction.class);

        roomService.setRoomExt(this);
        battleService = new BattleService();
        chatService = new ChatService();
        battleService.setRoomExt(this);
    }

    public void initService(){
        battleService.init(roomService.getRoom());
        chatService.init(roomService.getRoom());
    }

    public RoomService getRoomService() {
        return roomService;
    }

    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }

    public ChatService getChatService() {
        return chatService;
    }

    public void setChatService(ChatService chatService) {
        this.chatService = chatService;
    }

    public RecordService getRecordService() {
        return recordService;
    }

    public void setRecordService(RecordService recordService) {
        this.recordService = recordService;
    }

    public BattleService getBattleService() {
        return battleService;
    }

    public void setBattleService(BattleService battleService) {
        this.battleService = battleService;
    }
}
