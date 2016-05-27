package com.chenyg.wporter;

import java.util.Map;

/**
 * 缺乏必须参数的异常类。
 */
public class LackParamsException extends Exception
{
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    private String[] lackParamNames;
    private Map<String, String> descMap;
    

    /**
     * 
     * @param lackParamNames 缺少的参数名
     */
    public LackParamsException(String[] lackParamNames,Map<String, String> descMap)
    {
	this.lackParamNames = lackParamNames;
	this.descMap=descMap;
    }
    
    /**
     * 得到参数描述map.
     * @return
     */
    public Map<String, String> getNParamsDescMap()
    {
	return descMap;
    }

    /**
     * 得到缺少的参数.
     * 
     * @return
     */
    public String[] getLackParamNames()
    {
	return lackParamNames;
    }
}