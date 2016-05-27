package com.chenyg.wporter.util;

import java.util.List;

/**
 * 用于扫描器扫描类时
 */
public abstract class MyClassLoader extends ClassLoader
{
    public MyClassLoader()
    {
    }

    public MyClassLoader(ClassLoader parent)
    {
        super(parent);
    }

    /**
     * 得到指定包下的所有类名
     *
     * @param packageOrClassName 包或者类名，""表示默认包
     * @param childPackage       是否得到子包下的类名。
     * @return
     */
    public abstract List<String> getClassNames(String packageOrClassName, boolean childPackage);

    /**
     * 设置搜索的包。
     * @param packages
     */
    public abstract void setPackages(String ... packages);

    /**
     * 搜索
     */
    public abstract void seek();

    /**
     * 最终释放资源
     */
    public abstract void release();
}
