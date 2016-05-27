package com.chenyg.wporter;

/**
 * 用于初始化时获取配置参数
 *
 * @author ZhuiFeng
 */
public interface InitParamSource
{
    /**
     * 得到初始化的参数值
     *
     * @param name
     * @return
     */
    String getInitParameter(String name);
}
