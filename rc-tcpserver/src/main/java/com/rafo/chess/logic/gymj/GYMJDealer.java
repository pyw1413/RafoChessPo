package com.rafo.chess.logic.gymj;

import com.rafo.chess.model.*;

import java.util.List;

import static com.rafo.chess.model.GameRdb.*;

/**
 * 贵阳麻将牌局处理器
 * Created by 亚文 on 2016/9/9.
 */
public class GYMJDealer extends  AbstractDealer {

    public GYMJDealer(){

    }

    public GYMJDealer(RafoRoom room , List<GamePlayer> plyers ){
        init( room, plyers  );
    }


    public void init( RafoRoom aroom , List<GamePlayer> plyers  ){
        room = aroom;   //设定房间
        players = plyers;  //匹配玩家
        makeDealerId();//牌局ID生成
        bankerSettings();
        shuffle();//选择牌池并洗牌
        currTurnPlayer = 0;  //当前轮到谁
        lastTurnPlayer = 0;  //上次轮到谁
        lastOutCard = 0; //上次打出的牌
        currOutCard = 0; //当前打出的牌
        CFJPlayer = 0; //冲锋鸡
        ZRJPlayer = 0;//责任鸡
        FPJCard = 0; //翻牌鸡
        isOver = false;//牌局開始

        putRoom(room);
        putGamePlayers( players );
        putRoomPlayerIds(room.getRoomId(), players );
        putDealer(this);
    }



    public  GYMJDealer( int roomId ){
        GYMJDealer dealer = GameRdb.getDealerByRoomId(roomId);
        if(dealer != null){
            room = dealer.room;
            players =dealer.players;
            dealerId = dealer.dealerId;
            cardsPool = dealer.cardsPool;
            currTurnPlayer = dealer.currTurnPlayer;
            lastTurnPlayer = dealer.lastTurnPlayer;
            bankPlayer = dealer.bankPlayer;
            CFJPlayer = dealer.CFJPlayer;
            ZRJPlayer = dealer.ZRJPlayer;
            FPJCard = dealer.FPJCard;
            isOver = dealer.isOver;
            currOutCard = dealer.currOutCard;
            lastOutCard = dealer.lastOutCard;
            waitPlayerGameActionList = dealer.waitPlayerGameActionList;
            waitActionList = dealer.waitActionList;
        }

    }




    @Override
    //发牌
    public List<GamePlayer> fapai(){
        return super.fapai();
    }

    @Override
    //取牌
    public Integer[] getCards( int count ){
        return super.getCards(count);
    }



    @Override
    //发牌之后
    public List<GameAction> afterFaPai(Integer[] handCards , List<GameAction> openCards , Integer mCard  ){
        return super.afterFaPai(handCards,openCards,mCard);
    }





}
