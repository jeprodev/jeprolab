package com.jeprolab.models.core;

import com.jeprolab.JeproLab;
import com.jeprolab.controllers.JeproLabLoginController;
import com.jeprolab.models.JeproLabEmployeeModel;

import java.util.ArrayList;
import java.util.List;

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
        JeproLabAuthentication authenticator = JeproLabAuthentication.getInstance();
        JeproLabAuthenticationResponse response = authenticator.authenticate(userName, passWord, loginOptions);

        if(response.status == JeproLabAuthentication.SUCCESS_STATUS){
            /**
             * Validate that the user should be able to login (different to being authenticated).
             * This permits authentication plugins to block the user
             */
            List<Integer> authorisations = authenticator.authorise(response, loginOptions);
            for(int authorisation : authorisations){
                if(authorisation == JeproLabAuthentication.EXPIRED_STATUS ||authorisation == JeproLabAuthentication.DENIED_STATUS){
                    JeproLabLoginController.loginFailed(response);

                    /**
                     * If the silent is set, just return false
                     */
                    if(loginOptions.silent){
                        return false;
                    }

                    switch(authorisation){
                        case JeproLabAuthentication.EXPIRED_STATUS :
                            break;
                        case JeproLabAuthentication.DENIED_STATUS :
                            break;
                        default:
                            break;
                    }
                }
            }

            /**
             * Ok, the credentials are authentication
             */
            boolean result = JeproLabLoginController.onUserLogin(response, loginOptions);

            /**
             * If any of the user plugins did successfully complete the login routine then the whole method fails.
             *
             * Any errors raised should be done in the plugin as this provides the ability to provide much more
             * information about why the routine may have failed
             */
            JeproLabEmployeeModel employee = JeproLabFactory.getEmployee();
            if(response.type.equals("Cookie")){
                employee.setCookieLogin(true);
            }

            if(result){
                loginOptions.employee = employee;
                loginOptions.responseType = response.type;

                loginOptions.length = response.length;
                loginOptions.secure = response.secure;
                loginOptions.lifeTime = response.lifeTime;

                /**
                 * The user is successfully logged in. Run the next step
                 */
                JeproLabLoginController.onUserLogged(loginOptions);
            }
            return true;
        }

        /** Triggering login failure **/
        JeproLabLoginController.onUserLoginFailure(response);

        //if silent mode has been set
        if(loginOptions.silent){
            return  false;
        }

        //If status is not success, error must be recorded in order let trace for control
        if(response.status != JeproLabAuthentication.SUCCESS_STATUS){
            //set logging information
        }
        return false;
    }

    public void checkSession(){

    }
}