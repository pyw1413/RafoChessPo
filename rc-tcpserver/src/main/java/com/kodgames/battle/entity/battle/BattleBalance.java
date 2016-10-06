package com.kodgames.battle.entity.battle;

import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/17.
 */
public class BattleBalance {
    private int playerId;
    private int winType;   // 胡牌类型
    private List<Integer> cards = new ArrayList<>();     // 牌
    private int winScore;  // 番数
    private int winPoint;  // 积分
    private List<CardBalance> balances = new ArrayList<>();

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getWinType() {
        return winType;
    }

    public void setWinType(int winType) {
        this.winType = winType;
    }

    public List<Integer> getCards() {
        return cards;
    }

    public void setCards(List<Integer> cards) {
        this.cards = cards;
    }

    public int getWinScore() {
        return winScore;
    }

    public void setWinScore(int winScore) {
        this.winScore = winScore;
    }

    public int getWinPoint() {
        return winPoint;
    }

    public void setWinPoint(int winPoint) {
        this.winPoint = winPoint;
    }

    public List<CardBalance> getBalances() {
        return balances;
    }

    public void setBalances(List<CardBalance> balances) {
        this.balances = balances;
    }

    public void addCards(int card) {
        this.cards.add(card);
    }

    public void addBalances(CardBalance cardBlance) {
        this.balances.add(cardBlance);
    }

    public SFSObject toSFSObject(){
        SFSObject obj = new SFSObject();
        obj.putInt("playerId", playerId );
        obj.putInt("winType", winType );
        obj.putIntArray("cards", cards);
        obj.putInt("winScore", winScore );
        obj.putInt("winPoint", winPoint );
        SFSArray bs = new SFSArray();
        for(CardBalance cardBalance : balances){
            bs.addSFSObject(cardBalance.toSFSObject());
        }
        obj.putSFSArray("balances", bs);
        return obj;
    }
}
