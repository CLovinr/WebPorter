package com.chenyg.wporter.a.app;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

import com.chenyg.wporter.UnsupportedException;
import com.chenyg.wporter.WPRequest;
import com.chenyg.wporter.WPSession;
import com.chenyg.wporter.log.LogUtil;

class AppRequest implements WPRequest
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private AppCommunication communication;
    private AppPorterMain appPorterMain;

    public AppRequest(AppCommunication communication, AppPorterMain appPorterMain)
    {
        this.communication = communication;
        this.appPorterMain = appPorterMain;
    }

    @Override
    public String getCharacterEncoding()
    {
        return "";
    }

    @Override
    public void setCharacterEncoding(String encoding) throws UnsupportedEncodingException
    {

    }

    @Override
    public String getContentType()
    {
        return null;
    }

    @Override
    public void setContentType(String type)
    {

    }

    @Override
    public void setContentLength(int len)
    {

    }

    @Override
    public int getContentLength()
    {
        throw new UnsupportedException("not support!" + LogUtil.getCodePos());
    }

    @Override
    public String getRequestURI()
    {
        return communication.getRequestURI();
    }

    @Override
    public String getHeader(String name)
    {
        return communication.getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaderNames()
    {
        Map<String, String> map = communication.getHeaders();
        return new Hashtable<String, String>(map).keys();
    }

    @Override
    public InputStream getInputStream() throws IOException
    {
        return new ByteArrayInputStream(communication.getContent());
    }

    @Override
    public Object getParameter(String name)
    {
        return communication.getParam(name);
    }

    @Override
    public Enumeration<String> getParameterNames()
    {
        Map<String, Object> map = communication.getParamsMap();
        return new Hashtable<String, Object>(map).keys();
    }


    @Override
    public String getContextPath()
    {
        return appPorterMain.getWpMain().getContextPath();
    }

    @Override
    public String getTiedPrefix()
    {
        return appPorterMain.getWpMain().getTiedPrefix();
    }

    @Override
    public WPSession getSession()
    {
        return null;
        //throw new UnsupportedException("not support!");
    }

}
