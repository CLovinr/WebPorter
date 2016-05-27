package com.chenyg.wporter.incheck;

import com.chenyg.wporter.InCheckParser;
import com.chenyg.wporter.annotation.NotNULL;

/**
 * boolean类型
 * Created by 宇宙之灵 on 2015/9/14.
 */
public class BooleanChecker extends InCheckParser.Checker
{
    @Override
    public InCheckParser.CheckResult parse(@NotNULL Object value)
    {
        InCheckParser.CheckResult checkResult;
        try
        {
            Object v;
            if ((value.getClass().isPrimitive() && value.getClass().equals(Boolean.class)) || (value instanceof Boolean))
            {
                v = value;
            } else
            {
                v = Boolean.parseBoolean(value.toString());
            }
            checkResult = new InCheckParser.CheckResult(v);
        } catch (NumberFormatException e)
        {
            checkResult = InCheckParser.CheckResult.ILLEGAL;
        }
        return checkResult;
    }
}
