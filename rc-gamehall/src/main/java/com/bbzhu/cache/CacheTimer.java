package com.bbzhu.cache;

import com.bbzhu.utils.Dao;
import com.bbzhu.utils.Pojo;
import com.rafo.hall.handlers.LoginHandler;
import com.rafo.hall.model.cache.ServerConfig;
import com.rafo.hall.model.cache.ServerConfigIndex;
import com.rafo.hall.model.cache.ServerCount;
import com.rafo.hall.model.cache.curSerIndex;
import com.www.model.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/9/28.
 */
public class CacheTimer extends TimerTask{

   // private final Logger logger = LoggerFactory.getLogger("Extensions");
    @Override
    public void run() {
        synchronized (LoginHandler.createLock)
        {
            Object cur =  Cache.getInstance().get(curSerIndex.key);
            if (cur == null) {
                cur = 0;
            }
            Cache.getInstance().clean();
            List<Pojo> pojos = Dao.getInstance().findAll(new Server());
            int i= 0;
            for (Pojo pojo:pojos) {

                Server server = (Server)pojo;
             //   logger.info("id:" + server.getId());
                Cache.getInstance().set(ServerConfig.key +"."+ server.getId(),server);
                Cache.getInstance().set(ServerConfigIndex.key +"."+ i,server);
                i++;
            }
            Cache.getInstance().set(ServerCount.key , pojos.size());

            Cache.getInstance().set(curSerIndex.key , cur);
        }
    }
}
