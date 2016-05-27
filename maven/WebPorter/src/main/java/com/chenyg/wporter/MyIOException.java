package com.chenyg.wporter;

public class MyIOException extends RuntimeException
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public MyIOException()
    {
    }

    public MyIOException(String info)
    {
        super(info);
    }

    public MyIOException(Throwable throwable)
    {
        super(throwable);
    }
}
