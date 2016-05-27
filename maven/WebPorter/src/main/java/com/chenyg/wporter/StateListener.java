package com.chenyg.wporter;

import com.chenyg.wporter.log.WPLog;

import java.io.Serializable;

/**
 * <pre>
 *     状态监听。
 * {@linkplain #beforeInit(InitParamSource, WPLog)}--&#62;{@linkplain WPMain#init}--&#62;{@linkplain
 * #afterInit}...--&#62;
 * {@linkplain #onDestroy(WPLog)}
 * </pre>
 *
 * @author ZhuiFeng
 */
public interface StateListener extends Serializable
{
     void beforeInit(InitParamSource initParamSource, WPLog<?> wpLog);

     void afterInit(InitParamSource initParamSource, ParamSourceHandleManager paramSourceHandleManager, WPLog<?> wpLog);

     void onDestroy(WPLog<?> wpLog);
}
