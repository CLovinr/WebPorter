package com.chenyg.wporter.incheck;

import com.chenyg.wporter.InCheckParser;
import com.chenyg.wporter.annotation.NotNULL;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * 把json数组转换为ArrayList&#60;String&#62;.
 * Created by 宇宙之灵 on 2015/9/14.
 */
public class ArrayListArrChecker extends JSONArrayChecker
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
                ArrayList<String> list = new ArrayList<String>(array.length());
                for (int i = 0; i < array.length(); i++)
                {
                    list.add(array.getString(i));
                }
                checkResult.setValue(list);
            }
        } catch (JSONException e)
        {
            checkResult.setIsLegal(false);
        }
        return checkResult;
    }
}
