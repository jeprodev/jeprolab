package com.jeprolab.assets.tools;

import org.apache.commons.collections

import java.util.ArrayList;

public class JeproLabCache<K, D> {
    //protected static local
    private LRUMap jeproCacheMap;

    public void store(K key, D value){
        synchronized (jeproCacheMap){
            jeproCacheMap.put(key, new JeproLabCacheObject(value));
        }
    }

    public D retrive(K key){
        synchronized (jeproCacheMap){
            JeproLabCacheObject object = (JeproLabCacheObject)jeproCacheMap.get(key);

            if(object == null){
                return null;
            }else{
                return object.objectValue;
            }
        }
    }

    public void clean(){
        ArrayList<K> deleteKeys = null;
        synchronized (jeproCacheMap){
            MapIterator itr = jeproCacheMap.mapIterator();
            deleteKeys = new ArrayList<K>((jeproCacheMap.size()/2) + 1);
            K key = null;
            JeproLabCacheObject object = null;

            while(itr.hasNext()){
                key = (K)itr.next();
                object = (JeproLabCacheObject)itr.getValue();

                if(object != null ){
                    deleteKeys.add(key);
                }
            }
        }

        for(K ky : deleteKeys){
            synchronized (jeproCacheMap){
                jeproCacheMap.remove(key);
            }
            Thread.yield();
        }
    }

    protected class JeproLabCacheObject{
        public D objectValue;

        protected JeproLabCacheObject(D value){
            this.objectValue = value;
        }
    }

    public void remove(K key){
        synchronized (jeproCacheMap){
            jeproCacheMap.remove(key);
        }
    }

    public int size(){
        synchronized (jeproCacheMap){
            return jeproCacheMap.size();
        }
    }
}