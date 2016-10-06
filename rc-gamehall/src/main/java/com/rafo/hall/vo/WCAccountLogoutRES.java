package com.rafo.hall.vo;

import com.smartfoxserver.v2.entities.data.SFSObject;

/**
 * Created by Administrator on 2016/9/24.
 */
public class WCAccountLogoutRES {
    private int result;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public SFSObject toObject() {
        SFSObject sfsObject = new SFSObject();
        sfsObject.putInt("result",result);
        return sfsObject;
    }
}
