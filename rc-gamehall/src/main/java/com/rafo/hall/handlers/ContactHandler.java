package com.rafo.hall.handlers;

import com.rafo.hall.common.GlobalConstants;
import com.rafo.hall.service.LogoutService;
import com.rafo.hall.utils.CmdsUtils;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.smartfoxserver.v2.extensions.SFSExtension;

/**
 * Created by Administrator on 2016/9/22.
 */
public class ContactHandler extends BaseClientRequestHandler{
    @Override
    public void handleClientRequest(User user, ISFSObject params) {
        ISFSObject respObj = new SFSObject();
        respObj.putInt("result", GlobalConstants.CONTACT_SUCCESS);
        respObj.putUtfString("content","欢迎联系代理：12145");
        SFSExtension sfs = getParentExtension();
        sfs.send(CmdsUtils.CMD_CONTACT , respObj , user);

        //FIXME for test
        sfs.send(CmdsUtils.CMD_LOGOUT , new LogoutService().Logout().toObject() ,user);
    }
}
