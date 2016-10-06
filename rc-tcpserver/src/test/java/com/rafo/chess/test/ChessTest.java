package com.rafo.chess.test;
import static com.rafo.chess.common.Atakes.*;
import static com.rafo.chess.common.Avatas.*;
import static com.rafo.chess.model.AbstractGameFactory.gameInstance;

import java.util.ArrayList;
import java.util.List;

import com.rafo.chess.manager.RedisManager;
import com.rafo.chess.model.*;

/**
 * Created by Administrator on 2016/9/21.
 */
public class ChessTest {

    public static void main(String[] args) {

        RedisManager.getInstance().init("192.168.1.106");

        String password = "1234";
        GameRoomSettings gsetting = new GameRoomSettings();
        gsetting.setPassword(Integer.parseInt(password));
        gsetting.setGameType(0);
        gsetting.setPlayType(0);
        gsetting.setPlayTypeExt1("1,3,4");
        gsetting.setPlayTypeExt2("2,6,8,9,12");
        gsetting.setRoomSize(4);
        gsetting.setTotalRounds(8);
        gsetting.setCurrRounds(1);

        List<GamePlayer> plist = new ArrayList<>();
        for (int i = 0; i < gsetting.getRoomSize(); i++) {
            GamePlayer p = new GamePlayer();
            p.setUid(1003800 + i);
            p.setRoomId(String.valueOf(gsetting.getPassword()));
            p.setWuid("wx_" + String.valueOf(p.getUid()));
            p.setNickName("nick_" + String.valueOf(p.getUid()));
            p.setPic("pic_" + String.valueOf(p.getUid()));
            p.setGender(0);//性别
            p.setIp("10.1.2.15");
            p.setRoomCard(20);
            p.setSeatNo(i + 1);
            if (p.getSeatNo() == 1) {
                p.setBanker(true);
            } else {
                p.setBanker(false);
            }

            p.setScore(0);
            p.setCurrentActions(null);
            p.setLastGameAction(null);
            plist.add(p);
        }

        RafoRoom room = new RafoRoom(gsetting);
        AbstractGame agame = AbstractGameFactory.create(room, plist);
        List<GamePlayer> players = agame.dealFaPai();
        System.out.println("\n\n开始发牌===========================");
        for (GamePlayer p : players) {
            System.out.println("玩家: [" + p.getUid() + "] 手牌:" + toP(ArraySort(p.getHandCards())));
        }
        int currTurnPlayer = agame.getCurrTurnPlayer();
        System.out.println("\n轮到玩家: [" + currTurnPlayer + "] 出牌");
        String outCard = ScreenInput();

        for (GamePlayer p : players) {
            if (p.getUid() == currTurnPlayer) {
                agame.dealOutPai(p.getUid(), daPaiAction(Integer.parseInt(outCard)));
                System.out.println("玩家: [" + p.getUid() + "] 打出 =>" + toPai(agame.getCurrOutCard()));
            }
        }
        boolean isGameOver = false;

        while ( !isGameOver ) {
            AbstractGame bgame = gameInstance(Integer.parseInt(password));
            isGameOver = bgame.isGameOver();
            List<GamePlayer> bplayers = bgame.getPlayers();
            for (GamePlayer p : bplayers) {
                System.out.println("玩家: [" + p.getUid() + "] 手牌:" + toP(ArraySort(p.getHandCards())) + "       弃牌:" + toP(ArraySort(p.getOutCards())) +
                        "       明牌:" + toOpen(p.getOpenCards()));
            }
            for (GamePlayer p : bplayers) {
                if (p.getCurrentActions().size() > 0) {

                    if(hasOpt( p.getCurrentActions() ) ){ //有可进行的操作
                        System.out.println("玩家: [" + p.getUid() + "]可进行操作:【" + canOpt(p.getCurrentActions()) + " 过】");

                        String inCard = ScreenInput();
                        if (inCard.equals("PENG")) {
                            bgame.dealInPai(p.getUid(), pengPaiAction(bgame.getCurrOutCard()));
                            System.out.println("玩家: [" + p.getUid() + "]进行操作:【碰】");
                        } else if (inCard.equals("GANG")) {
                            bgame.dealInPai(p.getUid(), gangPaiAction(Action.GANG,null, bgame.getCurrOutCard()));
                            System.out.println("玩家: [" + p.getUid() + "]进行操作:【杠】");
                        } else if (inCard.equals("ANGANG")) {
                            bgame.dealInPai(p.getUid(), gangPaiAction(Action.ANGANG,null, bgame.getCurrOutCard()));
                            System.out.println("玩家: [" + p.getUid() + "]进行操作:【暗杠】");
                        } else if (inCard.equals("DIANGANG")) {
                            bgame.dealInPai(p.getUid(), gangPaiAction(Action.DIANGANG,null, bgame.getCurrOutCard()));
                            System.out.println("玩家: [" + p.getUid() + "]进行操作:【点杠】");
                        } else if (inCard.equals("HU")) {
                            bgame.dealInPai(p.getUid(), huPaiAction(Action.CAN_HU,null, bgame.getCurrOutCard()));
                            System.out.println("玩家: [" + p.getUid() + "]进行操作:【胡】");
                        }else if(inCard.equals("GUO")){
                            bgame.dealGuoPai(p.getUid());
                            System.out.println( "玩家: ["+ p.getUid()+"]进行操作:【过】" );
                        }else if(inCard.equals("TING")){
                            bgame.dealTingPai(p.getUid(),tingPaiAction());
                            System.out.println( "玩家: ["+ p.getUid()+"]进行操作:【听牌】" );
                        }else if(inCard.equals("CLEAR")){
                            bgame.clearDealer();
                            System.out.println( "玩家: ["+ p.getUid()+"]进行操作:【清除】" );
                        }else if(inCard.equals("FAPAI")){
                            bgame.dealFaPai();
                            System.out.println( "玩家: ["+ p.getUid()+"]进行操作:【发牌】" );
                        }
                    }

                    if( p.getCurrentActions().size() > 0 ){
                        for( GameAction ga:    p.getCurrentActions()){
                            if(   ga.getAction() == Action.CHONGFENGJI){
                                System.out.println("玩家: [" + p.getUid() + "] 打出冲锋鸡:");
                            }
                            if(   ga.getAction() == Action.ZERENJI){
                                System.out.println("玩家: [" + p.getUid() + "] 打出责任鸡:");
                            }
                            if(   ga.getAction() == Action.MO){
                                System.out.println("玩家: [" + p.getUid() + "] 摸牌:" + ga.getCards().get(0)[0] );
                            }
                        }
                    }
                }
            }

            if( bgame.isGameOver()){
                System.out.println( "游戏结束" );
              //  bgame.clearDealer();
                return;
            }

            currTurnPlayer = bgame.getCurrTurnPlayer();
            System.out.println("\n轮到玩家: [" + currTurnPlayer + "] 出牌");

            String ouCard = ScreenInput();

            for (GamePlayer p : bplayers) {
                if (p.getUid() == currTurnPlayer) {
                        bgame.dealOutPai(p.getUid(), daPaiAction(Integer.parseInt(ouCard)));
                        System.out.println("玩家: [" + p.getUid() + "] 打出 =>" + toPai(bgame.getCurrOutCard()));
                    }

            }
        }

    }
}
