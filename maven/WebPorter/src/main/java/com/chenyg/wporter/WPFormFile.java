package com.chenyg.wporter;

import java.io.IOException;
import java.io.InputStream;

/**
 * WPForm文件。
 */
public abstract class WPFormFile
{
    /**
     * 得到文件名
     *
     */
    public abstract String getName();

    /**
     * 得到文件长度
     *
     */
    public abstract long size();

    /**
     * 得到文件类型
     *
     */
    public abstract String getFileType();

    /**
     * 得到文件输入流
     *
     */
    public abstract InputStream getInputStream() throws IOException;

    @Override
    public String toString()
    {
        return "name=" + getName() + ";size=" + size() + ";type=" + getFileType();
    }
}
