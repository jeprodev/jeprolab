package com.jeprolab.assets.tools;


import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.map.LRUMap;

import java.util.ArrayList;
import java.util.List;


public class JeproLabCache<K, D> {
    private long timeToLive;
    private final LRUMap jeproCacheMap;
    private static JeproLabCache instance;

    public JeproLabCache(long cacheTimeToLive, final long cacheTimerInterval,  int maxItems){
        this.timeToLive = cacheTimeToLive * 1000;
        jeproCacheMap = new LRUMap(maxItems);
        if(this.timeToLive > 0 && cacheTimerInterval > 0){
            Thread cacheThread = new Thread(() -> {
                while (true){
                    try {
                        Thread.sleep(cacheTimerInterval * 1000);
                    }catch (InterruptedException ignored){

                    }
                    clean();
                }
            });
            cacheThread.setDaemon(true);
            cacheThread.start();
        }
    }

    public void store(K key, D value){
        synchronized (jeproCacheMap){
            jeproCacheMap.put(key, new JeproLabCacheObject(value));
        }
    }

    public D retrieve(K key){
        synchronized (jeproCacheMap){
            JeproLabCacheObject object = (JeproLabCacheObject)jeproCacheMap.get(key);

            if(object == null){
                return null;
            }else{
                object.lastAccessed = System.currentTimeMillis();
                return object.objectValue;
            }
        }
    }

    public void clean(){
        long currentTime = System.currentTimeMillis();
        ArrayList<K> deleteKeys;
        synchronized (jeproCacheMap){
            MapIterator itr = jeproCacheMap.mapIterator();
            deleteKeys = new ArrayList<>((jeproCacheMap.size()/2) + 1);
            K key;
            JeproLabCacheObject object;

            while(itr.hasNext()){
                key = (K)itr.next();
                object = (JeproLabCacheObject)itr.getValue();

                if(object != null && (currentTime > (timeToLive + object.lastAccessed))){
                    deleteKeys.add(key);
                }
            }
        }

        for(K key : deleteKeys){
            synchronized (jeproCacheMap){
                remove(key);
            }
            Thread.yield();
        }
    }

    public void remove(K key){
        synchronized (jeproCacheMap){
            jeproCacheMap.remove(key);
        }
    }

    public void update(K key, D value){
        synchronized(jeproCacheMap){
            jeproCacheMap.remove(key);
            jeproCacheMap.put(key, new JeproLabCacheObject(value));
        }
    }

    public boolean isStored(K key){
        synchronized (jeproCacheMap){
            return jeproCacheMap.containsKey(key);
        }
    }

    public int size(){
        synchronized (jeproCacheMap){
            return jeproCacheMap.size();
        }
    }

    public static JeproLabCache getInstance(){
        if(instance == null){
            instance = new JeproLabCache(900, 3600, 100);
        }
        return instance;
    }

    protected class JeproLabCacheObject{
        public long lastAccessed = System.currentTimeMillis();
        public D objectValue;

        protected JeproLabCacheObject(D value){
            this.objectValue = value;
        }
    }

}