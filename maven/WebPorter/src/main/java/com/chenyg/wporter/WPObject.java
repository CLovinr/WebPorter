package com.chenyg.wporter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.chenyg.wporter.annotation.ChildIn;
import com.chenyg.wporter.annotation.FatherIn;
import com.chenyg.wporter.base.AppValues;
import com.chenyg.wporter.base.RequestMethod;
import com.chenyg.wporter.log.WPLog;
import com.chenyg.wporter.util.FileTool;

/**
 * 接口使用的中间对象。
 */
public class WPObject
{

    /**
     * 参数名称对象。
     */
    public static class InNames
    {

        public InNames(String[] fnNames, String[] fuNames, String[] cnNames,
                String[] cuNames, String[] innerNames)
        {
            this.fnNames = fnNames;
            this.fuNames = fuNames;
            this.cnNames = cnNames;
            this.cuNames = cuNames;
            this.innerNames = innerNames;
        }

        /**
         * FatherIn必须参数的名称
         */
        public final String[] fnNames;
        /**
         * FatherIn非必须参数的名称
         */
        public final String[] fuNames;
        /**
         * ChildIn必须参数的名称
         */
        public final String[] cnNames;
        /**
         * ChildIn非必须参数的名称
         */
        public final String[] cuNames;

        public final String[] innerNames;
    }

    private WPForm wpForm;
    private WPLog<?> wpLog;

    private static final String[] EMPTY_STRS = new String[0];
    private static final Object[] EMPTY_OBJS = new Object[0];

    /**
     * @param appValues 会设置成cu
     */
    public WPObject(AppValues appValues)
    {
        inNames = new InNames(EMPTY_STRS, EMPTY_STRS, EMPTY_STRS, appValues.getNames(), EMPTY_STRS);
        this.fns = EMPTY_OBJS;
        this.fus = EMPTY_OBJS;
        this.cns = EMPTY_OBJS;
        this.cus = appValues.getValues();
        this.inners=EMPTY_OBJS;
    }

    public WPObject(RequestMethod method, WPLog<?> wpLog,
            WPorterType porterType)
    {
        this.method = method;
        this.wpLog = wpLog;
        this.porterType = porterType;
    }

    public WPObject(WPObject wpObject, AppValues appValues)
    {
        this(appValues);
        this.method = wpObject.method;
        this.isDoOptions = wpObject.isDoOptions;
        this.request = wpObject.request;
        this.response = wpObject.response;
        this.wpLog = wpObject.wpLog;
        this.porterType = wpObject.porterType;
        this.id = wpObject.id;

    }

    public void _setWPForm(WPForm wPorterForm)
    {
        if (this.wpForm != null)
        {
            throw new RuntimeException("the WPorterForm is already setted!");
        }
        this.wpForm = wPorterForm;
    }

    public WPForm getWPForm()
    {
        return wpForm;
    }

    private WPorterType porterType;

    /**
     * 请求方法.
     */
    public RequestMethod method;
    /**
     * 参数名称对象。
     */
    public InNames inNames;

    public boolean isDoOptions;
    /**
     * FatherIn必须参数的值.若{@linkplain FatherIn#neceParams()}
     * 含有必须参数的话，正确访问后，对应值会被设置到该成员中。
     */
    public Object[] fns;

    /**
     * FatherIn非必须参数的值.若{@linkplain FatherIn#unneceParams()} ()}
     * 含有非必须参数的话，若提供了对应值会被设置到该成员中，否则对应位置的值为null。
     */
    public Object[] fus;

    /**
     * ChildIn必须参数的值.若{@linkplain ChildIn#neceParams()}
     * 含有必须参数的话，正确访问后，对应值会被设置到该成员中。
     */
    public Object[] cns;

    /**
     * ChildIn非必须参数的值.若{@linkplain ChildIn#unneceParams()}
     * 含有非必须参数的话，若提供了对应值会被设置到该成员中，否则对应位置的值为null。
     */
    public Object[] cus;

    public Object[] inners;
    WPRequest request;
    WPResponse response;
    String id;
    Set<CurrentCloseListener> closeListenerSet;

    /**
     * 得到接口类型
     *
     * @return 类型
     */
    public WPorterType getPorterType()
    {
        return porterType;
    }

    /**
     * 对于rest,它表示对应的资源id，若为""则表示是整组资源； 对于默认的情况，它表示会话id，可能为null。
     *
     * @return 资源id或回话id或null
     */
    public String getId()
    {
        return id != null ? id : (getSession() != null ? getSession().getId()
                : null);
    }

    /**
     * 添加监听。
     *
     * @param closeListener
     */
    public void addCloseListener(CurrentCloseListener closeListener)
    {
        if (closeListenerSet == null)
        {
            closeListenerSet = new HashSet<>(1);
        }
        closeListenerSet.add(closeListener);
    }

    void closeListenerCall(boolean success)
    {

        if (closeListenerSet == null)
        {
            return;
        }

        Iterator<CurrentCloseListener> iterator = closeListenerSet.iterator();
        while (iterator.hasNext())
        {
            try
            {
                iterator.next().afterCloseResponse(success);
            } catch (Exception e)
            {
                if (wpLog != null)
                {
                    wpLog.error(e.toString(), e);
                }
            }
        }

    }

    /**
     * 得到请求的内容
     *
     * @return 返回请求内容体。
     * @throws MyIOException
     */
    public String getContentBody() throws MyIOException
    {
        try
        {
            return getContentBody(request, 1024);
        } catch (IOException e)
        {
            throw new MyIOException(e);
        }
    }

    /**
     * 得到内容
     *
     * @return 返回请求内容体。
     * @throws IOException
     */
    static String getContentBody(WPRequest request, int bufSize) throws IOException
    {

        return FileTool.getString(request.getInputStream(), bufSize, request.getCharacterEncoding());

    }

    public WPRequest getRequest()
    {
        return request;
    }

    public WPResponse getResponse()
    {
        return response;
    }

    public WPSession getSession()
    {
        return request.getSession();
    }


    /**
     * 得到日志记录对象
     *
     * @return 日志记录
     */
    public final WPLog<?> getWPLog()
    {
        return wpLog;
    }
}
