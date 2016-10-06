package com.rafo.hall.vo;

import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.protocol.serialization.SerializableSFSType;

/**
 * Created by Administrator on 2016/9/22.
 */
public class WCVisitEmailRES {
    private int result;
    private String lastLoginTime;
    private RESEmailList emailList;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public RESEmailList getEmailList() {
        return emailList;
    }

    public void setEmailList(RESEmailList emailList) {
        this.emailList = emailList;
    }

    public SFSObject toSFSObject(){
        SFSObject obj = new SFSObject();
        obj.putInt("result" , result);
        obj.putUtfString("lastLoginTime" , lastLoginTime);
        obj.putSFSObject("emailList" , emailList.toObject());
        return obj;
    }
}
