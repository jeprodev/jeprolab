package com.jeprolab.models.core;

import com.jeprolab.assets.config.JeproLabConfig;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.JeproLabCookie;
import com.jeprolab.models.JeproLabEmployeeModel;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproLabSession {
    /**
     * Internal state of session
     * One of 'inactive' - 'active' - 'expired' - 'destroyed' - 'error'
     *
     * @see JeproLabSession->getState()
     */
    protected String state = "inactive";

    /**
     * The session store object
     */
    protected JeproLabSessionStorage store;

    /**
     * Maximum age f unused session in minutes
     *
     *
     */
    protected int expire = 15;

    /**
     * Security policy
     * Check list to be done
     *
     */

    /**
     * The type of storage  for the session
     *
     */
    protected String storeName;

    /**
     * Force cookies to be Ssl only
     * Default False
     */
    protected boolean forceSsl = false;


    /**
     * JeproLabSession instance container
     */
    protected static JeproLabSession instance;

    /**
     * Constructor
     */
    public JeproLabSession(String storeN, JeproLabSessionOption options){
        if(JeproLabSession.sessionId()){
            JeproLabSession.unsetSession();
            JeproLabSession.destroySession();
        }
        this.store = JeproLabSessionStorage.getInstance(storeN, options);
        this.storeName = storeN;
        this.setCookieParams();
        this.state = "inactive";
    }

    /**
     * return the storeName
     * @return storeName
     */
    public String getStoreName(){
        return this.storeName;
    }

    /**
     * Get current state of session
     *
     * @return string session status
     */
    public String getState(){
        return this.state;
    }

    /**
     *
     */
    public int getExpiration(){
        return this.expire;
    }

    /**
     * Return the global JeproLabSession object, only creating it it doesn't already exist.
     * @param handler
     * @param options
     * @return JeproLabSession object
     */
    public static JeproLabSession getInstance(String handler, JeproLabSessionOption options){
        if(instance == null){
            instance = new JeproLabSession(handler, options);
        }
        return instance;
    }

    /**
     * Set session cookie parameters
     *
     */
    public void setCookieParams(){
        JeproLabCookie cookie = JeproLabContext.getContext().cookie;
        if(this.forceSsl){
            cookie.secure = true;
        }
        JeproLabConfig config = JeproLabFactory.getConfig();
        if (!config.getCookieDomain().equals("")){
            cookie.domain = config.getCookieDomain();
        }

        if(!config.getCookiePath().equals("")){
            cookie.path = config.getCookiePath();
        }
        JeproLabContext.getContext().cookie = cookie;
    }

    /**
     * Start a session
     */
    protected void start(){
        if (this.state.equals("restart")){
            this.sessionRegenerateId(true);
        }else{
            String sessionName = this.getSessionName();
            JeproLabCookie cookie = JeproLabContext.getContext().cookie;
            if(cookie.get(sessionName) == null){

            }
        }
    }

    /**
     * Restart an expired or locked session
     *
     */
    public void restart(){
        this.destroySession();
        if(!this.state.equals("destroyed")){
            this.store.register();
            this.state = "restart";
            this.sessionRegenerateId(true);
            this.start();
            this.validate();
            this.setCounter();
        }
    }
    //todo edit us
    protected static boolean sessionId(){
        return true;
    }

    protected static void unsetSession(){}

    protected String getSessionName(){
        return "";
    }

    protected static void destroySession(){}

    //protected void destroySession(){}

    protected void validate(){}

    protected void sessionRegenerateId(boolean it){}

    protected void setCounter(){}

    public JeproLabEmployeeModel getEmployee(){
        return new JeproLabEmployeeModel();
    }
}