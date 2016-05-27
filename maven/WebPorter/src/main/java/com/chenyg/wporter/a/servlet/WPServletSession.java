package com.chenyg.wporter.a.servlet;

import java.util.Enumeration;

import javax.servlet.http.HttpSession;

import com.chenyg.wporter.WPSession;

class WPServletSession extends WPSession
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private HttpSession httpSession;

    public WPServletSession(HttpSession httpSession)
    {
        super(httpSession.getId());
        this.httpSession = httpSession;
    }

    @Override
    public Object getAttribute(String name)
    {
        return httpSession.getAttribute(name);
    }

    @Override
    public void setAttribute(String name, Object value)
    {
        httpSession.setAttribute(name, value);
    }

    @Override
    public void removeAttribute(String name)
    {
        httpSession.removeAttribute(name);
    }

    @Override
    public Enumeration<String> getAttributeNames()
    {
        return httpSession.getAttributeNames();
    }

    @Override
    public boolean isNew()
    {
        return httpSession.isNew();
    }

}
