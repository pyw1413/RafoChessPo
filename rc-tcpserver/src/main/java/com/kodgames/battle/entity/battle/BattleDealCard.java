package com.kodgames.battle.entity.battle;

import com.smartfoxserver.v2.entities.data.SFSObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/17.
 */
public class BattleDealCard {
    private int playerId;  // 玩家id
    private List<Integer> cards = new ArrayList<>();     // 牌
    private List<Integer> disposeCards = new ArrayList<>(); // 打出的牌 断线使用

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public List<Integer> getCards() {
        return cards;
    }

    public void setCards(List<Integer> cards) {
        this.cards = cards;
    }

    public List<Integer> getDisposeCards() {
        return disposeCards;
    }

    public void setDisposeCards(List<Integer> disposeCards) {
        this.disposeCards = disposeCards;
    }

    public void addCards(Integer key) {
        this.cards.add(key);
    }

    public void addDisposeCards(int card) {
        this.disposeCards.add(card);
    }

    public SFSObject toSFSObject(){
        SFSObject obj = new SFSObject();
        obj.putInt("playerId", playerId);
        obj.putIntArray("cards", cards);
        obj.putIntArray("disposeCards", disposeCards);
        return obj;
    }
}
