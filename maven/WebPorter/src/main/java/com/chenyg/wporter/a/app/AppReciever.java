package com.chenyg.wporter.a.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppReciever
{
    ArrayList<Object> list = new ArrayList<Object>();

    HashMap<String, String> headers = new HashMap<String, String>();

    int errorCode = 0;
    int statusCode = 0;
    
    /**
     * 得到状态码
     * @return
     */
    public int getStatusCode()
    {
	return statusCode;
    }

    /**
     * 默认为0，表示无错误。
     * 
     * @return
     */
    public int getErrorCode()
    {
	return errorCode;
    }

    public Map<String, String> getHeaders()
    {
	return headers;
    }
    
    /**
     * 得到响应的第一个元素。
     * @return
     */
    public Object getFirstObject(){
	if (list.size()>0)
	{
	    return list.get(0);
	}else {
	     return null;
	}
    }

    /**
     * 得到顺序响应的结果对象.当出现异常时，结果只有一个元素Exception
     * 
     * @return
     */
    public List<Object> getResponse()
    {
	return list;
    }
}
