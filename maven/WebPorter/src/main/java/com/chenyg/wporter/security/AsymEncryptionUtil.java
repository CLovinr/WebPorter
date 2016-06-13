package com.chenyg.wporter.security;


import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * 非对称加密工具类
 * Created by 刚帅 on 2016/1/14.
 */
public abstract class AsymEncryptionUtil
{
    public static AsymEncryptionUtil rsaUtil(boolean buildKeypair) throws Exception
    {
        AsymEncryptionUtil util = new AsymEncryptionUtil()
        {
            RSA rsa;

            private RSA rsa()
            {
                if (rsa == null)
                {
                    try
                    {
                        rsa = new RSA();
                    } catch (Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                }
                return rsa;
            }

            @Override
            public byte[] publicKey()
            {
                RSAPublicKey rsaPublicKey = rsa().getPublicKey();
                return rsaPublicKey.getEncoded();
            }

            @Override
            public byte[] secretKey()
            {
                RSAPrivateKey rsaPrivateKey = rsa().getPrivateKey();
                return rsaPrivateKey.getEncoded();
            }


            private byte[] doFinal(boolean isEncypt, byte[] key, byte[] bytes, int... offsetAndLength) throws Exception
            {
                int offset = offsetAndLength.length == 0 ? 0 : offsetAndLength[0];
                int length = offsetAndLength.length == 0 ? bytes.length : offsetAndLength[1];
                byte[] rs = isEncypt ? RSA.encrypt(key, bytes, offset, length) : RSA
                        .decrypt(key, bytes, offset, length);
                return rs;
            }

            @Override
            public byte[] encrypt(byte[] publicKey, byte[] bytes, int... offsetAndLength) throws Exception
            {
                return doFinal(true, publicKey, bytes, offsetAndLength);
            }

            @Override
            public byte[] decrypt(byte[] secretKey, byte[] bytes, int... offsetAndLength) throws Exception
            {
                return doFinal(false, secretKey, bytes, offsetAndLength);
            }
        };
        return util;
    }

    public abstract byte[] publicKey();

    public abstract byte[] secretKey();


    public abstract byte[] encrypt(byte[] publicKey, byte[] bytes, int... offsetAndLength) throws Exception;

    public abstract byte[] decrypt(byte[] secretKey, byte[] bytes, int... offsetAndLength) throws Exception;
}
