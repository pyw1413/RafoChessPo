package com.rafo.hall.vo;

import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.protocol.serialization.SerializableSFSType;

/**
 * Created by Administrator on 2016/9/22.
 */
public class RESEmailDataPROTO {
    private String emailDate;
    private String emailContent;

    public String getEmailDate() {
        return emailDate;
    }

    public void setEmailDate(String emailDate) {
        this.emailDate = emailDate;
    }

    public String getEmailContent() {
        return emailContent;
    }

    public void setEmailContent(String emailContent) {
        this.emailContent = emailContent;
    }

    public SFSObject toObject() {
        SFSObject object = new SFSObject();
        object.putUtfString("emailDate" , emailDate);
        object.putUtfString("emailContent" , emailContent);
        return object;
    }
}
