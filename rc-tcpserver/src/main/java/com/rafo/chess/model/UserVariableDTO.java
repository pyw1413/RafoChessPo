package com.rafo.chess.model;

import com.smartfoxserver.v2.entities.data.ISFSObject;

/**
 * Created by Administrator on 2016/9/12.
 */
public class UserVariableDTO {
    private String wid;
    private String nName;
    private int age;
    private String img;
    private boolean gender;
    private boolean status;
    private int sit;

    public String getWid() {
        return wid;
    }

    public String getnName() {
        return nName;
    }

    public int getAge() {
        return age;
    }

    public String getImg() {
        return img;
    }

    public boolean isGender() {
        return gender;
    }

    public boolean isStatus() {
        return status;
    }

    public int getSit() {
        return sit;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public void setnName(String nName) {
        this.nName = nName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setSit(int sit) {
        this.sit = sit;
    }
}
