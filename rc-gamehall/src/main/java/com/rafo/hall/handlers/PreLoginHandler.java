package com.rafo.hall.handlers;

import com.bbzhu.cache.Cache;
import com.rafo.hall.common.GlobalConstants;
import com.rafo.hall.utils.CmdsUtils;
import com.smartfoxserver.bitswarm.sessions.ISession;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.entities.variables.SFSUserVariable;
import com.smartfoxserver.v2.entities.variables.UserVariable;
import com.smartfoxserver.v2.exceptions.SFSErrorCode;
import com.smartfoxserver.v2.exceptions.SFSErrorData;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.exceptions.SFSLoginException;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import com.rafo.hall.manager.RedisManager;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
import com.smartfoxserver.v2.extensions.SFSExtension;
import com.www.model.Server;

/**
 * Created by Administrator on 2016/9/18.
 */
public class PreLoginHandler extends BaseServerEventHandler {



    @Override
    public void handleServerEvent(ISFSEvent isfsEvent) throws SFSException {
        String uid = (String)isfsEvent.getParameter(SFSEventParam.LOGIN_NAME);
        String password = (String)isfsEvent.getParameter(SFSEventParam.LOGIN_PASSWORD);
        ISession session = (ISession) isfsEvent.getParameter(SFSEventParam.SESSION);

        String value = RedisManager.getInstance().hGet("uid." + uid , "status");

        User oldUser = getParentExtension().getParentZone().getUserManager().getUserByName(uid);
        if(oldUser != null && value != null){
            getApi().logout(oldUser);
            SFSObject obj = new SFSObject();
            obj.putInt("result", GlobalConstants.LOGOUT_KICKOFF);
            obj.putUtfString("msg","your account login in other place");
            getParentExtension().send(CmdsUtils.SFS_EVENT_ACCOUNT_LOGOUT,obj,oldUser);
        }
       /* if (value.trim().equalsIgnoreCase("1")) {
            SFSErrorData errData = new SFSErrorData(SFSErrorCode.LOGIN_ALREADY_LOGGED);
            errData.addParameter(uid);
            throw new SFSLoginException("Relogin uid : " + uid, errData);
        }*/
        RedisManager.getInstance().getRedis().hset("uid." + uid , "status" , "1");

        String token = RedisManager.getInstance().hGet("uid." + uid , "token");
        boolean tokenOK = false;
        if (token != null && !token.equals("")) {
            String expireStr = RedisManager.getInstance().hGet("uid." + uid , "expire");
            if (expireStr != null && !expireStr.equals("")){
                Long expire = Long.valueOf(expireStr);
                if (expire > System.currentTimeMillis()) {

                    if (getApi().checkSecurePassword(session, token, password))
                    {
                        session.setProperty("uid",uid);
                        tokenOK = true;
                    }
                }
            }
        }

        if (!tokenOK) {
            SFSErrorData data = new SFSErrorData(SFSErrorCode.LOGIN_BAD_PASSWORD);

            throw new SFSLoginException("Token error uid: "  + uid, data);
        }


    }
}
