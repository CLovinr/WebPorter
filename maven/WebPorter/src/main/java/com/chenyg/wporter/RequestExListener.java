package com.chenyg.wporter;

import com.chenyg.wporter.base.JResponse;
import com.chenyg.wporter.log.WPLog;

import java.io.Serializable;

/**
 * 请求结果不成功的监听接口。
 * Created by 宇宙之灵 on 2015/10/20.
 */
public interface RequestExListener extends Serializable
{
    void onEx(JResponse jResponse,WPLog<?> wpLog);
}
