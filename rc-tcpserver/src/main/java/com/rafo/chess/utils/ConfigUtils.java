package com.rafo.chess.utils;

/**
 * Created by 亚文 on 2016/9/9.
 */
public class ConfigUtils {

    public static final  int SHUFFLE_TIMES = 10000;


    //redis table-field name
    public static final   String T_PLAYER = "player@";
    public static final   String T_ROOMPLAYERS = "room_players@";
    public static final   String T_ROOM = "room@";
    public static final   String T_DEALER = "dealer@";
    public static final   String T_SHENYANG_DEALER = "sydealer@";
    public static final   String T_RECORDER = "recorder@";

    //  6|203,207,206^0&205,205,205,205^1
    public static final   String SEP_LIST = ";";
    public static final   String SEP_USER = "@";
    public static final   String SEP_ACTION = "|";
    public static final   String SEP_ACTION_T = "\\|";
    public static final   String SEP_SENTENCE = "&";
    public static final   String SEP_CARD = ",";
    public static final   String SEP_ACTYPE = "^";
    public static final   String SEP_ACTYPE_T = "\\^";


    public static final   int  MJ_HANDCARDS = 13;



    public static final   boolean   CHIPAI_FLG = false;  //能否吃牌玩法开关
    public static final   boolean   HASDOU_FLG = true;  //是否有豆玩法开关
    public static final   boolean   CHONGFENGJI_FLG = true;  //冲锋鸡玩法开关
    public static final   boolean   FIRSTTING_FLG = true;  //听牌必须第一次摸牌玩法开关









}
