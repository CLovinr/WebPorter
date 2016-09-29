package com.chenyg.wporter.security;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class AES
{
    private Cipher cipher;
    private SecretKey sKey;

    public class InitAESException extends Exception
    {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        public InitAESException(Throwable thr)
        {
            super(thr);
        }
    }


    private void init(byte[] pswEncoded, int bits) throws InitAESException
    {
        try
        {
            //sKeySpec = new SecretKeySpec(pswEncoded, "AES");
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(pswEncoded);
            kgen.init(bits, secureRandom);
            sKey = kgen.generateKey();
        } catch (Exception e)
        {
            throw new InitAESException(e);
        }
    }

    /**
     * @param psw
     * @param bits 多少位 128,192,256等
     * @throws InitAESException
     */
    public AES(byte[] psw, int bits) throws InitAESException
    {
        try
        {
            init(psw, bits);
        } catch (InitAESException e)
        {
            throw e;
        } catch (Exception e)
        {
            throw new InitAESException(e);
        }
    }

    /**
     * 加密
     *
     * @param content
     * @param offset
     * @param length
     * @return
     * @throws Exception
     */
    public byte[] encrypt(byte[] content, int offset, int length) throws Exception
    {
        cipher.init(Cipher.ENCRYPT_MODE, sKey);
        byte[] result = cipher.doFinal(content, offset, length);
        return result;
    }

    /**
     * 解密
     *
     * @param content 待解密内容
     * @return
     */
    public byte[] decrypt(byte[] content, int offset, int length) throws Exception
    {
        cipher.init(Cipher.DECRYPT_MODE, sKey);// 初始化
        byte[] result = cipher.doFinal(content, offset, length);
        return result; // 加密
    }
}

