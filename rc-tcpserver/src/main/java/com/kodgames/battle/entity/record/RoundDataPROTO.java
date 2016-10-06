package com.kodgames.battle.entity.record;

import com.kodgames.battle.entity.battle.BattleData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/17.
 */
public class RoundDataPROTO {

    private int id;
    private long startTime ;
    private List<PlayerPointInfoPROTO> playerInfo = new ArrayList<>(); // 四名玩家在这一局的得分信息
    private BattleData battleData ; // 战斗回放

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public List<PlayerPointInfoPROTO> getPlayerInfo() {
        return playerInfo;
    }

    public void setPlayerInfo(List<PlayerPointInfoPROTO> playerInfo) {
        this.playerInfo = playerInfo;
    }

    public BattleData getBattleData() {
        return battleData;
    }

    public void setBattleData(BattleData battleData) {
        this.battleData = battleData;
    }

    public void addPlayerInfo(PlayerPointInfoPROTO playerInfoPROTO) {
        this.playerInfo.add(playerInfoPROTO);
    }
}
