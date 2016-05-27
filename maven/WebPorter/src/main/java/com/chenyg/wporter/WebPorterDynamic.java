package com.chenyg.wporter;

import com.chenyg.wporter.annotation.*;
import com.chenyg.wporter.base.CheckType;
import com.chenyg.wporter.base.RequestMethod;
import com.chenyg.wporter.base.ResponseType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by 宇宙之灵 on 2015/10/3.
 */
public class WebPorterDynamic implements WPorter._FatherIn, WPorter._FatherOut, Serializable
{


    static class ChildMethod
    {
        ChildIn childIn;
        ChildOut childOut;
        ParamExtra paramExtra;
        Call call;

        ChildMethod(ChildIn childIn, ChildOut childOut, ParamExtra paramExtra, Call call)
        {
            this.childIn = childIn;
            this.childOut = childOut;
            this.paramExtra = paramExtra;
            this.call = call;
        }

        public String getName()
        {
            return call.getName();
        }
    }

    private String tiedName = "";
    private ThinkType thinkType = ThinkType.DEFAULT;
    private String[] neceParams = {}, unneceParams = {}, outers = {};
    private String defaultCheck = "";
    private InCheckParser inCheckParser;
    private HashMap<String, CheckPassable> passableMap = null;
    WPorterSearcher wPorterSearcher;
    HashMap<String, ChildMethod> normalMethods = new HashMap<String, ChildMethod>();
    HashMap<RequestMethod, ChildMethod> restMethods = new
            HashMap<RequestMethod, ChildMethod>();

    public WebPorterDynamic()
    {

    }

    ChildMethod getRestChildMethod(RequestMethod method)
    {
        ChildMethod childMethod = restMethods.get(method);
        return childMethod;
    }

    ChildMethod getNormalChildMethod(String childTied, RequestMethod method)
    {
        ChildMethod childMethod = normalMethods.get(childTied);
        if (childMethod != null && childMethod.childIn.method() != method)
        {
            childMethod = null;
        }
        return childMethod;
    }


    public InCheckParser getInCheckParser()
    {
        return inCheckParser;
    }

    public WebPorterDynamic setInCheckParser(InCheckParser inCheckParser)
    {
        this.inCheckParser = inCheckParser;
        return this;
    }


    /**
     * @param tiedName
     * @param checkPassable
     * @see WebPorter#addCheckPassable(String, CheckPassable)
     */
    public void addCheckPassable(String tiedName, CheckPassable checkPassable)
    {
        if (passableMap == null)
        {
            passableMap = new HashMap<String, CheckPassable>();
        }
        passableMap.put(tiedName, checkPassable);
    }

    /**
     * 移除所有绑定的检测对象。
     */
    public WebPorterDynamic removeAllCheckPassable()
    {
        if (passableMap != null)
        {
            passableMap.clear();
        }
        return this;
    }

    /**
     * @param tiedName 绑定名称
     * @see WebPorter#getCheckPassable(String)
     */
    public CheckPassable getCheckPassable(String tiedName)
    {
        return passableMap == null ? null : passableMap.get(tiedName);
    }

    /**
     * 添加
     *
     * @param childIn
     * @param childOut
     * @param paramExtral
     * @return
     */
    public WebPorterDynamic addChildPorter(@NotNULL Call call, @NotNULL ChildIn childIn, ChildOut childOut,
            ParamExtra paramExtral)
    {
        checkNull(childIn);
        checkNull(call);
        ChildMethod childMethod = new ChildMethod(childIn, childOut, paramExtral, call);
        if (childIn.thinktype == ThinkType.REST && thinkType() == ThinkType.REST)
        {
            childIn.setTiedName(ThinkType.REST.name() + childIn.method());
            restMethods.put(childIn.method(), childMethod);
        } else
        {
            normalMethods.put(childIn.tiedName(), childMethod);
        }

        return this;
    }

    public WebPorterDynamic removeFromWPMain()
    {
        if (wPorterSearcher != null)
        {
            Set<WebPorterDynamic> set = new HashSet<WebPorterDynamic>(1);
            set.add(this);
            wPorterSearcher.clearDynamic(set);
            wPorterSearcher = null;
        }
        return this;
    }

    public void clear()
    {
        restMethods.clear();
        normalMethods.clear();
    }

    /**
     * 得到父绑定名
     *
     * @param tiedName
     */
    public WebPorterDynamic setTiedName(String tiedName)
    {
        checkNull(tiedName);
        this.tiedName = tiedName;
        return this;
    }

    /**
     * 设置父绑定名
     *
     * @return
     */
    @Override
    public String tiedName()
    {
        return tiedName;
    }


    /**
     * @param thinkType
     * @see FatherIn#thinkType()
     */
    public WebPorterDynamic setThinkType(ThinkType thinkType)
    {
        checkNull(thinkType);
        this.thinkType = thinkType;
        return this;
    }

    /**
     * @return
     * @see FatherIn#thinkType()
     */
    @Override
    public ThinkType thinkType()
    {
        return thinkType;
    }

    /**
     * @param outers
     * @see FatherOut#outers()
     */
    public WebPorterDynamic setOuters(String[] outers)
    {
        checkNull(outers);
        this.outers = outers;
        return this;
    }

    /**
     * @return
     * @see FatherOut#outers()
     */
    @Override
    public String[] outers()
    {
        return outers;
    }


    /**
     * @param neceParams
     * @see FatherIn#neceParams()
     */
    public WebPorterDynamic setNeceParams(String[] neceParams)
    {
        checkNull(neceParams);
        this.neceParams = neceParams;
        return this;
    }


    /**
     * @return
     * @see FatherIn#neceParams()
     */
    @Override
    public String[] neceParams()
    {
        return neceParams;
    }

    /**
     * @param unneceParams
     * @see FatherIn#unneceParams()
     */
    public WebPorterDynamic setUnneceParams(String[] unneceParams)
    {
        checkNull(unneceParams);
        this.unneceParams = unneceParams;
        return this;
    }

    /**
     * @return
     * @see FatherIn#unneceParams()
     */
    @Override
    public String[] unneceParams()
    {
        return unneceParams;
    }

    /**
     * @param defaultCheck
     * @see FatherIn#defaultCheck()
     */
    public WebPorterDynamic setDefaultCheck(String defaultCheck)
    {
        checkNull(defaultCheck);
        this.defaultCheck = defaultCheck;
        return this;
    }

    /**
     * @see FatherIn#defaultCheck()
     */
    @Override
    public String defaultCheck()
    {
        return defaultCheck;
    }

    private static void checkNull(Object obj)
    {
        if (obj == null) throw new NullPointerException();
    }

    public static class ParamExtra implements WPorter._ParamExtra, Serializable
    {

        private String[] cnsdesc = {}, cus = {};

        /**
         * @return
         * @see com.chenyg.wporter.annotation.ParamExtra#cnsdesc()
         */
        @Override
        public String[] cnsdesc()
        {
            return cnsdesc;
        }

        /**
         * @param cnsdesc
         * @see com.chenyg.wporter.annotation.ParamExtra#cnsdesc()
         */
        public ParamExtra setCnsdesc(String[] cnsdesc)
        {
            checkNull(cnsdesc);
            this.cnsdesc = cnsdesc;
            return this;
        }

        /**
         * @param cus
         * @see com.chenyg.wporter.annotation.ParamExtra#cus()
         */
        public ParamExtra setCus(String[] cus)
        {
            checkNull(cus);
            this.cus = cus;
            return this;
        }

        /**
         * @return
         * @see com.chenyg.wporter.annotation.ParamExtra#cus()
         */
        @Override
        public String[] cus()
        {
            return cus;
        }
    }

    public static class ChildOut implements WPorter._ChildOut, Serializable
    {
        private ResponseType responseType;
        private String[] outers = {};

        public ChildOut(ResponseType responseType)
        {
            setValue(responseType);
        }

        /**
         * @return
         * @see com.chenyg.wporter.annotation.ChildOut#outers()
         */
        @Override
        public String[] outers()
        {
            return outers;
        }

        /**
         * @param outers
         * @see com.chenyg.wporter.annotation.ChildOut#outers()
         */
        public ChildOut setOuters(String[] outers)
        {
            checkNull(outers);
            this.outers = outers;
            return this;
        }

        /**
         * @param responseType
         * @see com.chenyg.wporter.annotation.ChildOut#value()
         */
        public ChildOut setValue(ResponseType responseType)
        {
            checkNull(responseType);
            this.responseType = responseType;
            return this;
        }

        /**
         * @return
         * @see com.chenyg.wporter.annotation.ChildOut#value()
         */
        @Override
        public ResponseType value()
        {
            return responseType;
        }
    }

    public static class ChildIn implements WPorter._ChildIn, Serializable
    {
        private String tiedName;
        private String[] neceParams = {}, unneceParams = {},innerParams={};
        private ThinkType thinktype = ThinkType.DEFAULT;
        private RequestMethod requestMethod = RequestMethod.GET;
        private CheckType checkType = CheckType.DEFAULT;
        private String checkTied = "";

        public ChildIn(String tiedName)
        {
            setTiedName(tiedName);
        }

        /**
         * 子绑定名
         *
         * @return
         */
        @Override
        public String tiedName()
        {
            return tiedName;
        }

        /**
         * 设置子绑定名
         *
         * @param tiedName
         */
        public ChildIn setTiedName(String tiedName)
        {
            checkNull(tiedName);
            this.tiedName = tiedName;
            return this;
        }

        /**
         * @return
         * @see com.chenyg.wporter.annotation.ChildIn#neceParams()
         */
        @Override
        public String[] neceParams()
        {
            return neceParams;
        }

        /**
         * @param neceParams
         * @see com.chenyg.wporter.annotation.ChildIn#neceParams()
         */
        public ChildIn setNeceParams(String[] neceParams)
        {
            checkNull(neceParams);
            this.neceParams = neceParams;
            return this;
        }

        /**
         * @return
         * @see com.chenyg.wporter.annotation.ChildIn#thinktype()
         */
        @Override
        public ThinkType thinktype()
        {
            return thinktype;
        }

        /**
         * @param thinktype
         * @see com.chenyg.wporter.annotation.ChildIn#thinktype()
         */
        public ChildIn setThinktype(ThinkType thinktype)
        {
            checkNull(thinktype);
            this.thinktype = thinktype;
            return this;
        }

        /**
         * @return
         * @see com.chenyg.wporter.annotation.ChildIn#unneceParams()
         */
        @Override
        public String[] unneceParams()
        {
            return unneceParams;
        }


        public ChildIn setInnerParams(String[] innerParams)
        {
            checkNull(innerParams);
            this.innerParams = innerParams;
            return this;
        }

        @Override
        public String[] innerParams()
        {
            return innerParams;
        }

        /**
         * @param unneceParams
         * @see com.chenyg.wporter.annotation.ChildIn#unneceParams()
         */
        public ChildIn setUnneceParams(String[] unneceParams)
        {
            checkNull(unneceParams);
            this.unneceParams = unneceParams;
            return this;
        }

        /**
         * @return
         * @see com.chenyg.wporter.annotation.ChildIn#method()
         */
        @Override
        public RequestMethod method()
        {
            return requestMethod;
        }

        /**
         * @param requestMethod
         * @see com.chenyg.wporter.annotation.ChildIn#method()
         */
        public ChildIn setRequestMethod(RequestMethod requestMethod)
        {
            checkNull(requestMethod);
            this.requestMethod = requestMethod;
            return this;
        }

        /**
         * @return
         * @see com.chenyg.wporter.annotation.ChildIn#checkType()
         */
        @Override
        public CheckType checkType()
        {
            return checkType;
        }

        /**
         * @param checkType
         * @see com.chenyg.wporter.annotation.ChildIn#checkType()
         */
        public ChildIn setCheckType(CheckType checkType)
        {
            checkNull(checkType);
            this.checkType = checkType;
            return this;
        }

        /**
         * @return
         * @see com.chenyg.wporter.annotation.ChildIn#checkPassTiedName()
         */
        @Override
        public String checkPassTiedName()
        {
            return checkTied;
        }

        /**
         * @param checkTied
         * @see com.chenyg.wporter.annotation.ChildIn#checkPassTiedName()
         */
        public ChildIn setCheckPassTiedName(String checkTied)
        {
            checkNull(checkTied);
            this.checkTied = checkTied;
            return this;
        }
    }


    public interface Call
    {
        Object call(WPObject wpObject) throws WPCallException;

        String getName();
    }

}
