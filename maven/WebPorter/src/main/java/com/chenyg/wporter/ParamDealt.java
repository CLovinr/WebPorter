package com.chenyg.wporter;

import com.chenyg.wporter.util.WPTool;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于处理参数.
 * 必须先解析类的，再解析接口的。
 */
@SuppressWarnings("serial")
public abstract class ParamDealt implements Serializable
{


    protected class Handle
    {
        private InCheckParser inCheckParser;
        private ParamSource paramSource;
        /**
         * 来自于分解的参数。
         */
        private HashMap<String, Object> params = new HashMap<>(0);

        public Handle(WPorterAndMethod wPorterAndMethod, ParamSource paramSource)
        {
            inCheckParser = wPorterAndMethod.wPorter.getInCheckParser();
            this.paramSource = paramSource;
        }

        public Object getParam(String name) throws InCheckParser.IllegalParamException
        {
            Object v = params.get(name);
            if (WPTool.isEmpty(v))
            {
                v = paramSource.getParameter(name);
                if (!WPTool.isEmpty(v) && inCheckParser != null)
                {
                    Object obj = inCheckParser.parse(name, v);
                    if (obj instanceof InCheckParser.DecodeParams)
                    {
                        InCheckParser.DecodeParams decodeParams = (InCheckParser.DecodeParams) obj;
                        Map<String, Object> map = decodeParams.getParams();
                        params.putAll(map);
                        Object val = map.get(name);
                        if (!WPTool.isEmpty(val))
                        {
                            v = val;
                        } else if (decodeParams.isAdd2Param())
                        {
                            v = decodeParams;
                        }
                    } else
                    {
                        v = obj;
                    }
                }
            }

            if ("".equals(v))
            {
                v = null;
            }
            return v;
        }
    }

    /**
     * 得到非必需参数的值（有默认值的）
     *
     * @param handle
     * @param name
     * @param index
     * @param paramExtra
     * @return
     */
    protected Object getCusValue(Handle handle, String name, int index,
            WPorter._ParamExtra paramExtra) throws InCheckParser.IllegalParamException

    {
        Object value = handle.getParam(name);
        if (value == null && paramExtra != null
                && paramExtra.cus().length > 0)
        {
            value = paramExtra.cus()[index];
            if ("".equals(value)) value = null;
        }
        return value;
    }

    /**
     * 添加参数描述
     *
     * @param map
     * @param name
     * @param index
     * @param wPorterAndMethod
     */
    protected void addDesc(Map<String, String> map, String name, int index, WPorterAndMethod wPorterAndMethod)
    {
        if (wPorterAndMethod.udesc != null)
        {
            String desc = wPorterAndMethod.udesc[index];
            map.put(name, "".equals(desc) ? name : desc);
        }
    }

    /**
     * 处理参数。
     *
     * @param wpfr
     * @param wpObject
     * @param paramSource
     * @param responseLosedArgs 是否响应缺少的必须参数。
     * @return 通过返回true，否则返回false。
     * @throws LackParamsException 当缺少必须参数且要响应缺少哪些参数时，抛出该异常。
     */

    public abstract boolean dealParams(WPorterAndMethod wpfr, WPObject wpObject, ParamSource paramSource,
            boolean responseLosedArgs) throws LackParamsException, InCheckParser
            .IllegalParamException;
}
