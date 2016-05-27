package com.chenyg.wporter.base;

import com.chenyg.wporter.annotation.ChildOut;
import com.chenyg.wporter.annotation.FatherOut;
import com.chenyg.wporter.annotation.FieldOut;
import com.chenyg.wporter.annotation.SimpleResponse;

/**
 * <pre>
 * 当{@linkplain com.chenyg.wporter.WPResponse#getContentType()}为null与"",且响应类型不为{@linkplain
 * #SelfResponse}则设置ContentType根据以下情况设置：
 * 1.返回对象为JSONObject或JSAONArray或JSONResponse,设置为application/json
 * 2.其他设置为text/plain
 * </pre>
 *
 * @author ZhuiFeng
 */
public enum ResponseType
{
    /**
     * 函数自己处理所有响应数据 <br>
     */
    SelfResponse,
    /**
     * 简单返回，默认为false。会检查{@linkplain SimpleResponse}里的3个注解标记的字段，若
     * {@linkplain SimpleResponse.Description} 和
     * {@linkplain SimpleResponse.Code}标记的变量不为null则返回响应结果.
     * <pre>
     *  Content-Type:application/json
     * </pre>
     */
    Simple,
    /**
     * 使用{@linkplain FieldOut}标记的字段来提取数据 <br>
     * Content-Type:application/json
     */
    ObjectField,
    /**
     * 定义输出参数，将通过反射从返回对象中获取同名的类变量的值。使用{@linkplain ChildOut#outers()}和
     * {@linkplain FatherOut#outers()} <br>
     * Content-Type:application/json
     */
    Outers,
    /**
     * 响应返回数据。(toString)
     */
    DirectResult,
}
