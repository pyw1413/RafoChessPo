package com.rafo.chess.handlers;

import com.rafo.chess.model.LoginUser;
import com.rafo.chess.service.LoginService;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2016/9/26.
 */
public class OnUserGoneHandler extends BaseServerEventHandler {

    private final Logger logger = LoggerFactory.getLogger("user");

    @Override
    public void handleServerEvent(ISFSEvent isfsEvent) throws SFSException {
        User user = (User) isfsEvent.getParameter(SFSEventParam.USER);
        try{
            LoginUser loginUser = new LoginUser(user);
            loginUser.setStatus(0);
            user.setProperty("sfsobj",loginUser.toSFSObject());
            LoginService.storeUser2redis(loginUser);
            logger.debug(System.currentTimeMillis()+"\t"+user.getName()+"\t"+  "disconnect"+"\t"+
                    user.getIpAddress()+"\t"+"success"+"\t"+"success");
        }catch (Exception e){
            logger.debug(System.currentTimeMillis()+"\t"+user.getName()+"\t"+  "disconnect"+"\t"+
                    user.getIpAddress()+"\t"+"failed"+"\t"+"system_err");
        }

    }
}
