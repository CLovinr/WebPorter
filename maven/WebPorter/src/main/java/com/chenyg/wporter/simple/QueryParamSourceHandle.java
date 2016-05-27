package com.chenyg.wporter.simple;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

import com.chenyg.wporter.*;
import com.chenyg.wporter.ParamSourceHandle;
import com.chenyg.wporter.base.RequestMethod;
import com.chenyg.wporter.log.WPLog;


/**
 * 用于处理地址查询参数,若没有指定参数，则从request中查找。
 */
public class QueryParamSourceHandle extends ParamSourceHandle
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final String ENCODE = "utf-8";

    @Override
    public ParamSource getParamSource(WPObject wpObject, RequestMethod method, final WPRequest request,
            WPorterAndMethod wpfr, final
    WPLog<?> wpLog)
    {
        return new ParamSource()
        {
            HashMap<String, String> hashMap = init();

            @Override
            public Object getParameter(String name)
            {
                Object v = hashMap == null ? null : hashMap.get(name);
                if (v == null)
                {
                    v = request.getParameter(name);
                }
                return v;
            }

            private HashMap<String, String> init()
            {
                String uri = request.getRequestURI();
                int index = uri.indexOf('?');
                if (index != -1)
                {
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    int sharpIndex = uri.indexOf("#");
                    if (sharpIndex == -1)
                    {
                        sharpIndex = uri.length();
                    }
                    String[] strs = uri.substring(index + 1, sharpIndex).split("&");
                    try
                    {
                        for (String string : strs)
                        {
                            index = string.indexOf('=');
                            if (index != -1)
                            {
                                hashMap.put(string.substring(0, index),
                                        URLDecoder.decode(string.substring(index + 1), ENCODE));
                            }
                        }
                    } catch (UnsupportedEncodingException e)
                    {
                        wpLog.error(e.getMessage(), e);
                    }
                    return hashMap;
                }
                return null;
            }
        };
    }

}
