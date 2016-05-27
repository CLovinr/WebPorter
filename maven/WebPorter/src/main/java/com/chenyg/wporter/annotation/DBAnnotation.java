package com.chenyg.wporter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解
 *
 * @author Administrator
 */
public class DBAnnotation
{

    private DBAnnotation()
    {
    }


    /**
     * 数据库键（字段）
     *
     * @author Administrator
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Key
    {
        /**
         * 默认为"",表示使用变量名;否则使用该值。
         *
         * @return 实际的数据库键或字段名
         */
        String value() default "";

        /**
         * 用于类型检测的参数描述，面向用户.
         *
         * @return 参数描述
         * @see com.chenyg.wporter.InCheckParser#bindChecker(String, String, String)
         */
        String udesc() default "";

        /**
         * 绑定检测类型
         *
         * @return 检测类型
         * @see com.chenyg.wporter.InCheckParser#bindChecker(String, String, String)
         */
        String type() default "";

        /**
         * 为null时，是否添加到数据库中去，默认为false。
         *
         * @return 为true，表示null值会被写到数据库；为false，则不会。
         */
        boolean nullSetOrAdd() default false;
    }

    /**
     * 数据库键（字段）,自动的(由数据库自己来添加的)，在添加新记录时自己添加的值无效。
     *
     * @author Administrator
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface AutoKey
    {

    }

    /**
     * 数据库键（字段）,自动的，在添加新记录时由本程序库自动添加，自己设置的值无效。
     *
     * @author Administrator
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface UUIDKey
    {

    }


    /**
     * 普通索引。
     *
     * @author Administrator
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Index
    {

    }

    /**
     * 唯一索引
     *
     * @author Administrator
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface UniqueIndex
    {

    }

}
