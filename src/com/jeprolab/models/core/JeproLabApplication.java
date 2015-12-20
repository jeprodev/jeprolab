package com.jeprolab.models.core;

/**
 *
 * Created by jeprodev on 19/06/2014.
 */
public class JeproLabApplication {
    private static JeproLabAuthentication authenticate;

    /**
     * Login authentication function
     *
     * Username and encoded password are passed the on user login event which is responsible for the user validation. A
     * successful validation updates the current session record with the user's details.
     *
     * @param userName
     * @param passWord
     * @return boolean true on success
     */
    public static boolean login(String userName, String passWord, JeproLabAuthenticationOption loginOptions){
        return true;
    }

    public void checkSession(){

    }
}