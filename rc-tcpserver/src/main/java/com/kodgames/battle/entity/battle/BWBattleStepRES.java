package com.kodgames.battle.entity.battle;


import com.smartfoxserver.v2.entities.data.SFSObject;

/**
 * Created by Administrator on 2016/9/17.
 */
public class BWBattleStepRES {

    private int result ;
    private String accountId ;
    private BattleData battleData ;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public BattleData getBattleData() {
        return battleData;
    }

    public void setBattleData(BattleData battleData) {
        this.battleData = battleData;
    }

    public SFSObject toSFSObject(){
        SFSObject obj = new SFSObject();

        obj.putInt("result", result );
        obj.putUtfString("accountId", accountId);
        if(battleData != null) {
            obj.putSFSObject("battleData", battleData.toSFSObject());
        }

        return obj;
    }

}
