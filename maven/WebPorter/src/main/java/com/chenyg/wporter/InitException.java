package com.chenyg.wporter;


/**
 * 框架初始化异常类。
 */
public class InitException extends Exception
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public InitException(Throwable throwable)
    {
        super(throwable);
    }

    public InitException(String msg)
    {
        super(msg);
    }

}
