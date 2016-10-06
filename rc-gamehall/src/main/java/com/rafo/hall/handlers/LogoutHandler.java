package com.rafo.hall.handlers;

import com.rafo.hall.manager.RedisManager;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

/**
 * Created by Administrator on 2016/9/27.
 */
public class LogoutHandler extends BaseServerEventHandler {
    @Override
    public void handleServerEvent(ISFSEvent isfsEvent) throws SFSException {
        User user = (User) isfsEvent.getParameter(SFSEventParam.USER);
        RedisManager.getInstance().getRedis().hset("uid." + user.getName() , "status" , "0");
    }
}