package com.chenyg.wporter;


import java.io.Serializable;

/**
 * 用于得到参数
 *
 * @author ZhuiFeng
 */
public abstract class ParamSource implements Serializable
{
    /**
     * 根据名称得到参数
     *
     * @param name 参数名称
     * @return 返回参数值
     */
    public abstract Object getParameter(String name);

}
