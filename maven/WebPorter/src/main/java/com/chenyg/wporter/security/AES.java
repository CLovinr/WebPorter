package com.chenyg.wporter.security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * AES加解密.
 *
 * @author ZhuiFeng
 */
class AES
{
    private Cipher cipher;
    private SecretKeySpec sKeySpec;
    static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    static final String KEY_ALGORITHM = "AES";

    class InitAESException extends Exception
    {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        public InitAESException(String info)
        {
            super(info);
        }
    }

    /**
     * @param psw
     * @param bits 多少位
     * @throws InitAESException
     */
    public AES(byte[] psw, int bits) throws InitAESException
    {
        try
        {
            sKeySpec = new SecretKeySpec(psw, KEY_ALGORITHM);
            cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        } catch (Exception e)
        {
            throw new InitAESException(e.toString());
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
