package com.rafo.chess.model;

/**
 * Created by 亚文 on 2016/9/10.
 */
public class ScoreDetail {
    private  int playerId; //玩家ID
    private  int score; //得分
    private  int reasonItemEnum; //得分名目
    private  int points; //番数

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getReasonItemEnum() {
        return reasonItemEnum;
    }

    public void setReasonItemEnum(int reasonItemEnum) {
        this.reasonItemEnum = reasonItemEnum;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
