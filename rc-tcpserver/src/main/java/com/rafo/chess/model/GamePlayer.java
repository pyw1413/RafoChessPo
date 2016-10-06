package com.rafo.chess.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/8.
 */
public class GamePlayer {

    public enum PlayState
    {
        Idle, Ready, Battle,
    }

    //内部UID
    private int uid;
    //房间ID
    private String roomId;
    //微信UID
    private String wuid;

    private String nickName;

    private String pic;

    private int gender;

    private String ip;
    //房卡
    private int roomCard;
    //座位号
    private int seatNo;
    //庄家
    private boolean banker;
    //得分
    private int score;
    //金币
    private int gold;
    //手牌
    private Integer[] handCards;
    //明牌
    private List<GameAction> openCards  = new ArrayList<>();
    //出牌
    private Integer[] outCards;

    //进牌 ##暂时没用
    private Integer[] inCards;
    //当前摸到的牌
    private int mCard;
    //上一步操作
    private GameAction lastGameAction;
    //当前可候选的游戏动作
    private List<GameAction> currentActions = new ArrayList<>();
    //是否听牌状态
    private boolean reachStatus;


    //首次摸牌
    private boolean firstMoPai;
    //漏杠牌
    private List<Integer> lostGangCards  = new ArrayList<>();

    private PlayState playState = PlayState.Idle;

    private boolean isOffline;



    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getWuid() {
        return wuid;
    }

    public void setWuid(String wuid) {
        this.wuid = wuid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getRoomCard() {
        return roomCard;
    }

    public void setRoomCard(int roomCard) {
        this.roomCard = roomCard;
    }

    public Integer[] getHandCards() {
        return handCards;
    }

    public void setHandCards(Integer[] handCards) {
        this.handCards = handCards;
    }

    public List<GameAction> getOpenCards() {
        return openCards;
    }

    public void setOpenCards(List<GameAction> openCards) {
        this.openCards = openCards;
    }

    public Integer[] getOutCards() {
        return outCards;
    }

    public void setOutCards(Integer[] outCards) {
        this.outCards = outCards;
    }

    public Integer[] getInCards() {
        return inCards;
    }

    public void setInCards(Integer[] inCards) {
        this.inCards = inCards;
    }

    public boolean isBanker() {
        return banker;
    }

    public void setBanker(boolean banker) {
        this.banker = banker;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public GameAction getLastGameAction() {
        return lastGameAction;
    }

    public void setLastGameAction(GameAction lastGameAction) {
        this.lastGameAction = lastGameAction;
    }

    public int getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(int seatNo) {
        this.seatNo = seatNo;
    }

    public List<GameAction> getCurrentActions() {
        return currentActions;
    }

    public void setCurrentActions(List<GameAction> currentActions) {
        this.currentActions = currentActions;
    }


    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }


    public boolean isReachStatus() {
        return reachStatus;
    }

    public void setReachStatus(boolean reachStatus) {
        this.reachStatus = reachStatus;
    }

    public boolean isFirstMoPai() {
        return firstMoPai;
    }

    public void setFirstMoPai(boolean firstMoPai) {
        this.firstMoPai = firstMoPai;
    }


    public List<Integer> getLostGangCards() {
        return lostGangCards;
    }

    public void setLostGangCards(List<Integer> lostGangCards) {
        this.lostGangCards = lostGangCards;
    }


    public PlayState getPlayState() {
        return playState;
    }

    public void setPlayState(PlayState playState) {
        this.playState = playState;
    }

    public List<GameAction> judgeHU(Integer outCard  ){
        List<GameAction> listhu =  null;


        return listhu;
    }


    public boolean isOffline() {
        return isOffline;
    }

    public void setOffline(boolean offline) {
        isOffline = offline;
    }

    public int getmCard() {
        return mCard;
    }

    public void setmCard(int mCard) {
        this.mCard = mCard;
    }
}
