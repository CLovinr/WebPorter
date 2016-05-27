package com.chenyg.wporter.a.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.chenyg.wporter.WPMain;
import com.chenyg.wporter.WPRequest;
import com.chenyg.wporter.WPSession;

public class WPServletRequest implements WPRequest
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private HttpServletRequest request;
    private WPMain wpMain;

    WPServletRequest(HttpServletRequest request, WPMain wpMain)
    {
        this.request = request;
        this.wpMain = wpMain;
    }

    @Override
    public String getRequestURI()
    {
        return request.getRequestURI();
    }

    @Override
    public String getHeader(String name)
    {
        return request.getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaderNames()
    {
        return request.getHeaderNames();
    }

    @Override
    public InputStream getInputStream() throws IOException
    {
        return request.getInputStream();
    }

    @Override
    public String getParameter(String name)
    {
        return request.getParameter(name);
    }

    @Override
    public Enumeration<String> getParameterNames()
    {
        return request.getParameterNames();
    }


    @Override
    public String getContextPath()
    {
        return wpMain.getContextPath();
    }

    @Override
    public String getTiedPrefix()
    {
        return wpMain.getTiedPrefix();
    }

    private WPServletSession session;

    @Override
    public WPSession getSession()
    {
        if (session == null)
        {
            session = new WPServletSession(request.getSession());
        }
        return session;
    }

    public HttpServletRequest getServletRequest()
    {
        return request;
    }


    @Override
    public String getCharacterEncoding()
    {
        return request.getCharacterEncoding();
    }

    @Override
    public void setCharacterEncoding(String encoding) throws UnsupportedEncodingException
    {
        request.setCharacterEncoding(encoding);
    }

    @Override
    public String getContentType()
    {
        return request.getContentType();
    }

    /**
     * 什么都不做
     */
    @Override
    public void setContentType(String type)
    {

    }

    /**
     * 什么都不做
     */
    @Override
    public void setContentLength(int len)
    {

    }

    @Override
    public int getContentLength()
    {
        return request.getContentLength();
    }

    public StringBuffer getRequestURL()
    {
        return request.getRequestURL();
    }


    /**
     * 得到host，包含协议。如http://localhost:8080/hello得到的是http://localhost:8080
     *
     * @param url
     * @return
     */
    public static String getHostFromURL(CharSequence url)
    {
        Pattern pattern = Pattern.compile("^(http|https)://([^/]+)");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find())
        {
            return matcher.group();
        } else
        {
            return "";
        }
    }

    public String getHost()
    {
        return getHostFromURL(getRequestURL());
    }

}
