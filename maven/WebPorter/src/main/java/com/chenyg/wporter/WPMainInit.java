package com.chenyg.wporter;

import com.chenyg.wporter.annotation.ThinkType;
import com.chenyg.wporter.base.CheckType;
import com.chenyg.wporter.log.WPLog;
import com.chenyg.wporter.util.PackageUtil;
import com.chenyg.wporter.util.WPTool;
import org.json.JSONArray;

import java.util.Set;

/**
 * Created by 宇宙之灵 on 2015/10/20.
 */
class WPMainInit
{
    ClassLoader classLoader;


    public WPLog<?> wpLog;
    public StateListener stateListenerManager;
    public WPorterSearcher wPorterSearcher;
    public CheckPassable globalCheck;
    public String tiedPrefix;
    public String encoding;
    public boolean responseLosedArgs;
    public String defaultCheckType;
    public String notFoundThinkType;
    public ParamSourceHandleManager paramManager;
    public ParamDealt paramDealt;
    public RequestExListener requestExListener;

    WPMainInit(ClassLoader classLoader, WPorterSearcher wPorterSearcher)
    {
        this.classLoader = classLoader;
        this.wPorterSearcher = wPorterSearcher;
        if (classLoader != null)
        {
            Thread.currentThread().setContextClassLoader(classLoader);
        }
    }

    public void init(WPMain wpMain, ParamDealt paramDealt, String config, Set<WebPorter> set, boolean isSys,
            ParamSourceHandleManager paramSourceHandleManager) throws InitException
    {
        this.init(wpMain, paramDealt, Config.toConfig(config), set, isSys, paramSourceHandleManager);
    }


    public void init(WPMain wpMain, ParamDealt paramDealt, Config config, Set<WebPorter> set, boolean isSys,
            ParamSourceHandleManager paramSourceHandleManager) throws InitException
    {
        this.paramDealt = paramDealt;
        InitParamSource sys = config.getPorterParamGeter();
        InitParamSource user = config.getUserParamGeter();
        getWPLog(sys);

        wpLog.warn("init:", wpMain.getName(), ":", wpMain.getPorterType());


        getStateListenerManager(sys);
        if (stateListenerManager != null)
        {
            stateListenerManager.beforeInit(user, wpLog);
        }
        if (wPorterSearcher == null)
        {
            wPorterSearcher = new WPorterSearcher(wpLog);
        }
        seekWebPorters(sys);
        wPorterSearcher.seek(set, isSys);
        getGlobalCheck(sys);
        getTiedPrefix(sys);

        wpLog.warn("\n[**porterPrefix**]:", wpMain.getContextPath(), tiedPrefix);

        getEncoding(sys);
        getResponseLosedArgs(sys);
        getSomeType(sys);
        getRequestExListener(sys);
        initEnd(user, paramSourceHandleManager);

        wpMain.stateListener = stateListenerManager;
        wpMain.wPorterExecuter = new WPorterExecuter(this);
    }

    private void initEnd(InitParamSource user, ParamSourceHandleManager paramSourceHandleManager)
    {
        paramManager = paramSourceHandleManager == null ? new ParamSourceHandleManager() : paramSourceHandleManager;


        if (stateListenerManager != null)
        {
            stateListenerManager.afterInit(user, paramManager, wpLog);
        }
    }

    private void getRequestExListener(InitParamSource initParamSource)
    {
        requestExListener = initHandle(new InitHandle<RequestExListener>()
        {

            @Override
            public RequestExListener handle(InitParamSource paramGeter) throws Exception
            {
                RequestExListener t = null;
                String exListenerClass = paramGeter.getInitParameter(Config.NAME_EXLISTENER);
                if (WPTool.notNullAndEmpty(exListenerClass))
                {

                    Class<?> c = PackageUtil.newClass(exListenerClass, classLoader);
                    t = (RequestExListener) c.newInstance();
                    wpLog.warn(exListenerClass, "\n");
                }

                return t;
            }
        }, "JSONResponse监听器:", "无法初始化JSONResponse监听器!", "JSONResponse监听器为空！", initParamSource);
    }

    private void getSomeType(InitParamSource initParamSource)
    {
        defaultCheckType = initParamSource.getInitParameter(Config.NAME_DEFAULT_CHECKTYPE);
        notFoundThinkType = initParamSource.getInitParameter(Config.NAME_NOT_FOUND_THINKTYPE);

        if (defaultCheckType == null)
        {
            defaultCheckType = CheckType.GLOBAL.name();
        }
        if (notFoundThinkType == null)
        {
            notFoundThinkType = ThinkType.DEFAULT.name();
        }

    }

    private void getResponseLosedArgs(InitParamSource initParamSource)
    {
        responseLosedArgs = false;
        if ("true".equalsIgnoreCase(initParamSource.getInitParameter(Config.NAME_RESPONSE_LOSEDARGS)))
        {
            responseLosedArgs = true;
        }
    }

    /**
     * 得到接口前缀
     *
     * @param initParamSource
     */
    private void getEncoding(InitParamSource initParamSource)
    {
        encoding = initParamSource.getInitParameter(Config.NAME_ENCODING);
        try
        {
            // 保证在编码错误时，使用utf-8编码
            "".getBytes(encoding);
        } catch (Exception e)
        {
            encoding = "utf-8";
        }
    }

    /**
     * 得到接口前缀
     *
     * @param initParamSource
     */
    private void getTiedPrefix(InitParamSource initParamSource)
    {
        tiedPrefix = initParamSource.getInitParameter(Config.NAME_TIED_PREFIX);
    }

    /**
     * 得到全局检测对象
     *
     * @param initParamSource
     */
    private void getGlobalCheck(InitParamSource initParamSource)
    {
        globalCheck = initHandle(new InitHandle<CheckPassable>()
        {

            @Override
            public CheckPassable handle(InitParamSource paramGeter) throws Exception
            {
                CheckPassable checkPassable = null;
                String globleCheckClass = paramGeter.getInitParameter(Config.NAME_GLOBAL_CHECK);
                if (WPTool.notNullAndEmpty(globleCheckClass))
                {
                    Class<?> c = PackageUtil.newClass(globleCheckClass, classLoader);
                    checkPassable = (CheckPassable) c.newInstance();
                    wpLog.warn(globleCheckClass);
                }
                return checkPassable;
            }
        }, "初始化全局检测对象:", "初始化全局检测对象异常!", "无法初始化全局检测对象", initParamSource);

    }

    /**
     * 搜索接口
     *
     * @param initParamSource
     */
    private void seekWebPorters(InitParamSource initParamSource)
    {
        JSONArray packagesArray = initHandle(new InitHandle<JSONArray>()
        {

            @Override
            public JSONArray handle(InitParamSource paramGeter) throws Exception
            {
                String porterPaths = paramGeter.getInitParameter(Config.NAME_WEBPORTER_PATHS);
                return new JSONArray(porterPaths);
            }
        }, "扫描接口：", "接口路径WebPorterPaths格式错误！", null, initParamSource);
        if (packagesArray != null)
        {
            wPorterSearcher.init(packagesArray, classLoader);
        }
    }

    /**
     * 得到状态监听管理器
     *
     * @param initParamSource
     */
    private void getStateListenerManager(InitParamSource initParamSource)
    {
        stateListenerManager = initHandle(new InitHandle<StateListener>()
        {

            @Override
            public StateListener handle(InitParamSource paramGeter) throws Exception
            {
                String listeners = paramGeter.getInitParameter(Config.NAME_STATE_LISTENERS);
                if (listeners == null || "".equals(listeners))
                {
                    return null;
                }
                return new StateListenerManager(new JSONArray(listeners), wpLog, classLoader);
            }
        }, "状态监听器：", "初始化状态监听器异常", "无状态监听器", initParamSource);

    }

    /**
     * 得到日志工具
     *
     * @param initParamSource
     * @throws InitException
     */
    private void getWPLog(InitParamSource initParamSource) throws InitException
    {
        String c = initParamSource.getInitParameter(Config.NAME_WPLOG);
        try
        {
            WPLog<?> wpLog = (WPLog<?>) PackageUtil.newClass(c, classLoader).newInstance();
            this.wpLog = wpLog;
        } catch (Exception e)
        {
            throw new InitException("No WPLog:" + e.getMessage());
        }
    }


    private <T> T initHandle(InitHandle<T> initHandle, String info, String exString,
            String nullString,
            InitParamSource paramGeter)
    {
        T t = null;
        try
        {
            wpLog.warn(info);
            t = initHandle.handle(paramGeter);
            if (t == null && nullString != null)
            {
                wpLog.warn(nullString);
            }
        } catch (Exception e)
        {
            wpLog.warn(exString);
            wpLog.warn(e.getMessage(), e);
        }
        return t;
    }

    /**
     * 用于初始化过程中.
     *
     * @author ZhuiFeng
     */
    interface InitHandle<T>
    {
        public T handle(InitParamSource paramGeter) throws Exception;
    }

}



