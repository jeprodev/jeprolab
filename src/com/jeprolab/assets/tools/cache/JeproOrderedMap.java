package com.jeprolab.assets.tools.cache;

/**
 *
 * Created by jeprodev on 16/01/2013.
 */
public interface JeproOrderedMap<K, V> extends JeproIterableMap<K, V> {
    JeproOrderedMapIterator<K, V> mapIterator();

    K firstKey();

    K lastKey();

    K nextKey();

    K previousKey();
}
