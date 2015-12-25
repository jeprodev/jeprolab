package com.jeprolab.models.core;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.db.JeproLabDataBaseConnector;
import com.jeprolab.models.JeproLabEmployeeModel;

import java.sql.ResultSet;
import java.sql.SQLException;
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
        response.type = "JeproLab";

        if(passWord.equals("")){
            response.status = JeproLabAuthentication.FAILURE_STATUS;
            response.errorMessage = JeproLab.getBundle().getString("JEPROLAB_AUTHENTICATION_EMPTY_PASSWORD_NOT_ALLOWED_MESSAGE");
            return  response;
        }

        JeproLabDataBaseConnector dbc = JeproLabFactory.getDataBaseConnector();
        String query = "SELECT " + dbc.quoteName("id") + ", " + dbc.quoteName("password") + " FROM " + dbc.quoteName("#__users");
        query += " WHERE " + dbc.quoteName("username") + " = " + dbc.quote(userName) + ";";

        dbc.setQuery(query);
        ResultSet result = dbc.loadObject();

        if(result != null){
            try{
                String retrievedUserPassWord = result.getString("password");
                int retrievedUserId = result.getInt("id");

                boolean match = this.verifyPassWord(passWord, retrievedUserPassWord, retrievedUserId);

                if(match){
                    response.status = JeproLabAuthentication.SUCCESS_STATUS;
                    response.errorMessage = "";
                }else{
                    response.status = JeproLabAuthentication.FAILURE_STATUS;
                    response.errorMessage = JeproLab.getBundle().getString("JEPROLAB_AUTHENTICATION_INVALID_PASSWORD");
                }
            }catch(SQLException sqlExcept){
                response.status = JeproLabAuthentication.UNKNOWN_STATUS;
                response.errorMessage = JeproLab.getBundle().getString("JEPROLAB_AUTHENTICATION_UNKNOWN_ERROR");
            }

        }else{
            response.status = JeproLabAuthentication.FAILURE_STATUS;
            response.errorMessage = JeproLab.getBundle().getString("JEPROLAB_AUTHENTICATION_NO_USER");
        }

        /** check the to factor
        if(response.status == JeproLabAuthentication.SUCCESS_STATUS){
            if(!check){
                if(){
                    optionConfig.otep = ;
                    check = true;
                }
            }

            if(!check){
                response.status = JeproLabAuthentication.FAILURE_STATUS;
                response.errorMessage = JeproLab.getBundle().getString("JEPROLAB_AUTHENTICATION_INVALID_SECRET_KEY");
            }
        } */
        return  response;

    }

    public List authorise(JeproLabAuthenticationResponse response, JeproLabAuthenticationOption loginOptions){
        return onEmployeeAuthorisation(response, loginOptions);
    }

    private static List onEmployeeAuthorisation(JeproLabAuthenticationResponse response, JeproLabAuthenticationOption loginOptions){
        //TODO trigger authorisation to notify that user is logged  add authorise to do things
        return new ArrayList<Integer>();
    }

    private boolean verifyPassWord(String passWrd, String cipherPassWord, int employeeId){
        boolean rehash = false; System.out.println(cipherPassWord);
        boolean match = false;

        if(cipherPassWord.indexOf("$P$") == 0 ){
            JeproLabPassWordHash passHash = new JeproLabPassWordHash(10, true);
            match = passHash.checkPassWord(passWrd, cipherPassWord);
            rehash = true;
        }else if(cipherPassWord.substring(0,1).equals("$")){

        }else if(cipherPassWord.substring(0, 8).equals("{SHA256}")){
            String encryption = cipherPassWord.substring(0, 8);
            String cipherSalt = cipherPassWord.substring(9, cipherPassWord.length() - 1);
            String encryptionTest = JeproLabAuthentication.getCipheredPassWord(cipherPassWord, cipherSalt, encryption, true);
            //match = JeproLabAuthentication.timingSafeComparison(encryptionTest, cipherPassWord);
            rehash = true;
        }else{
            rehash = true;
        }

        if((employeeId > 0) && match && rehash){
            JeproLabEmployeeModel employee = new JeproLabEmployeeModel(employeeId);
            employee.password = JeproLabAuthentication.hashPassWord(passWrd);
            employee.save();
        }
        return match;
    }

    private static String hashPassWord(String passWrd) {
        return null;
    }

    public static String getCipheredPassWord(String plainText){
        return getCipheredPassWord(plainText, "");
    }

    public static String getCipheredPassWord(String plainText, String salt){
        return getCipheredPassWord(plainText, salt, "md5-hex");
    }

    public static String getCipheredPassWord(String plainText, String salt, String encryption){
        return getCipheredPassWord(plainText, salt, encryption, false);
    }

    public static String getCipheredPassWord(String plainText, String salt, String encryption, boolean showEncrypted){
        /**  get the salt to be used **/
        salt = JeproLabAuthentication.getSalt(encryption, salt, plainText);
        String encrypted = "";
        /** Encrypt the password **/
        switch(encryption){
            case "plain" :
                encrypted = plainText;
                break;
            case "sha" :
                //todo
                encrypted = (showEncrypted) ? "{SHA}" + encrypted : encrypted;
                break;
            case "crypt" :
            case "crypt-des":
            case "crypt-md5":
            case "crypt-blowfish":
                encrypted = (showEncrypted ? "{crypt}" : "");
                break;
            case "md5-base64" :
                //encrypted;
                encrypted = (showEncrypted ? "{MD5}" : "") + encrypted;
                break;
            case "ssha":
                //encrypted;
                encrypted = (showEncrypted ? "{SSHA}" : "") + encrypted;
                break;
            case "smd5":
                //encrypted;
                encrypted = (showEncrypted ? "{SMD5}" : "") + encrypted;
                break;
            case "aprmd5" :
                int length = plainText.length();
                String context = plainText + "$par1$" + salt;
                int ind;
                for(ind = length; ind > 0; ind--){
                    //context += binary.substring(0, (ind > 16) ? 16 : ind);
                }

                /*for(ind = length; ind > 0){

                }*/
                break;
            case "sha256" :
                //encrypted ;
                break;
            case "md5-hex" :
            default :
                //encrypted;
                encrypted = (showEncrypted ? "{MD5}" : "") + encrypted;
                break;
        }
        return encrypted;
    }

    public static String getSalt(){
        return JeproLabAuthentication.getSalt("md5-hex");
    }

    public static String getSalt(String encryption){
        return JeproLabAuthentication.getSalt(encryption, "");
    }

    public static String getSalt(String encryption, String seed){
        return JeproLabAuthentication.getSalt(encryption, seed, "");
    }

    public static String getSalt(String encryption, String seed, String plainText){
        String encrypted = "";
        switch(encryption){
            case "crypt" :
            case "crypt-des" :
                if(!seed.equals("")){
                   // encrypted
                }else{
                    //encrypted
                }
                break;
            case "sha256" :
                if(!seed.equals("")){
                    //encrypted
                }else{
                    encrypted = JeproLabAuthentication.generateRandomPassWord(16);
                }
                break;
            case "crypt-md5" :
                if(!seed.equals("")){
                    //encrypted
                }else{
                    //encrypted = "$1$";
                }
                break;
            case "crypt-blowfish" :
                if(!seed.equals("")){
                    //encrypted
                }else{
                    //encrypted
                }
                break;
            case "ssha" :
                if(!seed.equals("")){
                    //encrypted
                }else{
                    //encrypted
                }
                break;
            case "smd5":
                if(!seed.equals("")){
                    //encrypted
                }else{
                    //encrypted
                }
                break;
            case "aprmd5" :
                if(!seed.equals("")){
                    //encrypted
                }else{
                    //encrypted
                }
                break;
            default:
                if(!seed.equals("")){
                    encrypted = seed;
                }
                break;
        }
        return encrypted;
    }

    public static String generateRandomPassWord(){
        return JeproLabAuthentication.generateRandomPassWord(8);
    }


    public static String generateRandomPassWord(int length){
        String salt = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int base = salt.length();
        String makePass = "";
        return makePass;
    }
}