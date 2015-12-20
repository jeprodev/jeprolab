package com.jeprolab.models.core;

/**
 *
 * Created by jeprodev on 06/06/2014.
 */
public class JeproLabAuthenticationResponse {
    public int status = JeproLabAuthentication.FAILURE_STATUS;

    public String type = "";

    public String errorMessage = "";

    public String userName = "";

    public String password = "";

    public String fullName = "";

    public String email = "";

    public String birthDate = "";

    public String gender = "";

    public String postCode = "";

    public String country = "";

    public String language = "";

    public String timeZone = "";
}