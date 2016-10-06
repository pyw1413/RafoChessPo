package com.kodgames.battle.service;

import com.kodgames.battle.entity.battle.BWBattleStartRES;
import com.kodgames.battle.entity.battle.BWBattleStepRES;
import com.kodgames.battle.entity.battle.WBBattleStartREQ;
import com.kodgames.battle.entity.battle.WBBattleStepREQ;
import com.kodgames.battle.service.battle.MBattleStep;
import com.kodgames.battle.service.battle.MajhongHelper;
import com.rafo.chess.logic.gymj.GYMJDealer;
import com.rafo.chess.logic.gymj.GYMJGame;
import com.rafo.chess.model.GamePlayer;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2016/9/18.
 */
public class BattleStartTest extends BattleBaseServiceTest {

    //发牌
    @Test
    public void testDeal(){
        WBBattleStartREQ message = new WBBattleStartREQ();
        message.setRoomId(roomID);
        message.setAccountId("A");
        Map<String, BWBattleStepRES> rA = battleService.battleStart(message);

        message = new WBBattleStartREQ();
        message.setRoomId(roomID);
        message.setAccountId("B");
        Map<String, BWBattleStepRES> rB = battleService.battleStart(message);

        message = new WBBattleStartREQ();
        message.setRoomId(roomID);
        message.setAccountId("C");
        Map<String, BWBattleStepRES> rC = battleService.battleStart(message);

        BWBattleStartRES aa = battleService.getBattleStatus("C");

        message = new WBBattleStartREQ();
        message.setRoomId(roomID);
        message.setAccountId("D");
        Map<String, BWBattleStepRES> rD = battleService.battleStart(message);

        aa = battleService.getBattleStatus("D");
        System.out.println("----------------");
//        roomService.getRoomStates();
    }

    @Test
    public void testDiscard(){

        System.out.println(MBattleStep.PlayType.CanKong);
        testDeal();
        Integer[] aCards = {12, 14, 15, 18, 21, 14, 23, 24, 29, 35, 36, 37, 38 ,39};
        Integer[] bCards = {11, 11, 11, 24, 26, 27, 27, 32, 32, 33, 35, 35, 38 };
        Integer[] cCards = {10, 12, 12, 12, 20, 21, 21, 18, 19, 20, 23, 34, 35 };
        Integer[] dCards = {13, 17, 16, 18, 18, 22, 22, 25, 28, 28, 33, 33, 37 };
        recreateCards(aCards, bCards, cCards, dCards);
        //A 打 14 ==》 B可以碰也可以杠
        WBBattleStepREQ message = new WBBattleStepREQ();
        message.setRoomId(roomID);
        message.setAccountId("A");
        message.setCard(12);
        message.setPlayType(MBattleStep.PlayType.Discard);
        Map<String, BWBattleStepRES> results = battleService.battleStep(message);

        //B 杠牌 => 杠出 并摸牌 11
        //{11, 11, 17, 19, 22, 23, 25, 26, 28, 31, 35 };
        message = new WBBattleStepREQ();
        message.setRoomId(roomID);
        message.setAccountId("B");
        message.setCard(12);
        message.setPlayType(MBattleStep.PlayType.DotKong);
//        message.setPlayType(MBattleStep.PlayType.Pong);
        results = battleService.battleStep(message);

        //B 打冲锋鸡
        message = new WBBattleStepREQ();
        message.setRoomId(roomID);
        message.setAccountId("B");
        message.setCard(12);
        message.setPlayType(MBattleStep.PlayType.Discard);
        results = battleService.battleStep(message);

        //A碰 责任鸡
        message = new WBBattleStepREQ();
        message.setRoomId(roomID);
        message.setAccountId("A");
        message.setCard(21);
        message.setPlayType(MBattleStep.PlayType.Pass);
        results = battleService.battleStep(message);

        //C 出33 B可以胡牌，D可以碰牌
        message = new WBBattleStepREQ();
        message.setRoomId(roomID);
        message.setAccountId("C");
        message.setCard(33);
        message.setPlayType(MBattleStep.PlayType.Discard);
        results = battleService.battleStep(message);

        //B 过胡
        message = new WBBattleStepREQ();
        message.setRoomId(roomID);
        message.setAccountId("B");
        message.setCard(33);
        message.setPlayType(MBattleStep.PlayType.Pass);
        results = battleService.battleStep(message);

        System.out.println();


        //D 碰牌

        //B 胡牌

        //断线重连

    }


    @Test
    public void testTwoEngine(){
        testDeal();
//        recreateCards();

        System.out.println("==================1==============");
        //A 打 14 ==》 B可以碰也可以杠
/*        WBBattleStepREQ message = new WBBattleStepREQ();
        message.setRoomId(roomID);
        message.setAccountId("A");
        message.setCard(21);
        message.setPlayType(MBattleStep.PlayType.Discard);

        Map<String, BWBattleStepRES> results = battleService.battleStep(message);

        gameService.play(101, MBattleStep.PlayType.Discard, 21);*/

        //TEST

        //碰 pass
        //过 pass
        //冲锋鸡 pass
        //责任鸡

//        play("A", 101, MBattleStep.PlayType.Discard, 21);
//        play("C", 103, MBattleStep.PlayType.Pong, 21);

        //1. 碰 pass
/*        play("A", 101, MBattleStep.PlayType.Discard, 35);
        play("B", 102, MBattleStep.PlayType.Pong, 35);*/

        //2. 过牌 PASS
/*        play("A", 101, MBattleStep.PlayType.Discard, 35);
        play("B", 102, MBattleStep.PlayType.Pass, 35);*/

        //3. 冲锋鸡 PASS
//        play("A", 101, MBattleStep.PlayType.Discard, 21);

        //4. 过牌冲锋鸡 current player 还有问题
/*        play("A", 101, MBattleStep.PlayType.Discard, 21);
        play("C", 103, MBattleStep.PlayType.Pass, 21);*/

        //5. 责任鸡 PASS
/*        play("A", 101, MBattleStep.PlayType.Discard, 21);
        play("C", 103, MBattleStep.PlayType.Pong, 21);*/

        //6. 明杠 （点豆）
/*        play("A", 101, MBattleStep.PlayType.Discard, 12);
        play("C", 103, MBattleStep.PlayType.DotKong, 12);*/

        //7. 过杠 PASS
/*        play("A", 101, MBattleStep.PlayType.Discard, 12);
        play("C", 103, MBattleStep.PlayType.Pass, 12);*/

        //8. 暗杠 有待看客户端实现
/*        play("A", 101, MBattleStep.PlayType.Discard, 39);
        play("B", 102, MBattleStep.PlayType.CealedKong, 11);*/

        //9. 自摸 听 胡 未通过
        /**
         * Integer[] aCards = {12, 14, 15, 18, 21, 14, 23, 24, 29, 35, 36, 37, 38 ,39};
         Integer[] bCards = {11, 11, 24, 25, 26, 27, 27, 32, 32, 32, 35, 36, 37 };
         Integer[] cCards = {10, 12, 12, 12, 20, 21, 21, 18, 19, 20, 23, 34, 35 };
         Integer[] dCards = {13, 17, 16, 18, 18, 22, 22, 25, 28, 28, 33, 33, 37 };
         */
//        play("A", 101, MBattleStep.PlayType.Discard, 39);

    }

    //自摸测试
    @Test
    public void zimoTest(){
        testDeal();
        Integer[] aCards = {12, 14, 15, 18, 21, 14, 23, 24, 29, 35, 36, 37, 38 ,39};
        Integer[] bCards = {11, 11, 14, 15, 16, 27, 27, 32, 32, 32, 35, 36, 37 };
        Integer[] cCards = {11, 12, 12, 12, 17, 18, 19, 21, 21, 23, 34, 35 };
        Integer[] dCards = {13, 17, 16, 18, 18, 22, 22, 25, 28, 28, 33, 33, 37 };

        recreateCards(aCards, bCards, cCards, dCards);

        play("A", 201, MBattleStep.PlayType.Discard, 39);
        play("B", 202, MBattleStep.PlayType.Hu, 11);
        System.out.println("======zimo=====");
    }


    @Test
    public void dianPaoTest(){
        testDeal();

        Integer[] aCards = {12, 14, 15, 18, 21, 14, 23, 24, 29, 35, 36, 37, 38 ,39};
        Integer[] bCards = {11, 15, 24, 25, 26, 27, 27, 32, 32, 32, 35, 36, 37 };
        Integer[] cCards = {11, 12, 12, 12, 19, 15, 16, 17, 22, 22, 33, 34, 35 };
        Integer[] dCards = {13, 17, 16, 18, 18, 22, 21, 25, 28, 28, 33, 33, 37 };

        recreateCards(aCards, bCards, cCards, dCards);

        //10. 打一轮，点炮
        play("A", 201, MBattleStep.PlayType.Discard, 12);
        play("C", 203, MBattleStep.PlayType.DotKong, 12);
        play("C", 203, MBattleStep.PlayType.Discard, 19);
        play("D", 204, MBattleStep.PlayType.Discard, 22);
        play("C", 203, MBattleStep.PlayType.Hu, 22);
    }


    @Test
    public void dianGang(){
        testDeal();

        Integer[] aCards = {12, 11, 15, 18, 21, 14, 23, 24, 29, 35, 36, 37, 38 ,39};
        Integer[] bCards = {11, 15, 24, 25, 26, 27, 27, 32, 32, 32, 35, 36, 37 };
        Integer[] cCards = {11, 12, 12, 27, 19, 15, 16, 17, 22, 22, 33, 34, 35 };
        Integer[] dCards = {11, 17, 16, 18, 18, 22, 21, 25, 28, 28, 33, 33, 37 };

        recreateCards(aCards, bCards, cCards, dCards);

        //转弯豆
        play("A", 201, MBattleStep.PlayType.Discard, 12);
        play("C", 203, MBattleStep.PlayType.Pong, 12);
        play("C", 203, MBattleStep.PlayType.Discard, 27);
        play("B", 202, MBattleStep.PlayType.Pong, 27);
        play("B", 202, MBattleStep.PlayType.Discard, 37);
//        play("C", 203, MBattleStep.PlayType.Discard, 12);
    }
    //TODO: 有人可碰，有人可以胡

    //TODO: 如果有责任鸡，又摸一张鸡


    public void play(String uid, int playerId, int type, int card){
        System.out.println("================================");
        WBBattleStepREQ message = new WBBattleStepREQ();
        message.setRoomId(roomID);
        message.setAccountId(uid);
        message.setCard(card);
        message.setPlayType(type);

        battleService.battleStep(message);
        gameService.play(playerId, type, card);
    }

    //自定义牌局
    private void recreateCards(Integer[] aCards, Integer[] bCards, Integer[] cCards, Integer[] dCards){
        List<Integer> cardsPool = new ArrayList<>();
/*        Integer[] aCards = {12, 14, 15, 18, 21, 14, 23, 24, 29, 35, 36, 37, 38 ,39};
        Integer[] bCards = {11, 11, 11, 24, 26, 27, 27, 32, 32, 33, 35, 35, 38 };
        Integer[] cCards = {10, 12, 12, 12, 20, 21, 21, 18, 19, 20, 23, 34, 35 };
        Integer[] dCards = {13, 17, 16, 18, 18, 22, 22, 25, 28, 28, 33, 33, 37 };*/

        //测自摸胡
/*        Integer[] aCards = {12, 14, 15, 18, 21, 14, 23, 24, 29, 35, 36, 37, 38 ,39};
        Integer[] bCards = {11, 11, 24, 25, 26, 27, 27, 32, 32, 32, 35, 36, 37 };
        Integer[] cCards = {10, 12, 12, 12, 20, 21, 21, 18, 19, 20, 23, 34, 35 };
        Integer[] dCards = {13, 17, 16, 18, 18, 22, 22, 25, 28, 28, 33, 33, 37 };*/

/*        Integer[] aCards = {12, 14, 15, 18, 21, 14, 23, 24, 29, 35, 36, 37, 38 ,39};
        Integer[] bCards = {11, 15, 24, 25, 26, 27, 27, 32, 32, 32, 35, 36, 37 };
        Integer[] cCards = {11, 12, 12, 12, 20, 21, 21, 18, 19, 20, 23, 34, 35 };
        Integer[] dCards = {13, 17, 16, 18, 18, 22, 22, 25, 28, 28, 33, 33, 37 };*/

        Map<Integer, Integer> dealCards = new HashMap<>();
        countDealCards(aCards, dealCards);
        countDealCards(bCards, dealCards);
        countDealCards(cCards, dealCards);
        countDealCards(dCards, dealCards);

        recreateCardPool(cardsPool, dealCards, MajhongHelper.CardType.Wan);
        recreateCardPool(cardsPool, dealCards, MajhongHelper.CardType.Tiao);
        recreateCardPool(cardsPool, dealCards, MajhongHelper.CardType.Tong);

        battleService.getBattleHelper().getPlayCards().put("A", asList(aCards));
        battleService.getBattleHelper().getPlayCards().put("B", asList(bCards));
        battleService.getBattleHelper().getPlayCards().put("C", asList(cCards));
        battleService.getBattleHelper().getPlayCards().put("D", asList(dCards));

        battleService.getBattleHelper().getCardPools().clear();
        battleService.getBattleHelper().getCardPools().addAll(cardsPool);

        battleService.getBattleHelper().getPlayerDealCards().put("A", countCards(aCards));
        battleService.getBattleHelper().getPlayerDealCards().put("B", countCards(bCards));
        battleService.getBattleHelper().getPlayerDealCards().put("C", countCards(cCards));
        battleService.getBattleHelper().getPlayerDealCards().put("D", countCards(dCards));


        //转换成新的牌
        GYMJDealer gyMJDealer = (GYMJDealer)gameService.getGyMJGame().getGdealer();
        Integer[] newPool = new Integer[cardsPool.size()];
        for(int i=0;i<cardsPool.size();i++){
            newPool[i] = gameService.oldCardToNewCard(cardsPool.get(i));
        }
        gyMJDealer.setCardsPool(newPool);

        Map<Integer, Integer[]> userCards = new HashMap<>();
        userCards.put(201, aCards);
        userCards.put(202, bCards);
        userCards.put(203, cCards);
        userCards.put(204, dCards);

        for(GamePlayer player : gyMJDealer.getPlayers()){
            player.setHandCards(toNewCard(userCards.get(player.getUid())));
        }
    }

    private Integer[] toNewCard(Integer[] cards){
        Integer[] newCards = new Integer[cards.length];
        for(int i=0;i<cards.length;i++){
            newCards[i] = gameService.oldCardToNewCard(cards[i]);
        }
        return newCards;
    }

    private ArrayList<Integer> asList(Integer[] cards){
        ArrayList<Integer> data = new ArrayList<>();
        for(Integer card: cards){
            data.add(card);
        }
        return data;
    }

    private void countDealCards(Integer[] cards, Map<Integer, Integer> dealCards){
        for(int i=0;i<cards.length;i++){
            Integer count = dealCards.get(cards[i]);
            if(count == null){
                dealCards.put(cards[i], 1);
            }else{
                dealCards.put(cards[i], ++count);
            }
        }
    }

    private ConcurrentHashMap<Integer, Integer> countCards(Integer[] cards){
        ConcurrentHashMap<Integer, Integer> count = new ConcurrentHashMap<>();
        for(Integer c : cards){
            if(count.containsKey(c)){
                count.put(c, count.get(c)+1);
            }else{
                count.put(c, 1);
            }
        }
        return count;
    }

    private void recreateCardPool(List<Integer> cardsPool, Map<Integer, Integer> dealCards, MajhongHelper.CardType cardType ){
        for(int i = cardType.Value(); i < cardType.Value() + MajhongHelper.ONE_TYPE_MAX; i++){
            Integer count = dealCards.get(i);
            int size = 4;
            if(count != null){
                size = 4 - count;
            }

            for(int j=0;j<size;j++){
                cardsPool.add(i);
            }
        }
    }


}
