package com.rafo.hall.vo;

import com.smartfoxserver.v2.entities.data.SFSObject;

/**
 * Created by Administrator on 2016/9/23.
 */
public class UserVO {
    private int ID;
    private int result;

    private int total;
    private String sex;
    private String status;
    private int haveNewEmail;
    private String unionid;
    private String refreshToken;
    private String country;
    private String city;
    private String time;
    private String name;
    private String province;
    private String account;
    private int card;
    private int points;
    private String head;
    private int forbitTime;

    private String ip;
    private String port;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getHaveNewEmail() {
        return haveNewEmail;
    }

    public void setHaveNewEmail(int haveNewEmail) {
        this.haveNewEmail = haveNewEmail;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getCard() {
        return card;
    }

    public void setCard(int card) {
        this.card = card;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public int getForbitTime() {
        return forbitTime;
    }

    public void setForbitTime(int forbitTime) {
        this.forbitTime = forbitTime;
    }

    public SFSObject toObject() {
        SFSObject object = new SFSObject();
        object.putInt("ID" , ID);
        object.putInt("result",result);
        object.putInt("total",total);
        object.putUtfString("sex" , sex);
        object.putUtfString("status" , status);
        object.putInt("haveNewEmail" , haveNewEmail);
        object.putUtfString("unionid" , unionid);
        object.putUtfString("refreshToken" , refreshToken);
        object.putUtfString("country" , country);
        object.putUtfString("city" , city);
        object.putUtfString("time" , time);
        object.putUtfString("name" , name);

        object.putUtfString("province" , province);
        object.putUtfString("account",account);
        object.putInt("card",card);
        object.putInt("points" , points);
        object.putUtfString("head" , head);
        object.putInt("forbitTime" , forbitTime);
        object.putUtfString("ip" , ip);
        object.putUtfString("port" , port);
        return object;
    }
}
