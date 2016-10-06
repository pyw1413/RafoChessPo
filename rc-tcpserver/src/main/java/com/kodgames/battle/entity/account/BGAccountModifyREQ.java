package com.kodgames.battle.entity.account;

/**
 * Created by Administrator on 2016/9/17.
 */
public class BGAccountModifyREQ {

    private int id ;
    private String accountID ;
    private int points ;
    private boolean alterCard ;
    private int roomID ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean isAlterCard() {
        return alterCard;
    }

    public void setAlterCard(boolean alterCard) {
        this.alterCard = alterCard;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }
}
