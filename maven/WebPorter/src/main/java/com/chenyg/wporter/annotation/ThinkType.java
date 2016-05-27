package com.chenyg.wporter.annotation;

public enum ThinkType
{

    /**
     * 默认类型
     */
    DEFAULT,
    /**
     * 目前只支持json.当某个WebPorter的thinkType=REST,而不为REST的函数依然有效
     * ，并且当有访问时会优先搜索非REST函数，因此，应该保证REST的id不与函数名绑定名一样
     */
    REST
}
