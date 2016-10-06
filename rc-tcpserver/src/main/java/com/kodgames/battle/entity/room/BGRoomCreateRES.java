package com.kodgames.battle.entity.room;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

/**
 * Created by Administrator on 2016/9/17.
 */
public class BGRoomCreateRES {
    private int result;
    private String accountID ;
    private int roomID ; //optional
    private int count ;  // 房间容纳局数对应的索引 0 - 8，1 - 16
    private int type ; // 玩法类型
    private int playerID ; // 玩家ID

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

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public SFSObject toSFSObject(){
        SFSObject obj = new SFSObject();
        obj.putInt("result", this.getResult());
        obj.putUtfString("accountID", this.getAccountID());
        if(roomID > 0){
            obj.putInt("roomID", this.getRoomID());
        }
        obj.putInt("count", count);
        obj.putInt("type", type);
        obj.putInt("playerID", playerID);
        return obj;
    }


}
