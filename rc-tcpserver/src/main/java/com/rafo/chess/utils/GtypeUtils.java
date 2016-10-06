package com.rafo.chess.utils;

/**
 * Created by 亚文 on 2016/9/9.
 */
public class GtypeUtils {




    public enum GameType{

        //麻将游戏
        MAHJONG,
        //扑克牌游戏
        POKER
        ;

        public static GameType getByOrdinal(int gmType) {
            return values()[gmType];
        }

    }

    public enum PlayType{

        //贵阳麻将
        GYMJ,
        //二人转转
        ZZMJ,
        //沈阳麻将
        SYMJ,
        ;

        public static PlayType getByOrdinal(int mjPlaytype) {
            return values()[mjPlaytype];
        }

    }



}
