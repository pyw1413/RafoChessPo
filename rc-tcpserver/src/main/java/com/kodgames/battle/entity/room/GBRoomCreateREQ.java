package com.kodgames.battle.entity.room;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/17.
 */
public class GBRoomCreateREQ {
    private String accountID;
    private int ID;
    private int roomID;
    private int count;
    private int type;
    private String ip;
    private int serverID ;
    private int flopChickenType;
    private int bankerType;

    public int getFlopChickenType() {
        return flopChickenType;
    }

    public void setFlopChickenType(int flopChickenType) {
        this.flopChickenType = flopChickenType;
    }

    public int getBankerType() {
        return bankerType;
    }

    public void setBankerType(int bankerType) {
        this.bankerType = bankerType;
    }

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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getServerID() {
        return serverID;
    }

    public void setServerID(int serverID) {
        this.serverID = serverID;
    }

    public SFSObject toSFSObject(){
        SFSObject obj = new SFSObject();
        obj.putUtfString("accountID",this.accountID);
        obj.putInt("ID",this.ID);
        obj.putInt("roomID",this.roomID);
        obj.putInt("count",this.count);
        obj.putInt("type",this.type);
        obj.putUtfString("ip",this.ip);
        obj.putInt("serverID",this.serverID);
        obj.putInt("flopChickenType",this.flopChickenType);
        obj.putInt("bankerType",this.bankerType);
        return obj;
    }

    public static GBRoomCreateREQ fromSFSOBject(ISFSObject obj){
        GBRoomCreateREQ result = new GBRoomCreateREQ();
        result.setCount(obj.getInt("count"));
        result.setServerID(obj.getInt("serverID"));
        result.setType(obj.getInt("type"));
        result.setAccountID(obj.getUtfString("accountID"));
        result.setID(obj.getInt("ID"));
        result.setIp(obj.getUtfString("ip"));
        result.setRoomID(obj.getInt("roomID"));
        result.setFlopChickenType(obj.getInt("flopChickenType"));
        result.setBankerType(obj.getInt("bankerType"));
        return result;
    }

    public Map<Object,Object> toMap(){
        Map<Object,Object> res = new HashMap<Object, Object>();
        res.put("count",Integer.toString(this.count));
        res.put("serverID",Integer.toString(this.serverID));
        res.put("type",Integer.toString(this.type));
        res.put("accountID",accountID);
        res.put("ID",Integer.toString(this.ID));
        res.put("roomID",Integer.toString(this.roomID));
        res.put("ip",this.ip);
        res.put("flopChickenType",Integer.toString(this.flopChickenType));
        res.put("bankerType",Integer.toString(this.bankerType));
        return res;
    }

    public static GBRoomCreateREQ fromMap(Map<Object,Object> map){
        GBRoomCreateREQ result = new GBRoomCreateREQ();
        result.setCount(Integer.parseInt(map.get("count").toString()));
        result.setServerID(Integer.parseInt(map.get("serverID").toString()));
        result.setType(Integer.parseInt(map.get("type").toString()));
        result.setAccountID(map.get("accountID").toString());
        result.setID(Integer.parseInt(map.get("ID").toString()));
        result.setIp(map.get("ip").toString());
        result.setRoomID(Integer.parseInt(map.get("roomID").toString()));
        result.setFlopChickenType(Integer.parseInt(map.get("flopChickenType").toString()));
        result.setBankerType(Integer.parseInt(map.get("bankerType").toString()));
        return result;
    }


}
