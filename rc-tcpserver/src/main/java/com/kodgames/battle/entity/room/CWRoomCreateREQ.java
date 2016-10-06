package com.kodgames.battle.entity.room;

/**
 * Created by Administrator on 2016/9/17.
 */
public class CWRoomCreateREQ {

    private int count ; // 房间容纳局数对应的索引 0 - 8，1 - 16
    private int type ; // 玩法类型

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
