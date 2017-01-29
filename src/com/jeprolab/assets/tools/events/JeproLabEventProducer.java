package com.jeprolab.assets.tools.events;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public interface JeproLabEventProducer {
    void eventProcessingFinished(JeproLabEvent<? extends JeproLabEventType> event);

    boolean eventProcessingException(JeproLabEvent<? extends  JeproLabEventType> event, JeproLabEventProcessingException exception);
}
