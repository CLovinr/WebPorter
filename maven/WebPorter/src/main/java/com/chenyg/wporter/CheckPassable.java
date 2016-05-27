package com.chenyg.wporter;


import java.io.Serializable;

import com.chenyg.wporter.base.RequestMethod;

/**
 * 安全检测接口
 * 
 * @author Administrator
 *
 */
public interface CheckPassable  extends Serializable
{

    

    /**
     * 检测当前访问是否通过.
     * 
     * @param method 请求方法
     * @param request 请求对象
     * @param response 响应对象
     * @return 若通过则返回null.否则不为null：该结果会通过{@linkplain com.chenyg.wporter.base.JResponse#setResult(Object)}输出;
     */
     Object willPass(RequestMethod method, WPRequest request, WPResponse response);

}
