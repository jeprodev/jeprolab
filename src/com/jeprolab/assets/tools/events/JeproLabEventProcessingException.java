package com.jeprolab.assets.tools.events;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabEventProcessingException extends Exception{
    private List<JeproLabEventCause> causes;

    public JeproLabEventProcessingException(){
        causes = new ArrayList<>();
    }

    public synchronized void addCause(JeproLabEvent<? extends JeproLabEventType> event, JeproLabEventListener listener, Throwable exception) {
        causes.add(new  JeproLabEventCause(event, listener, exception));
    }

    public synchronized int countCauses() {
        return causes.size();
    }

    public synchronized List<JeproLabEventCause> getCauses() {
        return Collections.unmodifiableList(causes);
    }

    @Override
    public synchronized String getLocalizedMessage() {
        String s = String.format("EventProcessingException caused by %d actual Throwable(s):%n", countCauses());
        for (int i = 0; i < causes.size(); i++) {
            JeproLabEventCause c = causes.get(i);
            s += String.format("(#%d) Event: %s. Listener: %s.%n",
                i + 1,
                (null == c.getCausingEvent() ? "null" : c.getCausingEvent().toString()),
                (null == c.getListener() ? "null" : c.getListener().toString()));

            s += String.format("(#%d) Throwable: %s.%n",
                i + 1,
                (null == c.getException() ? "null" : c.getException().toString()));
        }
        return s;
    }

    @Override
    public synchronized void printStackTrace(PrintStream s) {
        PrintWriter out = new PrintWriter(s);
        printStackTrace(out);
    }

    @Override
    public synchronized void printStackTrace(PrintWriter s) {
        synchronized (s) {
            s.println(this);
            StackTraceElement[] trace = getStackTrace();
            for (int i = 0; i < trace.length; i++)
                s.println("\tat " + trace[i]);
            s.flush();

            for (int i = 0; i < causes.size(); i++) {
                s.printf("Cause #%d of %d: %n", i + 1, countCauses());
                JeproLabEventCause c = causes.get(i);
                if (null != c.getException())
                    c.getException().printStackTrace(s);
            }
            s.flush();
        }
    }

    public static class JeproLabEventCause {
        private JeproLabEvent<? extends JeproLabEventType> event;
        private JeproLabEventListener listener;
        private Throwable exception;

        public JeproLabEventCause(JeproLabEvent<? extends JeproLabEventType> evt, JeproLabEventListener listener, Throwable excpt){
            event = evt;
            this.listener = listener;
            this.exception = excpt;
        }

        public JeproLabEvent<? extends JeproLabEventType> getCausingEvent(){
            return event;
        }

        public JeproLabEventListener getListener(){
            return listener;
        }

        public Throwable getException(){
            return exception;
        }
    }
}
