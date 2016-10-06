package com.kodgames.battle.entity.chat;

/**
 * Created by Administrator on 2016/9/17.
 */
public class WBChatREQ {

    private int roomID ; // 用户所在的房间
    private String senderAccountID ; // 用户唯一标识
    private String content; // 聊天内容
    private long sendTime; // 发送时间

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public String getSenderAccountID() {
        return senderAccountID;
    }

    public void setSenderAccountID(String senderAccountID) {
        this.senderAccountID = senderAccountID;
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
}
