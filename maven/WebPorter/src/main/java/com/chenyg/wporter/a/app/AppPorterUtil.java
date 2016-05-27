package com.chenyg.wporter.a.app;

import com.chenyg.wporter.Config;
import com.chenyg.wporter.InitException;
import com.chenyg.wporter.base.AppValues;
import com.chenyg.wporter.base.RequestMethod;

public class AppPorterUtil
{
    private static AppPorterMain appPorterMain;

    public static void startAppPorterMain(String contextPath, Config config, ClassLoader classLoader)
    {
        AppPorterMain appPorterMain = new AppPorterMain(contextPath);
        try
        {
            appPorterMain.start(config, null, false, classLoader);
            setAppPorterMain(appPorterMain);
        } catch (InitException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static AppPorterMain getAppPorterMain()
    {
        return appPorterMain;
    }

    public static void setAppPorterMain(AppPorterMain appPorterMain)
    {
        AppPorterUtil.appPorterMain = appPorterMain;
    }

    private static class Temp
    {
        private Object object;

        public void setObject(Object object)
        {
            this.object = object;
        }

        public Object getObject()
        {
            return object;
        }

        @Override
        public String toString()
        {
            return object == null ? "null" : object.toString();
        }
    }

    private static void check()
    {
        if (appPorterMain == null)
        {
            throw new AppPorterException(
                    "please call " + AppPorterUtil.class + ".setAppPorterMain(AppPorterMain) first!");
        }
    }

    /**
     * Enum.toString
     *
     * @param prefix
     * @param name
     * @param requestMethods 默认GET
     * @return
     */
    public static Object getPorterObject(Enum<?> prefix, Enum<?> name,
            RequestMethod... requestMethods) throws AppPorterException
    {
        return getPorterObject(prefix.toString(), name.toString(), null, null, requestMethods);
    }

    /**
     * Enum.toString
     *
     * @param prefix
     * @param name
     * @param appValues
     * @param requestMethods 默认GET
     * @return
     */
    public static Object getPorterObject(Enum<?> prefix, Enum<?> name, AppValues appValues,
            RequestMethod... requestMethods) throws AppPorterException
    {
        return getPorterObject(prefix.toString(), name.toString(), appValues == null ? null : appValues.getNames(),
                appValues == null ? null : appValues.getValues(), requestMethods);
    }

    /**
     * Enum.toString
     *
     * @param prefix
     * @param name
     * @param names
     * @param values
     * @param requestMethods 默认GET
     * @return
     */
    public static Object getPorterObject(Enum<?> prefix, Enum<?> name, String[] names,
            Object[] values, RequestMethod... requestMethods) throws AppPorterException
    {
        return getPorterObject(prefix.toString(), name.toString(), names, values, requestMethods);
    }

    /**
     * 得到接口的返回结果，接口必须满足：操作在当前线程，无必须参数
     *
     * @param prefix
     * @param name
     * @param requestMethods 默认GET
     * @return
     */
    public static Object getPorterObject(String prefix, String name,
            RequestMethod... requestMethods) throws AppPorterException
    {
        return getPorterObject(prefix, name, null, null, requestMethods);
    }


    /**
     * 得到接口的返回结果，接口必须满足：操作在当前线程
     *
     * @param prefix
     * @param name
     * @param appValues
     * @param requestMethods 默认GET
     * @return
     */
    public static Object getPorterObject(String prefix, String name, AppValues appValues,
            RequestMethod... requestMethods) throws AppPorterException
    {
        return getPorterObject(prefix, name, appValues == null ? null : appValues.getNames(),
                appValues == null ? null : appValues.getValues(), requestMethods);
    }


    /**
     * 得到接口的返回结果或设置.若是得到值，接口必须满足：操作在当前线程.
     * 若没有添加contextPath前缀，则会自动添加。
     *
     * @param prefix
     * @param name
     * @param names
     * @param values
     * @param requestMethods 默认为GET请求
     * @return
     * @throws AppPorterException
     */
    public static Object getPorterObject(String prefix, String name, String[] names,
            Object[] values, RequestMethod... requestMethods) throws AppPorterException
    {
        check();
        final Temp temp = new Temp();
        String contextPath = appPorterMain.getWpMain().getContextPath();
        if (!prefix.startsWith(contextPath))
        {
            prefix = contextPath + prefix;
        }
        AppCommunication communication = new AppCommunication(prefix + name);
        communication.setReqMethod(requestMethods.length > 0 ? requestMethods[0] : RequestMethod.GET);
        if (names != null && values != null)
        {
            for (int i = 0; i < values.length; i++)
            {
                communication.addParam(names[i], values[i]);
            }
        }

        appPorterMain.getAppPorterSender().send(communication, new AppPorterListener()
        {

            @Override
            public void onResponse(AppReciever appReciever)
            {
                temp.setObject(appReciever.getFirstObject());
            }
        });
        return temp.getObject();
    }
}
