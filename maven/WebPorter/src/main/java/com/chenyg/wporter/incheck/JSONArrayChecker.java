package com.chenyg.wporter.incheck;

import com.chenyg.wporter.InCheckParser;
import com.chenyg.wporter.annotation.NotNULL;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * 把json数组格式的字符串转换为{@linkplain JSONArray}
 * Created by 宇宙之灵 on 2015/9/14.
 */
public class JSONArrayChecker extends InCheckParser.Checker
{
    @Override
    public InCheckParser.CheckResult parse(@NotNULL Object value)
    {
        InCheckParser.CheckResult checkResult;
        try
        {
            Object v;
            if (value instanceof JSONArray)
            {
                v = value;
            } else
            {
                JSONArray array = new JSONArray(value.toString());
                v = array;
            }
            checkResult = new InCheckParser.CheckResult(v);
        } catch (JSONException e)
        {
            checkResult = InCheckParser.CheckResult.ILLEGAL;
        }
        return checkResult;
    }
}
