package com.jeprolab.assets.tools.cache;

import java.util.Map;

/**
 *
 * Created by jeprodev on 16/01/2013.
 */
public interface JeproPut<K, V> {
    void clear();

    Object put(K key, V value);

    void putAll(Map<? extends K, ? extends  V> t);
}