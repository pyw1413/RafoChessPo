package com.kodgames.battle.entity.battle;

import com.smartfoxserver.v2.entities.data.SFSObject;

/**
 * Created by Administrator on 2016/9/17.
 */
public class BattlePlayerStatus {

    private int playerId ;
    private int status ; // 0:未准备 1:已准备, 2:开始战斗了
    private int points;
    private boolean isOffline ; // 是否离线

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean isOffline() {
        return isOffline;
    }

    public void setOffline(boolean offline) {
        isOffline = offline;
    }

    public SFSObject toSFSObject(){
        SFSObject obj = new SFSObject();
        obj.putInt("playerId",this.playerId);
        obj.putInt("status",this.status);
        obj.putInt("points",this.points);
        obj.putBool("isOffline",this.isOffline());
        return obj;
    }
}
