package com.chenyg.wporter.incheck;

import com.chenyg.wporter.InCheckParser;
import com.chenyg.wporter.annotation.NotNULL;

/**
 * Created by 宇宙之灵 on 2015/9/14.
 */
public class ShortChecker extends InCheckParser.Checker
{
    @Override
    public InCheckParser.CheckResult parse(@NotNULL Object value)
    {
        InCheckParser.CheckResult checkResult;
        try
        {
            Object v;
            if ((value.getClass().isPrimitive() && value.getClass().equals(Short.class)) || (value instanceof Short))
            {
                v = value;
            } else
            {
                v = Short.parseShort(value.toString());
            }
            checkResult = new InCheckParser.CheckResult(v);
        } catch (NumberFormatException e)
        {
            checkResult = InCheckParser.CheckResult.ILLEGAL;
        }
        return checkResult;
    }
}
