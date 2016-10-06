package com.rafo.hall.vo;

import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/23.
 */
public class WCRoomRecordRES {
    private int result ;
    private List<RoomStatisticsPROTO> roomStatistics = new ArrayList<RoomStatisticsPROTO>();

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<RoomStatisticsPROTO> getRoomStatistics() {
        return roomStatistics;
    }

    public void setRoomStatistics(List<RoomStatisticsPROTO> roomStatistics) {
        this.roomStatistics = roomStatistics;
    }

    public SFSObject toObject() {
        SFSObject object = new SFSObject();
        object.putInt("result" , result);

        if(roomStatistics.size() > 0){
            SFSArray arr = new SFSArray();
            for(RoomStatisticsPROTO roomStatisticsPROTO : roomStatistics){
                arr.addSFSObject(roomStatisticsPROTO.toObject());
            }
            object.putSFSArray("roomStatistics", arr);
        }

        return object;
    }
}
