package com.chenyg.wporter.incheck;

import com.chenyg.wporter.InCheckParser;
import com.chenyg.wporter.annotation.NotNULL;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 把json格式的字符串转换为{@linkplain JSONObject}
 * Created by 宇宙之灵 on 2015/9/14.
 */
public class JSONObjectChecker extends InCheckParser.Checker
{
    @Override
    public InCheckParser.CheckResult parse(@NotNULL Object value)
    {
        InCheckParser.CheckResult checkResult;
        try
        {
            Object v;
            if (value instanceof JSONObject)
            {
                v = value;
            } else
            {
                JSONObject jsonObject = new JSONObject(value.toString());
                v = jsonObject;
            }
            checkResult = new InCheckParser.CheckResult(v);
        } catch (JSONException e)
        {
            checkResult = InCheckParser.CheckResult.ILLEGAL;
        }
        return checkResult;
    }

}
