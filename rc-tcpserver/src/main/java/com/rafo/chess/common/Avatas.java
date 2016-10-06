package com.rafo.chess.common;

import com.rafo.chess.model.*;

import java.util.ArrayList;
import java.util.List;

import static com.rafo.chess.utils.CardsUtils.SingleCardsPool;
import static com.rafo.chess.utils.ConfigUtils.*;

/**
 * Created by 亚文 on 2016/9/9.
 */
public class Avatas {


 /* --------------------------------------------------------------------------------------------------------------------
 * 常规数组操作    通用
 * ---------------------------------------------------------------------------------------------------------------------
 */

    //数组转字符串
    public static String Array2String(Integer[]  array ){
        String  str = "";
        if(array != null ){
            for(int i = 0 ; i < array.length ; i++ ){
                str += SEP_CARD +  array[i];
            }
        }

        str = str.replaceFirst(SEP_CARD,"");
        return  str;
    }

    //字符串转数组
    public static Integer [] String2Array(String str ){
        Integer[] arryInt = null ;
        if( !str.equals("") &&  str != null  ){
            String [] arryStr = str.split(SEP_CARD);
            arryInt = new Integer[arryStr.length ];
            for(int i = 0 ; i < arryStr.length;i++){
                arryInt[i] =Integer.parseInt( arryStr[i] ) ;
            }
        }

        return  arryInt;
    }

    public static List<Integer> Array2List(Integer[] array){
        List<Integer> list = new ArrayList<>();
        if( array != null && array.length > 0){
            for( int i = 0 ; i < array.length ; i ++   ){
                list.add(array[i] );
            }
        }
        return  list;
    }

    public static Integer[] List2Array( List<Integer> list ){
        Integer[] array = null;
        if( list != null && list.size() > 0 ){
            array = new Integer[ list.size()];
            for(int i = 0 ; i < list.size(); i ++ ){
                array[i] = list.get(i);
            }
        }
        return  array;
    }


    //排序
    public static  Integer[] ArraySort ( Integer[] cards ){
        if( cards != null ){
            for(int i = 0 ; i < cards.length ; i++  ){
                Integer tmp = cards[i];
                for( int j = i+1 ; j < cards.length ; j++ ){
                    if( tmp.intValue() > cards[j].intValue() ){
                        tmp = cards[j];
                        cards[j] = cards[i];
                        cards[i] =tmp;
                    }
                }
            }
        }
        return cards;
    }

    public static boolean  String2Boolean (String str ){
        if( str != null && str !="" ){
            if( str.equals( "true")){
                return  true;
            }else if(str.equals( "false")){
                return  false;

            }
        }
        return false;
    }

    //找出最大
    public static Integer ArrayMax( Integer[] cards ){
        Integer temp = null;
        if( cards != null ){
            temp = cards[0];
            for(int i = 0; i < cards.length ; i++ ){
                if(temp.intValue() <  cards[i].intValue()  ){
                    temp = cards[i];
                }
            }
        }
        return temp;
    }

    //找出最小
    public static Integer ArrayMin( Integer[] cards ){
        Integer temp = null;
        if( cards != null ){
            temp = cards[0];
            for(int i = 0; i < cards.length ; i++ ){
                if(temp.intValue() >  cards[i].intValue()  ){
                    temp = cards[i];
                }
            }
        }
        return temp;
    }


    public static Integer[] ArrayRemove( Integer[] cards , Integer ocard ){
        int count = 0;
        Integer[] reCards = new Integer[cards.length -1 ];
        int j = 0;
        for( int i = 0 ; i < cards.length ; i++ ){
            if( count == 0 &&  cards[i].equals( ocard )  ){
                count ++ ;
            }else {
                reCards[j] = cards[i];
                j++;
            }
        }
        return reCards;
    }

    public static Integer[] ArrayRemove( Integer[] cards , Integer[] ocards ){
       for(int i = 0; i < ocards.length; i ++  ){
           cards =   ArrayRemove(cards,ocards[i]);
       }
        return cards;
    }

    public static Integer[] ArrayRemoveAll( Integer[] cards , Integer ocard ){
        Integer[] reCards = null;
        if( cards != null ){
            int count = 0;
            for( int i = 0 ; i < cards.length ; i++ ){
                if( cards[i].equals( ocard )  ){
                    count ++ ;
                }
            }
            reCards = new Integer[cards.length - count ];
            int k = 0;
            for( int j = 0 ; j < cards.length ; j++ ){
                if( !cards[j].equals( ocard )  ){
                    reCards[k] = cards[j];
                    k++;
                }
            }
        }
        return reCards;
    }

    public static Integer[] ArrayAdd (  Integer[] cards , Integer ocard ){
        Integer[] reCards = null;
        if(cards != null ){
             reCards = new Integer[cards.length + 1 ];
            for(int i = 0 ; i < cards.length ; i++ ){
                reCards[i] = cards[i];
            }
            reCards[cards.length] = ocard;
        }else {
            reCards = new Integer[1];
            reCards[0] = ocard;
        }

        return  reCards;
    }

    public static Integer[] ArrayAdd (  Integer[] cards , Integer[] ocard ){
        Integer[] reCards = new Integer[cards.length + ocard.length ];
        for(int i = 0 ; i < cards.length ; i++ ){
            reCards[i] = cards[i];
        }
        for(int j = 0 ; j < ocard.length ; j++ ){
            reCards[cards.length+j] = ocard[j];
        }
        return  reCards;
    }

    public static boolean Contains( Integer[] cards,Integer ocard ){
        if(cards != null && cards.length > 0 ){
            for( int i = 0 ; i < cards.length ; i ++ ){
                if( cards[i].equals( ocard )){
                    return  true;
                }
            }
        }
        return  false;
    }





 /* --------------------------------------------------------------------------------------------------------------------
 * 对象列表-字符串互转操作
 * ---------------------------------------------------------------------------------------------------------------------
 */

    //打牌动作转字符串   动作|动作类型^牌,牌,牌 0|0^102,203,204&204,302,208&302,109,106
    public static String GameAction2String(GameAction action ){
        String str = "";
        if( action != null ){
            str += action.getAction().code() + SEP_ACTION;
            if(action.getActionType() != null){
                str += action.getActionType().code() + SEP_ACTYPE;
            }
            if( action.getCards() != null  ){
                for(Integer[] cards : action.getCards()){
                    str += SEP_SENTENCE + Array2String( cards );
                }
            }
        }
        str = str.replaceFirst(SEP_SENTENCE,"");
        return  str;
    }

    //打牌动作列表转字符串  0|0^102,203,204&204,302,208&302,109,106; 0|0^102,203,204&204,302,208&302,109,106
    public static String ListGameAction2String(List<GameAction> list ){
        String str = "";
        if(list != null ){
            for( GameAction action : list ){
                if( action != null ){
                    str += SEP_LIST + GameAction2String( action );
                }
            }
        }

        str =  str.replaceFirst(SEP_LIST,"");

        return str;
    }


    //明牌动作列表取牌的数组
    public static Integer[] OpenCards2Array(List<GameAction> list ){
       Integer[]  opencards = new Integer[0];
        if(list.size() > 0  ){
            for( GameAction action : list ){
                if( action != null ){
                    opencards = ArrayAdd(opencards,action.getCards().get(0) );
                }
            }
        }
        return opencards;
    }




    //字符串转打牌动作   0|0^ 102,203&204,205&302,304
    public static GameAction String2GameAction(String  str ){
        GameAction action = null;
        if( !str.equals("")  &&  str != null  ){
            action = new GameAction();
            String[] act =  str.split(SEP_ACTION_T); // |
            action.setAction( Action.getByCode( Integer.parseInt( act[0] ) )   );
            if( act.length == 2 ){
                if(act[1].contains(SEP_ACTYPE)){
                    String[] type = act[1].split(SEP_ACTYPE_T); // ^
                    if(type.length == 2){
                        action.setActionType( ActionType.getByCode(  Integer.parseInt(type[0]) )  );
                        String[] santes = type[1].split(SEP_SENTENCE);
                        List<Integer[]> list = new ArrayList<>();
                        for(int i = 0 ; i < santes.length ; i++ ){
                            Integer[] cards = String2Array( santes[i] );
                            list.add(cards);
                        }
                        action.setCards(list);
                    }else if(type.length == 1){
                        action.setActionType( ActionType.getByCode(  Integer.parseInt(type[0]) )  );
                    }
                }else {
                    String[] santes = act[1].split(SEP_SENTENCE);
                    List<Integer[]> list = new ArrayList<>();
                    for(int i = 0 ; i < santes.length ; i++ ){
                        Integer[] cards = String2Array( santes[i] );
                        list.add(cards);
                    }
                    action.setCards(list);
                }
            }
        }

        return action;
    }

    //字符串转打牌动作列表
    public static List<GameAction> String2GameActionList( String str ){

        List<GameAction> list =  new ArrayList<>();
        if( !str.equals("") &&  str != null   ){
            String[] strAct = str.split(SEP_LIST);
            for(int i = 0 ; i < strAct.length ; i ++ ){
                list.add( String2GameAction( strAct[i]) ) ;
            }
        }

        return  list;
    }

    //列表中对象的字段转字符串
    public static String  Uids2String(List<GamePlayer> list ){
        String str = "";
        if(list != null ){
            for(GamePlayer player : list ){
                str += SEP_CARD + player.getUid();
            }
        }
        str = str.replaceFirst(SEP_CARD,"");
        return str;
    }


   //用户id@动作
    public static PlayerGameAction String2PlayerGameAction(String str ){
        PlayerGameAction paction = null ;
        if( !str.equals("") &&  str != null  ){
            paction = new PlayerGameAction();
            String[] pact = str.split(SEP_USER);
            paction.setPuid( Integer.parseInt( pact[0] ) );
            String[] acts =  pact[1].split(SEP_SENTENCE);
            List<GameAction> list = new ArrayList<>();
            for(int i = 0 ; i < acts.length ; i++ ){
                list.add( String2GameAction( acts[i]) ) ;
            }
            paction.setGameActions(  list );
        }
        return  paction;
    }

    public static String PlayerGameAction2String(PlayerGameAction paction ){
        String str = "";
        if( paction != null ){
            str +=   paction.getPuid() + SEP_USER;
            for(  GameAction gac: paction.getGameActions()){
                str +=  SEP_SENTENCE + GameAction2String(gac);
            }

        }
        str =  str.replaceFirst(SEP_SENTENCE,"");

        return  str;
    }

    public static List<PlayerGameAction> String2PlayerGameActionList(String str ){

        List<PlayerGameAction> list =  new ArrayList<>();
        if( !str.equals("") &&  str != null  ){
            String[] paction_str = str.split(SEP_LIST);
            for(int i = 0 ; i < paction_str.length ; i ++ ){
                list.add(  String2PlayerGameAction( paction_str[i] )  ) ;
            }
        }
        return  list;
    }

    public static  String PlayerGameActionList2String(List<PlayerGameAction> plist ){
        String str = "";
        if( plist != null ){
            for(PlayerGameAction paction : plist ){
                str += SEP_LIST + PlayerGameAction2String(paction);
            }
        }
        str = str.replaceFirst(SEP_LIST,"");
        return  str;
    }



    public static String Action2Record(int uid , GameAction daction ,int targetUid,GameAction saction ){
        String str = "";
        str += uid + SEP_USER;
        str += GameAction2String(daction);
        if(targetUid > 0 && saction != null ){
            str += SEP_LIST  + targetUid + SEP_USER;
            str += GameAction2String(saction);
        }


        return str;
    }





 /* --------------------------------------------------------------------------------------------------------------------
 * 数组计数操作
 * ---------------------------------------------------------------------------------------------------------------------
 */

    public static int ArrayCount ( Integer[] cards , Integer ocard ){
        int count = 0 ;
        for( int i = 0 ; i < cards.length ; i++ ) {
            if (cards[i].equals( ocard ) ) {
                count++;
            }
        }
        return count;
    }

    // 对子个数 4张算2个对子
    public static List<Integer> ArrayCount24(Integer[] cards){
        List<Integer>   list = new ArrayList<>();
        if(cards != null ){
            for( int i = 0 ; i < cards.length ; i++ ) {
                int count = 0 ;
                for( int j = 0 ; j < cards.length ; j++  ){
                    if( cards[i].equals( cards[j] )  ){
                        count ++ ;
                    }
                }
                if(count == 2 && !list.contains(cards[i]) ){
                    list.add(cards[i] );
                }else if(count == 4 && !list.contains(cards[i])  ){
                    list.add(cards[i] );
                    list.add(cards[i] );
                }
            }
        }
        return list;
    }

    // 对子个数 4张不算对子
    public static List<Integer> ArrayCount2(Integer[] cards){
        List<Integer>   list = new ArrayList<>();
        if(cards != null ){
            for( int i = 0 ; i < cards.length ; i++ ) {
                int count = 0 ;
                for( int j = 0 ; j < cards.length ; j++  ){
                    if( cards[i].equals( cards[j] )  ){
                        count ++ ;
                    }
                }
                if(count == 2 && !list.contains(cards[i]) ){
                    list.add(cards[i] );
                }
            }
        }
        return list;
    }

    // 3张数量
    public static List<Integer> ArrayCount3 ( Integer[] cards){
        List<Integer>  list = new ArrayList<>();
        if(cards != null ){
            for( int i = 0 ; i < cards.length ; i++ ) {
                int count = 0 ;
                for( int j = 0 ; j < cards.length ; j++  ){
                    if( cards[i].equals( cards[j] )  ){
                        count ++ ;
                    }
                }
                if(count == 3 && !list.contains(cards[i]) ){
                    list.add(cards[i] );
                }
            }
        }
        return list;
    }

    // 4张数量
    public static List<Integer> ArrayCount4 ( Integer[] cards){
        List<Integer> list = new ArrayList<>();
        if(cards != null ){
            for( int i = 0 ; i < cards.length ; i++ ) {
                int count = 0 ;
                for( int j = 0 ; j < cards.length ; j++  ){
                    if( cards[i].equals( cards[j] )  ){
                        count ++ ;
                    }
                }
                if(count == 4 &&  !list.contains(cards[i])  ){
                    list.add(cards[i] );
                }
            }
        }
        return list;
    }


    // 2张以上牌个数
    public static List<Integer> ArrayCount234 ( Integer[] cards){
        List<Integer>  list = new ArrayList<>();
        if(cards != null ){
            for( int i = 0 ; i < cards.length ; i++ ) {
                int count = 0 ;
                for( int j = 0 ; j < cards.length ; j++  ){
                    if( cards[i].equals( cards[j] )  ){
                        count ++ ;
                    }
                }
                if(count >= 2 && !list.contains(cards[i]) ){
                    list.add(cards[i] );
                }
            }
        }
        return list;
    }

    // 3张以上牌个数
    public static List<Integer> ArrayCount34 ( Integer[] cards){
        List<Integer>  list = new ArrayList<>();
        if(cards != null  ){
            for( int i = 0 ; i < cards.length ; i++ ) {
                int count = 0 ;
                for( int j = 0 ; j < cards.length ; j++  ){
                    if( cards[i].equals( cards[j] )  ){
                        count ++ ;
                    }
                }
                if(count >= 3 && !list.contains(cards[i]) ){
                    list.add(cards[i] );
                }
            }
        }
        return list;
    }




 /* --------------------------------------------------------------------------------------------------------------------
 * 牌型判断
 * ---------------------------------------------------------------------------------------------------------------------
 */

    //有"豆"
    public static boolean hasGang( Integer[] openCards ){
        List<Integer> list4 =  ArrayCount4( openCards );
        if(list4.size() > 0 ){
            return  true;
        }else {
            return  false;
        }
    }

    //相同花色
    public static boolean isSameColor( Integer[] handCards ,Integer[] openCards   ){
        handCards = ArrayAdd(handCards,openCards );
        if( handCards != null ){
            int count = 0 ;
            for(int i=0; i < handCards.length ; i++ ){
               if( handCards[i].intValue()/100  !=  handCards[0].intValue()/100  ){
                   count++;
               }
            }
            if(count == 0){
                return true;
            }
        }
        return false;
    }

    //龙七对
    public static boolean isLong7Dui( Integer[] handCards  ){
        List<Integer> list4 =  ArrayCount4( handCards );
        if( list4.size() > 0 ){
           Integer[] resCards =   ArrayRemoveAll( handCards,list4.get(0));
            List<Integer> list2  =  ArrayCount24(resCards);
            if(list2.size() == 5 ){
                return true;
            }
        }
        return false;
    }

    //大对子
    public static boolean isBigDui( Integer[] handCards ,Integer[] openCards ){

        List<Integer> listh34 =   ArrayCount34( handCards );
        List<Integer> listo34 =   ArrayCount34( openCards );
        List<Integer> listh2 =   ArrayCount2( handCards );
        if( (listh34.size() + listo34.size()) == 4 &&  listh2.size() == 1){
            return true;
        }
        return false;
    }

    //七对
    public static boolean is7Dui( Integer[] handCards ){
        List<Integer> list2 =   ArrayCount24( handCards );
        if( list2.size() == 7 ){
            return true;
        }
        return false;
    }

    //一句牌（顺子）
    public static boolean isSentence( Integer[] handCards  ){
        if( handCards != null && handCards.length > 0){
            while ( handCards.length > 0 ){
                handCards =   ArraySort( handCards );
                if(  ArrayCount(handCards,handCards[0]) == 3 ){
                    handCards= ArrayRemoveAll(handCards,handCards[0] );
                } else if(  Contains( handCards, handCards[0]+1) && Contains( handCards, handCards[0]+2 )  ){
                    Integer temp = handCards[0];
                    handCards = ArrayRemove(handCards,temp );
                    handCards = ArrayRemove(handCards,temp+1 );
                    handCards = ArrayRemove(handCards,temp+2 );
                }else  {
                    return  false;
                }
                if(handCards.length == 0 ){
                    return true;
                }
            }
        }
        return false;
    }

    //平胡
    public static boolean isPinghu ( Integer[] cards ){
        if( cards == null || cards.length == 0 ){
            return false;
        }
        if( (cards.length -2)%3 != 0  ){ // 胡牌时张数 =  3N+2
            return false;
        }

       List<Integer> dList  =  ArrayCount234( cards );// 大于2张以上的牌
        if( dList.size() > 0 ){
            for(  Integer oCard    :dList){
               Integer[] rescards  = ArrayRemove( ArrayRemove(cards,oCard ), oCard ) ;
               if( isSentence( rescards) == true ){
                 return  true;
               }
            }
        }
        return false;
    }

    //软听判断
    public static GameAction judgeTING( Integer[] handCards,Integer[] openCards,Integer mcard,ActionType actionType ){
        GameAction action = null;
        handCards = ArrayAdd( handCards,mcard );
        for(int k=0; k < handCards.length; k++  ){
            Integer outCard = handCards[k];//要打出的牌
            Integer[] resCards = ArrayRemove(handCards,outCard  );
            Integer[] cards = SingleCardsPool();
            for(int i = 0 ; i < cards.length ; i ++ ){
                GameAction actionh = judgeHU( resCards ,openCards, cards[i] , actionType );
                if( actionh != null ){//可以听
                    action = tingPaiAction(outCard);
                }
            }
        }

        return action;
    }

    //硬听判断
    public static GameAction judgeYTING( Integer[] handCards,Integer[] openCards,ActionType actionType ){
        GameAction action = null;
        Integer[] cards = SingleCardsPool();
        for(int i = 0 ; i < cards.length ; i ++ ){
            GameAction actionh = judgeHU( handCards ,openCards, cards[i] , actionType );
            if( actionh != null ){//可以听
                action = tingPaiAction();//硬听 不需要打出牌
            }
        }

        return action;
    }





    //是否可胡牌判断
    public static GameAction judgeCanHU( Integer[] handCards ,Integer[] openCards,Integer outCard ,ActionType actionType ){
        GameAction  huaction =   judgeHU(handCards,openCards,outCard,actionType );
        if(huaction != null ){
            huaction = new GameAction();
            huaction.setAction( Action.CAN_HU);
            huaction.setActionType(actionType);
            List<Integer[]> list = new ArrayList<>();
            Integer[] cCards = new Integer[1];
            cCards[0] = outCard;
            list.add(cCards);
            huaction.setCards(list);
        }

        return  huaction;
    }


    //胡牌判断
    public static GameAction judgeHU( Integer[] handCards ,Integer[] openCards,Integer outCard ,ActionType actionType ){
        GameAction action = null ;
        handCards = ArrayAdd(handCards, outCard );

        if( is7Dui( handCards ) ){
            if( isLong7Dui( handCards )){
                if( isSameColor( handCards,openCards )){
                    action = new GameAction();
                    action.setAction( Action.QINGLONGDUI_HU);
                    action.setActionType(actionType);
                    List<Integer[]> list = new ArrayList<>();
                    Integer[] cCards = new Integer[1];
                    cCards[0] = outCard;
                    list.add(cCards);
                    action.setCards(list);
                    return action;
                }else {
                    action = new GameAction();
                    action.setAction( Action.LONGQIDUI_HU);
                    action.setActionType(actionType);
                    List<Integer[]> list = new ArrayList<>();
                    Integer[] cCards = new Integer[1];
                    cCards[0] = outCard;
                    list.add(cCards);
                    action.setCards(list);
                    return action;
                }
            }else {
                action = new GameAction();
                action.setAction( Action.QIDUI_HU);
                action.setActionType(actionType);
                List<Integer[]> list = new ArrayList<>();
                Integer[] cCards = new Integer[1];
                cCards[0] = outCard;
                list.add(cCards);
                action.setCards(list);
                return action;
            }
        }else {

            if( isBigDui( handCards,openCards )){
                if(isSameColor( handCards,openCards )){
                    action = new GameAction();
                    action.setAction( Action.QINGDADUI_HU);
                    action.setActionType(actionType);
                    List<Integer[]> list = new ArrayList<>();
                    Integer[] cCards = new Integer[1];
                    cCards[0] = outCard;
                    list.add(cCards);
                    action.setCards(list);
                    return action;
                }else {
                    action = new GameAction();
                    action.setAction( Action.DADUI_HU);
                    action.setActionType(actionType);
                    List<Integer[]> list = new ArrayList<>();
                    Integer[] cCards = new Integer[1];
                    cCards[0] = outCard;
                    list.add(cCards);
                    action.setCards(list);

                    return action;
                }
            }else{
                if( isPinghu(handCards) ){
                    if(isSameColor(handCards,openCards)){
                        action = new GameAction();
                        action.setAction( Action.QINGYISE_HU);
                        action.setActionType(actionType);
                        List<Integer[]> list = new ArrayList<>();
                        Integer[] cCards = new Integer[1];
                        cCards[0] = outCard;
                        list.add(cCards);
                        action.setCards(list);
                        return action;
                    }else {
                        if(HASDOU_FLG && ! hasGang( openCards ) && actionType == null ){ //非自摸有豆才可以平胡
                            return action;
                        }

                        action = new GameAction();
                        action.setAction( Action.PING_HU);
                        action.setActionType(actionType);
                        List<Integer[]> list = new ArrayList<>();
                        Integer[] cCards = new Integer[1];
                        cCards[0] = outCard;
                        list.add(cCards);
                        action.setCards(list);
                        return action;
                    }
                }
            }
        }

        return action;
    }
    //吃牌判断
    public static GameAction judgeCHI(Integer[] handCards, Integer outCard  ){
    	//风和字不能吃
    	if(outCard < 100) {
    		return null;
    	}
        GameAction naction =  new GameAction();
        naction.setAction( Action.CHI);
        List<Integer[]> list = new ArrayList<>();
        if( Contains(handCards,outCard+1) &&  Contains(handCards,outCard+2) ){
            Integer[] cards = new Integer[3];
            cards[0]=outCard;
            cards[1]=outCard+1;
            cards[2]=outCard+2;
            list.add( cards );
        }else if(  Contains(handCards,outCard-1) &&  Contains(handCards,outCard+1) ){
            Integer[] cards = new Integer[3];
            cards[0]=outCard-1;
            cards[1]=outCard;
            cards[2]=outCard+1;
            list.add( cards );
        }else if(  Contains(handCards,outCard-2) &&  Contains(handCards,outCard-1) ){
            Integer[] cards = new Integer[3];
            cards[0]=outCard;
            cards[1]=outCard-1;
            cards[2]=outCard-2;
            list.add( cards );
        }
        naction.setCards( list );
        if(list.size() == 0 ){
            naction = null;
        }
        return  naction;
    }

    //碰牌判断
    public static GameAction judgePENG(Integer[] handCards, Integer outCard  ){
        GameAction naction =  null;
        if( ArrayCount(handCards ,outCard ) == 2 || ArrayCount(handCards ,outCard ) == 3 ){
            naction = new GameAction();
            naction.setAction( Action.PENG);
            Integer[] cards = new Integer[3];
            cards[0]=outCard;
            cards[1]=outCard;
            cards[2]=outCard;
            List<Integer[]> list = new ArrayList<>();
            list.add( cards );
            naction.setCards( list );
        }
        return naction;
    }
    //打牌后杠牌判断 [杠牌]
    public static GameAction  judgeGANG(Integer[] handCards, Integer outCard  ){
        GameAction naction =  null;
        if( ArrayCount(handCards ,outCard ) == 3 ){
            naction = new GameAction();
            naction.setAction( Action.GANG);
            Integer[] cards = new Integer[4];
            cards[0]=outCard;
            cards[1]=outCard;
            cards[2]=outCard;
            cards[3]=outCard;
            List<Integer[]> list = new ArrayList<>();
            list.add( cards );
            naction.setCards( list );
        }
        return naction;
    }

    //摸牌后杠牌判断 [点杠、暗杠]
    public static GameAction  judgeMOGANG(Integer[] handCards,List<GameAction> openCards, Integer mCard  ){
        GameAction naction =  null;
        handCards = ArrayAdd(handCards, mCard );
        List<Integer> list4  =  ArrayCount4(handCards);

      if( list4.size() > 0 ){ //暗杠
            for(  Integer card :  list4 ){
                naction = new GameAction();
                naction.setAction( Action.ANGANG);
                Integer[] cards = new Integer[4];
                cards[0]=card;
                cards[1]=card;
                cards[2]=card;
                cards[3]=card;
                List<Integer[]> list = new ArrayList<>();
                list.add( cards );
                naction.setCards( list );
            }
        }
        if(openCards != null ){ //点杠
            for( GameAction action : openCards ){
                if( action.getAction() == Action.PENG  &&  action.getCards().get(0)[0].equals( mCard )  ){
                    naction = new GameAction();
                    naction.setAction( Action.DIANGANG);
                    Integer[] cards = new Integer[4];
                    cards[0]=mCard;
                    cards[1]=mCard;
                    cards[2]=mCard;
                    cards[3]=mCard;
                    List<Integer[]> list = new ArrayList<>();
                    list.add( cards );
                    naction.setCards( list );
                }
            }
        }

        return naction;
    }






 /* --------------------------------------------------------------------------------------------------------------------
 * 基础行为 吃 碰 杠 摸 打 过
 * ---------------------------------------------------------------------------------------------------------------------
 */

    //摸牌行为
    public static GameAction moPaiAction( Integer[] card ){
        GameAction maction = new GameAction();
        maction.setAction( Action.MO);
        List<Integer[]> list = new ArrayList<>();
        list.add(card );
        maction.setCards( list);
        return  maction;
    }

    //打牌行为
    public static GameAction daPaiAction( Integer card ){
        GameAction maction = new GameAction();
        maction.setAction( Action.DA);
        List<Integer[]> list = new ArrayList<>();
        Integer[] cards = new Integer[1];
        cards[0] = card;
        list.add(cards );
        maction.setCards( list);
        return  maction;
    }

    //软听行为
    public static GameAction tingPaiAction( Integer outCard ){
        GameAction maction = new GameAction();
        maction.setAction( Action.TING);
        List<Integer[]> list = new ArrayList<>();
        Integer[] ocards = new Integer[1];
        ocards[0] = outCard;
        list.add( ocards );
        maction.setCards(list);
        return  maction;
    }
    //硬听行为
    public static GameAction tingPaiAction( ){
        GameAction maction = new GameAction();
        maction.setAction( Action.TING);
        return  maction;
    }
    //过牌行为
    public static GameAction guoPaiAction(){
        GameAction maction = new GameAction();
        maction.setAction( Action.GUO);
        return  maction;
    }
    //黄庄行为
    public static GameAction huangZhuangAction(){
        GameAction maction = new GameAction();
        maction.setAction( Action.HUANG_ZHUANG);
        return  maction;
    }


    //碰牌行为
    public static GameAction pengPaiAction( Integer card ){
        GameAction maction = new GameAction();
        maction.setAction( Action.PENG);
        List<Integer[]> list = new ArrayList<>();
        Integer[] cards = new Integer[3];
        cards[0] = card;
        cards[1] = card;
        cards[2] = card;
        list.add( cards );
        maction.setCards( list);
        return  maction;
    }


    //胡牌行为
    public static GameAction huPaiAction( Action act,ActionType type, Integer card ){
        GameAction maction = new GameAction();
        maction.setAction( act );
        if(type != null ){
            maction.setActionType(type);
        }
        List<Integer[]> list = new ArrayList<>();
        Integer[] cards = new Integer[1];
        cards[0] = card;
        list.add( cards );
        maction.setCards( list);
        return  maction;
    }

    //杠牌行为
    public static GameAction gangPaiAction( Action act,ActionType type, Integer card ){
        GameAction maction = new GameAction();
        maction.setAction( act );
        if(type != null ){
            maction.setActionType(type);
        }

        List<Integer[]> list = new ArrayList<>();
        Integer[] cards = new Integer[4];
        cards[0] = card;
        cards[1] = card;
        cards[2] = card;
        cards[3] = card;
        list.add( cards );
        maction.setCards( list);
        return  maction;
    }

    //吃牌行为
    public static GameAction chiPaiAction( Action act, Integer[] cards ){
        GameAction maction = new GameAction();
        maction.setAction( act );

        List<Integer[]> list = new ArrayList<>();
        list.add( cards );
        maction.setCards( list);
        return  maction;
    }








    //下一家座位号
    public  static int nextPlayerSeatNo( int seatNo ){
        if( seatNo == 4){
            return 1;
        }else {
            return  seatNo + 1 ;
        }
    }

    //胡牌时根据手牌判断是否是自摸
    public static boolean isZiMo( Integer[] handCards ){
        int res = (handCards.length -2) % 3 ;
        if (res == 0 ){
            return  true;
        }else {
            return false;
        }
    }

    public static PlayerGameAction toPlayGameAction(int pid , List<GameAction> list ){

        PlayerGameAction pa = new PlayerGameAction();
        pa.setPuid(pid);
        pa.setGameActions(list );
        return pa;
    }










































}
