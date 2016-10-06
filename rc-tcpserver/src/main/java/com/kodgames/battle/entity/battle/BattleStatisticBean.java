package com.kodgames.battle.entity.battle;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/17.
 */
public class BattleStatisticBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int UID;
    private int roomID;
    private int playerID1;
    private int playerID2;
    private int playerID3;
    private int playerID4;
    private int ownerID;
    private int roomType;
    private int isFinish;
    private byte[] room_statistic;

    public int getUID() {
        return UID;
    }

    public void setUID(int UID) {
        this.UID = UID;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public int getPlayerID1() {
        return playerID1;
    }

    public void setPlayerID1(int playerID1) {
        this.playerID1 = playerID1;
    }

    public int getPlayerID2() {
        return playerID2;
    }

    public void setPlayerID2(int playerID2) {
        this.playerID2 = playerID2;
    }

    public int getPlayerID3() {
        return playerID3;
    }

    public void setPlayerID3(int playerID3) {
        this.playerID3 = playerID3;
    }

    public int getPlayerID4() {
        return playerID4;
    }

    public void setPlayerID4(int playerID4) {
        this.playerID4 = playerID4;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }

    public int getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(int isFinish) {
        this.isFinish = isFinish;
    }

    public byte[] getRoom_statistic() {
        return room_statistic;
    }

    public void setRoom_statistic(byte[] room_statistic) {
        this.room_statistic = room_statistic;
    }
}
