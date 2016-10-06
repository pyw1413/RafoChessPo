package com.kodgames.battle.service;

import com.kodgames.battle.entity.battle.BWBattleStepRES;
import com.kodgames.battle.entity.battle.WBBattleStartREQ;
import com.kodgames.battle.entity.room.GBRoomCreateREQ;
import com.kodgames.battle.entity.room.GBRoomEnterREQ;
import com.kodgames.battle.service.battle.BattleService;
import com.kodgames.battle.service.room.RoomService;
import com.rafo.chess.manager.RedisManager;
import com.rafo.chess.model.GameRoomSettings;
import com.rafo.chess.model.RafoRoom;
import com.rafo.chess.resources.DataContainer;
import com.rafo.chess.service.GameService;
import com.rafo.chess.service.RafoRoomService;
import org.junit.BeforeClass;

import java.util.Map;

/**
 * Created by Administrator on 2016/9/18.
 */
public class BattleBaseServiceTest {

    static RoomService roomService = new RoomService();
    static GameService gameService  = new GameService(null);
    static RafoRoomService rafoRoomService  = new RafoRoomService(null);
    static int roomID = 310232;
    static BattleService battleService;

    @BeforeClass
    public static void setupRoom(){
        createRoom();
        enterRoom();

        initGameService();

    }

    //房间模拟
    private static void createRoom(){
        GBRoomCreateREQ message = new GBRoomCreateREQ();
        message.setAccountID("A");
        message.setRoomID(roomID);
        message.setIp("1.1.1.1");
        message.setCount(0);
        message.setID(201);
        message.setType(0);

        roomService.createRoom(message);
        battleService = new BattleService();
        battleService.init(roomService.getRoom());
        
//        GBRoomCreateREQ message = new GBRoomCreateREQ();
//        message.setAccountID("A");
//        message.setRoomID(roomID);
//        message.setIp("1.1.1.1");
//        message.setCount(0);
//        message.setID(101);
//        message.setType(0);
//
//        roomService.createRoom(message);
//        battleService = new BattleService(roomService.getRoom());
    }

    private static GBRoomEnterREQ createUser(int id, String account){
        GBRoomEnterREQ message = new GBRoomEnterREQ();
        message.setRoomID(roomID);
        message.setIp("1.1.1.1");
        message.setID(id);
        message.setHead("");
        message.setSex("man");
        message.setAccountID(account);
        message.setName(account+ "_name");
        return message;
    }

    //玩家模拟
    private static void enterRoom(){
        roomService.enterRoom(createUser(201, "A"));
        roomService.enterRoom(createUser(202, "B"));
        roomService.enterRoom(createUser(203, "C"));
        roomService.enterRoom(createUser(204, "D"));
    }


    public static void initGameService(){

        String resourcePath = "D:/config";
        String dataPath = System.getProperty("user.dir") + "/build/classes";
        DataContainer.getInstance().init(dataPath, resourcePath);

        RedisManager.getInstance().init("localhost");

        GameRoomSettings gsetting = new GameRoomSettings();
        gsetting.setPassword(roomID);
        gsetting.setGameType(0);
        gsetting.setPlayType(0);
        gsetting.setPlayTypeExt1("1,3,4");
        gsetting.setPlayTypeExt2("2,6,8,9,12");
        gsetting.setRoomSize(4);
        gsetting.setTotalRounds(8);
        gsetting.setCurrRounds(1);
        gsetting.setRoomId(roomID);

        rafoRoomService.createRoom(gsetting);

        for (int i = 0; i < gsetting.getRoomSize(); i++) {
            rafoRoomService.enterRoom("A", 201, "1", "1", "");
            rafoRoomService.enterRoom("B", 202, "1", "1", "");
            rafoRoomService.enterRoom("C", 203, "1", "1", "");
            rafoRoomService.enterRoom("D", 204, "1", "1", "");
        }

        gameService.ready(201);
        gameService.ready(202);
        gameService.ready(203);
        gameService.ready(204);

        gameService.getGyMJGame().dealFaPai();
    }

}
