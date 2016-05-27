package com.chenyg.wporter;

import java.io.IOException;
import java.io.Serializable;
import java.util.Set;

import com.chenyg.wporter.annotation.MayNULL;
import com.chenyg.wporter.annotation.NotNULL;
import com.chenyg.wporter.log.WPLog;

import com.chenyg.wporter.base.RequestMethod;


/**
 * 对于sys和normal类型的接口，优先使用sys接口。
 *
 * @author ZhuiFeng
 */
public class WPMain implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    WPorterExecuter wPorterExecuter;
    StateListener stateListener;
    private String name;
    private WPorterType porterType;
    private ClassLoader _classLoader;
    private String contextPath;

    /**
     * @param name       标识名称
     * @param porterType 接口类型
     */
    public WPMain(String name, WPorterType porterType, String contextPath)
    {
        this.name = name;
        this.porterType = porterType;
        this.contextPath = dealContextPath(contextPath);
    }

    public String getContextPath()
    {
        return contextPath;
    }

    public String getName()
    {
        return name;
    }

    public WPorterType getPorterType()
    {
        return porterType;
    }

    private static String dealContextPath(String contextPath)
    {
        if (contextPath == null)
        {
            contextPath = "/";
        } else if (!contextPath.startsWith("/"))
        {
            contextPath = "/" + contextPath;
        }
        return contextPath;
    }

    /**
     * 设置类加载器
     *
     * @param classLoader
     * @return
     */
    public WPMain setClassLoader(ClassLoader classLoader)
    {
        this._classLoader = classLoader;
        return this;
    }

    /**
     * 处理请求
     *
     * @param request
     * @param response
     * @param method
     * @throws IOException
     */
    public void doRequest(WPRequest request, WPResponse response, RequestMethod method) throws IOException
    {
        if (wPorterExecuter != null)
        {
            wPorterExecuter.doRequest(request, response, method, porterType);
        }

    }

    /**
     * 清除指定接口
     *
     * @param set
     * @param isSys true sys接口，false normal接口
     */
    public void clearPorters(Set<WebPorter> set, boolean isSys)
    {
        WPorterSearcher wPorterSearcher = wPorterExecuter.getwPorterSearcher();
        if (wPorterSearcher != null)
        {
            wPorterSearcher.clear(set, isSys);
        }
    }

    /**
     * 清除指定接口
     *
     * @param set
     */
    public void clearDynamicPorters(Set<WebPorterDynamic> set)
    {
        WPorterSearcher wPorterSearcher = wPorterExecuter.getwPorterSearcher();
        if (wPorterSearcher != null)
        {
            wPorterSearcher.clearDynamic(set);
        }
    }

    /**
     * 得到WPorterSearcher
     *
     * @return
     */
    public WPorterSearcher getWPorterSearcher()
    {
        return wPorterExecuter.getwPorterSearcher();
    }

    public WPLog<?> getWPLog()
    {
        return wPorterExecuter.getWPLog();
    }

    /**
     * 清除所有接口
     *
     * @param isSys true sys接口，false normal接口
     */
    public void clearAllPorters(boolean isSys)
    {
        WPorterSearcher wPorterSearcher = wPorterExecuter.getwPorterSearcher();
        if (wPorterSearcher != null)
        {
            wPorterSearcher.clear(isSys);
        }
    }

    /**
     * 搜索接口
     *
     * @param set   接口集
     * @param isSys true sys接口，false normal接口
     */
    public void seekPorters(Set<WebPorter> set, boolean isSys)
    {
        WPorterSearcher wPorterSearcher = wPorterExecuter.getwPorterSearcher();
        if (wPorterSearcher != null)
        {
            wPorterSearcher.seek(set, isSys);
        }
    }

    /**
     * 搜索接口类。
     *
     * @param seekPackages 用于搜索的
     * @param classLoader  类加载器
     */
    public void seekPorters(@NotNULL WPorterSearcher.SeekPackages seekPackages, @MayNULL ClassLoader classLoader)
    {
        WPorterSearcher wPorterSearcher = wPorterExecuter.getwPorterSearcher();
        wPorterSearcher.seek(seekPackages, classLoader);
    }

    /**
     * 添加动态接口
     *
     * @param webPorterDynamic
     */
    public void addDynamicPorter(WebPorterDynamic webPorterDynamic)
    {
        WPorterSearcher wPorterSearcher = wPorterExecuter.getwPorterSearcher();
        if (wPorterSearcher != null)
        {
            wPorterSearcher.addPorter(webPorterDynamic);
        }
    }

    /**
     * 清除所有动态接口
     */
    public void clearDynamic()
    {
        WPorterSearcher wPorterSearcher = wPorterExecuter.getwPorterSearcher();
        if (wPorterSearcher != null)
        {
            wPorterSearcher.clearDynamic();
        }
    }

    /**
     * 得到编码方式
     *
     * @return
     */
    public String getEncode()
    {
        return wPorterExecuter.getEncode();
    }

    /**
     * @param paramDealt
     * @param config
     * @param wPorterSearcher
     * @param paramSourceHandleManager
     * @throws InitException
     */
    public void init(ParamDealt paramDealt, String config, WPorterSearcher wPorterSearcher,
            ParamSourceHandleManager paramSourceHandleManager) throws InitException
    {
        WPMainInit wpMainInit = new WPMainInit(_classLoader, wPorterSearcher);
        wpMainInit.init(this, paramDealt, config, null, false, paramSourceHandleManager);
    }

    /**
     * @param paramDealt
     * @param config
     * @param wPorterSearcher
     * @param paramSourceHandleManager
     * @throws InitException
     */
    public void init(ParamDealt paramDealt, Config config, WPorterSearcher wPorterSearcher,
            ParamSourceHandleManager paramSourceHandleManager) throws InitException
    {
        WPMainInit wpMainInit = new WPMainInit(_classLoader, wPorterSearcher);
        wpMainInit.init(this, paramDealt, config, null, false, paramSourceHandleManager);
    }


    /**
     * @param paramDealt
     * @param config
     * @param set
     * @param isSys                    对于set里的，true sys接口，false normal接口
     * @param paramSourceHandleManager
     * @throws InitException
     */
    public void init(ParamDealt paramDealt, Config config, Set<WebPorter> set, boolean isSys,
            ParamSourceHandleManager paramSourceHandleManager) throws InitException
    {
        WPMainInit wpMainInit = new WPMainInit(_classLoader, null);
        wpMainInit.init(this, paramDealt, config, set, isSys, paramSourceHandleManager);
    }

    /**
     * 初始化.
     *
     * @param config 配置内容
     * @param set    指定的webporter实例.可以为null
     * @param isSys  对于set里的，true sys接口，false normal接口
     * @throws InitException
     */
    public void init(ParamDealt paramDealt, String config, Set<WebPorter> set, boolean isSys,
            ParamSourceHandleManager paramSourceHandleManager) throws InitException
    {
        WPMainInit wpMainInit = new WPMainInit(_classLoader, null);
        wpMainInit.init(this, paramDealt, config, set, isSys, paramSourceHandleManager);
    }


    public String getTiedPrefix()
    {
        return wPorterExecuter.getTiedPrefix();
    }

    /**
     * 销毁
     */
    public void destroy()
    {
        if (stateListener != null)
        {
            stateListener.onDestroy(getWPLog());
        }

        if (wPorterExecuter != null)
        {
            wPorterExecuter.getwPorterSearcher().destroy();
            wPorterExecuter = null;
        }

    }

}
