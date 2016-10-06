package com.kodgames.battle.entity.room;

import com.smartfoxserver.v2.entities.data.SFSObject;

/**
 *  玩家投票信息
 */
public class VoteInfoPROTO {
    private int playerID = 1; // 玩家ID
    private int voteResult = 2; // 0 : 同意 1：拒绝 2：发起

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public int getVoteResult() {
        return voteResult;
    }

    public void setVoteResult(int voteResult) {
        this.voteResult = voteResult;
    }

    public SFSObject toSFSObject(){
        SFSObject obj = new SFSObject();
        obj.putInt("playerID",this.playerID);
        obj.putInt("voteResult",this.voteResult);
        return obj;
    }
}
