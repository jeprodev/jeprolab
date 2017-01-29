package com.jeprolab.assets.tools.events;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public enum JeproLabSystemEvent {
    SYSTEM_EVENT_PROCESSING_EXCEPTION,
    @JeproLabValidation(JeproLabValidation.Requirement.NEVER_VALIDATE)
    SYSTEM_EVENT_NO_OP
}
