package com.chenyg.wporter;

import java.io.Serializable;

import com.chenyg.wporter.base.RequestMethod;
import com.chenyg.wporter.log.WPLog;

/**
 * 用于得到参数源对象。
 */
@SuppressWarnings("serial")
public abstract class ParamSourceHandle implements Serializable
{
    public abstract ParamSource getParamSource(WPObject wpObject, RequestMethod method, WPRequest request,
            WPorterAndMethod wpfr, WPLog<?> wpLog);
}
