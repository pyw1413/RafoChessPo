package com.rafo.hall.vo;

import com.smartfoxserver.v2.entities.data.SFSObject;

/**
 * Created by Administrator on 2016/9/23.
 */
public class WCHaveNewEmailSynRES {
    private int newNumber;

    public int getNewNumber() {
        return newNumber;
    }

    public void setNewNumber(int newNumber) {
        this.newNumber = newNumber;
    }

    public SFSObject toObject() {
        SFSObject object = new SFSObject();
        object.putInt("newNumber",newNumber);
        return object;
    }
}
