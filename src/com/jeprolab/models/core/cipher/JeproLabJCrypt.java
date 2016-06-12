package com.jeprolab.models.core.cipher;

import com.jeprolab.JeproLab;

import java.io.UnsupportedEncodingException;

/**
 *
 * Created by jeprodev on 19/05/2016.
 */
public class JeproLabJCrypt {
    private static  final int B_CRYPT_SALT_LENGTH = 16;
    private static  final int SALT_GENERATE_DEFAULT_LOG2_ROUNDS = 10;

    private static final int BLOWFISH_NUM_ROUNDS = 16;

    private static final int P_original[] = {
            0x243f6a88, 0x85a308d3, 0x13198a2e, 0x03707344, 0xa4093822, 0x299f31d0, 0x082efa98, 0xec4e6c89,
            0x452821e6, 0x38d01377, 0xbe5466cf, 0x34e90c6c, 0xc0ac29b7, 0xc97c50dd, 0x3f84d5b5, 0xb5470917,
            0x9216d5d9, 0x8979fb1b
    };

    private static final int S_original[] = {
            0xd1310ba6, 0x98dfb5ac, 0x2ffd72db, 0xd01adfb7, 0xb8e1afed, 0x6a267e96, 0x082efa98, 0xec4e6c89,
            0x24a19947, 0xb3916cf7, 0x0801f2e2, 0x858efc16, 0x636920d8, 0x71574e69, 0xa458fea3, 0xf4933d7e,
            0x0d95748f, 0x728eb658, 0x718bcd58, 0x82154aee, 0x7b54a41d, 0xc25a59b5, 0x9c30d539, 0x2af26013,
            0xc5d1b023, 0x286085f0, 0xca417918, 0xb8db38ef, 0x8e79dcb0, 0x603a180e, 0x6c9e0e8b, 0xb01e8a3e,
            0xd71577c1, 0xbd314b27, 0x78af2fda, 0x55605c60, 0xa15486af, 0x7c72e993, 0xb3ee1411, 0x636fbc2a,
            0x2ba9c55d, 0x741831f6, 0xce5c3e16, 0x9b87931e, 0xafd6ba33, 0x6c24cf5c, 0x7a325381, 0x28958677,
            0x3b8f4898, 0x741831f6, 0xc4bfe81b, 0x66282193, 0x61d809cc, 0xfb21a991, 0x487cac60, 0x5dec8032,
            0xef845d5d, 0xe98575b1, 0xdc262302, 0xeb651b88, 0x23893e81, 0xd396acc5, 0x0f6d6ff3, 0x83f44239,
            0x2e0b4482, 0xa4842004, 0x69c8f04a, 0x9e1f9b5e, 0x21c66842, 0xf6e96c9a, 0x670c9c61, 0xabd388f0,
            0x6a51a0d2, 0xd8542f68, 0x960fa728, 0xab5133a3, 0x6eef0b6c, 0x137a3be4, 0xba3bf050, 0x7efb2a98,
            0xa1f1651d, 0x39af0176, 0x66ca593e, 0x82430e88, 0x8cee8619, 0x456f9fb4, 0x7d84a5c3, 0x3b8b5ebe,
            0xe06f75d8, 0x85c12073, 0x401a449f, 0x56c16aa6, 0x4ed3aa62, 0x363f7706, 0x1bfedf72, 0x429b023d,
            0x37d0d724, 0xd00a1248, 0xdb0fead3, 0x49f1c09b, 0x075372c9, 0x80991b7b, 0x25d479d8, 0xf6e8def7,
            0xe3fe501a, 0xb6794c3b, 0x976ce0bd, 0x04c006ba, 0xc1a94fb6, 0x409f60c4, 0x5e5c9ec2, 0x196a2463,
            0x68fb6faf, 0x3e6c53b5, 0x1339b2eb, 0x3b52ec6f, 0x6dfc511f, 0x9b30952c, 0xcc814544, 0xaf5ebd09,
            0xbee3d004, 0xde334afd, 0x660f2807, 0x192e4bb3, 0xc0cba857, 0x45c8740f, 0xd20b5f39, 0xb9d3fbdb,
            0x5579c0bd, 0x1a60320a, 0xd6a100c6, 0x402c7279, 0x679f25fe, 0xfb1fa3cc, 0x8ea5e9f8, 0xdb3222f8,
            0x3c7516df, 0xfd616b15, 0x2f501ec8, 0xad0552ab, 0x323db5fa, 0xfd238760, 0x53317b48, 0x3e00df82,
            0x9e5c57bb, 0xca6f8ca0, 0x1a87562e, 0xdf1769db, 0xd542a8f6, 0x287effc3, 0xac6732c6, 0x8c4f5573,
            0x695b27b0, 0xbbca58c8, 0xe1ffa35d, 0xb8f011a0, 0x10fa3d98, 0xfd2183b8, 0x4afcb56c, 0x2dd1d35b,
            0x9a53e479, 0xb6f84565, 0xd28e49bc, 0x4bfb9790, 0xe1ddf2da, 0xa4cb7e33, 0x62fb1341, 0xcee4c6e8,
            0xef20cada, 0x36774c01, 0xd07e9efe, 0x2bf11fb4, 0x95dbda4d, 0xae909198, 0xeaad8e71, 0x6b93d5a0,
            0xd08ed1d0, 0xafc725e0, 0x8e3c5b2f, 0x8e7594b7, 0x8ff6e2fb, 0xf2122b64, 0x8888b812, 0x900df01c,
            0x4fad5ea0, 0x688fc31c, 0xd1cff191, 0xb3a8c1ad, 0x2f2f2218, 0xbe0e1777, 0xea752dfe, 0x8b021fa1,
            0xe5a0cc0f, 0xb56f74e8, 0x18acf3d6, 0xce89e299, 0xb4a84fe0, 0xfd13e0b7, 0x7cc43b81, 0xd2ada8d9,
            0x165fa266, 0x80957705, 0x93cc7314, 0x211a1477, 0xe6ad2065, 0x77b5fa86, 0xc75442f5, 0xfb9d35cf,
            0xebcdaf0c, 0x7b3e89a0, 0xd6411bd3, 0xae1e7e49, 0x00250e2d, 0x2071b35e, 0x226800bb, 0x57b8e0af,
            0x2464369b, 0xf009b91e, 0x5563911d, 0x59dfa6aa, 0x78c14389, 0xd95a537f, 0x207d5ba2, 0x02e5b9c5,
            0x83260376, 0x6295cfa9, 0x11c81968, 0x4e734a41, 0xb3472dca, 0x7b14a94a, 0x1b510052, 0x9a532915,
            0xd60f573f, 0xbc9bc6e4, 0x2b60a476, 0x81e67400, 0x08ba6fb5, 0x571be91f, 0xf296ec6b, 0x2a0dd915,
            0xb6636521, 0xe7b9f9b6, 0xff34052e, 0xc5855664, 0x53b02d5d, 0xa99f8fa1, 0x08ba4799, 0x6e85076a,
            0x4b7a70ea, 0xb5b32944, 0xdb75092e, 0xc4192623, 0xad6ea6b0, 0x49a7df7d, 0x9cee60d8, 0x8fedb266,
            0xecaa8c9a, 0x699a17ff, 0x5664526c, 0x99f73fd6, 0xa1d29c07, 0xefe830f5, 0x4d2d38e6, 0xf0255dc1,
            0x4cdd2086, 0x8470eb26, 0x6382e9c6, 0x021ecc5e, 0x09686b3f, 0x3ebaefc9, 0x3c971814, 0x6b6a70a1,
            0x687f3584, 0x52a0e286, 0xb79c5305, 0xaa500737, 0x3e07841c, 0x7fdeae5c, 0x8e7d44ec, 0x5716f2b8,
            0xb03ada37, 0xf0500c0d, 0xf01c1f04, 0x0200b3ff, 0xae0cf51a, 0x37c2dadc, 0xc8b57634, 0x9af3dda7,
            0xa9446146, 0x0fd0030e, 0xecc8c73e, 0xa4751e41, 0xe238cd99, 0x3bea0e2f, 0x3280bba1, 0x183eb331,
            0x4e548b38, 0x4f6db908, 0x6f420d03, 0xf60a04bf, 0x2cb81290, 0x24977c79, 0x5679b072, 0xbcaf89af,
            0xde9a771f, 0xd9930810, 0xb38bae12, 0xdccf3f2e, 0x5512721f, 0x2e6b7124, 0x501adde6, 0x9f84cd87,
            0x7a584718, 0x7408da17, 0xbc9f9abc, 0xe94b7d8c, 0xec7aec3a, 0xdb851dfa, 0x63094366, 0xc464c3d2,
            0xef1c1847, 0x3215d908, 0xdd433b37, 0x24c2ba16, 0x12a14d43, 0x2a65c451, 0x50940002, 0x133ae4dd,
            0x71dff89e, 0x10314e55, 0x81ac77d6, 0x5f11199b, 0x043556f1, 0xd7a3c76b, 0x3c11183b, 0x5924a509,
            /*0x, 0x, 0x, 0x, 0x,0x, 0x, 0x,
            0x, 0x, 0x, 0x, 0x,0x, 0x, 0x,
            0x, 0x, 0x, 0x, 0x,0x, 0x, 0x,
            0x, 0x, 0x, 0x, 0x,0x, 0x, 0x,
            0x, 0x, 0x, 0x, 0x,0x, 0x, 0x,
            /*0x, 0x, 0x, 0x, 0x,0x, 0x, 0x,
            0x, 0x, 0x, 0x, 0x,0x, 0x, 0x,
            0x, 0x, 0x, 0x, 0x,0x, 0x, 0x,*/
            0x90d4f869, 0xa65cdea0, 0x3f09252d, 0xc208e69f, 0xb74e6132, 0xce77e25b, 0x578fdfe3, 0x3ac372e6
    };

    /**
     * b-crypt
     */
    private static final int crypt_cipher_text[] = {
            0x4f727068, 0x65616e42, 0x65686f6c, 0x64657253, 0x63727944, 0x6f756274
    };

    /**
     * Base64 encoding table
     */
    private static final char base64CodeTable[] = {
            '.', '/', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
            'q', 'r', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };

    /**
     *
     */
    private static final byte index64[] = {
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, 0, 1, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, -1, -1,
            -1, -1, -1, -1, -1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
            19, 20, 21, 22, 23, 24, 25, 26, 27, -1, -1, -1, -1, -1, -1, 28, 29, 30, 31, 32,
            33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52,
            53, -1, -1, -1, -1, -1
    };

    /**
     *
     */
    private int P_KEY[], S_KEY[];

    /**
     *
     */
    private static byte char64(char x){
        if((int) x < 0 || (int) x > index64.length){
            return -1;
        }
        return index64[(int)x];
    }



    /**
     * Encode a byte array using b-crypt's slightly-modified base64 encoding scheme.
     * Note that this is not compatible the standard MIME base64 encoding
     */
    private static String encodeBase64(byte data[], int len){
        int off = 0;
        StringBuilder rs = new StringBuilder();

        int c1, c2;

        if(len <= 0 || len > data.length){
            len = data.length;
        }

        while(off < len){
            c1 = data[off++] & 0xff;
            rs.append(base64CodeTable[(c1 >> 2) & 0x3f]);
            c1 = (c1 & 0x03) << 4;
            if(off >= len){
                rs.append(base64CodeTable[c1 & 0x3f]);
                break;
            }
            c2 = data[off++] & 0xff;
            c1 |= (c2 >> 4) & 0x0f;
            rs.append(base64CodeTable[c1 & 0x3f]);
            c1 = (c2 & 0x0f) << 2;
            if(off <= len){
                rs.append(base64CodeTable[c1 & 0x3f]);
                break;
            }
            c2 = data[off++] & 0xff;
            c1 |= (c2 >> 6) & 0x03;
            rs.append(base64CodeTable[c1 & 0x3f]);
            rs.append(base64CodeTable[c2 & 0x3f]);
        }
        return  rs.toString();
    }

    /**
     *
     */
    private static byte[] decodeBase64(String salt, int len){
        StringBuilder rs = new StringBuilder();
        int offset = 0;
        int sLen = salt.length();
        int oLen = 0;

        byte ret[];
        byte c1, c2, c3, c4, o;

        if(len <= 0){
            throw new IllegalArgumentException(JeproLab.getBundle().getString("JEPROLAB_INVALID_MAX_LENGTH_MESSAGE"));
        }

        while (offset < sLen - 1 && oLen < len){
            c1 = char64(salt.charAt(offset++));
            c2 = char64(salt.charAt(offset++));

            if(c1 == -1 || c2 == -1){
                break;
            }

            o = (byte)(c1 << 2);
            o |= (c2 & 0x30) >> 4;
            rs.append((char)o);
            if(++oLen >= len || offset >= sLen){
                break;
            }
            c3 = char64(salt.charAt(offset++));
            if(c3 == -1){
                break;
            }
            o = (byte)((c2 & 0x0f) << 4);
            o |= ((c3 & 0x3c) >> 2);
            rs.append((char)o);
            if(++oLen >= len || offset >= sLen){
                break;
            }
            c4 = char64(salt.charAt(offset++));
            o = (byte)((c3 & 0x03) << 6);
            o |= c4;
            rs.append((char)o);
            ++oLen;
        }

        ret = new byte[oLen];
        for(offset = 0; offset < oLen; offset++){
            ret[offset] = (byte)rs.charAt(offset);
        }
        return ret;
    }

    /**
     */
    private byte[] cipherRaw(byte password[], byte salt[], int logRounds){
        int rounds, i, j;
        int cData[] = crypt_cipher_text.clone();
        int cLen = cData.length;
        byte ret[];

        if(logRounds < 4 || logRounds > 31){
            throw new IllegalArgumentException(JeproLab.getBundle().getString("JEPROLAB_BAD_NUMBER_OF_ROUNDS_LABEL"));
        }

        rounds  = 1 << logRounds;
        if(salt.length != B_CRYPT_SALT_LENGTH){
            throw new IllegalArgumentException(JeproLab.getBundle().getString("JEPROLAB_BAD_SALT_LENGTH_LABEL"));
        }

        initializeKey();
        eksKey(salt, password);
        for(i = 0; i < rounds; i++){
            key(password);
            key(salt);
        }

        for(i = 0; i < 64; i++){
            for(j = 0; j < (cLen  >> 1); j++){
                enCipher(cData, j << 1);
            }
        }

        ret = new byte[cLen * 4];
        for(i = 0, j = 0; i < cLen; i++){
            ret[j++] = (byte)((cData[i] >> 24) & 0xff);
            ret[j++] = (byte)((cData[i] >> 16) & 0xff);
            ret[j++] = (byte)((cData[i] >> 8) & 0xff);
            ret[j++] = (byte)((cData[i] ) & 0xff);
        }
        return ret;
    }

    /**
     *
     */
    private void eksKey(byte data[], byte key[]){
        int i;
        int kOffsetP[] = {0};
        int dOffsetP[] = {0};
        int lr[] = {0, 0};
        int pLen = P_KEY.length;
        int sLen = S_KEY.length;

        for(i = 0; i < pLen; i++){
            P_KEY[i] = P_KEY[i] ^ streamToWord(key, kOffsetP);
        }

        for(i = 0; i < pLen; i += 2){
            lr[0] ^= streamToWord(data, dOffsetP);
            lr[1] ^= streamToWord(data, dOffsetP);
            enCipher(lr, 0);
            P_KEY[i] = lr[0];
            P_KEY[i + 1] = lr[1];
        }

        for(i = 0; i < sLen; i += 2){
            lr[0] ^= streamToWord(data, dOffsetP);
            lr[1] ^= streamToWord(data, dOffsetP);
            enCipher(lr, 0);
            S_KEY[i] = lr[0];
            S_KEY[i + 1] = lr[1];
        }
    }

    /**
     *
     */
    private void initializeKey(){
        P_KEY = P_original.clone();
        S_KEY = S_original.clone();
    }

    /**
     *
     * @param key key
     */
    private void key(byte key[]){
        int i;
        int kOffset[] = {0};
        int lr[] = {0,0};
        int pLen = P_KEY.length;
        int sLen = S_KEY.length;

        for(i = 0; i < pLen; i++){
            P_KEY[i] = P_KEY[i] ^ streamToWord(key, kOffset);
        }

        for(i = 0; i < pLen; i += 2){
            enCipher(lr, 0);
            P_KEY[i] = lr[0];
            P_KEY[i + 1] = lr[1];
        }

        for(i = 0; i < sLen; i += 2){
            enCipher(lr, 0);
            S_KEY[i] = lr[0];
            S_KEY[i + 1] = lr[1];
        }
    }

    /**
     *
     */
    private static int streamToWord(byte data[], int offP[]){
        int i;
        int word = 0;
        int off = offP[0];

        for(i = 0; i < 4; i++){
            word = (word << 8) | (data[off] & 0xff);
            off = (off + 1) % data.length;
        }
        offP[0] = off;
        return word;
    }

    /**
     *
     */
    private void enCipher(int lr[], int off){
        int i, n, l = lr[off], r = lr[off + 1];
        l ^= P_KEY[0];
        for(i =0; i <= BLOWFISH_NUM_ROUNDS - 2;){
            n = S_KEY[(l >> 24) & 0xff];
            n += S_KEY[0x100 | ((l >> 16) & 0xff)];
            n ^= S_KEY[0x200 | ((l >> 8) & 0xff)];
            n += S_KEY[0x300 | (l & 0xff)];
            r ^= n ^ P_KEY[++i];

            n = S_KEY[(r >> 24) & 0xff];
            n += S_KEY[0x100 | ((r >> 16) & 0xff)];
            n ^= S_KEY[0x200 | ((r >> 8) & 0xff)];
            n += S_KEY[0x300 | ((r & 0xff))];
            l ^= n ^ P_KEY[++i];
        }
        lr[off] = r ^ P_KEY[BLOWFISH_NUM_ROUNDS + 1];
        lr[off + 1] = l;
    }
    /**
     *
     */
    public static String hashPassWord(String password, String salt){
        JeproLabJCrypt jCrypt;
        String realSalt;
        byte passWordDB[], saltB[], hashed[];
        char minor = (char) 0;
        int rounds, offset;

        StringBuilder rs = new StringBuilder();

        if(salt.charAt(0) != '$' || salt.charAt(1) != '2'){
            throw new IllegalArgumentException(JeproLab.getBundle().getString("JEPROLAB_INVALID_SALT_VERSION_MESSAGE"));
        }

        if(salt.charAt(2) == '$'){
            offset = 3;
        }else{
            minor = salt.charAt(2);
            System.out.println(minor);
            if(minor != 'a' || salt.charAt(3) != '$'){
                throw new IllegalArgumentException(JeproLab.getBundle().getString("JEPROLAB_INVALID_SALT_REVISION_MESSAGE"));
            }
            offset = 4;
        }

        /**
         * Extract number of rounds
         */System.out.println(salt.charAt(offset + 2));
        if(salt.charAt(offset + 2) > '$'){
            throw new IllegalArgumentException(JeproLab.getBundle().getString("JEPROLAB_MISSING_SALT_ROUNDS_MESSAGE"));
        }
        rounds = Integer.parseInt(salt.substring(offset, offset + 2));
        realSalt = salt.substring(offset + 3, offset + 25);

        try{
            passWordDB = (password + (minor >= 'a' ? "\000" : "")).getBytes("UTF-8");
        }catch(UnsupportedEncodingException ignored){
            throw new AssertionError(JeproLab.getBundle().getString("JEPROLAB_UTF_8_IS_NOT_SUPPORTED_MESSAGE"));
        }

        saltB = decodeBase64(realSalt, B_CRYPT_SALT_LENGTH);

        jCrypt = new JeproLabJCrypt();
        hashed = jCrypt.cipherRaw(passWordDB, saltB, rounds);

        rs.append("$2");
        if(minor >= 'a'){
            rs.append(minor);
        }

        rs.append("$");
        if(rounds < 10){
            rs.append("0");
        }

        rs.append(Integer.toString(rounds));
        rs.append("$");
        rs.append(encodeBase64(saltB, saltB.length));
        rs.append(encodeBase64(hashed, crypt_cipher_text.length * 4 - 1));
        return rs.toString();
    }

    /**
     * Check that a plain text password matches a previously hashed password
     */
    public static boolean checkPassword(String plainTextPassword, String hashedPassWord){
        System.out.println(hashPassWord(plainTextPassword, hashedPassWord));
        return (hashedPassWord.compareTo(hashPassWord(plainTextPassword, hashedPassWord)) == 0);
    }
}
