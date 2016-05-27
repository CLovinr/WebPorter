package com.chenyg.wporter.annotation;

public interface MyConsumer<T, E extends Exception>
{
    void accept(T t) throws E;
}
