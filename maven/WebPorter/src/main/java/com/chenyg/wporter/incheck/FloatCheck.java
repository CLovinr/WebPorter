package com.chenyg.wporter.incheck;

import com.chenyg.wporter.InCheckParser;
import com.chenyg.wporter.annotation.NotNULL;

/**
 * Created by 宇宙之灵 on 2015/9/14.
 */
public class FloatCheck extends InCheckParser.Checker
{
    @Override
    public InCheckParser.CheckResult parse(@NotNULL Object value)
    {
        InCheckParser.CheckResult checkResult;

        try
        {
            Object v;
            if ((value.getClass().isPrimitive() && value.getClass().equals(Float.class)) || (value instanceof Float))
            {
                v = value;
            } else
            {
                v = Float.parseFloat(value.toString());
            }

            checkResult = new InCheckParser.CheckResult(v);
        } catch (NumberFormatException e)
        {
            checkResult = InCheckParser.CheckResult.ILLEGAL;
        }
        return checkResult;
    }
}
