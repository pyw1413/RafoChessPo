package com.kodgames.battle.entity.battle;

import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/17.
 */
public class BattleData {

    private int bankerId;  					// 庄家id
    private int battleTime;					// 当前局数
    private int battleCount;                 // 总局数
    private int bankerTime;                  // 连庄次数
    private List<BattleDealCard> battleDealCards = new ArrayList<>();  	// 初始牌信息，只有是发牌类型时，才下发次数据
    private List<BattleStep> battleSteps = new ArrayList<>();          	// 战斗操作
    private List<BattleBalance> battleBalances = new ArrayList<>();        // 战斗结果
    private List<BattleCensus> battleCensuss = new ArrayList<>();         // 战局统计

    public int getBankerId() {
        return bankerId;
    }

    public void setBankerId(int bankerId) {
        this.bankerId = bankerId;
    }

    public int getBattleTime() {
        return battleTime;
    }

    public void setBattleTime(int battleTime) {
        this.battleTime = battleTime;
    }

    public int getBattleCount() {
        return battleCount;
    }

    public void setBattleCount(int battleCount) {
        this.battleCount = battleCount;
    }

    public List<BattleDealCard> getBattleDealCards() {
        return battleDealCards;
    }

    public void setBattleDealCards(List<BattleDealCard> battleDealCards) {
        this.battleDealCards = battleDealCards;
    }

    public List<BattleStep> getBattleSteps() {
        return battleSteps;
    }

    public void setBattleSteps(List<BattleStep> battleSteps) {
        this.battleSteps = battleSteps;
    }

    public List<BattleBalance> getBattleBalances() {
        return battleBalances;
    }

    public void setBattleBalances(List<BattleBalance> battleBalances) {
        this.battleBalances = battleBalances;
    }

    public List<BattleCensus> getBattleCensuss() {
        return battleCensuss;
    }

    public void setBattleCensuss(List<BattleCensus> battleCensuss) {
        this.battleCensuss = battleCensuss;
    }

    public void addBattleSteps(BattleStep battleStep) {
        this.battleSteps.add(battleStep);
    }

    public void addBattleBalances(BattleBalance battleBalance) {
        this.battleBalances.add(battleBalance);
    }

    public void addBattleCensuss(BattleCensus battleCensus) {
        this.battleCensuss.add(battleCensus);
    }

    public void addBattleDealCards(BattleDealCard dealCard) {
        this.battleDealCards.add(dealCard);
    }

    public SFSObject toSFSObject(){
        SFSObject obj = new SFSObject();
        obj.putInt("bankerId", bankerId);
        obj.putInt("battleTime", battleTime);
        obj.putInt("battleCount", battleCount);
        obj.putInt("bankerTime", bankerTime);

        if(battleDealCards.size() > 0){
            SFSArray arr = new SFSArray();
            for(BattleDealCard battleDealCard : battleDealCards){
                arr.addSFSObject(battleDealCard.toSFSObject());
            }
            obj.putSFSArray("battleDealCards", arr);
        }

        if(battleSteps.size() > 0){
            SFSArray arr = new SFSArray();
            for(BattleStep battleStep : battleSteps){
                arr.addSFSObject(battleStep.toSFSObject());
            }
            obj.putSFSArray("battleSteps", arr);
        }

        //battleBalances 不能为空
        SFSArray arr1 = new SFSArray();
        for(BattleBalance battleBalance : battleBalances){
            arr1.addSFSObject(battleBalance.toSFSObject());
        }
        obj.putSFSArray("battleBalances", arr1);

        if(battleCensuss.size() > 0){
            SFSArray arr = new SFSArray();
            for(BattleCensus battleCensus : battleCensuss){
                arr.addSFSObject(battleCensus.toSFSObject());
            }
            obj.putSFSArray("battleCensuss", arr);
        }

        return obj;
    }
}
