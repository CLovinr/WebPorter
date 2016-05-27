package com.chenyg.wporter.annotation;

import java.lang.annotation.*;

/**
 * 要注意注入攻击
 * Created by 刚帅 on 2016/1/22.
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
public @interface InjectionWarn
{
}
