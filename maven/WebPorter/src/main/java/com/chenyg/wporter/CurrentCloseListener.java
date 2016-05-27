package com.chenyg.wporter;

/**
 * 当本次响应关闭后的回调。
 * Created by 刚帅 on 2016/1/12.
 */
public interface CurrentCloseListener
{
    /**
     * 远程连接关闭后的回调函数。
     * @param success 是否正常关闭
     */
    void afterCloseResponse(boolean success);
}
