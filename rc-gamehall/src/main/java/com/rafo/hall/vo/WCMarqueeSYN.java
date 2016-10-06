package com.rafo.hall.vo;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.protocol.serialization.SerializableSFSType;

/**
 * Created by Administrator on 2016/9/22.
 */
public class WCMarqueeSYN {
    private String content;
    private int rollTimes;
    private String color;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRollTimes() {
        return rollTimes;
    }

    public void setRollTimes(int rollTimes) {
        this.rollTimes = rollTimes;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public SFSObject toObject() {
        SFSObject object = new SFSObject();
        object.putUtfString("content" , content);
        object.putInt("rollTimes" , rollTimes);
        object.putUtfString("color",color);
        return object;
    }
}
