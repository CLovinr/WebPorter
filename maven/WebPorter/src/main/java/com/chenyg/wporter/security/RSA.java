package com.chenyg.wporter.security;

import java.security.*;
import java.security.interfaces.*;

 class RSA
{
    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;

    public RSA(int bits) throws NoSuchAlgorithmException
    {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(bits);
        KeyPair keyPair = keyPairGen.generateKeyPair();

        privateKey = (RSAPrivateKey) keyPair.getPrivate(); // 私钥
        publicKey = (RSAPublicKey) keyPair.getPublic(); // 公钥
    }

    public RSA()
    {

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


}
