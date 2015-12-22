package com.jeprolab.models.core;

/**
 *
 * Created by jeprodev on 21/12/2015.
 */
public class JeproLabPassWordHash {
    private String itoa64 = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private int iterationCountLog2;

    private boolean portableHashes;

    private long randomState;

    public JeproLabPassWordHash(int iterationLog2, boolean portable_hashes){
        if(iterationLog2 < 4 || iterationLog2 > 32 ){
            iterationLog2 = 8;
        }
        this.iterationCountLog2 = iterationLog2;
        this.portableHashes = portable_hashes;

        this.randomState = System.currentTimeMillis();
    }

    /*public String hashPassWord(String passWord){
        String random = "";
        return "*";
    }

    public String generateExtendedSalt(String input){
        String outPut = "_";
        int countLog2 = Math.min(this.iterationCountLog2 + 8, 24);
        //int count =
        return outPut;
    }

    public String generateSaltBlowFish(String input){
        String outPut = "$2a$";
        this.itoa64 = "./ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    }

    public String getRandomBytes(int count){

    }

    public String encode64(int count){
        int i = 0;
        String outPut = "";
        do{
            if(i++ >= count){
                break;
            }
            outPut += this.itoa64.getC ;
        }while(i < count);
        return outPut;
    }
*/
    public boolean checkPassWord(String passWord, String storedHash){
        String hash = "";
        return (hash.equals(storedHash));
    }
}