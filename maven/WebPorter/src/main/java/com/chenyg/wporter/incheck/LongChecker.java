package com.chenyg.wporter.incheck;

import com.chenyg.wporter.InCheckParser;
import com.chenyg.wporter.annotation.NotNULL;

/**
 * Created by 宇宙之灵 on 2015/9/14.
 */
public class LongChecker extends InCheckParser.Checker
{
    @Override
    public InCheckParser.CheckResult parse(@NotNULL Object value)
    {
        InCheckParser.CheckResult checkResult;

        try
        {
            Object v;
            if ((value.getClass().isPrimitive() && value.getClass().equals(Long.class)) || (value instanceof Long))
            {
                v = value;
            }else{
                v = Long.parseLong(value.toString());
            }

            checkResult = new InCheckParser.CheckResult(v);
        } catch (NumberFormatException e)
        {
            checkResult = InCheckParser.CheckResult.ILLEGAL;
        }
        return checkResult;
    }
}
