package com.kodgames.battle.entity.record;

import com.kodgames.battle.entity.record.RoomStatisticsPROTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/17.
 */
public class BWRoomRecordRES {

    private int result ;
    private String accountID ;
    private List<RoomStatisticsPROTO> roomBattleStatistics = new ArrayList<>();

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public List<RoomStatisticsPROTO> getRoomBattleStatistics() {
        return roomBattleStatistics;
    }

    public void setRoomBattleStatistics(List<RoomStatisticsPROTO> roomBattleStatistics) {
        this.roomBattleStatistics = roomBattleStatistics;
    }

    public void addRoomBattleStatistics(RoomStatisticsPROTO roomStatisticsPROTO) {
        this.roomBattleStatistics.add(roomStatisticsPROTO);
    }
}
