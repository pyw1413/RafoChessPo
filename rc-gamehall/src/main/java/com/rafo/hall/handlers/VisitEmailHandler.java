package com.rafo.hall.handlers;

import com.rafo.hall.utils.CmdsUtils;
import com.rafo.hall.vo.RESEmailDataPROTO;
import com.rafo.hall.vo.RESEmailList;
import com.rafo.hall.vo.WCVisitEmailRES;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.smartfoxserver.v2.extensions.SFSExtension;

/**
 * Created by Administrator on 2016/9/22.
 */
public class VisitEmailHandler extends BaseClientRequestHandler{
    @Override
    public void handleClientRequest(User user, ISFSObject isfsObject) {
        WCVisitEmailRES wcVisitEmailRES = new WCVisitEmailRES();
        RESEmailDataPROTO resEmailDataPROTO = new RESEmailDataPROTO();
        resEmailDataPROTO.setEmailContent("this is an email");
        resEmailDataPROTO.setEmailDate("2016-09-22 15:22:00");
        RESEmailList resEmailList = new RESEmailList();
        resEmailList.getEmailDate().add(resEmailDataPROTO);
        resEmailList.getEmailDate().add(resEmailDataPROTO);
        resEmailList.getEmailDate().add(resEmailDataPROTO);
        wcVisitEmailRES.setEmailList(resEmailList);
        wcVisitEmailRES.setResult(0);
        wcVisitEmailRES.setLastLoginTime("2016-09-22 15:22:00");

        SFSExtension sfs = getParentExtension();
        sfs.send(CmdsUtils.CMD_WCVISITEMAIL , wcVisitEmailRES.toSFSObject() , user);
    }
}
