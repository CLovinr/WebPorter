package com.chenyg.wporter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用来定义接口输出的。若某个接口使用了该注解，且{@linkplain ChildOut}不为null以及返回某个对象不为null，输出内容将根据注解从返回对象中选择对应类变量的值.
 *
 * @author ZhuiFeng
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface FatherOut
{
    /**
     * 定义输出参数列表，将通过反射从返回对象中获取同名的类变量的值。
     *
     * @return 输出参数列表
     */
    String[] outers() default {};
}
