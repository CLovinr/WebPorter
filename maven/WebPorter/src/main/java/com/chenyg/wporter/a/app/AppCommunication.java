package com.chenyg.wporter.a.app;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.chenyg.wporter.base.RequestMethod;

public class AppCommunication
{

    private String uri = "";
    //private HashMap<String, String> headers = new HashMap<String, String>();
    private static final HashMap<String, String> EMPTY_HEADERS = new HashMap<>(0);
    private HashMap<String, Object> params = new HashMap<String, Object>();
    private byte[] content;
    //private int errorCode = 0;

    private RequestMethod reqMethod;

    /**
     * get请求.
     *
     * @param requestURI
     */
    public AppCommunication(String requestURI)
    {
        this(requestURI, RequestMethod.GET);
    }

    public AppCommunication(String requestURI, RequestMethod method)
    {
        setReqMethod(method);
        setRequestURI(requestURI);
    }
//
//    /**
//     * 0表示无错误。
//     *
//     * @return
//     */
//    public int getErrorCode()
//    {
//        return errorCode;
//    }
//
//    public void setErrorCode(int errorCode)
//    {
//        this.errorCode = errorCode;
//    }

    public void setContent(Bytesable bytesable)
    {
        this.content = bytesable.toBytes();
    }

    public byte[] getContent()
    {
        return content;
    }

    public AppCommunication()
    {
        this("", RequestMethod.GET);
    }

    public Map<String, String> getHeaders()
    {
        return EMPTY_HEADERS;
    }

    public String getHeader(String name)
    {
        return null;
    }

    public void addHeader(String name, String value)
    {
        //headers.put(name, value);
    }

    /**
     * @param requestURI 为null的话，直接返回。
     */
    public void setRequestURI(String requestURI)
    {
        if (requestURI == null)
        {
            return;
        }
        this.uri = requestURI;
    }

//    /**
//     * 清除
//     *
//     * @param params  是否清除参数
//     * @param headers 是否清除头
//     */
//    public void clear(boolean params, boolean headers)
//    {
////        if (headers)
////        {
////            this.headers.clear();
////        }
//        if (params)
//        {
//            this.params.clear();
//        }
//    }

    /**
     * 得到请求uri
     *
     * @return
     */
    public String getRequestURI()
    {
        return uri;
    }

    /**
     * 得到参数
     *
     * @return
     */
    public Map<String, Object> getParamsMap()
    {
        return params;
    }

    public Object getParam(String name)
    {
        return params.get(name);
    }

    public void addParam(String name, Object value)
    {
        params.put(name, value);
    }

    public void setReqMethod(RequestMethod reqMethod)
    {
        if (reqMethod == null)
        {
            throw new NullPointerException();
        }
        this.reqMethod = reqMethod;
    }

    /**
     * @return 默认为GET
     */
    public RequestMethod getReqMethod()
    {
        return reqMethod;
    }

    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("method=").append(getReqMethod().name()).append("\n");
        stringBuilder.append("header:\n");
        Iterator<String> key = getHeaders().keySet().iterator();
        while (key.hasNext())
        {
            String string = (String) key.next();
            stringBuilder.append(string).append("=").append(getHeaders().get(string)).append("\n");
        }
        stringBuilder.append("param:\n");
        key = params.keySet().iterator();
        while (key.hasNext())
        {
            String string = (String) key.next();
            stringBuilder.append(string).append("=").append(params.get(string)).append("\n");
        }
        return stringBuilder.toString();
    }

}
