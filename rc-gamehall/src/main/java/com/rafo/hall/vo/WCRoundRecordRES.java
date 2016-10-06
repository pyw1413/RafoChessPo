package com.rafo.hall.vo;

import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/23.
 */
public class WCRoundRecordRES {
    private int result;
    private List<RoundDataPROTO> roundData = new ArrayList<RoundDataPROTO>();

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<RoundDataPROTO> getRoundData() {
        return roundData;
    }

    public void setRoundData(List<RoundDataPROTO> roundData) {
        this.roundData = roundData;
    }

    public SFSObject toObject() {
        SFSObject object = new SFSObject();
        object.putInt("result",result);

        if (roundData.size() > 0) {
            SFSArray arr = new SFSArray();
            for(RoundDataPROTO data : roundData) {
                arr.addSFSObject(data.toObject());
            }
            object.putSFSArray("roundData",arr);
        }
        return object;
    }
}
