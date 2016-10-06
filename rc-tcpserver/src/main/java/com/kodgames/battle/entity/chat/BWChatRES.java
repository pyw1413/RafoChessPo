package com.kodgames.battle.entity.chat;

import com.smartfoxserver.v2.entities.data.SFSObject;

/**
 * Created by Administrator on 2016/9/17.
 */
public class BWChatRES {
    private int result;
    private String accountID;
    private int roomID ; // 用户所在的房间
    private int senderChair;   // 发送者ID
    private String content; // 聊天内容
    private long sendTime ; // 发送时间

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

    public int getSenderChair() {
        return senderChair;
    }

    public void setSenderChair(int senderChair) {
        this.senderChair = senderChair;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public SFSObject toSFSObject(){
        SFSObject obj = new SFSObject();
        obj.putInt("result",this.result);
        obj.putUtfString("accountID",this.accountID);
        obj.putInt("roomID",this.roomID);
        obj.putUtfString("content",this.content==null?"":this.content);
        obj.putInt("senderChair",this.senderChair);
        obj.putLong("sendTime",this.sendTime);
        return obj;
    }
}
