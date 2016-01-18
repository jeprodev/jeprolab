package com.jeprolab.assets.tools.cache;

import java.util.Map;

/**
 *
 * Created by jeprodev on 16/01/2013.
 */
public interface JeproIterableMap<K, V> extends Map<K, V>, JeproPut<K, V>, JeproIterableGet<K, V> {
}
