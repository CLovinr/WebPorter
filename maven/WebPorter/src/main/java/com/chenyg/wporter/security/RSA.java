package com.chenyg.wporter.security;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.interfaces.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

class RSA
{
    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;
    private static Cipher cipher;


    static
    {
        try
        {
            cipher = Cipher.getInstance("RSA");
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        } catch (NoSuchPaddingException e)
        {
            e.printStackTrace();
        }
    }

    public RSA(int bits) throws Exception
    {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(bits);
        KeyPair keyPair = keyPairGen.generateKeyPair();

        privateKey = (RSAPrivateKey) keyPair.getPrivate(); // 私钥
        publicKey = (RSAPublicKey) keyPair.getPublic(); // 公钥
    }

    /**
     * 2048位。
     *
     * @throws Exception
     */
    public RSA() throws Exception
    {
        this(2048);
    }

    public void setPublicKey(RSAPublicKey publicKey)
    {
        this.publicKey = publicKey;
    }

    public RSAPublicKey getPublicKey()
    {
        return publicKey;
    }

    public void setPrivateKey(RSAPrivateKey privateKey)
    {
        this.privateKey = privateKey;
    }

    public RSAPrivateKey getPrivateKey()
    {
        return privateKey;
    }

    private static PublicKey getPublicKey(byte[] key) throws Exception
    {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    private static PrivateKey getPrivateKey(byte[] key) throws Exception
    {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    public static byte[] encrypt(byte[] publicKey, byte[] content, int offset, int length) throws Exception
    {
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
        byte[] rs = cipher.doFinal(content, offset, length);
        return rs;
    }

    public static byte[] decrypt(byte[] privateKey, byte[] content, int offset, int length) throws Exception
    {
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKey));
        byte[] rs = cipher.doFinal(content, offset, length);
        return rs;
    }

    public static int maxContentLength(int bits)
    {
        return bits / 8 - 11;
    }

}
