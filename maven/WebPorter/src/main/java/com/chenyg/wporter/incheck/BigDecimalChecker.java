package com.chenyg.wporter.incheck;

import com.chenyg.wporter.InCheckParser;
import com.chenyg.wporter.annotation.NotNULL;

import java.math.BigDecimal;

/**
 * Created by 刚帅 on 2016/1/12.
 */
public class BigDecimalChecker extends InCheckParser.Checker
{
    @Override
    public InCheckParser.CheckResult parse(@NotNULL Object value)
    {
        InCheckParser.CheckResult checkResult;

        try
        {
            Object v;
            if (value instanceof BigDecimal)
            {
                v = value;
            } else
            {
                v = new BigDecimal(value.toString());
            }

            checkResult = new InCheckParser.CheckResult(v);
        } catch (NumberFormatException e)
        {
            checkResult = InCheckParser.CheckResult.ILLEGAL;
        }
        return checkResult;
    }
}
