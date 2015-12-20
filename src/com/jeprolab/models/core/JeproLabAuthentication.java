package com.jeprolab.models.core;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by jeprodev on 06/06/2014.
 */
public class JeproLabAuthentication {
    public static final int SUCCESS_STATUS = 1;

    public static final int CANCEL_STATUS = 2;

    public static final int FAILURE_STATUS = 4;

    public static final int EXPIRED_STATUS = 8;

    public static final int DENIED_STATUS = 16;

    public static final int UNKNOWN_STATUS = 32;

    protected static JeproLabAuthentication instance = null;

    public static JeproLabAuthentication getInstance(){
        if(instance == null){
            instance = new JeproLabAuthentication();
        }
        return instance;
    }

    public JeproLabAuthenticationResponse authenticate(String userName, String passWord, JeproLabAuthenticationOption loginOptions){
        JeproLabAuthenticationResponse response = onUserAuthenticate(userName, passWord, loginOptions, new JeproLabAuthenticationResponse());

        if(response.status == JeproLabAuthentication.SUCCESS_STATUS){
            if(response.type == null){
                response.type = "JeproLab";
            }
        }

        if(response.userName.equals("")){
            response.userName = userName;
        }

        if(response.fullName.equals("")){
            response.fullName = userName;
        }
        if(response.password.equals("") && passWord != null && passWord.equals("")){
            response.password = passWord;
        }

        return response;
    }
//todo
    public JeproLabAuthenticationResponse onUserAuthenticate(String userName, String passWord,JeproLabAuthenticationOption options, JeproLabAuthenticationResponse response){
        return  null;
    }

    public List authorise(JeproLabAuthenticationResponse response, JeproLabAuthenticationOption loginOptions){
        return onEmployeeAuthorisation(response, loginOptions);
    }

    private static List onEmployeeAuthorisation(JeproLabAuthenticationResponse response, JeproLabAuthenticationOption loginOptions){
        //TODO trigger authorisation to notify that user is logged  add authorise to do things
        return new ArrayList<>();
    }
}