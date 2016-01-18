package com.jeprolab.assets.tools.cache;

import java.util.Collection;
import java.util.Set;

/**
 *
 * Created by jeprodev on 16/01/2013.
 */
public interface JeproGet<K, V> {
    boolean containsKey(Object key);

    boolean containsValue(Object value);

    Set<java.util.Map.Entry<K, V>> entrySet();

    V get(Object key);

    boolean isEmpty();

    V remove(Object key);

    Set<K> keySet();

    int size();

    Collection<V> values();
}