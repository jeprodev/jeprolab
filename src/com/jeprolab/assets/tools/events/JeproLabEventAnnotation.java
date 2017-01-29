package com.jeprolab.assets.tools.events;

import com.jeprolab.JeproLab;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabEventAnnotation {
    private JeproLabEventListener listener;
    private Object info;

    public JeproLabEventAnnotation(JeproLabEventListener lis, Object obj){
        if(lis == null){
            throw new NullPointerException(JeproLab.getBundle().getString("JEPROLAB_MAY_NOT_CREATE_EVENT_ANNOTATION_FOR_NULL_LISTENER_MESSAGE"));
        }
        this.listener = lis;
        this.info = obj;
    }

    public JeproLabEventListener getListener(){
        return this.listener;
    }

    public Object getInfo(){
        return info;
    }

    @Override
    public String toString() {
        return "EventAnnotation {listener=("
            + listener.toString() + "); info=("
            + (null == info ? "null" : info.toString()) + ");}";
    }

    @Override
    public boolean equals(Object o) {
        return o != null && o instanceof JeproLabEventAnnotation && equals((JeproLabEventAnnotation) o);

    }

    public boolean equals(JeproLabEventAnnotation o) {
        return o != null && getListener().equals(o.getListener()) && ((getInfo() == o.getInfo()) || (getInfo().equals(o.getInfo())));

    }
}
