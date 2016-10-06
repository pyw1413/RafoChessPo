package com.rafo.hall.manager;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import com.rafo.hall.core.HallExtension;
/**
 * Created by 亚文 on 2016/9/7.
 */


public class RedisManager {

    private static JedisPool pool;
   // private static JedisPool loginServerJedisPool;
    private static RedisManager instance = new RedisManager();

    private RedisManager() {
    }

    public static RedisManager getInstance(){
        return instance;
    }
    
    public void init(HallExtension extension) {
        Properties props = extension.getConfigProperties();
        if (pool == null) {
            try {
                String host = props.getProperty("redis.host");
                int port = Integer.parseInt(props.getProperty("redis.port"));
                int timeout = Integer.parseInt(props.getProperty("redis.timeout","10000"));
                int maxTotal = Integer.parseInt(props.getProperty("redis.maxtotal","1000"));
                int maxIdel = Integer.parseInt(props.getProperty("redis.maxidel","400"));

                JedisPoolConfig config = new JedisPoolConfig();
                config.setMaxTotal(maxTotal);
                config.setMaxIdle(maxIdel);
                config.setMaxWaitMillis(-1);

                pool = new JedisPool(config, host, port, timeout );
            }catch (Exception e){
                throw new RuntimeException(e);
            }

        }

      /*  if (loginServerJedisPool == null) {
            try {
                String host = props.getProperty("redis.host");
                int port = Integer.parseInt(props.getProperty("redis.port"));
                String password = props.getProperty("redis.password");
                int db =  Integer.parseInt(props.getProperty("redis.db"));
                int timeout = Integer.parseInt(props.getProperty("timeout","10000"));
                int maxTotal = Integer.parseInt(props.getProperty("maxtotal","1000"));
                int maxIdel = Integer.parseInt(props.getProperty("maxidel","400"));

                JedisPoolConfig config = new JedisPoolConfig();
                config.setMaxTotal(maxTotal);
                config.setMaxIdle(maxIdel);
                config.setMaxWaitMillis(-1);

                loginServerJedisPool = new JedisPool(config, host, port, timeout,password,db);
            }catch (Exception e){
                throw new RuntimeException(e);
            }

        }*/
    }

    public Jedis getRedis() {
        return pool.getResource();
    }

  /*  public Jedis getLoginServerJedis(){
        return loginServerJedisPool.getResource();
    }
*/


    public void destory() {
        if (pool != null) {
           pool.destroy();
        }
    }

    public  String get(String key){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.get(key);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }

        return null;
    }


    public  String hGet (String key,String field){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.hget(key,field);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }

        return null;
    }




    public  List<String> hMGet (String key, String ... field){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.hmget(key,field);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }

        return null;
    }



    public  List<String> hMGet (String key,  List<String>  fields){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();

            String[] arr = new String[fields.size()];
            int i = 0;
            for (String item : fields) {
                arr[i++] = item;
            }


            return  jedis.hmget(key, arr);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }

        return null;
    }

    public Map<String, String> hMGetAll(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Map<String, String> ret = jedis.hgetAll(key);
            return ret;
        } catch (Throwable e) {
            e.printStackTrace();
            return new HashMap<>();
        } finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public Long zadd(String key , double score , String member) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Long ret = jedis.zadd(key , score , member);
            return ret;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0L;
        } finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public Double zincrby(String key , double score , String member) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Double ret = jedis.zincrby(key , score , member);
            return ret;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0d;
        } finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }





    public  void set(String key, String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            jedis.set(key, value);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public  void set(byte[] key, byte[]  value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            jedis.set(key, value);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public  void setExpireTime(String key, int seconds){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            jedis.expire(key, seconds);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }


    public  void setPExpireTime(String key, long time){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            jedis.pexpireAt(key, time);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }



    public  void hsetnx(String key, String field, String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            jedis.hsetnx(key, field, value);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public  void incre(String key){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            jedis.incr(key);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public  String hMSet(String key, String nestedKey,String value){
        Jedis jedis = null;
        Map<String, String> map = new HashMap<>();
        try{
            map.put(nestedKey, value);
            jedis = pool.getResource();
            return jedis.hmset(key, map);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }




    public  String hMSet(String key, Map<String, String> map){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.hmset(key, map);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public  void setex(String key,int ttl, String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            jedis.setex(key, ttl,value);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }


    public  void del(String key){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            jedis.del(key);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }


    public  boolean exists (String key){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
           return  jedis.exists(key);
        }catch (Exception e){
            e.printStackTrace();
            return  false;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public  void del(byte[] key){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            jedis.del(key);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }



}
