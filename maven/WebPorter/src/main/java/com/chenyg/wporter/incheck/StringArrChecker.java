package com.chenyg.wporter.incheck;

import com.chenyg.wporter.InCheckParser;
import com.chenyg.wporter.annotation.NotNULL;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * 把josn数组转换为String[]
 * Created by 宇宙之灵 on 2015/9/14.
 */
public class StringArrChecker extends JSONArrayChecker
{
    @Override
    public InCheckParser.CheckResult parse(@NotNULL Object value)
    {
        InCheckParser.CheckResult checkResult = super.parse(value);
        try
        {
            if (checkResult.isLegal())
            {
                JSONArray array = (JSONArray) checkResult.getValue();
                String[] strs = new String[array.length()];
                for (int i = 0; i < strs.length; i++)
                {
                    strs[i] = array.getString(i);
                }
                checkResult.setValue(strs);
            }
        } catch (JSONException e)
        {
            checkResult.setIsLegal(false);
        }
        return checkResult;
    }
}
