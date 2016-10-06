package com.kodgames.battle.entity.record;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/17.
 */
public class BGRecordLogSYN {
    private int roomID ;
    private long roomStartTime ;
    private int roomType ;
    private List<Integer> playerIDs = new ArrayList<>();
    private int ownerID ;
    private boolean isFinished ;
    private long roundRecordStartTime ;
    private int currRoundCount ;

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public long getRoomStartTime() {
        return roomStartTime;
    }

    public void setRoomStartTime(long roomStartTime) {
        this.roomStartTime = roomStartTime;
    }

    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }

    public List<Integer> getPlayerIDs() {
        return playerIDs;
    }

    public void setPlayerIDs(List<Integer> playerIDs) {
        this.playerIDs = playerIDs;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public long getRoundRecordStartTime() {
        return roundRecordStartTime;
    }

    public void setRoundRecordStartTime(long roundRecordStartTime) {
        this.roundRecordStartTime = roundRecordStartTime;
    }

    public int getCurrRoundCount() {
        return currRoundCount;
    }

    public void setCurrRoundCount(int currRoundCount) {
        this.currRoundCount = currRoundCount;
    }

    public void addAllPlayerIDs(List<Integer> players) {
        this.playerIDs.addAll(players);
    }
}
