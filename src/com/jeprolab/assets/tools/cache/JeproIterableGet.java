package com.jeprolab.assets.tools.cache;

/**
 *
 * Created by jeprodev on 16/01/2013.
 */
public interface JeproIterableGet<K, V> extends JeproGet<K, V>{
    MapIterator<K, V> mapIterator();
}