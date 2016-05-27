package com.chenyg.wporter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.chenyg.wporter.base.CheckType;
import com.chenyg.wporter.base.RequestMethod;

/**
 * 描述输入参数的注解。
 * <pre>
 * 1.绑定的名字{@linkplain #tiedName()}(为""的情况下，绑定名字将为函数名)
 * 2.请求方法{@linkplain #method()}，默认为GET
 * 3.安全检测类型{@linkplain #checkType()}
 *  <strong>特别：</strong>使用该注解注释的函数，形参列表：
 * {@linkplain com.chenyg.wporter.WPObject}，也可以没有
 * 接口函数应为public的
 * </pre>
 *
 * @author Administrator
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface ChildIn
{

    /**
     * 绑定的名字(为""的情况下,绑定名为函数名)。
     *
     * @return 绑定的名字
     */
    String tiedName();

    /**
     * 必须参数列表
     *
     * @return 必须参数列表
     */
    String[] neceParams() default {};

    /**
     * 默认为ThinkType.DEFAULT.这个要配合父类的注解，只有当类和函数都为sREST时，最终才为REST。
     *
     * @return {@linkplain ThinkType}
     * @see FatherIn#thinkType()
     */
    ThinkType thinktype() default ThinkType.DEFAULT;

    /**
     * 非必须参数列表
     *
     * @return 非必须参数列表
     */
    String[] unneceParams() default {};

    /**
     * 内部参数列表，只在某些情况下用，如OftenDB中会用到。
     * @return
     */
    String[] innerParams() default {};

    /**
     * 请求方法，默认为GET.注意：对于OPTIONS,函数返回类型为void即可。
     *
     * @return 请求方法
     */
    RequestMethod method() default RequestMethod.GET;

    /**
     * 安全检测类型(默认为 DEFAULT)
     *
     * @return 安全检测类型
     */
    CheckType checkType() default CheckType.DEFAULT;

    /**
     * 检测对象的绑定名称。{@linkplain #checkType()}为{@linkplain CheckType#SELF}或
     * {@linkplain CheckType#SELF_GLOBAL}或{@linkplain CheckType#GLOBAL_SELF}时使用。
     *
     * @return 检测对象的绑定名称
     */
    String checkPassTiedName() default "";

}
