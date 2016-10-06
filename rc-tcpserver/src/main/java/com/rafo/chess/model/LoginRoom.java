package com.rafo.chess.model;


import com.kodgames.battle.common.Room;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/9/13.
 */
public class LoginRoom implements Serializable {
    private int roomID;
    private String ownerAccountID;
    private int roundTotal; //总局数
    private int battleTime; // 已经战斗局数
    private int playType; // 玩法类型
    private long createTime;
    private int roomType; // 房间的类型，0或者1
    private boolean isInBattle; // 是否处于打牌中
    private int serverId;

    public boolean isInBattle() {
        return isInBattle;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public String getOwnerAccountID() {
        return ownerAccountID;
    }

    public void setOwnerAccountID(String ownerAccountID) {
        this.ownerAccountID = ownerAccountID;
    }

    public int getRoundTotal() {
        return roundTotal;
    }

    public void setRoundTotal(int roundTotal) {
        this.roundTotal = roundTotal;
    }

    public int getBattleTime() {
        return battleTime;
    }

    public void setBattleTime(int battleTime) {
        this.battleTime = battleTime;
    }

    public int getPlayType() {
        return playType;
    }

    public void setPlayType(int playType) {
        this.playType = playType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }

    public void setInBattle(boolean inBattle) {
        isInBattle = inBattle;
    }

    public LoginRoom() {
    }

    public LoginRoom(Room room,int serverId) {
        this.battleTime = room.getBattleTime();
        this.createTime = room.getCreateTime();
        this.isInBattle = room.isInBattle();
        this.ownerAccountID = room.getOwner();
        this.playType = room.getType();
        this.roomID = room.getRoomID();
        this.roomType = room.getRoomType();
        this.roundTotal = room.getBattleCount();
        this.serverId = serverId;
    }

    public HashMap<String,String> toStrMap(){
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("bt",Integer.toString(this.battleTime));
        map.put("ct",Long.toString(this.createTime));
        map.put("isb",this.isInBattle?"T":"F");
        map.put("ownId",this.ownerAccountID==null?"":this.ownerAccountID);
        map.put("pt",Integer.toString(this.playType));
        map.put("rmId",Integer.toString(this.roomID));
        map.put("rmt",Integer.toString(this.roomType));
        map.put("rndt",Integer.toString(this.roundTotal));
        map.put("serId",Integer.toString(this.serverId));

        return map;
    }

    public LoginRoom fromStrMap(HashMap<String,String> map){
        LoginRoom loginRoom = new LoginRoom();
        loginRoom.setBattleTime(Integer.parseInt(map.get("bt")));
        loginRoom.setCreateTime(Long.parseLong(map.get("ct")));
        loginRoom.setInBattle("T".equals(map.get("isb"))?true:false);
        loginRoom.setOwnerAccountID(map.get("ownId"));
        loginRoom.setPlayType(Integer.parseInt(map.get("pt")));
        loginRoom.setRoomID(Integer.parseInt(map.get("rmId")));
        loginRoom.setRoomType(Integer.parseInt(map.get("rmt")));
        loginRoom.setRoundTotal(Integer.parseInt(map.get("rndt")));
        loginRoom.setServerId(Integer.parseInt(map.get("serId")));
        return loginRoom;
    }
}
