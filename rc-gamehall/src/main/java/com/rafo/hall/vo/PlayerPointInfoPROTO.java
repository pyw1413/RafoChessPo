package com.rafo.hall.vo;

import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.protocol.serialization.SerializableSFSType;

/**
 * Created by Administrator on 2016/9/17.
 */
public class PlayerPointInfoPROTO {

    private int playerID ; // 用户ID，非accountID
    private String nickName ; // 用户昵称
    private int chair ; // 用户座位
    private int point ; // 得分，可正可负
    private String head;

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

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public SFSObject toObject() {
        SFSObject object = new SFSObject();
        object.putInt("playerID" , playerID);
        object.putUtfString("nickName" , nickName);
        object.putInt("chair",chair);
        object.putInt("point",point);
        object.putUtfString("head",head);
        return object;
    }
}
