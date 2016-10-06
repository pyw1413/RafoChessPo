package com.kodgames.battle.entity.battle;

import com.kodgames.battle.entity.battle.BattlePlayerStatus;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/17.
 */
public class BWBattleStartRES {

    private String accountId;
    private int playerId;
    private List<BattlePlayerStatus> playerStatus = new ArrayList<>();
    private int currentBattleCount = 4;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public List<BattlePlayerStatus> getPlayerStatus() {
        return playerStatus;
    }

    public void setPlayerStatus(List<BattlePlayerStatus> playerStatus) {
        this.playerStatus = playerStatus;
    }

    public int getCurrentBattleCount() {
        return currentBattleCount;
    }

    public void setCurrentBattleCount(int currentBattleCount) {
        this.currentBattleCount = currentBattleCount;
    }

    public void addPlayerStatus(BattlePlayerStatus status) {
        this.playerStatus.add(status);
    }

    public SFSObject toSFSObject(){
        SFSObject obj = new SFSObject();
        obj.putUtfString("accountId",this.accountId);
        obj.putInt("playerId",this.playerId);
        SFSArray arr = new SFSArray();
        for(BattlePlayerStatus one:playerStatus){
            arr.addSFSObject(one.toSFSObject());
        }
        obj.putSFSArray("playerStatus",arr);
        obj.putInt("currentBattleCount",this.currentBattleCount);
        return obj;
    }

}
