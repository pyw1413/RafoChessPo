package com.kodgames.battle.entity.battle;

import com.smartfoxserver.v2.entities.data.SFSObject;

/**
 * Created by Administrator on 2016/9/17.
 */
public class BattleCensus {

    private int playerId;
    private int winSelf; // 自摸次数
    private int winOther; // 接炮次数
    private int discardOther; // 点炮次数
    private int kong; // 暗杠次数
    private int cealedKong; // 明杠次数
    private int point;      // 分数

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getWinSelf() {
        return winSelf;
    }

    public void setWinSelf(int winSelf) {
        this.winSelf = winSelf;
    }

    public int getWinOther() {
        return winOther;
    }

    public void setWinOther(int winOther) {
        this.winOther = winOther;
    }

    public int getDiscardOther() {
        return discardOther;
    }

    public void setDiscardOther(int discardOther) {
        this.discardOther = discardOther;
    }

    public int getKong() {
        return kong;
    }

    public void setKong(int kong) {
        this.kong = kong;
    }

    public int getCealedKong() {
        return cealedKong;
    }

    public void setCealedKong(int cealedKong) {
        this.cealedKong = cealedKong;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public SFSObject toSFSObject(){
        SFSObject obj = new SFSObject();

        obj.putInt("playerId", playerId );
        obj.putInt("winSelf", winSelf );
        obj.putInt("winOther", winOther );
        obj.putInt("discardOther", discardOther );
        obj.putInt("kong", kong );
        obj.putInt("cealedKong", cealedKong );
        obj.putInt("point", point );

        return obj;
    }
}
