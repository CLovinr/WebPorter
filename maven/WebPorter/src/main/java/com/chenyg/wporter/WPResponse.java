package com.chenyg.wporter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

public interface WPResponse extends WPChat,Serializable
{
    /**
     * 响应错误。
     * 
     * @param code 错误代码
     * @throws IOException 
     */
    void sendError(int code) throws IOException;

    /**
     * 添加头
     * @param name 名称
     * @param value 值
     */
    void addHeader(String name, String value);

    /**
     * 设置某个头的值
     * @param name 名称
     * @param value 值
     */
     void setHeader(String name, String value);

    /**
     * 设置状态码
     * 
     * @param sc 状态码
     */
    void setStatus(int sc);

    /**
     * 得到输出流
     * 
     * @throws IOException
     */
     OutputStream getOutputStream() throws IOException;

    /**
     * 得到字符输出流.
     * 
     * @throws IOException
     */
    WPPrinter getPrinter() throws IOException;
}
