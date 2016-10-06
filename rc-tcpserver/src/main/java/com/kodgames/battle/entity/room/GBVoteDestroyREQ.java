package com.kodgames.battle.entity.room;

/**
 * 玩牌中，发起申请解散房间或就解散房间投票
 */
public class GBVoteDestroyREQ {

    private String accountID ;
    private int roomID ;
    private int voteResult ; // 0 : 同意 1：拒绝 2：发起

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

    public int getVoteResult() {
        return voteResult;
    }

    public void setVoteResult(int voteResult) {
        this.voteResult = voteResult;
    }
}
