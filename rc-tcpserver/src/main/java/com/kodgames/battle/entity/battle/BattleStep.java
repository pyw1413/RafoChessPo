package com.kodgames.battle.entity.battle;

import com.smartfoxserver.v2.entities.data.SFSObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/17.
 */
public class BattleStep {
    private int ownerId ;   // 出牌玩家id
    private int targetId ;  // 目标玩家id
    private int playType ;  // 战斗类型
    private List<Integer> card = new ArrayList<>();      // 牌值
    private int remainCardCount ; // 剩余牌数
    private boolean ignoreOther;

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public int getPlayType() {
        return playType;
    }

    public void setPlayType(int playType) {
        this.playType = playType;
    }

    public List<Integer> getCard() {
        return card;
    }

    public void setCard(List<Integer> card) {
        this.card = card;
    }

    public int getRemainCardCount() {
        return remainCardCount;
    }

    public void setRemainCardCount(int remainCardCount) {
        this.remainCardCount = remainCardCount;
    }

    public void addCard(Integer card) {
        this.card.add(card);
    }

    public boolean isIgnoreOther() {
        return ignoreOther;
    }

    public void setIgnoreOther(boolean ignoreOther) {
        this.ignoreOther = ignoreOther;
    }

    public void addPlayType(int playType){
        this.playType |= playType;
    }


    public BattleStep clone(){
        BattleStep step = new BattleStep();
        step.setOwnerId(ownerId);
        step.setTargetId(targetId);
        step.setPlayType(playType);
        List<Integer> nCards = new ArrayList<>();
        for(Integer c : card){
            nCards.add(c);
        }
        step.setCard(nCards);
        step.setRemainCardCount(remainCardCount);

        return step;
    }

    public SFSObject toSFSObject(){
        SFSObject obj = new SFSObject();
        obj.putInt("ownerId",ownerId);
        obj.putInt("targetId",targetId);
        obj.putInt("playType",playType);
        obj.putIntArray("card",card);
        obj.putInt("remainCardCount",remainCardCount);

        return obj;
    }

}
