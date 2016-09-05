package com.chenyg.wporter.app.porter;

import com.chenyg.wporter.WebPorter;
import com.chenyg.wporter.annotation.ChildIn;

/**
 * Created by https://github.com/CLovinr on 2016/9/4.
 */
public class HelloPorter extends WebPorter
{
    @ChildIn(tiedName = "")
    public Object say()
    {
        return "Hello World!";
    }
}
