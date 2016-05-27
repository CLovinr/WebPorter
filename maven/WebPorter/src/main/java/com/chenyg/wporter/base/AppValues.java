package com.chenyg.wporter.base;


public abstract class AppValues
{

    /**
     * 得到键名或字段名列表。
     * @return 键名或字段名列表
     */
    public abstract String[] getNames();

    /**
     * 得到键名或字段值列表。
     * @return 键名或字段值列表
     */
    public abstract Object[] getValues();
    
}
