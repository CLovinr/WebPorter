package com.chenyg.wporter;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * 接口自定义的表单对象。
 */
public abstract class WPForm extends ParamSource implements Closeable
{

    public static final String HEADER_NAME = "WPorterType";
    private Map<String, Object> params;
    private String paramEncoding;

    public WPForm(Map<String, Object> params, String paramEncoding)
    {
        this.params = params;
        this.paramEncoding = paramEncoding;
    }

    /**
     * 得到参数编码方式
     */
    public String getParamEncoding()
    {
        return paramEncoding;
    }

    /**
     * 得到参数的个数
     */
    public int getParamCount()
    {
        return params.size();
    }

    /**
     * 得到参数名称集
     */
    public Iterator<String> getParamNames()
    {
        return params.keySet().iterator();
    }

    /**
     * 得到参数值的集合。
     */
    public Iterator<Object> getParamValues()
    {
        return params.values().iterator();
    }

    /**
     * 得到指定名称的参数
     *
     * @param name
     * @return
     */
    @Override
    public Object getParameter(String name)
    {
        return params.get(name);
    }

    /**
     * 剩余的文件个数（不包括正在读取的文件）
     */
    public abstract int remainFileCount();

    /**
     * 是否还有文件
     */
    public abstract boolean hasFile();

    /**
     * 得到下一个文件
     *
     * @throws IOException
     */
    public abstract WPFormFile nextFile() throws IOException;


}
