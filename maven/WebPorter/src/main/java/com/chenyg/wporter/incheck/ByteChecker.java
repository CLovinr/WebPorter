package com.chenyg.wporter.incheck;

import com.chenyg.wporter.InCheckParser;
import com.chenyg.wporter.annotation.NotNULL;

/**
 * byte类型
 * Created by 宇宙之灵 on 2015/9/14.
 */
public class ByteChecker extends InCheckParser.Checker
{
    @Override
    public InCheckParser.CheckResult parse(@NotNULL Object value)
    {
        InCheckParser.CheckResult checkResult;
        try
        {
            Object v;
            if ((value.getClass().isPrimitive() && value.getClass().equals(Byte.class)) || (value instanceof Byte))
            {
                v = value;
            } else
            {
                v = Byte.parseByte(value.toString());
            }
            checkResult = new InCheckParser.CheckResult(v);
        } catch (NumberFormatException e)
        {
            checkResult = InCheckParser.CheckResult.ILLEGAL;
        }
        return checkResult;
    }
}
