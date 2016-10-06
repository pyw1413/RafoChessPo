package com.rafo.hall.vo;

import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/23.
 */
public class RoundDataPROTO {
    private int id;
    private long startTime;
    private List<PlayerPointInfoPROTO> playerInfo = new ArrayList<PlayerPointInfoPROTO>();

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

    public SFSObject toObject() {
        SFSObject object = new SFSObject();
        object.putInt("id" , id);
        object.putLong("startTime" , startTime);

        if (playerInfo.size() > 0) {
            SFSArray arr = new SFSArray();
            for(PlayerPointInfoPROTO info : playerInfo) {
                arr.addSFSObject(info.toObject());
            }
            object.putSFSArray("playerInfo",arr);
        }

        return object;
    }
}
