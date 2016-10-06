package com.rafo.chess.model;

import com.rafo.chess.logic.gymj.GYMJGame;
import com.rafo.chess.utils.GtypeUtils;

import java.util.List;

import static com.rafo.chess.model.GameRdb.getRoom;

/**
 * Created by 亚文 on 2016/9/8.
 */
public class AbstractGameFactory {

        public static AbstractGame create(RafoRoom room,List<GamePlayer> players){

            AbstractGame AGame = null;
            int gameType = room.getGameType();
            int playType = room.getPlayType();
            GtypeUtils.GameType gameTypeEnum = GtypeUtils.GameType.getByOrdinal( gameType );
            GtypeUtils.PlayType playTypeEnum = GtypeUtils.PlayType.getByOrdinal( playType );

            switch (gameTypeEnum){
                case MAHJONG:  //麻将
                    switch (playTypeEnum){
                        case GYMJ: //贵阳麻将
                            AGame = new GYMJGame(room,players);
                            break;

                    }
                    break;
                case POKER: //扑克
                    break;

            }



         return  AGame;
        }




        /**
         * 根据房间号得到游戏实例
         */
        public static  AbstractGame gameInstance ( int roomId ){
            AbstractGame AGame = null;
            RafoRoom room = getRoom(roomId);
            if(room != null){
                int gameType = room.getGameType();
                int playType = room.getPlayType();
                GtypeUtils.GameType gameTypeEnum = GtypeUtils.GameType.getByOrdinal( gameType );
                GtypeUtils.PlayType playTypeEnum = GtypeUtils.PlayType.getByOrdinal( playType );
                switch (gameTypeEnum){
                    case MAHJONG:  //麻将
                        switch (playTypeEnum){
                            case GYMJ: //贵阳麻将
                                AGame = new GYMJGame(roomId);
                                break;



                        }
                        break;
                    case POKER: //扑克
                        break;

                }

            }

            return  AGame;
        }
















}
