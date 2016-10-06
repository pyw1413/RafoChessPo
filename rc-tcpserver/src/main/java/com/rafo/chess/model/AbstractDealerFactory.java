package com.rafo.chess.model;

import com.rafo.chess.logic.gymj.GYMJDealer;
import com.rafo.chess.symj.SYMJDealer;
import com.rafo.chess.utils.GtypeUtils;

import java.util.List;

import static com.rafo.chess.model.GameRdb.getRoom;

/**
 * Created by 亚文 on 2016/9/8.
 */
public class AbstractDealerFactory {

        public static AbstractDealer create(RafoRoom room,List<GamePlayer> players){

            AbstractDealer ADealer = null;
            int playType = room.getPlayType();
            GtypeUtils.PlayType playTypeEnum = GtypeUtils.PlayType.getByOrdinal( playType );

                    switch (playTypeEnum){
                        case GYMJ: //贵阳玩法
                            ADealer = new GYMJDealer(room,players);
                            break;

                    }




         return  ADealer;
        }




        /**
         * 根据房间号得到牌局处理器实例
         */
        public static  AbstractDealer dealerInstance ( int roomId ){
            AbstractDealer ADealer = null;
            RafoRoom room = getRoom(roomId);
            if(room != null ){
                int gameType = room.getGameType();
                int playType = room.getPlayType();
                GtypeUtils.GameType gameTypeEnum = GtypeUtils.GameType.getByOrdinal( gameType );
                GtypeUtils.PlayType playTypeEnum = GtypeUtils.PlayType.getByOrdinal( playType );
                switch (gameTypeEnum){
                    case MAHJONG:  //麻将
                        switch (playTypeEnum){
                            case GYMJ: //贵阳玩法
                                ADealer =  new GYMJDealer(roomId);
                                break;
                            case SYMJ: //沈阳玩法
                            	ADealer = new SYMJDealer(roomId);

                        }
                        break;
                    case POKER: //扑克
                        break;

                }




            }
            return  ADealer;
        }








}
