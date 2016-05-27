package com.chenyg.wporter.simple;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.chenyg.wporter.*;
import com.chenyg.wporter.ParamSourceHandle;
import com.chenyg.wporter.base.ContentType;
import com.chenyg.wporter.base.RequestMethod;
import com.chenyg.wporter.log.WPLog;
import com.chenyg.wporter.util.FileTool;

/**
 * put方法的，用于解析application/x-www-form-urlencoded的方法体
 *
 * @author ZhuiFeng
 */
public class PutParamSourceHandle extends ParamSourceHandle
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final Pattern ENCODE_PATTERN = Pattern.compile("charset=([^;]+)");

    /**
     * 得到编码方式，默认为utf-8
     *
     * @param contentType
     * @return
     */
    private String getEncode(String contentType)
    {
        String encode;
        Matcher matcher = ENCODE_PATTERN.matcher(contentType);
        if (matcher.find())
        {
            encode = matcher.group(1);
        } else
        {
            encode = "utf-8";
        }
        return encode;
    }

    @Override
    public ParamSource getParamSource(WPObject wpObject, RequestMethod method, WPRequest request, WPorterAndMethod wpfr,
            WPLog<?> wpLog)
    {
        String ctype = request.getContentType();
        if (ctype != null && ctype.indexOf(ContentType.APP_FORM_URLENCODED.getType()) != -1)
        {
            String encode = getEncode(ctype);
            try
            {
                String body = FileTool.getString(request.getInputStream());
                String[] strs = body.split("&");
                final HashMap<String, String> paramsMap = new HashMap<String, String>(strs.length);
                int index;
                for (String string : strs)
                {
                    index = string.indexOf('=');
                    if (index != -1)
                    {
                        paramsMap.put(string.substring(0, index),
                                URLDecoder.decode(string.substring(index + 1), encode));
                    }
                }
                return new ParamSource()
                {

                    @Override
                    public Object getParameter(String name)
                    {

                        return paramsMap.get(name);
                    }
                };
            } catch (IOException e)
            {
                wpLog.error(e.getMessage(), e);
            }
        }
        return null;
    }

}
