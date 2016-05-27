package com.chenyg.wporter;

/**
 * Created by 宇宙之灵 on 2015/10/3.
 */
public interface WebPorterFun
{
     Object call(
            WPObject wpObject) throws WPCallException;
}
