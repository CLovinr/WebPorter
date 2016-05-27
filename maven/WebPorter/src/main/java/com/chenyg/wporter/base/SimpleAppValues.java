package com.chenyg.wporter.base;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class SimpleAppValues extends AppValues
{

    private String[] names;
    private Object[] values;


    /**
     * 提取键值对。
     *
     * @param jsonObject
     * @return
     */
    public static SimpleAppValues fromJSON(JSONObject jsonObject)
    {
        SimpleAppValues simpleAppValues = new SimpleAppValues();

        if (jsonObject != null)
        {
            simpleAppValues.names = new String[jsonObject.length()];
            int i = 0;
            Iterator<?> keys = jsonObject.keys();
            while (keys.hasNext())
            {
                String key = (String) keys.next();
                simpleAppValues.names[i++] = key;
            }
            try
            {
                simpleAppValues.values = new Object[simpleAppValues.names.length];
                for (int j = 0; j < simpleAppValues.values.length; j++)
                {

                    simpleAppValues.values[j] = jsonObject.get(simpleAppValues.names[j]);

                }
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        return simpleAppValues;
    }

    public SimpleAppValues add(AppValues appValues)
    {

        if (appValues != null)
        {
            String[] names = appValues.getNames();
            Object[] values = appValues.getValues();

            String[] strs;
            Object[] objects;

            int len1 = this.names == null ? 0 : this.names.length;
            int len2 = names == null ? 0 : names.length;
            strs = new String[len1 + len2];
            objects = new Object[strs.length];
            int offset = 0;
            if (len1 > 0)
            {
                System.arraycopy(this.names, 0, strs, offset, len1);
                System.arraycopy(this.values, 0, objects, offset, len1);
                offset += len1;
            }
            if (len2 > 0)
            {
                System.arraycopy(names, 0, strs, offset, len2);
                System.arraycopy(values, 0, objects, offset, len2);
            }
            this.names = strs;
            this.values = objects;
        }
        return this;
    }

    public SimpleAppValues(String... names)
    {
        names(names);
    }

    @Override
    public String[] getNames()
    {
        return names;
    }

    @Override
    public Object[] getValues()
    {
        return values;
    }


    /**
     * 设置值
     *
     * @param values 值列表
     * @return 返回自己
     */
    public SimpleAppValues values(Object... values)
    {
        this.values = values;
        return this;
    }

    /**
     * 设置名称
     *
     * @param names 名称列表
     * @return 返回自己
     */
    public SimpleAppValues names(String... names)
    {
        this.names = names;
        return this;
    }

}
