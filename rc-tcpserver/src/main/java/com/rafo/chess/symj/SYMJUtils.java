package com.rafo.chess.symj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.rafo.chess.common.Avatas;
import com.rafo.chess.model.Action;
import com.rafo.chess.model.ActionType;
import com.rafo.chess.model.GameAction;

/**
 * @author KZC
 * 沈阳麻将规则类
 *
 */
public class SYMJUtils {

	
    /**
     * 飘胡/大对子
     * @param cards
     * @return
     */
    public static boolean isDaDuiZi(Integer[] cards){
        List<Integer> list3 = Avatas.ArrayCount3(cards);
        List<Integer> list2 = Avatas.ArrayCount2(cards);
        if( list3.size() == 4 &&  list2.size() == 1){
            return true;
        }
        return false;
    }
    
    /**
     * 三门齐
     * @param cards
     * @return
     */
    public static boolean isAll3Have(Integer[] cards) {
    	if(cards == null || cards.length <= 0) {
    		return false;
    	}
    	List<Integer> types = new ArrayList<Integer>();
    	for(int cardId : cards) {
    		//风字过掉
    		if(cardId < 100) {
    			continue;
    		}
    		int type = cardId/100;
    		if(types.contains(type)) {
    			continue;
    		}
    		types.add(type);
    		if(types.size() >= 3) {
    			return true;
    		}
    	}
    	return false;
    }
    
    
    /**
     * 有无19或风字
     * @param cards
     * @return
     */
    public static boolean has19OrZi(Integer[] cards) {
    	if(cards == null || cards.length <= 0) {
    		return false;
    	}
    	for(int cardId : cards) {
    		if(cardId < 100 || cardId % 100 == 1 || cardId% 100 == 9) {
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * 判断胡牌
     * @param handCards
     * @param openCards
     * @param outCard
     * @param actionType
     * @return
     */
    public static GameAction judgeHU(Integer[] handCards, List<GameAction> openCards, Integer outCard, ActionType actionType){
        GameAction action = null ;
        List<Integer[]> filterCards = filterOpenCards(openCards);
        //所有的牌
        Integer[] allCard = new Integer[0];
        if(!filterCards.isEmpty()) {
        	for(Integer[] actionCards : filterCards) {
        		allCard = Avatas.ArrayAdd(allCard, actionCards);
        	}
        }
        //手里的牌
        handCards = Avatas.ArrayAdd(handCards, outCard);
        allCard = Avatas.ArrayAdd(allCard, handCards);
        //三门齐
        if(!isAll3Have(allCard)) {
        	return action;
        }
        //19或风
        if(!has19OrZi(allCard)) {
        	return action;
        }
        //大对子
        if(isDaDuiZi(allCard)) {
        	action = new GameAction();
            action.setAction(Action.DADUI_HU);
            action.setActionType(actionType);
            List<Integer[]> list = new ArrayList<>();
            list.add(handCards);
            action.setCards(list);
            return action;
        } else {
        	boolean countHeng = true;
			// 判断有没有横
			for (GameAction ga : openCards) {
				if (ga.getAction() == Action.PENG || ga.getAction() == Action.GANG || ga.getAction() == Action.DIANGANG
						 || ga.getAction() == Action.ANGANG) {
					countHeng = false;
				}
			}
			if(isPinghu(handCards, countHeng)) {
				action = new GameAction();
                action.setAction( Action.PING_HU);
                action.setActionType(actionType);
                List<Integer[]> list = new ArrayList<>();
                list.add(handCards);
                action.setCards(list);
                return action;
			}
        }
        return action;
    }

	
	/**
	 * @param cards
	 * @param countHeng用不用算横
	 * @return
	 */
	public static boolean isSentence(Integer[] cards, boolean countHeng) {
		int hengCount = 0;
		if (cards != null && cards.length > 0) {
			while (cards.length > 0) {
				cards = Avatas.ArraySort(cards);
				if (Avatas.Contains(cards, cards[0] + 1) && Avatas.Contains(cards, cards[0] + 2)) {
					Integer temp = cards[0];
					cards = Avatas.ArrayRemove(cards, temp);
					cards = Avatas.ArrayRemove(cards, temp + 1);
					cards = Avatas.ArrayRemove(cards, temp + 2);
				} else if (Avatas.ArrayCount(cards, cards[0]) == 3) {
					cards = Avatas.ArrayRemoveAll(cards, cards[0]);
					hengCount++;
				} else {
					return false;
				}
				if (cards.length == 0) {
					if(countHeng && hengCount <= 0) {
						return false;
					} else {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 平胡
	 * @param cards
	 * @return
	 */
	public static boolean isPinghu(Integer[] cards, boolean countHeng) {
		if (cards == null || cards.length == 0) {
			return false;
		}
		if ((cards.length - 2) % 3 != 0) { // 胡牌时手牌张数 = 3N+2
			return false;
		}

		List<Integer> dList = Avatas.ArrayCount234(cards);// 大于2张以上的牌
		if (dList.size() > 0) {
			for (Integer oCard : dList) {
				Integer[] rescards = Avatas.ArrayRemove(Avatas.ArrayRemove(cards, oCard), oCard);
				if (isSentence(rescards, countHeng) == true) {
					return true;
				}
			}
		}
		return false;
	}

    //听牌判断
    public static GameAction judgeTING( Integer[] handCards ){
        GameAction action = null;
        return action;
    }


    //胡牌判断
    public static GameAction judgeHU( Integer[] handCards ,List<GameAction> openCards, ActionType actionType ){
    	 GameAction action = null ;
         List<Integer[]> filterCards = filterOpenCards(openCards);
         //所有的牌
         Integer[] allCard = new Integer[0];
         if(!filterCards.isEmpty()) {
         	for(Integer[] actionCards : filterCards) {
         		allCard = Avatas.ArrayAdd(allCard, actionCards);
         	}
         }
         //手里的牌
         allCard = Avatas.ArrayAdd(allCard, handCards);
         //三门齐
         if(!isAll3Have(allCard)) {
         	return action;
         }
         //19或风
         if(!has19OrZi(allCard)) {
         	return action;
         }
         //大对子
         if(isDaDuiZi(allCard)) {
         	action = new GameAction();
             action.setAction(Action.DADUI_HU);
             action.setActionType(actionType);
             List<Integer[]> list = new ArrayList<>();
             list.add(handCards);
             action.setCards(list);
             return action;
         } else {
         	boolean countHeng = true;
 			// 判断有没有横
 			for (GameAction ga : openCards) {
 				if (ga.getAction() == Action.PENG || ga.getAction() == Action.GANG || ga.getAction() == Action.DIANGANG
 						 || ga.getAction() == Action.ANGANG) {
 					countHeng = false;
 				}
 			}
 			if(isPinghu(handCards, countHeng)) {
 				action = new GameAction();
                 action.setAction( Action.PING_HU);
                 action.setActionType(actionType);
                 List<Integer[]> list = new ArrayList<>();
                 list.add(handCards);
                 action.setCards(list);
                 return action;
 			}
         }
         return action;
    }

	/**
	 * @param actions
	 * @return
	 */
	public static List<Integer[]> filterOpenCards(List<GameAction> actions) {
		List<Integer[]> openCards = new ArrayList<>();
		if (actions != null && !actions.isEmpty()) {
			for (GameAction ga : actions) {
				Action action = ga.getAction();
				if (action == Action.CHI || action == Action.PENG || action == Action.GANG || action == Action.DIANGANG
						|| action == Action.ANGANG ) {
					openCards.add(ga.getCards().get(0));
				}
			}
		}
		return openCards;
	}
	
	 /**
     * 计算旋风杠
     * @param cards
     * @return
     */
    public static List<Integer[]> getXuanfengGang(Integer[] cards) {
    	List<Integer> dxnb = Arrays.asList(10,11,12,13);
    	List<Integer> zfb = Arrays.asList(14,15,16);
    	List <Integer> handCards = Avatas.Array2List(cards);
    	List<Integer[]> xfg = new ArrayList<>();
    	if(handCards.containsAll(dxnb)) {
    		xfg.add(Avatas.List2Array(dxnb));
    	}
    	if(handCards.containsAll(zfb)) {
    		xfg.add(Avatas.List2Array(zfb));
    	}
    	return xfg;
    }
    
    /**
     * 判断是不是旋风杠
     * @param handCards
     * @param mycard
     * @return
     */
    public static GameAction judgeXuanFengGang(Integer[] handCards, int myCard) {
    	 List<Integer[]> xfg = getXuanfengGang(handCards);
    	 GameAction action =  null;
    	 if(xfg.size() > 0) {
    		 action = new GameAction();
    		 //action.setAction(Action.XUANFENGGANG);
    		 List<Integer[]> list = new ArrayList<>();
    		 for(Integer[] xfgCards : xfg) {
    			 list.add(xfgCards);
    		 }
    		 action.setCards(list);
    	 }
    	 return action;
    }
}
