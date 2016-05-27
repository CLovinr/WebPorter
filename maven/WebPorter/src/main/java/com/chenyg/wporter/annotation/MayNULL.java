package com.chenyg.wporter.annotation;

import java.lang.annotation.*;

/**
 * 可以为null
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
public @interface MayNULL
{
}