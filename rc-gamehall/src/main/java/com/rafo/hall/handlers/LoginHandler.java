package com.rafo.hall.handlers;

import com.bbzhu.cache.Cache;
import com.rafo.hall.common.GlobalConstants;
import com.rafo.hall.service.EmailService;
import com.rafo.hall.service.MarqueeService;
import com.rafo.hall.utils.CmdsUtils;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.entities.variables.SFSUserVariable;
import com.smartfoxserver.v2.entities.variables.UserVariable;
import com.smartfoxserver.v2.exceptions.SFSException;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import com.rafo.hall.manager.RedisManager;
import com.smartfoxserver.v2.exceptions.SFSLoginException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
import com.smartfoxserver.v2.extensions.SFSExtension;
import com.www.model.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2016/9/18.
 */
public class LoginHandler extends BaseServerEventHandler {
    public static Object createLock = new Object();

    private final Logger logger = LoggerFactory.getLogger("login");
    @Override
    public void handleServerEvent(ISFSEvent isfsEvent) throws SFSException {
        User user = (User) isfsEvent.getParameter(SFSEventParam.USER);
        String uid =(String)user.getSession().getProperty("uid");
        user.setName(uid);
        Map<String , String> uinfo = RedisManager.getInstance().hMGetAll("uid."+uid);
        ISFSObject respObj = new SFSObject();

        if (uinfo.size() == 0) {
            respObj.putInt("result",GlobalConstants.LOGIN_FAILED_NOTUSER);
            send(CmdsUtils.CMD_Update , respObj , user);
            logger.debug("无效的用户ID用来登录：uid : " + uid );
            throw new SFSLoginException("login error;");
        }

        Iterator<Map.Entry<String, String>> entryIterator = uinfo.entrySet().iterator();
        while (entryIterator.hasNext()) {
            Map.Entry<String, String> entry = entryIterator.next();
            String key = entry.getKey();String value = entry.getValue();
            if (key.equals("ID") || key.equals("card") || key.equals("points") || key.equals("total") || key.equals("room") || key.equals("haveNewEmail")) {
                respObj.putInt(key,Integer.valueOf(value));
            } else {
                respObj.putUtfString(key,value);
            }

        }
        logger.debug("uid:" + uid + ";;room:" + uinfo.get("room"));
        if (uinfo.get("room") != null && !uinfo.get("room").equals("")) {
            String rid = uinfo.get("room");
            Server server = getServerIdByRoomId(rid);
            if (server != null) {
                respObj.putUtfString("ip" , server.getIp());
                respObj.putUtfString("port",String.valueOf(server.getPort()));//断线重连
            }

        } else {
            respObj.putUtfString("ip" , "");
            respObj.putUtfString("port","");//
        }
        SFSExtension extension = getParentExtension();
       // trace("login res .."  + respObj.getInt("ID") + "---" + user.getName());
        logger.debug("登录回应 ..ID:"  + respObj.getInt("ID") + "---" + " IP:"+ respObj.getUtfString("ip") + " ---- PORT:" + respObj.getUtfString("port"));
       // UserVariable us = new SFSUserVariable("sfsobj", respObj);



       // getApi().setUserVariables(user, Arrays.asList(us));
        respObj.putInt("result" , GlobalConstants.LOGIN_SUCCESS);
        send(CmdsUtils.CMD_Update , respObj , user);

        //throw new SFSLoginException("login error;");
    }

    //FIXME
    private Server getServerIdByRoomId(String roomId) {
        String serverid = RedisManager.getInstance().hGet("roomid." + roomId , "serId");
        Object o = Cache.getInstance().get(com.rafo.hall.model.cache.Server.key + "." + serverid);
        if (o != null)
        {
            return(Server)o;
        } else {
            return null;
        }
    }
}
