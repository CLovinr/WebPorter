package com.chenyg.wporter.util;

public interface Foreach<T>
{
    /**
     * 
     * @param t
     * @return 返回true，继续；false，中断。
     */
    public boolean each(T t);
}
