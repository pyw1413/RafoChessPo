package com.rafo.hall.vo;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.protocol.serialization.SerializableSFSType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 */
public class RESEmailList {
    private List<RESEmailDataPROTO> emailDate = new ArrayList<RESEmailDataPROTO>();

    public List<RESEmailDataPROTO> getEmailDate() {
        return emailDate;
    }

    public void setEmailDate(List<RESEmailDataPROTO> emailDate) {
        this.emailDate = emailDate;
    }

    public SFSObject toObject() {
        SFSObject obj = new SFSObject();

        if(emailDate.size() > 0){
            SFSArray arr = new SFSArray();
            for(RESEmailDataPROTO resEmailDataPROTO : emailDate){
                arr.addSFSObject(resEmailDataPROTO.toObject());
            }
            obj.putSFSArray("emailDate", arr);
        }
        return obj;
    }
}
