package com.rafo.hall.vo;

import com.smartfoxserver.v2.protocol.serialization.SerializableSFSType;

/**
 * Created by Administrator on 2016/9/22.
 */
public class WCAccountModifySYN{
    private int id;
    private int points;
    private int roomCard;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getRoomCard() {
        return roomCard;
    }

    public void setRoomCard(int roomCard) {
        this.roomCard = roomCard;
    }
}
