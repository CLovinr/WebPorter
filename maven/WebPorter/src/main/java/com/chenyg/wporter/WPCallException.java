package com.chenyg.wporter;

import com.chenyg.wporter.base.JResponse;
import com.chenyg.wporter.util.WPTool;

/**
 * Created by 刚帅 on 2015/10/23.
 *
 * @see #getJResponse()
 */
public class WPCallException extends RuntimeException
{
    public WPCallException(String msg)
    {
        super(msg);
    }

    public WPCallException(String msg, Throwable throwable)
    {
        super(msg, throwable);
    }

    public WPCallException(Throwable cause)
    {
        super(cause);
    }

    public WPCallException(JResponse jResponse)
    {
        setJResponse(jResponse);
    }

    private JResponse jResponse;

    public void setJResponse(JResponse jResponse)
    {
        this.jResponse = jResponse;
    }

    /**
     * 若返回值不为空则，且该异常被框架捕获，则向客户端返回该对象。
     *
     * @return
     */
    public JResponse getJResponse()
    {
        return jResponse;
    }

    @Override
    public String toString()
    {
        String str = getMessage();

        return WPTool.isNullOrEmpty(str) ? super.toString() : str;
    }
}
