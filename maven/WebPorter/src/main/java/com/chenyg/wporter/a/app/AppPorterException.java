package com.chenyg.wporter.a.app;

public class AppPorterException extends RuntimeException
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public AppPorterException()
    {
    }
    
    public AppPorterException(String info)
    {
	super(info);
    }
    
    public AppPorterException(Throwable throwable)
    {
	super(throwable);
    }

}
