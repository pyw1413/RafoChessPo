package com.rafo.chess.model;

import static com.rafo.chess.common.Avatas.*;
import static com.rafo.chess.utils.ConfigUtils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rafo.chess.logic.gymj.GYMJDealer;
import com.rafo.chess.manager.RedisManager;
import com.rafo.chess.symj.SYMJDealer;

/**
 * Created by 亚文 on 2016/9/10.
 */
public class GameRdb {


    public static void putGamePlayer( GamePlayer player){
        String key = T_PLAYER + player.getUid();
        Map<String, String> playermap = new HashMap<>();
        playermap.put("uid",String.valueOf(player.getUid()));
        playermap.put("uid",String.valueOf(player.getUid()));
        playermap.put("roomId",player.getRoomId());
        playermap.put("wuid",player.getWuid());
        playermap.put("nickName",player.getNickName());
        playermap.put("pic",player.getPic());
        playermap.put("gender",String.valueOf( player.getGender() ));
        playermap.put("ip",player.getIp());
        playermap.put("roomCard",String.valueOf(player.getRoomCard()));
        playermap.put("seatNo",String.valueOf(player.getSeatNo()));
        playermap.put("score",String.valueOf(player.getScore()));
        playermap.put("gold",String.valueOf(player.getGold()));
        playermap.put("banker",String.valueOf(player.isBanker()));
        playermap.put("handCards", Array2String( player.getHandCards() ));
        playermap.put("openCards", ListGameAction2String( player.getOpenCards() ));
        playermap.put("outCards", Array2String( player.getOutCards() ));
        playermap.put("inCards", Array2String( player.getInCards() ));
        playermap.put("mCard", String.valueOf(player.getmCard()));
        playermap.put("lastGameAction", GameAction2String( player.getLastGameAction() ));
        playermap.put("currActions", ListGameAction2String( player.getCurrentActions()));
        playermap.put("reachStatus",String.valueOf(player.isReachStatus()));
        playermap.put("firstMoPai",String.valueOf(player.isFirstMoPai()));
        playermap.put("lostGangCards", Array2String( List2Array( player.getLostGangCards()) )  );
        RedisManager.getInstance().hMSet(key,playermap);

    }



    public static GamePlayer getGamePlayer(int playerUid ){
        String key = T_PLAYER +  playerUid;
        Map<String, String> playermap =  RedisManager.getInstance().hMGetAll( key);
        GamePlayer gplayer = new GamePlayer();
        gplayer.setUid(  Integer.parseInt( playermap.get("uid") ) );
        gplayer.setRoomId(playermap.get("roomId") );
        gplayer.setWuid(playermap.get("wuid") );
        gplayer.setNickName(playermap.get("nickName") );
        gplayer.setPic(playermap.get("pic") );
        gplayer.setGender(Integer.parseInt( playermap.get("gender")) );
        gplayer.setIp(playermap.get("ip") );
        gplayer.setRoomCard(  Integer.parseInt( playermap.get("roomCard") ) );
        gplayer.setSeatNo(  Integer.parseInt( playermap.get("seatNo") ) );
        gplayer.setScore(  Integer.parseInt( playermap.get("score") ) );
        gplayer.setGold(  Integer.parseInt( playermap.get("gold") ) );
        gplayer.setBanker(  String2Boolean(playermap.get("banker")) ) ;
        gplayer.setHandCards( String2Array( playermap.get("handCards")  ));
        gplayer.setOpenCards( String2GameActionList( playermap.get("openCards")  ));
        gplayer.setOutCards( String2Array( playermap.get("outCards")  ));
        gplayer.setInCards( String2Array( playermap.get("inCards")  ));
        gplayer.setmCard( Integer.parseInt( playermap.get("mCard")  ));
        gplayer.setLastGameAction(  String2GameAction( playermap.get("lastGameAction")  ));
        gplayer.setCurrentActions(   String2GameActionList( playermap.get("currActions") ));
        gplayer.setReachStatus( String2Boolean(playermap.get("reachStatus")) );
        gplayer.setFirstMoPai( String2Boolean(playermap.get("firstMoPai")) );
        gplayer.setLostGangCards( Array2List( String2Array( playermap.get("lostGangCards") )) );

        return  gplayer;
    }

    public static void putGamePlayers( List<GamePlayer> playerList ){
        for(GamePlayer player : playerList ){
            putGamePlayer(player);
        }
    }

    public static List<GamePlayer> getRoomPlayers( int roomId ){
        Integer[] playerIds = String2Array( getRoomPlayerIds( roomId ) );
        List<GamePlayer> list = new ArrayList<>();
        for(int pid : playerIds){
            list.add( getGamePlayer( pid) );
        }
        return list;
    }


    public static void putRoomPlayerIds( int roomId , List<GamePlayer> playerList){
        String key = T_ROOMPLAYERS +  roomId;
        RedisManager.getInstance().hMSet(key,"uid",Uids2String(playerList) );
    }

    public static String getRoomPlayerIds( int roomId ){
        String key = T_ROOMPLAYERS +  roomId;
        String str = RedisManager.getInstance().hGet(key,"uid");

        return  str;
    }


    public static  void putRoom (  RafoRoom room ){
        String key = T_ROOM +  room.getRoomId();
        Map<String, String> roommap = new HashMap<>();
        roommap.put("roomId",String.valueOf(room.getRoomId())  );
        roommap.put("password",String.valueOf(room.getPassword())  );
        roommap.put("gameType",String.valueOf(room.getGameType())  );
        roommap.put("playType",String.valueOf( room.getPlayType())  );
        roommap.put("playTypeExt1",String.valueOf( room.getPlayTypeExt1())  );
        roommap.put("playTypeExt2",String.valueOf( room.getPlayTypeExt2())  );
        roommap.put("roomSize",String.valueOf( room.getRoomSize()) );
        roommap.put("totalRounds",String.valueOf(room.getTotalRounds())  );
        roommap.put("currRounds",String.valueOf(room.getCurrRounds())  );
        roommap.put("roomStatus",String.valueOf(room.getRoomStatus())  );
        roommap.put("bankPlayer",String.valueOf(room.getBankPlayer())  );
        RedisManager.getInstance().hMSet(key,roommap);

    }

    public static RafoRoom getRoom (int roomId ) {
        String key = T_ROOM + roomId;
        RafoRoom room = null;
        Map<String, String> map =  RedisManager.getInstance().hMGetAll(key);
        if( map.size() > 0 ){
            room = new RafoRoom();
            room.setRoomId(  Integer.parseInt( map.get("roomId") )  ) ;
            room.setPassword(  Integer.parseInt( map.get("password") )  );
            room.setGameType( Integer.parseInt( map.get("gameType") ) );
            room.setPlayType( Integer.parseInt( map.get("playType") ) );
            room.setPlayTypeExt1( map.get("playTypeExt1")  );
            room.setPlayTypeExt2(  map.get("playTypeExt2")  );
            room.setRoomSize( Integer.parseInt( map.get("roomSize") ) );
            room.setTotalRounds( Integer.parseInt( map.get("totalRounds") ) );
            room.setCurrRounds( Integer.parseInt( map.get("currRounds") ));
            room.setRoomStatus(  Integer.parseInt( map.get("roomStatus") ));
            room.setBankPlayer(  Integer.parseInt( map.get("bankPlayer") ));
        }
        return  room;
    }


    public static  void putDealer ( AbstractDealer dealer){

        String key = T_DEALER + dealer.getDealerId();
        Map<String, String> dealermap = new HashMap<>();

        dealermap.put("dealerId",String.valueOf(dealer.getDealerId())  );
        dealermap.put("cardsPool",Array2String(dealer.getCardsPool())  );
        dealermap.put("currTurnPlayer",String.valueOf(dealer.getCurrTurnPlayer())  );
        dealermap.put("lastTurnPlayer",String.valueOf( dealer.getLastTurnPlayer())  );
        dealermap.put("bankPlayer",String.valueOf( dealer.getBankPlayer())  );
        dealermap.put("CFJPlayer",String.valueOf( dealer.getCFJPlayer())  );
        dealermap.put("ZRJPlayer",String.valueOf( dealer.getZRJPlayer())  );
        dealermap.put("FPJCard",String.valueOf( dealer.getFPJCard())  );
        dealermap.put("isOver",String.valueOf( dealer.isOver())  );
        dealermap.put("currOutCard",String.valueOf( dealer.getCurrOutCard())  );
        dealermap.put("lastOutCard",String.valueOf( dealer.getLastOutCard())  );
        dealermap.put("waitPlayerActions", PlayerGameActionList2String( dealer.getWaitPlayerGameActionList() )   );
        dealermap.put("waitActions", PlayerGameActionList2String( dealer.getWaitActionList()  ) );
        RedisManager.getInstance().hMSet(key,dealermap);
    }

    public static void delDealer( int dealerId ){
        String key = T_DEALER + dealerId;
        RedisManager.getInstance().del( key );
    }

    public static void delRecorder( int dealerId ){
        String key = T_RECORDER + dealerId;
        RedisManager.getInstance().del( key );
    }

    public static GYMJDealer getDealerById (int dealerId ){
        int roomId = dealerId/100;
        String key = T_DEALER + dealerId;

        GYMJDealer mjdealer = null;
        Map<String, String> map =  RedisManager.getInstance().hMGetAll(key);
        if(map.size() > 0 ){
            mjdealer = new GYMJDealer();
            mjdealer.setDealerId(  Integer.parseInt( map.get("dealerId") )  );
            mjdealer.setCardsPool( String2Array( map.get("cardsPool") ) );
            mjdealer.setCurrTurnPlayer(  Integer.parseInt( map.get("currTurnPlayer") ) );
            mjdealer.setLastTurnPlayer(  Integer.parseInt( map.get("lastTurnPlayer") ) );
            mjdealer.setBankPlayer( Integer.parseInt( map.get("bankPlayer") ) );
            mjdealer.setCFJPlayer( Integer.parseInt( map.get("CFJPlayer") ) );
            mjdealer.setZRJPlayer( Integer.parseInt( map.get("ZRJPlayer") ) );
            mjdealer.setFPJCard( Integer.parseInt( map.get("FPJCard") ) );
            mjdealer.setOver( String2Boolean( map.get("isOver"))  );
            mjdealer.setCurrOutCard( Integer.parseInt( map.get("currOutCard") ) );
            mjdealer.setLastOutCard( Integer.parseInt( map.get("lastOutCard") ) );
            mjdealer.setWaitPlayerGameActionList(String2PlayerGameActionList( map.get("waitPlayerActions") ) );
            mjdealer.setWaitActionList(String2PlayerGameActionList( map.get("waitActions"))  );
            RafoRoom room =  getRoom( roomId );
            List<GamePlayer> players = getRoomPlayers( roomId );
            mjdealer.setRoom( room );
            mjdealer.setPlayers( players );
        }
        return mjdealer;
    }

    public static GYMJDealer getDealerByRoomId(int roomId  ){
        RafoRoom room =  getRoom(roomId);
        int dealerId =  room.getRoomId()* 100  + room.getCurrRounds();
        GYMJDealer mjdealer = getDealerById( dealerId );

        return mjdealer;
    }
    
    /**
     * 获得沈阳麻将处理器
     * @param roomId
     * @return
     */
    public static SYMJDealer getSYDealerByRoomId(int roomId  ){
        RafoRoom room =  getRoom(roomId);
        int dealerId =  room.getRoomId()* 100  + room.getCurrRounds();
        SYMJDealer mjdealer = getSYDealerById( dealerId );
        //TODO:要修改处理类,根据不同的类型取出不同的处理器
        return mjdealer;
    }
    
    
    /**
     * 获得沈阳麻将处理器
     * @param dealerId
     * @return
     */
    public static SYMJDealer getSYDealerById (int dealerId ){
        int roomId = dealerId/100;
        String key = T_DEALER + dealerId;
        SYMJDealer mjdealer = null;
        Map<String, String> map =  RedisManager.getInstance().hMGetAll(key);
        if(map.size() > 0 ){
            mjdealer = new SYMJDealer();
            mjdealer.setDealerId(  Integer.parseInt( map.get("dealerId") )  );
            mjdealer.setCardsPool( String2Array( map.get("cardsPool") ) );
            mjdealer.setCurrTurnPlayer(  Integer.parseInt( map.get("currTurnPlayer") ) );
            mjdealer.setLastTurnPlayer(  Integer.parseInt( map.get("lastTurnPlayer") ) );
            mjdealer.setBankPlayer( Integer.parseInt( map.get("bankPlayer") ) );
            mjdealer.setCurrOutCard( Integer.parseInt( map.get("currOutCard") ) );
            mjdealer.setLastOutCard( Integer.parseInt( map.get("lastOutCard") ) );
            mjdealer.setWaitPlayerGameActionList(String2PlayerGameActionList( map.get("waitPlayerActions") ) );
            mjdealer.setWaitActionList(String2PlayerGameActionList( map.get("waitActions"))  );
            RafoRoom room =  getRoom( roomId );
            List<GamePlayer> players = getRoomPlayers( roomId );
            mjdealer.setRoom( room );
            mjdealer.setPlayers( players );
        }
        return mjdealer;
    }

    public static void  putRecord(int dealerId,  String record ){
        String key = T_RECORDER + dealerId;
        RedisManager.getInstance().rPush(key,record);
    }


    public static List<RecorderInfo> getRecord( int dealerId ){
        String key = T_RECORDER + dealerId;
        List<String> list =  RedisManager.getInstance().lRang(key,0,-1);

        List<RecorderInfo> recordList = new ArrayList<>();
        for(  String  record:    list ){
            RecorderInfo recorderInfo = new RecorderInfo();
            String[] paction =  record.split(SEP_LIST);
            if(paction.length == 1 ){
                PlayerGameAction act =   String2PlayerGameAction( paction[0] );
                recorderInfo.setUid( act.getPuid());
                recorderInfo.setCardFrom( act.getPuid());
                recorderInfo.setCard( act.getGameActions().get(0).getCards().get(0)[0] );
                recorderInfo.setPlayType( act.getGameActions().get(0).getAction().code() );
            }else if( paction.length == 2 ){
                PlayerGameAction acta =   String2PlayerGameAction( paction[0] );
                PlayerGameAction actb =   String2PlayerGameAction( paction[1] );
                recorderInfo.setUid( acta.getPuid());
                recorderInfo.setCardFrom( actb.getPuid());
                recorderInfo.setCard( actb.getGameActions().get(0).getCards().get(0)[0] );
                recorderInfo.setPlayType( acta.getGameActions().get(0).getAction().code() );
            }
            recordList.add( recorderInfo);
        }
        return  recordList ;
    }






}
