package com.btd.wallet.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by yzy on 2018/5/3 16:35
 */

public class MD5Util {
    private static final char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    public static String md5(String string) {
        Object var1 = null;

        byte[] encodeBytes;
        try {
            encodeBytes = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException var3) {
            throw new RuntimeException(var3);
        } catch (UnsupportedEncodingException var4) {
            throw new RuntimeException(var4);
        }

        return toHexString(encodeBytes);
    }


    public static String toHexString(byte[] bytes) {
        if(bytes == null) {
            return "";
        } else {
            StringBuilder hex = new StringBuilder(bytes.length * 2);
            byte[] var2 = bytes;
            int var3 = bytes.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                byte b = var2[var4];
                hex.append(hexDigits[b >> 4 & 15]);
                hex.append(hexDigits[b & 15]);
            }

            return hex.toString();
        }
    }
}
