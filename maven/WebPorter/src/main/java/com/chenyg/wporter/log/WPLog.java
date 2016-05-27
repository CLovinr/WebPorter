package com.chenyg.wporter.log;

/**
 * Created by 宇宙之灵 on 2015/10/20.
 */
public interface WPLog<T>
{
    WPLog<T> log(T type, Object... objects);

    WPLog<T> trace(Object... objects);

    WPLog<T> debug(Object... objects);

    WPLog<T> info(Object... objects);

    WPLog<T> warn(Object... objects);

    WPLog<T> error(Object... objects);

    WPLog<T> fatal(Object... objects);
}
