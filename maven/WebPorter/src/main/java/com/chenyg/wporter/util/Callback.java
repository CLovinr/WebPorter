package com.chenyg.wporter.util;

public interface Callback<T>
{
    /**
     * 转换成功的回调函数
     * @param t
     */
    public void success(T t);
    
    /**
     * 转换失败的回调函数
     * @param e
     */
    public void error(Exception e);
}
