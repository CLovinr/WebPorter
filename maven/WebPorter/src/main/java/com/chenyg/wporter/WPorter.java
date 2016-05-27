package com.chenyg.wporter;

import com.chenyg.wporter.annotation.*;
import com.chenyg.wporter.base.CheckType;
import com.chenyg.wporter.base.RequestMethod;
import com.chenyg.wporter.base.ResponseType;
import com.chenyg.wporter.log.LogUtil;
import com.chenyg.wporter.util.WPTool;

import java.lang.reflect.Method;

/**
 * Created by 宇宙之灵 on 2015/10/2.
 */
public class WPorter
{
    private _FatherIn fatherIn;
    private _FatherOut fatherOut;
    private _ChildIn childIn;
    private _ChildOut childOut;
    private _ParamExtra paramExtra;
    private InCheckParser inCheckParser;
    private Object webPorter;
    private Object method;

    public WPorter(WebPorterDynamic webPorterDynamic, WebPorterDynamic.ChildMethod childMethod)
    {
        this.fatherIn = webPorterDynamic;
        this.fatherOut = webPorterDynamic;
        this.childIn = childMethod.childIn;
        this.childOut = childMethod.childOut;
        this.paramExtra = childMethod.paramExtra;
        this.inCheckParser = webPorterDynamic.getInCheckParser();
        this.webPorter = webPorterDynamic;
        this.method = childMethod.call;
    }

    public InCheckParser getInCheckParser()
    {
        return inCheckParser;
    }

    /**
     * @param tiedName 绑定名
     * @see WebPorter#getCheckPassable(String)
     */
    public CheckPassable getCheckPassable(String tiedName)
    {
        if (webPorter instanceof WebPorter)
        {
            WebPorter webPorter = (WebPorter) this.webPorter;
            return webPorter.getCheckPassable(tiedName);
        } else
        {
            WebPorterDynamic webPorterDynamic = (WebPorterDynamic) this.webPorter;
            return webPorterDynamic.getCheckPassable(tiedName);
        }
    }

    public WebPorterFun getWebPorterFun()
    {
        return new WebPorterFun()
        {
            @Override
            public Object call(
                    WPObject wpObject) throws WPCallException
            {
                try
                {
                    if (method instanceof WebPorterDynamic.Call)
                    {
                        WebPorterDynamic.Call call = (WebPorterDynamic.Call) method;
                        return call.call(wpObject);
                    } else
                    {
                        Method _m = (Method) method;
                        if (_m.getParameterTypes().length == 0)
                        {
                            return _m.invoke(webPorter);
                        } else
                        {
                            return _m.invoke(webPorter, wpObject);
                        }
                    }
                } catch (WPCallException e)
                {
                    throw e;
                } catch (Exception e)
                {
                    Throwable cause = e.getCause();
                    cause = cause == null ? e : cause;
                    String info = cause.getMessage();
                    info = WPTool.isNullOrEmpty(info) ? cause.toString() : info;
                    info += ":" + LogUtil.toString(cause.getStackTrace()[0]);
                    throw new WPCallException(info,
                            cause);
                }

            }
        };
    }

    public static class _ChildIn_ implements _ChildIn
    {
        private ChildIn childIn;
        private String tiedName;

        public _ChildIn_(ChildIn childIn, String tiedName)
        {
            this.childIn = childIn;
            this.tiedName = tiedName;
        }

        @Override
        public String tiedName()
        {
            return tiedName;
        }

        @Override
        public String[] neceParams()
        {
            return childIn.neceParams();
        }

        @Override
        public ThinkType thinktype()
        {
            return childIn.thinktype();
        }

        @Override
        public String[] unneceParams()
        {
            return childIn.unneceParams();
        }

        @Override
        public String[] innerParams()
        {
            return childIn.innerParams();
        }

        @Override
        public RequestMethod method()
        {
            return childIn.method();
        }

        @Override
        public CheckType checkType()
        {
            return childIn.checkType();
        }

        @Override
        public String checkPassTiedName()
        {
            return childIn.checkPassTiedName();
        }
    }

    private static class _ChildOut_ implements _ChildOut
    {
        private ChildOut childOut;

        public _ChildOut_(ChildOut childOut)
        {
            this.childOut = childOut;
        }

        @Override
        public String[] outers()
        {
            return childOut.outers();
        }

        @Override
        public ResponseType value()
        {
            return childOut.value();
        }
    }

    private static class _FatherIn_ implements _FatherIn
    {
        private FatherIn fatherIn;
        private WebPorter webPorter;

        public _FatherIn_(FatherIn fatherIn, WebPorter webPorter)
        {
            this.fatherIn = fatherIn;
            this.webPorter = webPorter;
        }

        @Override
        public String tiedName()
        {
            return webPorter.getTiedName();
        }


        @Override
        public ThinkType thinkType()
        {
            return fatherIn.thinkType();
        }


        @Override
        public String[] neceParams()
        {
            return fatherIn.neceParams();
        }

        @Override
        public String[] unneceParams()
        {
            return fatherIn.unneceParams();
        }

        @Override
        public String defaultCheck()
        {
            return fatherIn.defaultCheck();
        }
    }

    private static class _FatherOut_ implements _FatherOut
    {
        private FatherOut fatherOut;

        public _FatherOut_(FatherOut fatherOut)
        {
            this.fatherOut = fatherOut;
        }

        @Override
        public String[] outers()
        {
            return fatherOut.outers();
        }
    }

    public static class _ParamExtra_ implements _ParamExtra
    {

        private ParamExtra paramExtra;

        public _ParamExtra_(ParamExtra paramExtra)
        {
            this.paramExtra = paramExtra;
        }

        @Override
        public String[] cnsdesc()
        {
            return paramExtra.cnsdesc();
        }

        @Override
        public String[] cus()
        {
            return paramExtra.cus();
        }
    }

    public WPorter(WebPorter webPorter, Method method, String tiedName)
    {
        this.webPorter = webPorter;
        this.method = method;
        this.inCheckParser = webPorter.getInCheckParser();
        this.childIn = new _ChildIn_(method.getAnnotation(ChildIn.class), tiedName);
        if (method.isAnnotationPresent(ChildOut.class))
        {
            this.childOut = new _ChildOut_(method.getAnnotation(ChildOut.class));
        }
        if (method.isAnnotationPresent(ParamExtra.class))
        {
            this.paramExtra = new _ParamExtra_(method.getAnnotation(ParamExtra.class));
        }
        Class<?> c = webPorter.getClass();
        this.fatherIn = new _FatherIn_(c.getAnnotation(FatherIn.class), webPorter);
        if (c.isAnnotationPresent(FatherOut.class))
        {
            this.fatherOut = new _FatherOut_(c.getAnnotation(FatherOut.class));
        }
    }

    public _ChildIn getChildIn()
    {
        return childIn;
    }

    public _ChildOut getChildOut()
    {
        return childOut;
    }

    public _FatherIn getFatherIn()
    {
        return fatherIn;
    }

    public _FatherOut getFatherOut()
    {
        return fatherOut;
    }

    public _ParamExtra getParamExtra()
    {
        return paramExtra;
    }

    public interface _FatherOut
    {
        String[] outers();
    }

    public interface _FatherIn
    {
        String tiedName();

        ThinkType thinkType();

        String[] neceParams();

        String[] unneceParams();

        String defaultCheck();
    }


    public interface _ChildIn
    {
        String tiedName();

        String[] neceParams();

        ThinkType thinktype();

        String[] unneceParams();

        String[] innerParams();

        RequestMethod method();

        CheckType checkType();

        String checkPassTiedName();
    }

    public interface _ChildOut
    {
        String[] outers();

        ResponseType value();
    }

    public interface _ParamExtra
    {
        String[] cnsdesc();

        String[] cus();
    }

}
