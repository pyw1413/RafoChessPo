package com.kodgames.battle.entity.room;

/**
 * Created by Administrator on 2016/9/17.
 */
public class WGRoomCreateREQ {

    private String accountID ;
    private int ID ;
    private int count ;
    private int type ;
    private String ip ;

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
