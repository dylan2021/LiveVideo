package com.mm.android.deviceaddmodule.mobilecommon.utils;

import android.text.TextUtils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Helper
{
    private static byte[] hex = new byte[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    /**
     * 对字符串进行MD5加密
     * @param rawString   要加密的字符串
     * @return MD5摘要码
     */
    public static byte[] encode2byte(String rawString) {
        if (TextUtils.isEmpty(rawString)) {
            return null;
        }try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(rawString.getBytes());
            return md5.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 对字符串进行MD5加密  
     * @param rawString   要加密的字符串
     * @return MD5摘要码  
     */ 
    public static String encode(String rawString)
    {
        String md5String = null;
        if (TextUtils.isEmpty(rawString)) {
            return md5String;
        }
        try
        {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(rawString.getBytes());
            md5String = convertToHexString(md5.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        
        if (null != md5String)
        {
            return md5String.toUpperCase();
        }
        
        return md5String;
    }


    public static String encodeToLowerCase(String rawString){
        String mD5String = encode(rawString);
        if (TextUtils.isEmpty(mD5String)) {
            return mD5String;
        }
        return mD5String.toLowerCase();
    }

    private static String convertToHexString(byte[] digests)
    {
        byte[] md5String = new byte[digests.length * 2];
        
        int index = 0;
        for (byte digest : digests)
        {
            md5String[index]         = hex[(digest >> 4) & 0x0F];
            md5String[index + 1]     = hex[digest &0x0F];
            index += 2;
        }
        
        return new String(md5String);
    }
}
