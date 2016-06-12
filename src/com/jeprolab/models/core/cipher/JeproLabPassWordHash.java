package com.jeprolab.models.core.cipher;

/**
 *
 * Created by jeprodev on 18/06/2014.
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

    public boolean checkPassWord(String passWord, String storedHash){
        String hash = "";
        return (hash.equals(storedHash));
    }
}