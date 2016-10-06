package com.rafo.chess.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.*;

/**
 * Created by Administrator on 2016/9/26.
 */
public class ServerCleanerBeforeRestart {

    private static JedisPool pool;

    public static void main(String[] args) {
        ServerCleanerBeforeRestart cleaner = new ServerCleanerBeforeRestart();
        cleaner.initJedis(args[0],args[1]);
        cleaner.cleanServerUserCount(args[2]);
        cleaner.cleanUserStatusAndRoom();
        cleaner.cleanRoom();

    }

    public void cleanServerUserCount(String serverId){
        String key = "server."+serverId;
        Map<String,String> map = new HashMap<String ,String>();
        map.put("serverId",serverId);
        map.put("uc",Integer.toString(0));
        Jedis jedis = pool.getResource();
        String v = jedis.hget(key,"serverId");
        System.out.println(v);
        jedis.hmset(key,map);
        closeJedis(jedis);
    }


    public void cleanUserStatusAndRoom(){
        Jedis jedis = pool.getResource();
        Set<String> set = jedis.keys("uid.*");
        Iterator<String> it = set.iterator();
        while(it.hasNext()){
            String keyStr = it.next();
            Map<String,String> map = new HashMap<String ,String>();
            map.put("room","0");
            map.put("status","0");
            jedis.hmset(keyStr,map);
        }
        closeJedis(jedis);
    }

    public void cleanRoom(){
        Jedis jedis = pool.getResource();
        Set<String> set = jedis.keys("roomid.*");
        Iterator<String> it = set.iterator();
        while(it.hasNext()){
            String keyStr = it.next();
            jedis.del(keyStr);
        }
        closeJedis(jedis);
    }

    private void initJedis(String ip,String p){
        if (pool == null) {
            try {
                String host = ip;
                int port = Integer.parseInt(p);
                int db =  0;
                int timeout = 10000;
                int maxTotal = 1000;
                int maxIdel = 400;

                JedisPoolConfig config = new JedisPoolConfig();
                config.setMaxTotal(maxTotal);
                config.setMaxIdle(maxIdel);
                config.setMaxWaitMillis(-1);

                pool = new JedisPool(config, host, port, timeout ,null , db );
            }catch (Exception e){

                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }


    private Jedis getJedis(){
        return pool.getResource();
    }
    private void closeJedis(Jedis jedis){
       jedis.close();
    }
}
