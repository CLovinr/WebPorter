package com.chenyg.wporter.a.app;


public abstract class AppPorterSender
{
    public abstract void send(AppCommunication communication, AppPorterListener listener)throws AppPorterException;
}
