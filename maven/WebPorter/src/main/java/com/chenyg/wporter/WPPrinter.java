package com.chenyg.wporter;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class WPPrinter implements Closeable,Serializable
{

    /**
     * 输出
     * @param object
     * @throws IOException
     */
    public abstract void print(Object object) throws IOException;

    /**
     * 刷新
     */
    public abstract void flush();

    /**
     * 关闭
     * @throws IOException
     */
    public abstract void close() throws IOException;
}
