package com.chenyg.wporter.incheck;

import com.chenyg.wporter.InCheckParser;
import com.chenyg.wporter.annotation.NotNULL;

/**
 * Created by 宇宙之灵 on 2015/9/14.
 */
class _IntXChecker extends InCheckParser.Checker
{
    private int radix;

    public _IntXChecker(int radix)
    {
        this.radix = radix;
    }

    @Override
    public InCheckParser.CheckResult parse(@NotNULL Object value)
    {
        InCheckParser.CheckResult checkResult;

        try
        {
            Object v;
            if ((value.getClass().isPrimitive() && value.getClass().equals(Integer.class)) || (value instanceof Integer))
            {
                v = value;
            } else
            {
                int x = (int) Long.parseLong(value.toString(), radix);
                v = x;
            }

            checkResult = new InCheckParser.CheckResult(v);
        } catch (NumberFormatException e)
        {
            checkResult = InCheckParser.CheckResult.ILLEGAL;
        }
        return checkResult;
    }
}
