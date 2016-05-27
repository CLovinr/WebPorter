package com.chenyg.wporter.annotation;

import com.chenyg.wporter.base.ResponseType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 *     描述输出的注解
 * 用来定义接口输出的。若某个接口使用了该注解，且返回某个对象不为null，输出内容将根据注解从返回对象中选择对应类变量的值.
 * </pre>
 *
 * @author ZhuiFeng
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface ChildOut
{


    /**
     * 定义输出参数，将通过反射从返回对象中获取同名的类变量的值。
     *
     * @return 输出参数
     */
    String[] outers() default {};

    /**
     * 响应类型.
     *
     * @return 响应类型
     */
    ResponseType value();

}
