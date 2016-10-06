package com.kodgames.battle.entity.room;

/**
 * Created by Administrator on 2016/9/17.
 */
public class GBRoomDestoryREQ {

    private String accountID ;
    private int roomID ;

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }
}
