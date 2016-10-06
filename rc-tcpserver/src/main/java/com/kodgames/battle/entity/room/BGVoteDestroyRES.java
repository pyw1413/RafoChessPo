package com.kodgames.battle.entity.room;

import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/17.
 */
public class BGVoteDestroyRES implements Cloneable {

    private int result ; // 解散结果: 投票中 解散成功 有人拒绝 已经有人发起解散 不在房间 已经投过票
    private String accountID; // 用于获取GateWay的Node
    private int roomID;
    private List<VoteInfoPROTO> playerVoteInfo = new ArrayList<>();
    private String remainTime;

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

    public List<VoteInfoPROTO> getPlayerVoteInfo() {
        return playerVoteInfo;
    }

    public void setPlayerVoteInfo(List<VoteInfoPROTO> playerVoteInfo) {
        this.playerVoteInfo = playerVoteInfo;
    }

    public String getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(String remainTime) {
        this.remainTime = remainTime;
    }

    public void addPlayerVoteInfo(VoteInfoPROTO voteInfo) {
        this.playerVoteInfo.add(voteInfo);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return (BGVoteDestroyRES)super.clone();
    }

    public SFSObject toSFSObject(){
        SFSObject obj = new SFSObject();
        obj.putInt("result",this.result);
        obj.putUtfString("accountID",this.accountID);
        obj.putInt("roomID",this.roomID);
        if(this.playerVoteInfo != null){
            if(this.playerVoteInfo.size()>0){
                SFSArray arr = new SFSArray();
                for(VoteInfoPROTO v:playerVoteInfo){
                    arr.addSFSObject(v.toSFSObject());

                }
                obj.putSFSArray("playerVoteInfo",arr);
            }
        }

        obj.putUtfString("remainTime",this.remainTime==null?"":this.remainTime);
        return obj;

    }

}
