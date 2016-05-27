package com.chenyg.wporter.incheck;

import com.chenyg.wporter.InCheckParser;
import com.chenyg.wporter.annotation.NotNULL;

/**
 * Created by 宇宙之灵 on 2015/9/14.
 */
public class StringChecker extends InCheckParser.Checker
{
    @Override
    public InCheckParser.CheckResult parse(@NotNULL Object value)
    {
        InCheckParser.CheckResult checkResult = new InCheckParser.CheckResult(value.toString());
        return checkResult;
    }
}
