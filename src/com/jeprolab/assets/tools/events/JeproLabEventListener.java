package com.jeprolab.assets.tools.events;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public interface JeproLabEventListener {
    boolean checkEventListener(JeproLabEvent<? extends JeproLabEventType> event) throws Exception;
    boolean checkEventValidity(JeproLabEvent<? extends JeproLabEventType> event, boolean soFar) throws Exception;
    void eventRaised(JeproLabEvent<? extends JeproLabEventType> event) throws Exception;

    boolean permissionRegisteredEventManager(JeproLabEventManager manager);
    boolean permissionUnregisteredWithEventManager(JeproLabEventManager manager);
    boolean completedRegisteredWithEventManager(JeproLabEventManager manager);
    boolean completedUnregisteredWithEventManager(JeproLabEventManager manager);
}
