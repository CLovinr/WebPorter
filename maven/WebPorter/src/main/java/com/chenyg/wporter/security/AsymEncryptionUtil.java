package com.chenyg.wporter.security;


/**
 * 非对称加密工具类
 * Created by 刚帅 on 2016/1/14.
 */
public abstract class AsymEncryptionUtil
{

    private static class RsaAsymEncryptionUtil extends AsymEncryptionUtil
    {

        private byte[] pk, sk;

        RsaAsymEncryptionUtil(boolean buildKeypair)
        {
            if (buildKeypair)
            {

            }
        }

        @Override
        public byte[] publicKey()
        {
            return pk;
        }

        @Override
        public byte[] secretKey()
        {
            return sk;
        }

        @Override
        public byte[] encrypt(byte[] publicKey, byte[] bytes, int... offsetAndLength)
        {
            return new byte[0];
        }

        @Override
        public byte[] decrypt(byte[] secretKey, byte[] bytes, int... offsetAndLength)
        {
            return new byte[0];
        }



    }

    public static AsymEncryptionUtil rsaUtil(boolean buildKeypair)
    {
        return null;
    }

    public abstract byte[] publicKey();

    public abstract byte[] secretKey();


    public abstract byte[] encrypt(byte[] publicKey, byte[] bytes, int... offsetAndLength);

    public abstract byte[] decrypt(byte[] secretKey, byte[] bytes, int... offsetAndLength);
}
