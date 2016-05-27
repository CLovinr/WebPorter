package com.chenyg.wporter.security;

import com.chenyg.wporter.util.BytesTool;

import java.security.SecureRandom;

/**
 * 对称加密工具类
 * Created by 刚帅 on 2016/1/14.
 */
public class SymEncryptionUtil
{

    public static byte[] aesEncrypt(byte[] psw, int bits, byte[] content, int... offsetAndLength)
    {
        return aes(true, psw, bits, content, offsetAndLength);
    }

    public static byte[] aesDecrypt(byte[] psw, int bits, byte[] content, int... offsetAndLength)
    {
        return aes(false, psw, bits, content, offsetAndLength);
    }

    public static String randomKeyHex(int bits)
    {
        byte[] bs = randomKey(bits);
        return BytesTool.toHex(bs, 0, bs.length);
    }

    public static byte[] randomKey(int bits)
    {
        SecureRandom secureRandom = new SecureRandom();
        int n = bits / 8;
        byte[] bytes = new byte[n];
        secureRandom.nextBytes(bytes);

        return bytes;
    }

    private static byte[] aes(boolean isEncrypt, byte[] psw, int bits, byte[] content, int... offsetAndLength)
    {
        try
        {
            AES aes = new AES(psw, bits);
            int offset, length;
            if (offsetAndLength.length > 0)
            {
                offset = offsetAndLength[0];
                length = offsetAndLength[1];
            } else
            {
                offset = 0;
                length = content.length;
            }
            if (isEncrypt)
            {
                return aes.encrypt(content, offset, length);
            } else
            {
                return aes.decrypt(content, offset, length);
            }

        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
