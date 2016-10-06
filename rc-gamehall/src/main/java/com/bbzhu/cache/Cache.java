package com.bbzhu.cache;

import java.util.HashMap;
import java.util.WeakHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Administrator on 2016/9/19.
 *
 */
public class Cache {
    private Cache(){};
    private Map<Object,Object> data = new HashMap<Object,Object>();
    private final static Cache instance = new Cache();
    private final ReadWriteLock lock = new ReentrantReadWriteLock() ;
    private final Lock read = lock.readLock();
    private final Lock write = lock.writeLock();

    public static Cache getInstance() {
        return instance;
    }

    public Object get(Object key) {
        try{
            read.lock();
            return data.get(key);
        } catch (Exception ex) {
            return null;
        }finally {
            read.unlock();
        }
    }

    public void set(Object key , Object value) {
        try{
            write.lock();
            data.put(key , value);
        } catch (Exception ex) {

        }finally {
            write.unlock();
        }
    }

    public void clean() {
        try {
            write.lock();
            data.clear();
        } catch (Exception ex) {

        }finally {
            write.unlock();
        }
    }

    public Object test() {
        return data;
    }
}
