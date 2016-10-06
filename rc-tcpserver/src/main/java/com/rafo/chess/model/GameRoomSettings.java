package com.rafo.chess.model;

/**
 * 游戏房间设置类
 * Created by 亚文 on 2016/9/8.
 */
public class GameRoomSettings {
        private int password;
        private int gameType;
        private int playType;
        private String playTypeExt1;
        private String playTypeExt2;
        private int roomSize;
        private int totalRounds;
        private int currRounds;
        private int roomId;
        private int roomType;
        private int ownerId;
    public GameRoomSettings(){


    }

    public int getCurrRounds() {
        return currRounds;
    }

    public void setCurrRounds(int currRounds) {
        this.currRounds = currRounds;
    }

    public GameRoomSettings( int password, int gameType, int playType, String playTypeExt1, String playTypeExt2, int roomSize, int totalRounds, int currRounds){
        this.password = password;
        this.gameType = gameType;
        this.playType = playType;
        this.playTypeExt1 = playTypeExt1;
        this.playTypeExt2 = playTypeExt2;
        this.roomSize = roomSize;
        this.totalRounds = totalRounds;

        this.currRounds = currRounds;
    }


    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public int getGameType() {
        return gameType;
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
    }

    public int getPlayType() {
        return playType;
    }

    public void setPlayType(int playType) {
        this.playType = playType;
    }

    public String getPlayTypeExt1() {
        return playTypeExt1;
    }

    public void setPlayTypeExt1(String playTypeExt1) {
        this.playTypeExt1 = playTypeExt1;
    }

    public String getPlayTypeExt2() {
        return playTypeExt2;
    }

    public void setPlayTypeExt2(String playTypeExt2) {
        this.playTypeExt2 = playTypeExt2;
    }

    public int getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(int roomSize) {
        this.roomSize = roomSize;
    }

    public int getTotalRounds() {
        return totalRounds;
    }

    public void setTotalRounds(int totalRounds) {
        this.totalRounds = totalRounds;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
}
