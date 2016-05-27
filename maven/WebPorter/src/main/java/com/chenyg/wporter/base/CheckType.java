package com.chenyg.wporter.base;

/**
 * 检测类型
 *
 * @author Administrator
 */
public enum CheckType
{

    /**
     * 不检测
     */
    NONE,
    /**
     * 交由全局检测
     */
    GLOBAL,
    /**
     * 自己检测
     */
    SELF,
    /**
     * 先自己检测，再全局检测
     */
    SELF_GLOBAL,
    /**
     * 先全局检测，再自己检测
     */
    GLOBAL_SELF,
    /**
     * 使用默认的,当没有设置默认检测类型时，为{@linkplain #GLOBAL}
     */
    DEFAULT

}
