package com.rafo.hall.vo;

import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.protocol.serialization.SerializableSFSType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/17.
 */
// 一个房间历史战绩统计，包含四个玩家的得分，名称等
public class RoomStatisticsPROTO{
    private int recordID;
    private long startTime ;
    private int roomID ;
    private List<PlayerPointInfoPROTO> playerInfo = new ArrayList<>(); // 四个玩家在房间内的总积分统计
    public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public List<PlayerPointInfoPROTO> getPlayerInfo() {
        return playerInfo;
    }

    public void setPlayerInfo(List<PlayerPointInfoPROTO> playerInfo) {
        this.playerInfo = playerInfo;
    }

    public void addPlayerInfo(PlayerPointInfoPROTO playerPointInfoPROTO) {
        this.playerInfo.add(playerPointInfoPROTO);
    }

    public SFSObject toObject() {
        SFSObject object = new SFSObject();
        object.putInt("recordID" , recordID);
        object.putLong("startTime" , startTime);
        object.putInt("roomID" , roomID);

        if(playerInfo.size() > 0){
            SFSArray arr = new SFSArray();
            for(PlayerPointInfoPROTO playerPointInfoPROTO : playerInfo){
                arr.addSFSObject(playerPointInfoPROTO.toObject());
            }
            object.putSFSArray("playerInfo", arr);
        }
        return object;
    }
}
