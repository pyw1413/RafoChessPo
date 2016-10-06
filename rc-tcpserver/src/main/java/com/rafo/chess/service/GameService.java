package com.rafo.chess.service;

import com.kodgames.battle.common.constants.GlobalConstants;
import com.kodgames.battle.entity.battle.*;
import com.kodgames.battle.service.battle.MajhongHelper;
import com.kodgames.battle.service.room.RoomHelper;
import com.rafo.chess.calculate.CalculateManager;
import com.rafo.chess.calculate.ResultFlag;
import com.rafo.chess.core.GameExtension;
import com.rafo.chess.logic.gymj.GYMJGame;
import com.rafo.chess.model.*;
import com.rafo.chess.utils.CmdsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by Administrator on 2016/9/26.
 */
public class GameService {

    private final Logger logger = LoggerFactory.getLogger(GameService.class);

    private GYMJGame gyMJGame;
    private RafoRoom room;
    private boolean isGameStart = false; //游戏开始
    private GameExtension gameExtension;
    private static Map<Integer, Action> playType2Action = new HashMap<>();
    private static Map<Action, Integer> action2PlayType = new HashMap<>();
    private static Map<Action, Integer> realAction2PlayType = new HashMap<>();
    private static Map<ActionType, Integer> actionType2PlayType = new HashMap<>();
    private static Map<Integer, Integer> resultFlag2PlayType = new HashMap<>();

    public GYMJGame getGyMJGame() {
        return gyMJGame;
    }

    //老的逻辑的类型
    public class PlayType {
        public static final int Idle = 0;
        public static final int Deal = 1 << 1; // 发牌
        public static final int Draw = 1 << 2; // 摸牌
        public static final int Discard = 1 << 3; // 打牌
        public static final int CanPong = 1 << 4; // 可碰
        public static final int Pong = 1 << 5; // 碰
        public static final int CanKong = 1 << 6; // 可明杠
        public static final int Kong = 1 << 7; // 明杠
        public static final int CanCealedKong = 1 << 8; // 可暗杠
        public static final int CealedKong = 1 << 9; // 暗杠
        public static final int CanDotKong = 1 << 10; // 可点杠
        public static final int DotKong = 1 << 11; // 点杠
        public static final int CanHu = 1 << 12; // 可胡
        public static final int Hu = 1 << 13; // 胡牌
        public static final int He = 1 << 14;
        public static final int CanReadyHand = 1 << 15; // 可听牌
        public static final int ReadyHand = 1 << 16; // 听牌(软听)
        public static final int OffLine = 1 << 17; // 离线
        public static final int Pass = 1 << 18; // 过
        public static final int ChargeChicken = 1 << 19; // 冲锋鸡
        public static final int DutyChicken = 1 << 20; // 责任鸡

        // 以下的不参与出牌操作，用于结算
        public static final int NormalChicken = 1 << 21; // 普通鸡
        public static final int FlopChicken = 1 << 22; // 翻牌鸡
        public static final int KillReadyHand = 1 << 23; // 杀胡
        public static final int QiangGang = 1 << 24; // 抢杠
        public static final int GangShangHu = 1 << 25; // 杠上胡
        public static final int GangHouPao = 1 << 26; // 杠后炮
        public static final int WinSelf = 1 << 27; // 自摸
        public static final int DiscardOther = 1 << 28; // 点炮
        public static final int DotKongOther = 1 << 29; // 点明杠
        public static final int HardReadHand = 1 << 30; // 硬听
        public static final int BeKilledReadHand = (1 << 30) + 1; // 被杀报
    }

    static {
        playType2Action.put(PlayType.Draw, Action.MO);
        playType2Action.put(PlayType.Discard, Action.DA);
        playType2Action.put(PlayType.CanPong, Action.PENG);
        playType2Action.put(PlayType.CanKong, Action.GANG);
        playType2Action.put(PlayType.CanCealedKong, Action.ANGANG);
        playType2Action.put(PlayType.CanDotKong, Action.DIANGANG);
        playType2Action.put(PlayType.CanHu, Action.CAN_HU);
        playType2Action.put(PlayType.Hu, Action.CAN_HU);
        playType2Action.put(PlayType.Pass, Action.GUO);
        playType2Action.put(PlayType.Discard, Action.DA);
        playType2Action.put(PlayType.Pong, Action.PENG);
        playType2Action.put(PlayType.Kong, Action.DIANGANG);
        playType2Action.put(PlayType.CealedKong, Action.ANGANG);
        playType2Action.put(PlayType.DotKong, Action.GANG);
        playType2Action.put(PlayType.ChargeChicken, Action.CHONGFENGJI);
        playType2Action.put(PlayType.DutyChicken, Action.ZERENJI);
        playType2Action.put(PlayType.CanReadyHand, Action.TING);

        action2PlayType.put(Action.MO, PlayType.Draw);
        action2PlayType.put(Action.DA, PlayType.Discard);
        action2PlayType.put(Action.PENG, PlayType.CanPong);
        action2PlayType.put(Action.GANG, PlayType.CanDotKong);
        action2PlayType.put(Action.ANGANG, PlayType.CanCealedKong);
        action2PlayType.put(Action.DIANGANG, PlayType.CanKong);
        action2PlayType.put(Action.GUO, PlayType.Pass);
        action2PlayType.put(Action.DEAL, PlayType.Deal);
        action2PlayType.put(Action.CAN_HU, PlayType.CanHu);
        action2PlayType.put(Action.CHONGFENGJI, PlayType.ChargeChicken);
        action2PlayType.put(Action.ZERENJI, PlayType.DutyChicken);
        action2PlayType.put(Action.TING, PlayType.CanReadyHand);

        actionType2PlayType.put(ActionType.PUTONGJI, PlayType.NormalChicken);
        actionType2PlayType.put(ActionType.CHONGFENGJI, PlayType.ChargeChicken);
        actionType2PlayType.put(ActionType.ZERENJI, PlayType.DutyChicken);
        actionType2PlayType.put(ActionType.ZIMO, PlayType.WinSelf);
        actionType2PlayType.put(ActionType.GANGSHANGHU, PlayType.GangShangHu);
        actionType2PlayType.put(ActionType.REPAO, PlayType.GangHouPao);
        actionType2PlayType.put(ActionType.QIANGGANGHU, PlayType.QiangGang);
        actionType2PlayType.put(ActionType.TIANHU, PlayType.Hu); //TODO: 没有对应的类型
        actionType2PlayType.put(ActionType.YINGTING, PlayType.HardReadHand);
        actionType2PlayType.put(ActionType.RUANTING, PlayType.HardReadHand);

        realAction2PlayType.put(Action.DA, PlayType.Discard);
        realAction2PlayType.put(Action.DA, PlayType.Discard);
        realAction2PlayType.put(Action.PENG, PlayType.Pong);
        realAction2PlayType.put(Action.GANG, PlayType.DotKong);
        realAction2PlayType.put(Action.ANGANG, PlayType.CealedKong);
        realAction2PlayType.put(Action.DIANGANG, PlayType.Kong);
        realAction2PlayType.put(Action.GUO, PlayType.Pass);
        realAction2PlayType.put(Action.DEAL, PlayType.Deal);
        realAction2PlayType.put(Action.CHONGFENGJI, PlayType.Discard);
        realAction2PlayType.put(Action.ZERENJI, PlayType.Discard);


        resultFlag2PlayType.put(0, 0);
        resultFlag2PlayType.put(ResultFlag.ACT_WEIJIAOZUI.getFlag(), 0);
        resultFlag2PlayType.put(ResultFlag.HU_PINGHU.getFlag(), PlayType.Hu);
        resultFlag2PlayType.put(ResultFlag.HU_DADUIZI.getFlag(), MajhongHelper.HuType.DaDuiZiHu.ordinal());
        resultFlag2PlayType.put(ResultFlag.HU_QIDUI.getFlag(), MajhongHelper.HuType.QiDuiHu.ordinal());
        resultFlag2PlayType.put(ResultFlag.HU_LONGQIDUI.getFlag(), MajhongHelper.HuType.LongQiDuiHu.ordinal());
        resultFlag2PlayType.put(ResultFlag.HU_QINGYISE.getFlag(), MajhongHelper.HuType.QingYiSeHu.ordinal());
        resultFlag2PlayType.put(ResultFlag.HU_QINGQIDUI.getFlag(), MajhongHelper.HuType.QingQiDuiHu.ordinal());
        resultFlag2PlayType.put(ResultFlag.HU_QINGDADUI.getFlag(), MajhongHelper.HuType.QingDaduiHU.ordinal());
        resultFlag2PlayType.put(ResultFlag.HU_QINGLONGDUI.getFlag(), MajhongHelper.HuType.QingLongBeiHu.ordinal());
        resultFlag2PlayType.put(ResultFlag.ACT_DIANPAO.getFlag(), PlayType.DiscardOther);
        resultFlag2PlayType.put(ResultFlag.ACT_ZIMO.getFlag(), PlayType.WinSelf);
        resultFlag2PlayType.put(ResultFlag.ACT_JIAOZUI.getFlag(), MajhongHelper.HuType.Ting.ordinal());
        resultFlag2PlayType.put(ResultFlag.ACT_PUTONGJI.getFlag(), PlayType.NormalChicken);
        resultFlag2PlayType.put(ResultFlag.ACT_CHONGFENGJI.getFlag(), PlayType.ChargeChicken);
        resultFlag2PlayType.put(ResultFlag.ACT_FANPAIJI.getFlag(), PlayType.FlopChicken);
        resultFlag2PlayType.put(ResultFlag.ACT_ZERENJI.getFlag(), PlayType.DutyChicken);
        resultFlag2PlayType.put(ResultFlag.ACT_MINGDOU.getFlag(), PlayType.Kong);
        resultFlag2PlayType.put(ResultFlag.ACT_DIANMINGDOU.getFlag(), PlayType.DotKongOther);
        resultFlag2PlayType.put(ResultFlag.ACT_GANGSHANGHU.getFlag(), PlayType.GangShangHu);
        resultFlag2PlayType.put(ResultFlag.ACT_GANGHOUPAO.getFlag(), PlayType.GangHouPao);
        resultFlag2PlayType.put(ResultFlag.ACT_DIANGANGHOUPAO.getFlag(), PlayType.DiscardOther);
        resultFlag2PlayType.put(ResultFlag.ACT_QIANGGANGHU.getFlag(), PlayType.QiangGang);
        resultFlag2PlayType.put(ResultFlag.ACT_YINGBAO.getFlag(), PlayType.HardReadHand);
        resultFlag2PlayType.put(ResultFlag.ACT_RUANBAO.getFlag(), PlayType.ReadyHand);
    }

    public GameService(GameExtension roomExt) {
        this.gameExtension = roomExt;
    }

    //2. ready
    public void ready(int playerId) {

        setPlayerStatus(playerId, false);
        sendBattleStatus(playerId); //发送准备

        if (isAllPlayerReady()) { //发牌
            battleStart();
            sendBattleStatus(playerId); //battle

            GameAction gameAction = new GameAction();
            gameAction.setAction(Action.DEAL);
            gameAction.setPlayerId(playerId);
            gyMJGame = (GYMJGame) AbstractGameFactory.create(room, this.room.getPlayers());
            gyMJGame.dealFaPai();

            sendBattleData(gameAction, gyMJGame.getPlayers(), 0);
        }
    }

    public void playerOffline(int playerId) {
        setPlayerStatus(playerId, true);
        sendBattleStatus(playerId);
    }


    //3. battle step 打牌
    public void play(int playerId, int playType, int card) {

        int lastPlayerId = gyMJGame.getGdealer().getCurrTurnPlayer(); // 上一步是谁
        GameAction gameAction = new GameAction();
        gameAction.setAction(playType2Action.get(playType));
        gameAction.setPlayerId(playerId);

        List<Integer[]> cards = new ArrayList<>();
        cards.add(new Integer[]{oldCardToNewCard(card)});

        gameAction.setCards(cards);

        try {
            if (gameAction.getAction() == Action.DA) {
                gyMJGame.dealOutPai(playerId, gameAction);
            } else {
                gyMJGame.dealInPai(playerId, gameAction);
            }

            if (gyMJGame.getGdealer().isOver()) {
                calculateResult(playerId); //算分，取结果
            } else {
                sendBattleData(gameAction, gyMJGame.getPlayers(), lastPlayerId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendFailedStatus(playerId);
        }

    }


    public void sendFailedStatus(int playerId) {
        BWBattleStepRES res = new BWBattleStepRES();
        res.setResult(GlobalConstants.BW_Battle_Step_InValid_Operator);
        res.setAccountId(String.valueOf(playerId));
//        gameExtension.send(CmdsUtils.CMD_BATTLE_STEP, res.toSFSObject(), gameExtension.getApi().getUserByName(String.valueOf(playerId)));
    }

    public void setPlayerStatus(int playerId, boolean offline) {
        //set user status
        GamePlayer player = this.room.getPlayerMapping().get(playerId);
        if (player == null) {
            return;
        }

        player.setOffline(offline);

        if (!offline) {
            player.setPlayState(GamePlayer.PlayState.Ready);
        }
    }

    public boolean isAllPlayerReady() {
        if (room.getRoomSize() != this.room.getPlayers().size()) {
            return false;
        }

        for (GamePlayer player : this.room.getPlayers()) {
            if (player.isOffline() || player.getPlayState() != GamePlayer.PlayState.Ready) {
                return false;
            }
        }
        return true;
    }


    public void battleStart() {
        for (GamePlayer player : this.room.getPlayers()) {
            player.setPlayState(GamePlayer.PlayState.Battle);
        }
        this.isGameStart = true;
        this.room.setInBattle(true);
    }


    // 发送玩家状态
    public void sendBattleStatus(int playerId) {
        BWBattleStartRES res = new BWBattleStartRES();
        for (GamePlayer player : this.room.getPlayers()) {
            if (playerId == player.getUid())
                res.setPlayerId(playerId);

            BattlePlayerStatus statusBuilder = new BattlePlayerStatus();
            statusBuilder.setPlayerId(player.getUid());
            statusBuilder.setStatus(player.getPlayState().ordinal());
            statusBuilder.setPoints(player.getScore());
            statusBuilder.setOffline(player.isOffline());
            res.addPlayerStatus(statusBuilder);
        }

        res.setCurrentBattleCount(gyMJGame == null ? 1 : gyMJGame.getGdealer().getRoom().getCurrRounds());
        res.setAccountId(String.valueOf(playerId));

//        this.gameExtension.send(CmdsUtils.CMD_BATTLE_READY, res.toSFSObject(), this.gameExtension.getParentRoom().getUserList());
    }

    //发送玩家打牌信息
    public void sendBattleData(GameAction currentAction, List<GamePlayer> players, int lastPlayerId) {
        List<BattleStep> steps = new ArrayList<>();

        //1. 打出去的牌
        BattleStep step = actionToBattleStep(currentAction, currentAction.getPlayerId(), true);
        step.setOwnerId(currentAction.getPlayerId());
        if (lastPlayerId == 0) {
            step.setTargetId(currentAction.getPlayerId());
        } else {
            step.setTargetId(lastPlayerId);
        }
        if (currentAction.getAction() == Action.GUO) {
            step.setIgnoreOther(true);
        }

        steps.add(step);

        //可进行的操作
        int currentActionCount = 0;
        int targetPlayerId = 0;
        for (GamePlayer player : players) {
            targetPlayerId = currentAction.getPlayerId();
            if (player.getCurrentActions() != null) {
                step = null;
                for (GameAction action : player.getCurrentActions()) {

                    if (step != null && (step.isIgnoreOther() || action.getAction() == Action.DIANGANG)) { //多个Action合并成一个
                        BattleStep newStep = actionToBattleStep(action, action.getPlayerId(), false);
                        step.addPlayType(newStep.getPlayType());
                    } else {
                        step = actionToBattleStep(action, action.getPlayerId(), false);
                        step.setOwnerId(player.getUid());
                        if (action.getAction() == Action.MO) {
                            step.setTargetId(player.getUid());
                        } else {
                            step.setTargetId(targetPlayerId);
                        }
                        targetPlayerId = player.getUid(); //TODO: 冲锋鸡责任鸡
                        if (action.getAction() == Action.CHONGFENGJI || action.getAction() == Action.ZERENJI) {
                            step.setTargetId(0);
                            step.setOwnerId(lastPlayerId);

                        }

                        if (action.getAction() != Action.CHONGFENGJI && action.getAction() != Action.ZERENJI) {
                            currentActionCount++;
                        }

                        if (action.getAction() != Action.MO && action.getAction() != Action.CHONGFENGJI && action.getAction() != Action.ZERENJI) {
                            step.setIgnoreOther(true);
                        }

                        steps.add(step);
                    }
                }
            }
        }

        //如果当前没有可进行操作，添加摸牌动作，表示用户可以出牌（客户端的约定）
        if (currentActionCount == 0) {
            GameAction action = new GameAction();
            action.setAction(Action.MO);
            action.setPlayerId(gyMJGame.getCurrTurnPlayer());
            List<Integer[]> cards = new ArrayList<>();
            cards.add(new Integer[]{});
            action.setCards(cards);
            step = actionToBattleStep(action, action.getPlayerId(), false);
            step.setOwnerId(gyMJGame.getCurrTurnPlayer());
            step.setTargetId(gyMJGame.getCurrTurnPlayer());
            steps.add(step);
        }

        sendBattleStep(steps);

    }

    public void sendBattleStep(List<BattleStep> steps) {

        Map<Integer, BWBattleStepRES> results = new HashMap<>();

        for (BattleStep step : steps) {
            for (GamePlayer player : gyMJGame.getPlayers()) {
                if (player.getUid() != step.getOwnerId() && step.isIgnoreOther()) {
                    continue;
                }

                BWBattleStepRES res = results.get(player.getUid());
                BattleData battleData = null;
                if (res == null) {
                    res = new BWBattleStepRES();
                    results.put(player.getUid(), res);

                    res.setResult(GlobalConstants.BW_Battle_Step_SUCCESS);
                    res.setAccountId(String.valueOf(player.getUid()));
                    battleData = new BattleData();
                    res.setBattleData(battleData);

                    battleData.setBankerId(gyMJGame.getGdealer().getBankPlayer());
                    battleData.setBattleTime(gyMJGame.getGdealer().getRoom().getCurrRounds());
                    battleData.setBattleCount(gyMJGame.getGdealer().getRoom().getTotalRounds());
                } else {
                    battleData = res.getBattleData();
                }

                BattleStep stepBuilder = step.clone();

                if (stepBuilder.getPlayType() == PlayType.Draw && stepBuilder.getOwnerId() != player.getUid()) {
                    stepBuilder.getCard().clear();
                    stepBuilder.addCard(-1);
                }

                if (step.getPlayType() == PlayType.Deal) { //发牌
                    stepBuilder.setOwnerId(player.getUid());
                    for (GamePlayer p : gyMJGame.getPlayers()) {
                        BattleDealCard dealBuild = new BattleDealCard();
                        dealBuild.setPlayerId(p.getUid());
                        if (player.getUid() == p.getUid()) {
                            // 设置手上的牌
                            if (p.getHandCards() != null) {
                                for (Integer c : p.getHandCards()) {
                                    dealBuild.addCards(newCardToOldCard(c));
                                }
                            }
                        }

                        // 设置打出去的牌
                        if (player.getOutCards() != null) {
                            for (int c : player.getOutCards()) {
                                dealBuild.addDisposeCards(newCardToOldCard(c));
                            }
                        }

                        battleData.addBattleDealCards(dealBuild);

                        // 设置玩家状态信息 TODO: 还不全
                        if (p.getOpenCards() != null && p.getOpenCards().size() > 0) {
                            BattleBalance balance = new BattleBalance();
                            balance.setPlayerId(p.getUid());
                            for (GameAction ga : player.getOpenCards()) {
                                CardBalance cardBlance = new CardBalance();
                                cardBlance.setType(actionType2PlayType.get(ga.getAction()));
                                cardBlance.setCard(ga.getCards().get(0)[0]);
                                balance.addBalances(cardBlance);
                            }
                            battleData.addBattleBalances(balance);
                        }
                    }

                }
                battleData.addBattleSteps(stepBuilder);
            }
        }

        for (Map.Entry<Integer, BWBattleStepRES> palayerResult : results.entrySet()) {
            System.out.println("GameService2 : " + palayerResult.getKey() + " : " + palayerResult.getValue().toSFSObject().toJson());
//            gameExtension.send(CmdsUtils.CMD_BATTLE_STEP, palayerResult.getValue().toSFSObject(), gameExtension.getApi().getUserByName(String.valueOf(palayerResult.getKey())));
        }

    }


    //牌局结束，走算分流程
    public void calculateResult(int playerId) {
        GamePlayer player = this.room.getPlayerMapping().get(playerId);
        GameAction winAction = player.getLastGameAction();
        winAction.setPlayerId(playerId);

        BattleStep step = actionToBattleStep(winAction, playerId, false);
        if (winAction.getAction() == Action.HUANG_ZHUANG) {
            step.setPlayType(PlayType.He);
        } else {
            step.setPlayType(PlayType.Hu);
        }
        step.getCard().clear();


        step.setOwnerId(playerId);
        step.setTargetId(playerId);

        BattleData battleData = new BattleData();

        try {
            CalculateManager.calculateByRoomId(gyMJGame.getGdealer().getDealerId(), battleData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        battleData = convertBattleDataToOldClient(battleData);

        Map<Integer, BWBattleStepRES> results = new HashMap<>();
        for (GamePlayer p : gyMJGame.getPlayers()) {
            BWBattleStepRES res = new BWBattleStepRES();
            res.setResult(GlobalConstants.BW_Battle_Step_SUCCESS);
            res.setAccountId(String.valueOf(p.getUid()));

            BattleStep newStep = step.clone();
            newStep.getCard().clear();
            if (p.getUid() == playerId) {
                newStep.addCard(0);
            } else {
                newStep.addCard(-1);
            }

            BattleData data = new BattleData();
            data.addBattleSteps(newStep);
            data.setBankerId(gyMJGame.getGdealer().getBankPlayer());
            data.setBattleTime(gyMJGame.getGdealer().getRoom().getCurrRounds());
            data.setBattleCount(gyMJGame.getGdealer().getRoom().getTotalRounds());
            data.setBattleBalances(battleData.getBattleBalances());
            data.setBattleCensuss(battleData.getBattleCensuss());
            res.setBattleData(data);

            results.put(p.getUid(), res);
        }

        for (Map.Entry<Integer, BWBattleStepRES> palayerResult : results.entrySet()) {
            System.out.println("GameService END : " + palayerResult.getKey() + " : " + palayerResult.getValue().toSFSObject().toJson());
            gameExtension.trace("GameService END : " + palayerResult.getValue().toSFSObject().toJson());
            gameExtension.send(CmdsUtils.CMD_BATTLE_STEP, palayerResult.getValue().toSFSObject(), gameExtension.getApi().getUserByName(String.valueOf(palayerResult.getKey())));
        }

        gyMJGame.clearDealer();
        isGameStart = false;
        for (GamePlayer p : gyMJGame.getPlayers()) {
            p.setPlayState(GamePlayer.PlayState.Idle);
        }

        // 完成所有牌局，解散房间
        if (room.getTotalRounds() < room.getCurrRounds()) {
            // 设置房间状态为打牌状态，为退出房间使用
            room.setInBattle(false);
            this.gameExtension.getRoomService().autoDestroyRoom(room.getRoomId());
            try {
                RoomHelper.destroyRoom(gameExtension.getParentRoom());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public BattleData convertBattleDataToOldClient(BattleData battleData) {
        BattleData newData = new BattleData();

        List<BattleBalance> balances = battleData.getBattleBalances();
        for (BattleBalance balance : balances) {
            BattleBalance b = new BattleBalance();
            b.setPlayerId(balance.getPlayerId());
            b.setWinPoint(balance.getWinPoint());
            b.setWinScore(balance.getWinScore());
            b.setWinType(resultFlag2PlayType.get(balance.getWinType()));

            for (Integer c : balance.getCards()) {
                b.addCards(newCardToOldCard(c));
            }

            for (CardBalance cardBalance : balance.getBalances()) {
                CardBalance cb = new CardBalance();
                cb.setCard(newCardToOldCard(cardBalance.getCard()));
                cb.setType(resultFlag2PlayType.get(cardBalance.getType()));
                cb.setScore(cardBalance.getScore());

                b.addBalances(cb);
            }

            newData.addBattleBalances(b);
        }

        for (BattleCensus battleCensus : battleData.getBattleCensuss()) {
            BattleCensus bc = new BattleCensus();
            bc.setPlayerId(battleCensus.getPlayerId());
            bc.setWinSelf(battleCensus.getWinSelf());
            bc.setDiscardOther(battleCensus.getDiscardOther());
            bc.setKong(battleCensus.getKong());
            bc.setCealedKong(battleCensus.getCealedKong());
            bc.setPoint(battleCensus.getPoint());
        }

        return newData;
    }


    private BattleStep actionToBattleStep(GameAction gameAction, int playerId, boolean inAction) {
        BattleStep stepBuilder = new BattleStep();
        int newType = 0;
        try {
            if (inAction) {
                newType = realAction2PlayType.get(gameAction.getAction());
            } else {
                if (gameAction.getAction().code() >= Action.HUANG_ZHUANG.code() && gameAction.getAction().code() <= Action.QINGLONGDUI_HU.code()) { //胡牌从另一个映射表中取状态
                    if (actionType2PlayType.get(gameAction.getActionType()) == null) {
                        newType = PlayType.DiscardOther;
                    } else {
                        newType = actionType2PlayType.get(gameAction.getActionType());
                    }
                } else {
                    newType = action2PlayType.get(gameAction.getAction());
                }
            }
        } catch (Exception e) {
//            gameExtension.trace(gameAction.getAction() + ":" + gameAction.getActionType() + " : " + playerId + " " + inAction);
            e.printStackTrace();
        }

        stepBuilder.setPlayType(newType);
        if (gameAction.getCards() != null) {
            /*if(gameAction.getAction() == Action.MO && gameAction.getPlayerId() != playerId){
                stepBuilder.addCard(-1);
            }else */
            if (gameAction.getCards().size() > 0) {

                for (int c : gameAction.getCards().get(0)) {
                    stepBuilder.addCard(newCardToOldCard(c));
                } //暂时只返回一个
                /*if(gameAction.getCards().get(0).length > 0){
                    stepBuilder.addCard(newCardToOldCard(gameAction.getCards().get(0)[0]));
                }*/
            }
        }
        stepBuilder.setRemainCardCount(gyMJGame.getGdealer().getCardsPool().length);
        return stepBuilder;
    }

    private int newCardToOldCard(int newCard) { //将新的牌的万条筒转换为老逻辑的万条筒
        //101 201 301
        return (newCard / 100) * 10 + newCard % 100;
    }

    public int oldCardToNewCard(int oldCard) {
        //101 201 301
        return (oldCard / 10) * 100 + oldCard % 10;
    }

    public RafoRoom getRoom() {
        return room;
    }


    public void setRoom(RafoRoom room) {
        this.room = room;
    }
}
