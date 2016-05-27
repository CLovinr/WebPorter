package com.chenyg.wporter;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.chenyg.wporter.annotation.*;
import com.chenyg.wporter.log.WPLog;
import com.chenyg.wporter.util.MyClassLoader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chenyg.wporter.base.RequestMethod;
import com.chenyg.wporter.util.PackageUtil;
import com.chenyg.wporter.util.WPTool;

/**
 * 用来扫描webporter.
 *
 * @author ZhuiFeng
 */
public class WPorterSearcher implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    static class MyMethod<T>
    {
        T method;
        String[] udesc;

        public MyMethod(T method, WPorter._ParamExtra paramExtra)
        {
            this.method = method;
            if (paramExtra != null)
            {
                if (paramExtra.cnsdesc().length > 0)
                {
                    udesc = paramExtra.cnsdesc();
                } else
                {
                    udesc = null;
                }
            }
        }
    }

    static class MethodTied<T> implements Serializable
    {
        /**
         *
         */
        private static final long serialVersionUID = 1L;
        /**
         * methodTiedName:Method; 对于rest,则
         */
        HashMap<String, MyMethod<T>> methods;
        Object webPorter;
        /**
         * 父绑定是否以正斜杠结尾
         */
        boolean endsWithForwardslash;

        ThinkType thinkType;

        public MethodTied(Object webPorter)
        {
            this.webPorter = webPorter;
        }

        private String getTiedName(WPorter._ChildIn childIn, String methodName, String funStr, WPLog<?> wpLog)
        {

            switch (childIn.method())
            {
                case WS_DISCONNECT:
                    wpLog.warn("\t\tadd:" + RequestMethod.WS_DISCONNECT.name()
                            + funStr);
                    return RequestMethod.WS_DISCONNECT.name();
                case WS_OPEN:
                    wpLog.warn("\t\tadd:" + RequestMethod.WS_OPEN.name()
                            + funStr);
                    return RequestMethod.WS_OPEN.name();
                default:
                    String tiedName = childIn.tiedName().equals("") ? methodName
                            : childIn.tiedName();

                    wpLog.warn("\t\tadd:" + tiedName
                            + ","
                            + childIn.method() + funStr);
                    return tiedName;
            }
        }

        public void addMethod(T method, String methodName,
                WPorter._ChildIn childIn, ThinkType fthinkType, WPLog<?> wpLog)
        {
            String funStr = "\t(" + (method instanceof Method ? "fun" : "call name") + ":"
                    + methodName
                    + ")";

            MyMethod<T> myMethod = new MyMethod<T>(method,
                    method instanceof Method ? (((Method) method)
                            .isAnnotationPresent(ParamExtra.class) ? new WPorter._ParamExtra_(((Method) method)
                            .getAnnotation(
                                    ParamExtra.class)) : null) : ((WebPorterDynamic.ChildMethod) method).paramExtra);


            if (fthinkType == ThinkType.REST && childIn.thinktype() == ThinkType.REST)
            {
                methods.put(ThinkType.REST.name(), myMethod);
                wpLog.warn("\t\tadd-rest:" + childIn.method() + funStr);
            } else
            {

                String childbind = getTiedName(childIn, methodName, funStr, wpLog);

                if (childbind.equals(ThinkType.REST.name()))
                {
                    throw new RuntimeException("the tiedName should not be " + ThinkType.REST.name()
                            + " at method "
                            + methodName);
                }

                methods.put(childbind, myMethod);

            }

        }


        /**
         * 对于绑定名。若以正斜杠结尾，则会去掉它。
         *
         * @param webPorter
         * @return
         */
        static String getFTiedName(WebPorter webPorter)
        {
            String tiedName = webPorter.getTiedName();
            if (tiedName.endsWith("/"))
            {
                tiedName = tiedName.substring(0, tiedName.length() - 1);
            }
            return tiedName;
        }


        /**
         * 对于绑定名。若以正斜杠结尾，则会去掉它。
         *
         * @param fatherIn
         * @return
         */
        static String getFTiedName(WPorter._FatherIn fatherIn)
        {
            String tiedName = fatherIn.tiedName();
            if (tiedName.endsWith("/"))
            {
                tiedName = tiedName.substring(0, tiedName.length() - 1);
            }
            return tiedName;
        }


        /**
         * 对于绑定名。若以正斜杠结尾，endsWithForwardslash变为真。
         *
         * @param thinkType
         * @return
         */
        void init(ThinkType thinkType)
        {
            this.thinkType = thinkType;
            methods = new HashMap<String, MyMethod<T>>(5);
            if (webPorter instanceof WebPorterDynamic)
            {
                endsWithForwardslash = ((WebPorterDynamic) webPorter).tiedName().endsWith("/");
            } else
            {
                endsWithForwardslash = ((WebPorter) webPorter).getTiedName().endsWith("/");
            }


        }

        @Override
        public final int hashCode()
        {
            return webPorter.hashCode();
        }

        @Override
        public final boolean equals(Object obj)
        {
            if (obj != null)
            {
                if (obj instanceof WebPorter)
                {
                    return webPorter.equals(obj);
                } else if (obj instanceof MethodTied)
                {
                    MethodTied methodTied = (MethodTied) obj;
                    return webPorter.equals(methodTied.webPorter);
                }
            }
            return false;
        }
    }

    /*
     * <请求方法<父绑定名，MethodTied>
     */
    private Map<RequestMethod, Map<String, MethodTied>> normalportersMap;
    private Map<RequestMethod, Map<String, MethodTied>> sysportersMap;
    private Map<RequestMethod, Map<String, MethodTied>> dynamicPorters;
    private WPLog<?> wpLog;


    WPorterSearcher(WPLog<?> wpLog)
    {
        setWPLog(wpLog);
        sysportersMap = initMap(false);
        normalportersMap = initMap(false);
        dynamicPorters = initMap(true);
    }


    void setWPLog(WPLog<?> wpLog)
    {
        this.wpLog = wpLog;
    }


    private Map<RequestMethod, Map<String, MethodTied>> initMap(boolean isConcurrent)
    {
        RequestMethod[] methods = RequestMethod.values();
        Map<RequestMethod, Map<String, MethodTied>> map;
        if (isConcurrent)
        {
            map = new ConcurrentHashMap<RequestMethod, Map<String, MethodTied>>();
        } else
        {
            map = new HashMap<RequestMethod, Map<String, MethodTied>>();
        }

        for (RequestMethod requestMethod : methods)
        {
            if (isConcurrent)
            {
                map.put(requestMethod, new ConcurrentHashMap<String, MethodTied>(10));
            } else
            {
                map.put(requestMethod, new HashMap<String, MethodTied>(10));
            }

        }

        return map;

    }

    /**
     * 清除所有接口
     *
     * @param isSys 是否是sys类型的接口
     */
    public void clear(boolean isSys)
    {
        if (isSys)
        {
            sysportersMap.clear();
            sysportersMap = initMap(false);
        } else
        {
            normalportersMap.clear();
            normalportersMap = initMap(false);
        }
    }

    /**
     * 清除所有动态接口
     */
    public void clearDynamic()
    {
        dynamicPorters.clear();
        dynamicPorters = initMap(true);
    }


    /**
     * 清除指定动态接口
     *
     * @param set 动态接口集
     */
    public void clearDynamic(Set<WebPorterDynamic> set)
    {
        _clear(set, true, false);
    }


    /**
     * 清除指定接口
     *
     * @param set
     */
    private void _clear(Set<?> set, boolean isDynamic, boolean isSys)
    {
        if (set == null)
        {
            return;
        }
        Iterator<Map<String, MethodTied>> iterator = isDynamic ? dynamicPorters.values()
                .iterator() : (isSys ? sysportersMap.values().iterator()
                : normalportersMap.values().iterator());

        while (iterator.hasNext() && set.size() > 0)
        {
            Map<String, MethodTied> hashMap = iterator.next();
            Iterator<MethodTied> iterator2 = hashMap.values().iterator();
            while (iterator2.hasNext())
            {
                MethodTied methodTied = iterator2.next();
                if (set.contains(methodTied.webPorter))
                {
                    wpLog.warn("remove:", methodTied.webPorter.getClass().toString(), "\n");
                    iterator2.remove();
                    set.remove(methodTied.webPorter);
                }
            }
        }
    }


    /**
     * 清除指定接口
     *
     * @param set 要清除的接口集
     */
    public void clear(Set<WebPorter> set, boolean isSys)
    {
        _clear(set, false, isSys);
    }


    public static class SeekPackages
    {
        JSONArray jsonArray = new JSONArray();

        public SeekPackages()
        {

        }

        public SeekPackages addPorters(boolean isSys, String... packages)
        {
            JSONObject jsonObject = new JSONObject();
            try
            {
                jsonObject.put(Config.NAME_WEBPORTER_PATHS_isSys, isSys);

                JSONArray array = new JSONArray();
                for (int i = 0; i < packages.length; i++)
                {
                    array.put(packages[i]);
                }
                jsonObject.put(Config.NAME_WEBPORTER_PATHS_porters, array);
                jsonArray.put(jsonObject);
            } catch (JSONException e)
            {

                e.printStackTrace();
            }
            return this;
        }
    }

    /**
     * 搜索接口类。
     *
     * @param seekPackages 用于搜索的
     * @param classLoader  类加载器
     */
    public void seek(@NotNULL SeekPackages seekPackages, @MayNULL ClassLoader classLoader)
    {
        init(seekPackages.jsonArray, classLoader);
    }

    void init(@NotNULL JSONArray packagePaths, @MayNULL ClassLoader classLoader)
    {
        if (packagePaths == null)
        {
            return;
        }
        MyClassLoader myClassLoader = null;
        if (classLoader != null && (classLoader instanceof MyClassLoader))
        {


            ArrayList<String> packagesList = new ArrayList<>();

            for (int i = 0; i < packagePaths.length(); i++)
            {
                JSONObject jsonObject;
                try
                {
                    jsonObject = packagePaths.getJSONObject(i);
                    JSONArray porters = jsonObject.getJSONArray(Config.NAME_WEBPORTER_PATHS_porters);
                    for (int j = 0; j < porters.length(); j++)
                    {
                        packagesList.add(porters.getString(j));
                    }

                } catch (JSONException e)
                {
                    wpLog.error(e.getMessage(), e);
                }

            }

            String[] packages = packagesList.toArray(new String[0]);
            myClassLoader = (MyClassLoader) classLoader;
            myClassLoader.setPackages(packages);
            myClassLoader.seek();
        }

        for (int i = 0; i < packagePaths.length(); i++)
        {
            JSONObject jsonObject;
            try
            {
                jsonObject = packagePaths.getJSONObject(i);
                JSONArray porters = jsonObject.getJSONArray(Config.NAME_WEBPORTER_PATHS_porters);
                boolean isSys = jsonObject.getBoolean(Config.NAME_WEBPORTER_PATHS_isSys);
                for (int j = 0; j < porters.length(); j++)
                {
                    seekPackage(porters.getString(j), isSys, classLoader);
                }

            } catch (JSONException e)
            {
                wpLog.error(e.getMessage(), e);
            }

        }

        if (myClassLoader != null)
        {
            myClassLoader.release();
        }
    }

    /**
     * 给指定的实例端口来扫描
     *
     * @param set   为null则直接返回
     * @param isSys 是否是系统接口
     */
    public void seek(Set<WebPorter> set, boolean isSys)
    {
        if (set == null)
        {
            return;
        }
        Iterator<WebPorter> iterator = set.iterator();
        while (iterator.hasNext())
        {
            WebPorter webPorter = iterator.next();
            try
            {
                addPorter(webPorter, isSys);
            } catch (Exception e)
            {
                wpLog.error(e.getMessage(), e);
            }

        }
    }

    /**
     * 退出时调用
     */
    public void destroy()
    {
        _destroy(sysportersMap);
        sysportersMap = null;
        _destroy(normalportersMap);
        normalportersMap = null;
    }

    private void _destroy(Map<RequestMethod, Map<String, MethodTied>> map)
    {
        Iterator<Map<String, MethodTied>> iterator = map.values().iterator();
        while (iterator.hasNext())
        {
            Map<String, MethodTied> hashMap = iterator.next();
            Iterator<MethodTied> iterator2 = hashMap.values().iterator();
            while (iterator2.hasNext())
            {
                Object _webPorter = iterator2.next().webPorter;
                if (_webPorter instanceof WebPorter)
                {
                    WebPorter webPorter = (WebPorter) _webPorter;
                    try
                    {
                        if (!webPorter.isAlreadyExited)
                        {
                            webPorter.onExiting();
                            webPorter.isAlreadyExited = true;
                        }

                    } catch (Exception e)
                    {
                        wpLog.error(e.getMessage(), e);
                    }
                }

            }

        }
        map.clear();

    }

    private void seekPackage(String porter, boolean isSys, ClassLoader classLoader)
    {
        wpLog.warn("扫描包：", porter);
        List<String> classeses = PackageUtil.getClassName(porter);
        for (int i = 0; i < classeses.size(); i++)
        {
            Class<?> c = null;
            try
            {
                c = PackageUtil.newClass(classeses.get(i), classLoader);
                if ((!Modifier.isAbstract(c.getModifiers())) && !Modifier
                        .isPrivate(c.getModifiers()) && WPTool.isSubclassOf(c, WebPorter.class))
                {
                    Constructor<?> constructor = c.getDeclaredConstructor();
                    constructor.setAccessible(true);

                    wpLog.warn( "at "+ c.getName() + ".<init>(" + c.getSimpleName() + ".java:1)");

                    WebPorter webPorter = (WebPorter) constructor.newInstance();
                    addPorter(webPorter, isSys);
                }
            } catch (Exception e)
            {
                wpLog.error(c);
                wpLog.error(e.getMessage(), e);
            }
        }
    }


    /**
     * 添加动态接口
     *
     * @param webPorterDynamic 动态接口类
     */
    public void addPorter(final WebPorterDynamic webPorterDynamic)
    {
        if (webPorterDynamic.wPorterSearcher != null)
        {
            throw new RuntimeException("it is already been added!(" + webPorterDynamic.tiedName() + ")");
        } else
        {
            webPorterDynamic.wPorterSearcher = this;
        }

        String prefix = null;
        Map<String, MethodTied> map = null;


        WPorter._FatherIn fatherIn = webPorterDynamic;
        wpLog.warn("扫描WebPorter(thinkType="
                + fatherIn.thinkType().name()
                + ",defaultCheck='" + fatherIn.defaultCheck() + "')");

        prefix = fatherIn.tiedName();

        wpLog.warn("\tfather:" + prefix);
        MethodTied methodTied;
        String ftiedName = MethodTied.getFTiedName(fatherIn);

        Iterator<WebPorterDynamic.ChildMethod> iterator = new Iterator<WebPorterDynamic.ChildMethod>()
        {
            Iterator<WebPorterDynamic.ChildMethod> normal = webPorterDynamic.normalMethods.values().iterator();
            Iterator<WebPorterDynamic.ChildMethod> rest = webPorterDynamic.restMethods.values().iterator();

            @Override
            public boolean hasNext()
            {
                return normal.hasNext() || rest.hasNext();
            }

            @Override
            public WebPorterDynamic.ChildMethod next()
            {
                return normal.hasNext() ? normal.next() : rest.next();
            }

            @Override
            public void remove()
            {
                if (normal.hasNext())
                {
                    normal.remove();
                } else
                {
                    rest.remove();
                }
            }
        };
        while (iterator.hasNext())
        {
            WebPorterDynamic.ChildMethod childMethod = iterator.next();
            WPorter._ChildIn childIn = childMethod.childIn;
            // 根据方法，存放不同的HashMap
            map = dynamicPorters.get(childIn.method());
            methodTied = map.get(ftiedName);
            if (methodTied == null)
            {
                methodTied = new MethodTied(webPorterDynamic);
                methodTied.init(fatherIn.thinkType());
            }
            map.put(ftiedName, methodTied);
            methodTied.addMethod(childMethod, childMethod.getName(), childIn, fatherIn.thinkType(), wpLog);
        }
        wpLog.warn("***********************************************************");
    }

    /**
     * 添加WPorter实例
     *
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private void addPorter(WebPorter webPorter, boolean isSys) throws InstantiationException, IllegalAccessException
    {
        Map<String, MethodTied> map;


        Class<?> cClass = webPorter.getClass();

        FatherIn fatherIn = cClass.getAnnotation(FatherIn.class);
        wpLog.warn("扫描WebPorter(isSys=" + isSys
                + ",thinkType="
                + fatherIn.thinkType().name()
                + ",useClassName=" + fatherIn.useClassName() + ",defaultCheck='" + fatherIn.defaultCheck() + "')");

        String ftiedName = MethodTied.getFTiedName(webPorter);

        wpLog.warn("\tfather:" + webPorter.getTiedName());
        Method[] methods = cClass.getMethods();

        MethodTied methodTied;

        for (Method method : methods)
        {
            if (method.isAnnotationPresent(ChildIn.class))
            {

                ChildIn childIn = method.getAnnotation(ChildIn.class);
                // 根据方法，存放不同的HashMap
                map = getHashMap(childIn.method(), isSys);
                methodTied = map.get(ftiedName);
                if (methodTied == null)
                {
                    methodTied = new MethodTied(webPorter);
                    methodTied.init(fatherIn.thinkType());
                }
                map.put(ftiedName, methodTied);
                methodTied.addMethod(method, method.getName(), new WPorter._ChildIn_(childIn, childIn.tiedName()),
                        fatherIn.thinkType(), wpLog);
            }
        }
        wpLog.warn("***********************************************************");
    }

    private Map<String, MethodTied> getHashMap(RequestMethod method, boolean isSys)
    {
        Map<String, MethodTied> hashMap = isSys ? getSysHashMap(method)
                : getNormalHashMap(method);
        return hashMap;
    }

    private Map<String, MethodTied> getSysHashMap(RequestMethod method)
    {
        Map<String, MethodTied> map = sysportersMap.get(method);
        return map;
    }


    private Map<String, MethodTied> getNormalHashMap(RequestMethod method)
    {
        Map<String, MethodTied> map = normalportersMap.get(method);
        return map;
    }


    private MethodTied getDynamicMethodTied(RequestMethod method, String fTiedName)
    {
        MethodTied methodTied = null;
        Map<String, MethodTied> map = dynamicPorters.get(method);
        if (map != null)
        {
            methodTied = map.get(fTiedName);
        }
        return methodTied;
    }

    private MethodTied getMethodTied(RequestMethod method, String fTiedName)
    {
        MethodTied methodTied = null;
        Map<String, MethodTied> map = getSysHashMap(method);
        if (map != null)
        {
            methodTied = map.get(fTiedName);
        }
        if (methodTied == null)
        {
            map = getNormalHashMap(method);
            if (map != null)
            {
                methodTied = map.get(fTiedName);
            }
        }


        return methodTied;
    }

    /**
     * 根据请求方法和uri得到绑定的相关接口与函数。
     *
     * @param method 请求的方法
     * @param uri    资源地址
     */
    public WPorterAndMethod getWPorterAndMethod(RequestMethod method, String uri,
            String urlEncoding) throws UnsupportedEncodingException
    {

        WPorterAndMethod wpfr = null;

        int indexWhen = uri.indexOf("?");// 查询参数分隔

        // 最后一个正斜杠的位置
        int indexForward = uri.lastIndexOf('/', indexWhen == -1 ? uri.length() - 1
                : indexWhen - 1);

        int fTiedEndIndex = indexForward > 0 ? indexForward
                : (indexWhen > 0 ? indexWhen : -1);
        String fTiedName = fTiedEndIndex == -1 ? ""
                : uri.substring(0, fTiedEndIndex);

        MethodTied<?> methodTied = getMethodTied(method, fTiedName);

        String childBind = indexForward != -1 ? (uri.substring(indexForward + 1, indexWhen == -1 ? uri.length()
                : indexWhen))
                : (uri.substring(0, indexWhen == -1 ? uri.length()
                : indexWhen));
        childBind = URLDecoder.decode(childBind, urlEncoding);

        wpfr = getWPorterAndMethod(methodTied, childBind, false);
        //dynamic
        if (wpfr == null)
        {
            methodTied = getDynamicMethodTied(method, fTiedName);
            wpfr = getWPorterAndMethod(methodTied, childBind, true);
        }

        return wpfr;
    }

    private WPorterAndMethod getWPorterAndMethod(MethodTied<?> methodTied, String childBind, boolean isDynamic)
    {
        WPorterAndMethod wpfr = null;
        if (methodTied != null)
        {


            ThinkType thinkType = methodTied.thinkType;

            MyMethod mthd = methodTied.methods.get(childBind);
            String ctiedName = childBind;
            if (mthd != null)
            {
                childBind = null;
            } else if (thinkType == ThinkType.REST)
            {
                mthd = methodTied.methods.get(ThinkType.REST.name());
            }
            if (mthd != null)
            {
                if (isDynamic)
                {
                    WebPorterDynamic webPorterDynamic = (WebPorterDynamic) methodTied.webPorter;
                    wpfr = new WPorterAndMethod(
                            new WPorter(webPorterDynamic, (WebPorterDynamic.ChildMethod) mthd.method),
                            mthd.udesc);
                } else
                {
                    WebPorter webPorter = (WebPorter) methodTied.webPorter;
                    wpfr = new WPorterAndMethod(new WPorter(webPorter, (Method) mthd.method, ctiedName), mthd.udesc);
                }
                wpfr.thinkType = thinkType;
                wpfr.childBind = childBind;
            }

        }
        return wpfr;
    }
}
