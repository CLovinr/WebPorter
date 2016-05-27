package com.chenyg.wporter.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 除去开发环境的警告提示
 * 
 * @author ZhuiFeng
 *
 */
@Retention(RetentionPolicy.SOURCE)
public @interface NoWarn
{

}
