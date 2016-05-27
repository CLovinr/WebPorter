package com.chenyg.wporter;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Enumeration;

public interface WPRequest extends WPChat, Serializable
{
    /**
     * 得到请求的uri
     */
    String getRequestURI();

    /**
     * 得到某个头的值。
     *
     * @param name 名称
     */
    String getHeader(String name);

    /**
     * 得到所有头的名称
     */
    Enumeration<String> getHeaderNames();

    /**
     * 得到输入流
     *
     * @throws IOException
     */
    InputStream getInputStream() throws IOException;

    /**
     * 得到参数，例如地址参数和表单参数等。
     *
     * @param name 参数名称
     */
    Object getParameter(String name);

    /**
     * 得到所有参数的名称。
     */
    Enumeration<String> getParameterNames();

    /**
     * 得到上下文路径
     */
    String getContextPath();

    /**
     * 得到会话对象。
     *
     * @throws UnsupportedException 有的会不支持如:app
     */
    WPSession getSession() throws UnsupportedException;

    /**
     * 得到绑定的前缀
     * @return
     */
    String getTiedPrefix();
}
