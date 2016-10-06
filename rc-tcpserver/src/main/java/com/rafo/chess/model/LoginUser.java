package com.rafo.chess.model;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/9/13.
 */
public class LoginUser implements Serializable {
    private int id;
    private String account;
    private String name;
    private String province;
    private String country;
    private String city;
    private String head;
    private String sex;//1男2女
    private String refreshToken;
    private int haveNewEmail;//消息数量
    private int card;
    private int points;
    private String forbitTime;
    private int room;
    private String ip;
    private long timestamp;
    private int status;
    private int total;
    private String token;
    private long expire;
    private String unionid;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }
    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public int getHaveNewEmail() {
        return haveNewEmail;
    }

    public void setHaveNewEmail(int haveNewEmail) {
        this.haveNewEmail = haveNewEmail;
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

    public String getForbitTime() {
        return forbitTime;
    }

    public void setForbitTime(String forbitTime) {
        this.forbitTime = forbitTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
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

    public int getRoom() {
        return room;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public LoginUser() {
    }

    public LoginUser(User user) {

        ISFSObject obj = (SFSObject)user.getProperty("sfsobj");
        this.id = obj.getInt("ID");
        this.name = obj.getUtfString("name");
        this.province = obj.getUtfString("province");
        this.country = obj.getUtfString("country");
        this.city =  obj.getUtfString("province");
        this.head =  obj.getUtfString("head");
        this.sex =  obj.getUtfString("sex");
        this.card = obj.getInt("card");
        this.room = obj.getInt("room");
        this.ip = user.getIpAddress();
        this.timestamp = user.getLoginTime();
        this.status = obj.getInt("status");
        this.account = obj.getUtfString("account");
        this.refreshToken = obj.getUtfString("refreshToken");
        this.haveNewEmail = obj.getInt("haveNewEmail");
        this.points = obj.getInt("points");
        this.total = obj.getInt("total");
        this.unionid = obj.getUtfString("unionid");

    }

    public HashMap<String,String> toStrMap(){
        HashMap<String,String> result = new HashMap<String, String>();
        result.put("ID",this.id+"");
        result.put("name",this.name==null?"":this.name);
        result.put("province",this.province==null?"":this.province);
        result.put("country",this.country==null?"":this.country);
        result.put("city",this.city==null?"":this.city);
        result.put("head",this.head==null?"":this.head);
        result.put("sex",this.sex==null?"":this.sex);
        result.put("card",Integer.toString(this.card));
        result.put("room",Integer.toString(this.room));
        result.put("ip",this.ip==null?"":this.ip);
        result.put("time",Long.toString(this.timestamp));
        result.put("status",Integer.toString(this.status));
        result.put("account",this.account==null?"":this.account);
        result.put("refreshToken",this.refreshToken==null?"":this.refreshToken);
        result.put("haveNewEmail",Integer.toString(this.haveNewEmail));
        result.put("points",Integer.toString(this.points));
        result.put("total",Integer.toString(this.total));
        result.put("unionid",this.unionid);

        return result;
    }

    public SFSObject toSFSObject(){
        SFSObject obj = new SFSObject();
        obj.putInt("ID",this.id);
        obj.putUtfString("name",this.name==null?"":this.name);
        obj.putUtfString("province",this.province==null?"":this.province);
        obj.putUtfString("country",this.country==null?"":this.country);
        obj.putUtfString("city",this.city==null?"":this.city);
        obj.putUtfString("head",this.head==null?"":this.head);
        obj.putUtfString("sex",this.sex==null?"1":this.sex);
        obj.putInt("room",this.room);
        obj.putInt("card",this.card);
        obj.putUtfString("ip",this.ip);
        obj.putLong("time",this.timestamp);
        obj.putInt("points",this.points);
        obj.putUtfString("account",this.account);
        obj.putInt("haveNewEmail",this.haveNewEmail);
        obj.putInt("status",this.status);
        obj.putUtfString("refreshToken",this.refreshToken==null?"":this.refreshToken);
        obj.putInt("total",this.total);
       obj.putUtfString("unionid",this.unionid==null?"":this.unionid);

        return obj;
    }

    public LoginUser fromStrMap(HashMap<String,String> map){
        LoginUser loginUser = new LoginUser();
        loginUser.setId(Integer.parseInt(map.get("ID")));
        loginUser.setName(map.get("name"));
        loginUser.setProvince(map.get("province"));
        loginUser.setCountry(map.get("country"));
        loginUser.setCity(map.get("city"));
        loginUser.setHead(map.get("head"));
        loginUser.setIp(map.get("ip"));
        loginUser.setSex(map.get("sex"));
        loginUser.setRoom(Integer.parseInt(map.get("room")));
        loginUser.setCard(Integer.parseInt(map.get("card")));
        loginUser.setTimestamp(Long.parseLong(map.get("time")));
        loginUser.setStatus(Integer.parseInt(map.get("status")));
        loginUser.setAccount(map.get("account"));
        loginUser.setRefreshToken(map.get("refreshToken"));
        loginUser.setHaveNewEmail(Integer.parseInt(map.get("haveNewEmail")));
        loginUser.setPoints(Integer.parseInt(map.get("points")));
        loginUser.setUnionid(map.get("unionid"));
        return loginUser;
    }
}
