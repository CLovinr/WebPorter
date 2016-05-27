package com.chenyg.wporter;

import java.io.UnsupportedEncodingException;

interface WPChat
{
    /**
     * 得到编码方式。
     *
     * @return
     */
    String getCharacterEncoding();

    /**
     * 设置编码方式
     *
     * @param encoding
     * @throws UnsupportedEncodingException
     */
    void setCharacterEncoding(String encoding) throws UnsupportedEncodingException;

    /**
     * 得到内容类型。
     *
     * @return
     */
    String getContentType();

    /**
     * 设置内容类型
     *
     * @param type
     */
    void setContentType(String type);

    /**
     * 设置内容长度
     *
     * @param len
     */
    void setContentLength(int len);

    /**
     * 得到内容长度
     *
     * @return
     */
    int getContentLength();
}
