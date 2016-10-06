package com.kodgames.battle.entity.battle;

/**
 * Created by Administrator on 2016/9/17.
 */
public class WBBattleStartREQ {

    private String accountId ;
    private int roomId = 2;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}
