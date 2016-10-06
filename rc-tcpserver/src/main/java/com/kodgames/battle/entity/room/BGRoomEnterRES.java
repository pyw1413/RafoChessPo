package com.kodgames.battle.entity.room;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/17.
 */
public class BGRoomEnterRES {
    private int result ;
    private String applierAccountID ;
    private int roomID ;
    private List<PlayerInfoSSPROTO> playerInfo = new ArrayList<>(); // 服务器间通信用的玩家信息
    private BGVoteDestroyRES bgVoteDestroyRES ;
    private int roomType ; // 房间类型， 0,1
    private int currentBattleCount ; // 当前战斗次数
    private int applierID ; // 申请者的玩家id

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getApplierAccountID() {
        return applierAccountID;
    }

    public void setApplierAccountID(String applierAccountID) {
        this.applierAccountID = applierAccountID;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public List<PlayerInfoSSPROTO> getPlayerInfo() {
        return playerInfo;
    }

    public void setPlayerInfo(List<PlayerInfoSSPROTO> playerInfo) {
        this.playerInfo = playerInfo;
    }

    public BGVoteDestroyRES getBgVoteDestroyRES() {
        return bgVoteDestroyRES;
    }

    public void setBgVoteDestroyRES(BGVoteDestroyRES bgVoteDestroyRES) {
        this.bgVoteDestroyRES = bgVoteDestroyRES;
    }

    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }

    public int getCurrentBattleCount() {
        return currentBattleCount;
    }

    public void setCurrentBattleCount(int currentBattleCount) {
        this.currentBattleCount = currentBattleCount;
    }

    public int getApplierID() {
        return applierID;
    }

    public void setApplierID(int applierID) {
        this.applierID = applierID;
    }

    public void addPlayerInfo(PlayerInfoSSPROTO playerSSInfo) {
        this.playerInfo.add(playerSSInfo);
    }

    public ISFSObject toSFSObject(){
        ISFSObject resp = new SFSObject();
        resp.putUtfString("aaid",this.getApplierAccountID());
        resp.putInt("aid",this.getApplierID());

        if(this.getBgVoteDestroyRES() != null){
            BGVoteDestroyRES voteRES = this.getBgVoteDestroyRES();

            ISFSObject vote = new SFSObject();
            vote.putUtfString("id",voteRES.getAccountID());
            if(voteRES.getPlayerVoteInfo() != null){
                List<VoteInfoPROTO> voteInfoList = voteRES.getPlayerVoteInfo();
                if(voteInfoList.size() > 0){
                    SFSArray voteInfoArry = new SFSArray();
                    for(VoteInfoPROTO info:voteInfoList){
                        SFSObject obj = new SFSObject();
                        obj.putInt("pid",info.getPlayerID());
                        obj.putInt("res",info.getVoteResult());
                        voteInfoArry.addSFSObject(obj);
                    }
                    vote.putSFSArray("voteArr",voteInfoArry);
                }
            }
            vote.putUtfString("rt",voteRES.getRemainTime());
            vote.putInt("res",voteRES.getResult());
            vote.putInt("roomId",voteRES.getRoomID());


            resp.putSFSObject("vote",vote);
        }

        resp.putInt("roomId",this.getRoomID());
        resp.putInt("bt",this.getCurrentBattleCount());
        resp.putInt("res",this.getResult());
        resp.putInt("roomType",this.getRoomType());
        if(this.getPlayerInfo() != null){
            List<PlayerInfoSSPROTO> playerInfoList = this.getPlayerInfo();
            if(playerInfoList.size() > 0 ){
                SFSArray playerInfoArr = new SFSArray();
                for(PlayerInfoSSPROTO player:playerInfoList){
                    SFSObject obj = new SFSObject();
                    obj.putInt("pid",player.getPlayerID());
                    obj.putInt("cha",player.getChair());
                    obj.putInt("room",player.getRoom());
                    obj.putInt("stat",player.getStatus());
                    obj.putUtfString("aid",player.getAccountID());
                    obj.putUtfString("img",player.getHead());
                    obj.putUtfString("ip",player.getIp());
                    obj.putUtfString("name",player.getName());
                    obj.putUtfString("gender",player.getSex());
                    playerInfoArr.addSFSObject(obj);
                }
                resp.putSFSArray("players",playerInfoArr);
            }
        }
        return resp;
    }

    public SFSObject toRoomEnterResSFSObj(User user){
        SFSObject res = new SFSObject();
        res.putInt("result",this.result);
        res.putInt("chair",getChair(user));
        if(this.getPlayerInfo() != null){
            List<PlayerInfoSSPROTO> playerInfoList = this.getPlayerInfo();
            if(playerInfoList.size() > 0 ){
                SFSArray playerInfoArr = new SFSArray();
                for(PlayerInfoSSPROTO player:playerInfoList){
                    SFSObject obj = new SFSObject();
                    obj.putInt("playerID",player.getPlayerID());
                    obj.putInt("chair",player.getChair());
                    obj.putInt("status",player.getStatus());
                    obj.putUtfString("head",player.getHead());
                    obj.putUtfString("ip",player.getIp());
                    obj.putUtfString("name",player.getName());
                    obj.putUtfString("sex",player.getSex());
                    obj.putBool("isOffline",player.isOffline());
                    playerInfoArr.addSFSObject(obj);
                }
                res.putSFSArray("playerInfo",playerInfoArr);
            }
        }
        res.putInt("roomType",this.roomType);
        res.putInt("roomID",this.roomID);
        res.putInt("flopChickenType",0);
        res.putInt("bankerType",0);
        res.putInt("currentBattleCount",this.currentBattleCount);
        res.putInt("applierID",this.applierID);

        return res;
    }

    public int getChair(User user){
        for(PlayerInfoSSPROTO p:this.playerInfo){
            if(p.getPlayerID() == Integer.parseInt(user.getName())){
                return p.getChair();
            }
        }
        return 0;
    }
}
