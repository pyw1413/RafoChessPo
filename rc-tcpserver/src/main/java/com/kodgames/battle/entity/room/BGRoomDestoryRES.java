package com.kodgames.battle.entity.room;

import com.smartfoxserver.v2.entities.data.SFSObject;

/**
 * Created by Administrator on 2016/9/17.
 */
public class BGRoomDestoryRES {

    private int result ;
    private String accountID ;
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

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public SFSObject toSFSObject(){
        SFSObject obj = new SFSObject();
        obj.putInt("result",this.result);
        obj.putUtfString("accountID",this.accountID);
        obj.putInt("roomID",this.roomID);
        return obj;
    }
}
