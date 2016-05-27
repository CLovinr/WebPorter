package com.chenyg.wporter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.chenyg.wporter.base.RequestMethod;
import com.chenyg.wporter.log.WPLog;

/**
 * <pre>
 * 参数处理器的管理器:
 * 1.会先从ParamGeterDealt获取对应名称的参数值
 * 2.若获取为null，则从request.getParameter(name)获取
 * </pre>
 *
 * @author ZhuiFeng
 */
public class ParamSourceHandleManager implements Serializable
{

    class SecureParamDealEx extends Exception
    {

    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private HashMap<RequestMethod, ParamSourceHandle> map = new HashMap<RequestMethod, ParamSourceHandle>(10);




    public ParamSourceHandleManager addParamHandles(RequestMethod method, ParamSourceHandle... paramSourceHandles)
    {
        if (paramSourceHandles != null)
        {
            for (ParamSourceHandle paramSourceHandle : paramSourceHandles)
            {
                map.put(method, paramSourceHandle);
            }
        }

        return this;
    }

    public ParamSourceHandleManager addParamHandles(ParamSourceHandle paramSourceHandle, RequestMethod... methods)
    {
        if (paramSourceHandle != null)
        {
            for (RequestMethod method : methods)
            {
                map.put(method, paramSourceHandle);
            }
        }

        return this;
    }

    public ParamSourceHandleManager addParamHandles(Map<RequestMethod, ParamSourceHandle> map)
    {
        if (map != null)
        {
            this.map.putAll(map);
        }

        return this;
    }


    private class TempParamSource extends ParamSource
    {
        private ParamSource geter;
        private WPRequest request;

        public TempParamSource(WPObject wpObject, RequestMethod method,
                WPRequest request,
                WPorterAndMethod wpfr, WPLog<?> wpLog)
        {
            this.request = request;
            ParamSourceHandle dealt = map.get(method);

            if (dealt != null)
            {
                geter = dealt.getParamSource(wpObject, method, request, wpfr, wpLog);
            }
        }

        @Override
        public Object getParameter(String name)
        {
            Object object = null;

            if (geter != null)
            {
                object = geter.getParameter(name);
            }

            if (object == null)
            {
                object = request.getParameter(name);
            }



            return object;
        }
    }


    ParamSource getParamSource(WPObject wpObject, RequestMethod method, WPRequest request,
            WPorterAndMethod wpfr, WPLog<?> wpLog)
    {
        ParamSource paramSource = new TempParamSource(wpObject, method, request, wpfr, wpLog);
        return paramSource;
    }
}
