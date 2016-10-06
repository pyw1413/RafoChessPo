package com.kodgames.battle.entity.room;

import com.smartfoxserver.v2.entities.data.SFSObject;

/**
 * Created by Administrator on 2016/9/17.
 */
public class BGRoomQuitRES implements Cloneable {
    private int result ;
    private String accountID ; // 获得GateWayNode使用
    private int quitterID ; // 退出者的ID
    private String quitterAccountID ;
    private int roomID ;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public int getQuitterID() {
        return quitterID;
    }

    public void setQuitterID(int quitterID) {
        this.quitterID = quitterID;
    }

    public String getQuitterAccountID() {
        return quitterAccountID;
    }

    public void setQuitterAccountID(String quitterAccountID) {
        this.quitterAccountID = quitterAccountID;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return (BGRoomQuitRES)super.clone();
    }

    public SFSObject toSFSObject(){
        SFSObject obj = new SFSObject();
        obj.putInt("result",this.result);
        obj.putInt("quitter",this.quitterID);
        return obj;

    }
}
