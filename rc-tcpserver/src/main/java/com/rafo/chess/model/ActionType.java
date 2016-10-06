package com.rafo.chess.model;

/**
 * Created by 亚文 on 2016/9/8.
 */
public enum ActionType {

    LOSTGANG(1),//漏杠牌
    //鸡的类型
    PUTONGJI(2),//普通鸡
    CHONGFENGJI(3),//冲锋鸡
    ZERENJI(4),//责任鸡

    //胡的类型
    ZIMO(5),//自摸
    YINGTING(6), //硬报
    RUANTING(7), //软报
    TIANHU(8),//天胡
    GANGSHANGHU(9), //杠上胡
    REPAO(10),  //热炮（杠后放炮）
    QIANGGANGHU(11)//抢杠胡
    ;

    private int code;

     ActionType( int code ){
        this.code = code;
    }

    public int code(){
        return  code;
    }

    public static ActionType getByCode(int code ) {
        switch ( code){
            case 1:
                return LOSTGANG;
            case 2:
                return PUTONGJI;
            case 3:
                return CHONGFENGJI;
            case 4:
                return ZERENJI;
            case 5:
                return ZIMO;
            case 6:
                return YINGTING;
            case 7:
                return RUANTING;
            case 8:
                return TIANHU;
            case 9:
                return GANGSHANGHU;
            case 10:
                return REPAO;
            case 11:
                return QIANGGANGHU;
        }
        return null;
    }



}
