package com.jeprolab.assets.tools.cache;



import com.jeprolab.JeproLab;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * Created by jeprodev on 16/01/2013.
 */
public class JeproCacheMap <K, V> implements JeproBoundedMab<K, V>, Serializable, Cloneable{
    protected static final int DEFAULT_MAX_SIZE = 100;

    private boolean scanUnitRemovable;

    public JeproCacheMap(){
        this(DEFAULT_MAX_SIZE, DEFAULT_LOAD_FACTOR, false);
    }

    public JeproCacheMap(final int maxSize){
        this(maxSize, DEFAULT_LOAD_FACTOR);
    }

    public JeproCacheMap(final int maxSize, final int initialSize){
        this(maxSize, initialSize, DEFAULT_LOAD_FACTOR);
    }

    public JeproCacheMap(final int maxSize, final  boolean unitRemovable){
        this(maxSize, DEFAULT_LOAD_FACTOR, unitRemovable);
    }

    public JeproCacheMap(final int maxSize, final  float loadFactor){
        this(maxSize, loadFactor, false);
    }

    public JeproCacheMap(final int maxSize, final int initialSize, final float loadFactor){
        this(maxSize, initialSize, loadFactor, false);
    }

    public JeproCacheMap(final int maxSize, final float loadFactor, final  boolean unitRemovable){
        this(maxSize, maxSize, loadFactor, unitRemovable);
    }

    public JeproCacheMap(final int maxSize, final int initialSize, final float loadFactor, final  boolean unitRemovable){
        super(initialSize, loadFactor);
        if(maxSize < 1){
            throw new IllegalArgumentException(JeproLab.getBundle().getString("JEPROLAB_LABEL"));
        }

        if(initialSize > loadFactor){
            throw new IllegalArgumentException(JeproLab.getBundle().getString("JEPROLAB_LABEL"));
        }

        this.maxSize = maxSize;
        this.scanUnitRemovable = unitRemovable;
    }

    public JeproCacheMap(final Map<? extends K, ? extends V> map, final boolean unitRemovable){
        this(map.size(), DEFAULT_LOAD_FACTOR, unitRemovable);
        putAll();
    }

    public V get(final  Object key){
        return get(key, true);
    }

    public V get(final Object key, final boolean updateCache){
        final LinkEntry<K,V> entry = getEntry(key);
        if(entry == null){
            return null;
        }

        if(updateCache){
            moveToCache(entry);
        }
        return entry.getValue();
    }

    protected void moveToCache(final LinkEntry<K, V> entry){
        if(entry.after != header){
            modCount++;
            if (entry.before == null){
                throw new IllegalStateException(JeproLab.getBundle().getString(""));
            }
            entry.before.after = entry.after;
            entry.after.before = entry.before;

            entry.after = header;
            entry.before = header.before;
            header.before.after = entry;
            header.before = entry;
        }else if(entry == header){
            throw new  IllegalStateException(JeproLab.getBundle().getString(""));
        }
    }

    protected void updateEntry(final HashEntry<K, V> entry, final V newValue){
        moveToCache((LinkEntry<K, V>) entry);
        entry.setValue(newValue);
    }
}
