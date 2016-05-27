package com.chenyg.wporter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 参数补充
 *
 * @author ZhuiFeng
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface ParamExtra
{


    /**
     * 必须参数描述,缺少必须参数时用到;要么大小为0个要么必须与参数列表对应。其中为""的表示使用变量名;若为{}表示无描述，默认为{}。
     *
     * @return 必须参数描述
     */
    String[] cnsdesc() default {};

    /**
     * 非必需参数的默认值，默认为{},表示不使用默认值；若不为{},则必须全部提供，元素可以为""(会被转换为null)
     *
     * @return 非必需参数的默认值列表
     */
    String[] cus() default {};
}
