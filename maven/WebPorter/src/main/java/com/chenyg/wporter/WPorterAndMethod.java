package com.chenyg.wporter;


import com.chenyg.wporter.annotation.ThinkType;

public class WPorterAndMethod
{


    public WPorter wPorter;

    /**
     * 为null表示没有描述
     */
    public final String[] udesc;


    public ThinkType thinkType;

    /**
     * 对于rest，则是id;其他的，则是函数绑定名。
     */
    public String childBind;

    public WPorterAndMethod(WPorter wPorter, String[] udesc)
    {
        this.udesc = udesc;
        this.wPorter = wPorter;
    }


}
