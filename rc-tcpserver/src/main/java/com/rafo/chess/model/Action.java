package com.rafo.chess.model;

/**
 * Created by Administrator on 2016/9/8.
 */
public enum Action {
    //发牌
    DEAL(0),
    //听牌
    TING(1),
    //摸牌
    MO(2),
    //打牌
    DA(3),
    //过牌
    GUO(4),
    //吃牌
    CHI(5),
    //碰牌
    PENG(6),
    //杠
    GANG(7) ,
    //暗杠
    ANGANG(8),
    //点杠
    DIANGANG(9),

    //可胡
    CAN_HU(10),


    //冲锋鸡
    CHONGFENGJI(11),
    //责任鸡
    ZERENJI(12),

    //黄庄
    HUANG_ZHUANG(20),
    //平胡
    PING_HU(21),
    //大对子
    DADUI_HU(22) ,
    //七对
    QIDUI_HU(23) ,
    //龙七对
    LONGQIDUI_HU(24),
    //清一色
    QINGYISE_HU(25),
    //清七对
    QINGQIDUI_HU(26),
    //清大对
    QINGDADUI_HU(27) ,
    //青龙对
    QINGLONGDUI_HU(28),

    ;
    private  int code;

    Action( int numer ) {
        this.code = numer;
    }

    public int code(){
        return  code;
    }

    public static Action getByCode(int code ) {
        switch ( code){
            case 1:
                return TING;
            case 2:
                return MO;
            case 3:
                return DA;
            case 4:
                return GUO;
            case 5:
                return CHI;
            case 6:
                return PENG;
            case 7:
                return GANG;
            case 8:
                return ANGANG;
            case 9:
                return DIANGANG;
            case 10:
                return CAN_HU;
            case 11:
                return CHONGFENGJI;
            case 12:
                return ZERENJI;


            case 20:
                return HUANG_ZHUANG;
            case 21:
                return PING_HU;
            case 22:
                return DADUI_HU;
            case 23:
                return QIDUI_HU;
            case 24:
                return LONGQIDUI_HU;
            case 25:
                return QINGYISE_HU;
            case 26:
                return QINGQIDUI_HU;
            case 27:
                return QINGDADUI_HU;
            case 28:
                return QINGLONGDUI_HU;

        }
        return null;
    }



}
