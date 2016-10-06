package com.kodgames.battle.entity.room;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/17.
 */
// 达成条件，服务器主动解散房间
public class BGAutoDestroySYN {

    private int result ;
    private List<String> accountIDs = new ArrayList<>();
    private String ownerAccountID ;
    private int roomID;
    private int roomCount;
    private int roomType;
    private String ip;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<String> getAccountIDs() {
        return accountIDs;
    }

    public void setAccountIDs(List<String> accountIDs) {
        this.accountIDs = accountIDs;
    }

    public String getOwnerAccountID() {
        return ownerAccountID;
    }

    public void setOwnerAccountID(String ownerAccountID) {
        this.ownerAccountID = ownerAccountID;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public int getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(int roomCount) {
        this.roomCount = roomCount;
    }

    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void addAccountIDs(String accountID) {
        this.accountIDs.add(accountID);
    }
}
