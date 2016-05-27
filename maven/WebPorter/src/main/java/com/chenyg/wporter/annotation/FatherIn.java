package com.chenyg.wporter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用在类上. <br>
 * 1.绑定的名字{@linkplain #tiedName()}({@linkplain #useClassName()}
 * 为true时，为""的情况下，绑定名字为"")
 *
 * @author Administrator
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface FatherIn
{
    /**
     * 绑定的名字(为""的情况下，绑定名字为"";)
     *
     * @return 绑定的名字
     */
    String tiedName() default "";

    /**
     * 绑定名字是否使用类名，默认为true,但是当{@linkplain #tiedName()}不为空时，则使用 {@linkplain #tiedName()}。 <br>
     * 当使用类名时，1.去掉末尾的Porter;2.加上/
     *
     * @return 绑定名字是否使用类名
     */
    boolean useClassName() default true;

    /**
     * 默认为ThinkType.DEFAULT.
     *
     * @return {@linkplain ThinkType}
     * @see ChildIn#thinktype()
     */
    ThinkType thinkType() default ThinkType.DEFAULT;

    /**
     * 是否加上父绑定名，默认false。
     *
     * @return 是否加上父绑定名
     */
    boolean addSTiedName() default false;

    /**
     * 必须参数列表
     *
     * @return 必须参数列表
     */
    String[] neceParams() default {};

    /**
     * 非必须参数列表
     *
     * @return 非必须参数列表
     */
    String[] unneceParams() default {};

    /**
     * 默认检测对象的绑定名称。当ChildIn的为DEFAULT:1)此值不为""时，则使用此名称绑定的检测对象;2)若为"",则使用全局设置默认的类型。
     *
     * @return 默认检测对象的绑定名称
     */
    String defaultCheck() default "";

}