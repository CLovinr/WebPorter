package com.chenyg.wporter.security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * AES加解密.
 */
public class AES
{
    private Cipher cipher;
    private SecretKeySpec sKeySpec;

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

    /**
     * @param pswEncoded
     * @throws InitAESException
     */
    public AES(byte[] pswEncoded) throws InitAESException
    {
        init(pswEncoded);
    }

    private void init(byte[] pswEncoded) throws InitAESException
    {
        try
        {
            sKeySpec = new SecretKeySpec(pswEncoded, "AES");
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        } catch (Exception e)
        {
            throw new InitAESException(e);
        }
    }

    public AES(int bits, SecureRandom secureRandom) throws InitAESException
    {
        try
        {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(bits, secureRandom);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            init(enCodeFormat);
        } catch (InitAESException e)
        {
            throw e;
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
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(bits, new SecureRandom(psw));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            init(enCodeFormat);
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
        cipher.init(Cipher.ENCRYPT_MODE, sKeySpec);
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
        cipher.init(Cipher.DECRYPT_MODE, sKeySpec);// 初始化
        byte[] result = cipher.doFinal(content, offset, length);
        return result; // 加密
    }
}
