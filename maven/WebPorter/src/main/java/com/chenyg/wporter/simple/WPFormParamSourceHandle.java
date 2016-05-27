package com.chenyg.wporter.simple;

import java.io.IOException;


import com.chenyg.wporter.*;
import com.chenyg.wporter.ParamSource;
import com.chenyg.wporter.base.ContentType;
import com.chenyg.wporter.base.RequestMethod;
import com.chenyg.wporter.log.WPLog;

public class WPFormParamSourceHandle extends ParamSourceHandle
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private ParamSourceHandle otherDealt;
    private int bufSize;
    private String encoding = "utf-8";

    /**
     * @param otherDealt 当不为WPForm时的处理
     * @param bufSize    缓存数组的大小
     */
    public WPFormParamSourceHandle(ParamSourceHandle otherDealt, int bufSize)
    {
        this.otherDealt = otherDealt;
        if (bufSize < 128)
        {
            throw new RuntimeException("the bufSize is too small!");
        }
        this.bufSize = bufSize;
    }

    @Override
    public ParamSource getParamSource(WPObject wpObject, RequestMethod method, WPRequest request, WPorterAndMethod wpfr,
            WPLog<?> wpLog)
    {
        String type = request.getContentType();
        if (type != null && type.indexOf(ContentType.WPORTER_FORM.getType()) != -1
                || (type = request.getHeader(WPForm.HEADER_NAME)) != null
                && type.indexOf(ContentType.WPORTER_FORM.getType()) != -1)
        {
            try
            {
                WPForm wpForm = WPFormBuilder.decode(request.getInputStream(), bufSize, encoding,true);
                wpObject._setWPForm(wpForm);
                return wpForm;
            } catch (IOException e)
            {
                try
                {
                    throw e;
                } catch (IOException e1)
                {
                    throw new RuntimeException(e.getMessage());
                }
            }
        } else if (otherDealt != null)
        {
            return otherDealt.getParamSource(wpObject, method, request, wpfr, wpLog);
        }

        return null;
    }


}
