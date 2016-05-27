package com.chenyg.wporter;

import java.io.Serializable;
import java.util.Enumeration;

/**
 * 会话对象。
 */
@SuppressWarnings("serial")
public abstract class WPSession implements Serializable
{
    private String _id;

    public WPSession(String _id)
    {
        this._id = _id;
    }

    /**
     * 得到会话的唯一id
     */
    public String getId()
    {
        return _id;
    }

    /**
     * 得到属性值。
     *
     * @param name 属性名
     * @return 若存在，返回对应的值；否则返回null。
     */
    public abstract Object getAttribute(String name);

    /**
     * 设置属性
     *
     * @param name  属性名
     * @param value 属性值
     */
    public abstract void setAttribute(String name, Object value);

    /**
     * 移除属性
     *
     * @param name 要移除的属性。
     */
    public abstract void removeAttribute(String name);

    /**
     * 得到所有属性名。
     */
    public abstract Enumeration<String> getAttributeNames();

    /**
     * 是否是新生成的。
     */
    public abstract boolean isNew();
}
