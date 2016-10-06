package com.rafo.chess.model;

/**
 * Created by Administrator on 2016/9/27.
 */
public class GameServer {
    private String ip;
    private int port;
    private int id;
    private int maxUser;

    public GameServer(String ip, int port, int id, int maxUser) {
        this.ip = ip;
        this.port = port;
        this.id = id;
        this.maxUser = maxUser;
    }


    public GameServer() {
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMaxUser() {
        return maxUser;
    }

    public void setMaxUser(int maxUser) {
        this.maxUser = maxUser;
    }
}
