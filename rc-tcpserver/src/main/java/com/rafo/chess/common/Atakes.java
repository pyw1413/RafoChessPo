package com.rafo.chess.common;

import com.rafo.chess.model.Action;
import com.rafo.chess.model.GameAction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 亚文 on 2016/9/20.
 */
public class Atakes {

    //牌面转换
    public static String toPai( Integer cards ){
        String color_s = "";
        String value_s = "";
        int color = cards / 100 ;
        int value = cards % 100;
        switch (color){
            case 1:
                color_s = "万";
                break;
            case 2:
                color_s = "条";
                break;
            case 3:
                color_s = "筒";
                break;
        }

        switch ( value ){
            case 1:
                value_s = "一";
                break;
            case 2:
                value_s = "二";
                break;
            case 3:
                value_s = "三";
                break;
            case 4:
                value_s = "四";
                break;
            case 5:
                value_s = "五";
                break;
            case 6:
                value_s = "六";
                break;
            case 7:
                value_s = "七";
                break;
            case 8:
                value_s = "八";
                break;
            case 9:
                value_s = "九";
                break;
            case 10:
                value_s = "东风";
                break;
            case 11:
                value_s = "南风";
                break;
            case 12:
                value_s = "西风";
                break;
            case 13:
                value_s = "北风";
                break;
            case 14:
                value_s = "红中";
                break;
            case 15:
                value_s = "发财";
                break;
            case 16:
                value_s = "白板";
                break;
        }

        String str = "";
        if(value <=9 ){
            str = " " +  value_s + color_s;
        }else {
            str = " " +  value_s;
        }
        return  str;
    }


    public static String toP( Integer cards[] ){
        String str = "";
        if(cards != null ){
            for(int i = 0 ; i < cards.length ; i ++ ){
                str += toPai(cards[i]);
            }
        }
        return str;
    }


    public static String  ScreenInput()  {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = null;
        try {
            str = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  str;
    }


    public static boolean hasOpt ( List<GameAction> actionlist ){
        int count = 0 ;
        for( GameAction ga:   actionlist){
            switch (ga.getAction()){
                case  CHI:
                case  PENG:
                case  GANG:
                case  ANGANG:
                case  DIANGANG:
                case  PING_HU:
                case  DADUI_HU:
                case  QIDUI_HU:
                case  LONGQIDUI_HU:
                case  QINGYISE_HU:
                case  QINGQIDUI_HU:
                case  QINGDADUI_HU:
                case  QINGLONGDUI_HU:
                    count++;
                    break;
            }
        }
        if( count > 0 ){
            return  true;
        }else {
            return false;
        }
    }



    public static String canOpt ( List<GameAction> actionlist ){
        String str = "";
        for( GameAction ga:   actionlist){
           Action act =   ga.getAction();
            str += " " + toAc( act);
        }

        return str;
    }

    public static String toAc ( Action act ){
        String str = "";
     switch (act ){
         case TING:
             str = "听";
             break;
         case CHI:
             str = "吃";
             break;
         case PENG:
             str = "碰";
             break;
         case GANG:
         case ANGANG:
         case DIANGANG:
             str = "杠";
             break;
         case CAN_HU:
             str = "胡";
             break;
     }

        return str;
    }




    public static String toOpen( List<GameAction>  list ){

        String  str = "" ;
        if(list.size() > 0  ){
            for( GameAction ga :    list ){

                str += " " +  toP( ga.getCards().get(0) )  ;
            }
        }

         return  str;
    }






}
