package com.chenyg.wporter;

/**
 * 用于初始化过程中.
 * @author ZhuiFeng
 *
 */
interface InitHandle<T>
{
    public T handle(InitParamSource paramGeter) throws Exception;
}
