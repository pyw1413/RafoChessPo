package com.kodgames.battle.entity.record;

/**
 * Created by Administrator on 2016/9/17.
 */
public class PlayerPointInfoPROTO {

    private int playerID ; // 用户ID，非accountID
    private String nickName ; // 用户昵称
    private int chair ; // 用户座位
    private int point ; // 得分，可正可负

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getChair() {
        return chair;
    }

    public void setChair(int chair) {
        this.chair = chair;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
