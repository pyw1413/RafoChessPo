package com.rafo.hall.core;

import com.bbzhu.cache.Cache;
import com.bbzhu.cache.CacheTimer;
import com.bbzhu.database.DatabaseConn;
import com.bbzhu.utils.Dao;
import com.rafo.hall.handlers.*;
import com.smartfoxserver.v2.core.SFSEventType;
import com.www.model.Server;
import com.bbzhu.utils.Pojo;
import java.util.List;
import java.util.*;
import com.rafo.hall.model.cache.*;

import com.rafo.hall.manager.RedisManager;
import com.rafo.hall.utils.CmdsUtils;
import com.smartfoxserver.v2.extensions.SFSExtension;


/**
 * Created by Administrator on 2016/9/18.
 */
public class HallExtension extends SFSExtension {

    public void init()
    {
        RedisManager.getInstance().init(this);
        Properties props = this.getConfigProperties();
        DatabaseConn.getInstance().addDataSource(0,props.getProperty("local.jdbc.driver"),props.getProperty("local.jdbc.url"),props.getProperty("local.jdbc.user"),props.getProperty("local.jdbc.password"));

        //FIXME
        //init server conifg

       // List<Pojo> pojos = Dao.getInstance().findAll(new Server());
       // for (int i = 0 ; i < pojos.size() ; i++) {
       //     Server server = (Server)pojos.get(i);
       //     Cache.getInstance().set(ServerConfig.key + "." + server.getId(),server);
      //      Cache.getInstance().set(ServerConfigIndex.key + "." + i,server);
     // }

       // Cache.getInstance().set(ServerCount.key, pojos.size());
       //Cache.getInstance().set(curSerIndex.key , 0);

       new Timer().schedule(new CacheTimer() ,0,30000);

        //FIXME for test
        //Map<String , String> map = new HashMap<String,String>();
       // map.put("serId","1");
      //  RedisManager.getInstance().hMSet("roomid.1",map);


        addRequestHandler(CmdsUtils.CMD_GetSer, GetserHandler.class);
        addEventHandler(SFSEventType.USER_LOGIN, PreLoginHandler.class);
        addEventHandler(SFSEventType.USER_JOIN_ZONE, LoginHandler.class);
        addEventHandler(SFSEventType.USER_DISCONNECT, LogoutHandler.class);
        addEventHandler(SFSEventType.USER_LOGOUT, LogoutHandler.class);

        addRequestHandler(CmdsUtils.CMD_ROUNDRECORD , RoundRecordHandler.class);

        addRequestHandler(CmdsUtils.CMD_CONTACT , ContactHandler.class);
        addRequestHandler(CmdsUtils.CMD_ROOMRECORD , RoomRecordHandler.class);
        addRequestHandler(CmdsUtils.CMD_WCVISITEMAIL , VisitEmailHandler.class);
    }
}
