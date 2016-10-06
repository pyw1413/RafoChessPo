package com.rafo.hall.handlers;

import com.bbzhu.cache.Cache;
import com.rafo.hall.common.GlobalConstants;
import com.rafo.hall.manager.RedisManager;
import com.rafo.hall.service.EmailService;
import com.rafo.hall.service.MarqueeService;
import com.rafo.hall.utils.CmdsUtils;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.smartfoxserver.v2.extensions.SFSExtension;
import com.www.model.Server;
import com.rafo.hall.model.cache.*;

import java.util.Set;

/**
 * Created by Administrator on 2016/9/18.
 */
public class GetserHandler extends BaseClientRequestHandler {

    @Override
    public void handleClientRequest(User user, ISFSObject params) {
        String roomid = params.getUtfString("roomid");
        Integer count = params.getInt("count");
        Integer type = params.getInt("type");
        Integer flopChickenType = params.getInt("flopChickenType");
        Integer bankerType = params.getInt("bankerType");
        Server server = null;
        ISFSObject respObj = new SFSObject();
        if (roomid == null || roomid.equals("")) {
            server = getServerByPreCreate();
        } else {
            server = getServerIdByRoomId(roomid);
            respObj.putUtfString("roomid",roomid);
        }

        respObj.putInt("result",0);
        if (server != null) {
            respObj.putUtfString("ip",server.getIp());
            respObj.putInt("port",server.getPort());
        } else {
            respObj.putInt("result", GlobalConstants.ROOM_ENTER_FAILED_NUMBER_ERROR);  //
        }

        if (count != null && type != null && flopChickenType != null && bankerType != null) {
            respObj.putInt("count",count);
            respObj.putInt("type",type);
            respObj.putInt("flopChickenType" , flopChickenType);
            respObj.putInt("bankerType",bankerType);
        }

        //FIXME
        //send(CmdsUtils.CMD_MARQUEESYN , MarqueeService.getInstance().send("one get Server~",3,"").toObject(), user);
        //send(CmdsUtils.CMD_NEWEMAILSYN  , EmailService.getInstance().sendNewNumber(1).toObject(),user);

        SFSExtension sfs = getParentExtension();
        sfs.send(CmdsUtils.CMD_GetSer , respObj , user);


    }

    //FIXME
    private Server getServerByPreCreate() {
        synchronized (LoginHandler.createLock) {
        int max = (int) Cache.getInstance().get(ServerCount.key);
        int cur = (int)Cache.getInstance().get(curSerIndex.key);
        cur++;
        if (cur >= max) {
            cur = 0;
        }
        Cache.getInstance().set(curSerIndex.key,cur);
        return (Server)Cache.getInstance().get(ServerConfigIndex.key + "." + cur);
         }
    }

    private Server getServerByPreCreateNum() {
        Set<String> keys = RedisManager.getInstance().getRedis().keys("server.*");
        Integer min = Integer.MAX_VALUE;
        String serverid = "";
        Integer v = 0;
        for (String key : keys) {
            String value = RedisManager.getInstance().getRedis().hget(key , "num");
            trace("server:key:" + key + ";num:" + value);
            v = Integer.valueOf(value);
            if (min > v) {
                min = v;
                serverid = RedisManager.getInstance().getRedis().hget(key , "serverid");
            }
        }
        Object o = null;
        if (!serverid.equals("")) {
            o = Cache.getInstance().get(com.rafo.hall.model.cache.Server.key + "." + serverid);
        }
        if (o != null)
        {
            return(Server)o;
        } else {
            return null;
        }
    }

    //FIXME
    private Server getServerIdByRoomId(String roomId) {
        int rid = Integer.valueOf(roomId);
        String serverid = RedisManager.getInstance().hGet("roomid." + rid , "serId");
        Object o = Cache.getInstance().get(com.rafo.hall.model.cache.ServerConfig.key + "." + serverid);
        if (o != null)
        {
            return(Server)o;
        } else {
            return null;
        }
    }
}
