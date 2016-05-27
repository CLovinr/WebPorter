package com.chenyg.wporter.annotation;

import java.lang.annotation.*;

public class SimpleResponse
{
    private SimpleResponse()
    {
    }

    /**
     * 返回码，结果要是{@linkplain com.chenyg.wporter.base.ResultCode}。
     *
     * @author ZhuiFeng
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @Documented
    public @interface Code
    {

    }

    /**
     * 响应内容，结果是字符串
     *
     * @author ZhuiFeng
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @Documented
    public @interface Result
    {

    }

    /**
     * 响应描述，字符串
     *
     * @author ZhuiFeng
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @Documented
    public @interface Description
    {

    }
}
