package com.chenyg.wporter.app;

import com.chenyg.wporter.Config;
import com.chenyg.wporter.InitException;
import com.chenyg.wporter.a.app.AppCommunication;
import com.chenyg.wporter.a.app.AppPorterListener;
import com.chenyg.wporter.a.app.AppPorterMain;
import com.chenyg.wporter.a.app.AppReciever;
import com.chenyg.wporter.base.CheckType;
import com.chenyg.wporter.log.LogUtil;
import com.chenyg.wporter.log.WPLogSys;
import org.junit.Test;

/**
 * Created by https://github.com/CLovinr on 2016/9/4.
 */
public class TestAppPorter
{
    @Test
    public void main() throws InitException
    {
        AppPorterMain porterMain = new AppPorterMain("");
        Config.SimpleBuilder builder = new Config.SimpleBuilder(WPLogSys.class);
        builder.setDefaulCheckType(CheckType.NONE);
        builder.addPorterPaths(true,"com.chenyg.wporter.app.porter");
        porterMain.start(builder.build(),null,false,null);
        long time = System.currentTimeMillis();

        porterMain.getAppPorterSender().send(new AppCommunication("/Hello/say"), new AppPorterListener()
        {
            @Override
            public void onResponse(AppReciever appReciever)
            {
                LogUtil.printErrPosLn(appReciever);
            }
        });

        LogUtil.printErrPosLn("time=",System.currentTimeMillis()-time);

        porterMain.stop();
    }
}
