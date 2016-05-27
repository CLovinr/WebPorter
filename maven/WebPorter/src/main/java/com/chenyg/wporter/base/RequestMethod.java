package com.chenyg.wporter.base;

/**
 * 请求方法
 * 
 * @author Administrator
 *
 */
public enum RequestMethod
{
    POST, GET, OPTIONS, HEAD, PUT, DELETE, TARCE, WS_OPEN, 
    /**
     * 关闭时，会调用对应的函数，并传人一个jResponse
     */
    WS_DISCONNECT

}